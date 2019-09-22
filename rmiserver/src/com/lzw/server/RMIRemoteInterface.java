package com.lzw.server;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Administrator
 */
public interface RMIRemoteInterface extends Remote {

    public String sayHi() throws RemoteException; //远程方法必须申明抛出RemoteException

    public String sayHello(String str) throws RemoteException;
}
