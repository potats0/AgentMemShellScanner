package com.unicodesec;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws IOException, AttachNotSupportedException, AgentLoadException, AgentInitializationException {
        if (args.length == 0) {
            System.err.println("eg：java -jar antiAgentMemShell.jar 23232 classname，23232为需要attach的jvm进程号, classname为想还原的类");
            System.exit(-1);
        }
        VirtualMachine vmObj = null;

        try {
            vmObj = VirtualMachine.attach(args[0]);
            String agentpath = App.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            if (vmObj != null) {
                if (args.length > 1) {
                    vmObj.loadAgent(agentpath, args[1]);
                } else {
                    vmObj.loadAgent(agentpath);
                }
            }
        } finally {
            if (null != vmObj) {
                vmObj.detach();
            }


        }
    }
}
