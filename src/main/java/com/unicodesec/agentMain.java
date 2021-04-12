package com.unicodesec;

import java.lang.instrument.Instrumentation;

/**
 * @program: AgentMemShellScanner
 * @description: javaagent premain
 * @author: potats0
 * @create: 2021-04-10 09:25
 **/
public class agentMain {
    private static Instrumentation inst;
    public static void premain(String featureString, Instrumentation instrumentation) {
        System.out.println("===Step in Agent Method1===");
        System.out.println("supportReDefine:" + instrumentation.isRedefineClassesSupported());
        System.out.println("supportReTransform:" + instrumentation.isRetransformClassesSupported());
        System.out.println("featureString:" + featureString);
    }

    public static void agentmain(String featureString, Instrumentation instrumentation) {
        inst = instrumentation;
        System.out.println("该agent已经注入中间件JVM进程中");
        for(Class<?> clazz: instrumentation.getAllLoadedClasses()) {
            if(clazz.getName().equals(featureString)) {
                Class targetClazz = clazz;
                System.out.printf("正在还原%s\n", featureString);
                transform(targetClazz);
                return;
            }
        }
        System.out.println("没有找到类，请确定类名是否正确");

    }

    /**
     * Registers a transformer and executes the transform
     * @param clazz The class to transform
     */
    protected static void transform(Class<?> clazz) {
        recoveryTransform dt = new recoveryTransform();
        inst.addTransformer(dt, true);
        try {
            inst.retransformClasses(clazz);
        } catch (Exception ex) {
            throw new RuntimeException("Failed to transform [" + clazz.getName() + "]", ex);
        } finally {
            inst.removeTransformer(dt);
        }
    }

}
