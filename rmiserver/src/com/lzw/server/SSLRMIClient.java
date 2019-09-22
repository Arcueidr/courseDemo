package com.lzw.server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class SSLRMIClient {
    public static void main(String[] args) {

        if (args.length < 1) {
            System.err.println("Usage: RMIClient <host> [port]");
//            System.exit(1);
        }
        //args[0];
        String host = "www.lzw.com";
//        String host = "localhost";
        int port = SSLRMIServer.RMI_PORT;
        if (args.length == 2) {
            port = Integer.valueOf(args[1]);
        }

        try {
// Registry registry = LocateRegistry.getRegistry(host, port);
            Registry registry = LocateRegistry.getRegistry(host, port, new MySSLRMIClientSocketFactory());
            RMIRemoteInterface stub = (RMIRemoteInterface) registry.lookup("newHello");

// FileOutputStream fos = new FileOutputStream("c:\\t.tmp");
// ObjectOutputStream oos = new ObjectOutputStream(fos);
// oos.writeObject(stub);
// oos.close();

            String response = stub.sayHi();
            System.out.println("sayHi: " + response);
            System.out.println("sayHello: " + stub.sayHello("Java World"));
        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }

//    public static void main2(String[] args) {
//
//        try {
//            FileInputStream fis = new FileInputStream("c:\\t.tmp");
//            ObjectInputStream ois = new ObjectInputStream(fis);
//            RMIRemoteInterface stub = (RMIRemoteInterface) ois.readObject();
//
//            String response = stub.sayHi();
//            System.out.println("response: " + response);
//
//        } catch (Exception e) {
//            System.err.println("Client exception: " + e.toString());
//            e.printStackTrace();
//        }
//
//    }
}
