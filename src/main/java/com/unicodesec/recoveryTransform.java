package com.unicodesec;

import javassist.ClassPool;
import javassist.CtClass;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

/**
 * @program: AgentMemShellScanner
 * @description: 还原被agent类型内存马修改的类的字节码
 * @author: potats0
 * @create: 2021-04-10 09:31
 **/
public class recoveryTransform implements ClassFileTransformer {


    @Override
    public byte[] transform(final ClassLoader loader, final String className, final Class<?> classBeingRedefined, final ProtectionDomain protectionDomain, final byte[] classfileBuffer) {


            try {
                final ClassPool classPool = ClassPool.getDefault();
                final CtClass clazz = classPool.get(className);
                byte[] byteCode = clazz.toBytecode();
                clazz.detach();
                System.out.printf("已经还原%s\n", className);
                return byteCode;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return classfileBuffer;
    }
}

