package com.lzw.server;

import java.rmi.AlreadyBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ProxyBinderInterface extends Remote  {
    public void proxyBind(String label, Remote stub) throws RemoteException, AlreadyBoundException;
}
