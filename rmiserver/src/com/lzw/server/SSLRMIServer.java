package com.lzw.server;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class SSLRMIServer  implements RMIRemoteInterface {
    public static final String PROXY_BINDER = "PROXY_BINDER";
    public static final String HOST = "www.lzw.com";
    public static final int RMI_PORT = 2222;

    public SSLRMIServer() {

    }

    @Override
    public String sayHi() {
        return "Hello World!";
    }

    @Override
    public String sayHello(String str) throws RemoteException {
        // TODO Auto-generated method stub
        return "Hi " + str;
    }

    public static void main(String args[]) {

        try {
            int port = (args.length < 1) ? RMI_PORT : Integer.valueOf(args[0]);

            SSLRMIServer obj = new SSLRMIServer();
            RMIRemoteInterface stub = (RMIRemoteInterface) UnicastRemoteObject
                    .exportObject(obj, 0);

// ProxyBinderInterface proxyBinder = (ProxyBinderInterface)UnicastRemoteObject.e

            // Bind the remote object's stub in the registry

            // RMI registry not allow remote hosts to bind
// Registry registry = LocateRegistry.getRegistry(HOST, port);
// Registry registry = LocateRegistry.createRegistry(port);
            Registry registry =
                    LocateRegistry.createRegistry(port, null , new MySSLRMIServerSocketFactory());
            registry.bind("newHello", stub);
//            registry.bind(PROXY_BINDER, obj.new ProxyBinder());

            System.err.println("Server ready");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    class ProxyBinder extends UnicastRemoteObject implements ProxyBinderInterface {

        /**
         *
         */
        private static final long serialVersionUID = -2373753944757892323L;

        protected ProxyBinder() throws RemoteException {
            super();
            // TODO Auto-generated constructor stub
        }

        @Override
        public void proxyBind(String label, Remote stub) throws RemoteException, AlreadyBoundException {
            Registry registry = LocateRegistry.getRegistry(HOST, RMI_PORT);
            registry.bind(label, stub);
        }

    }
}
