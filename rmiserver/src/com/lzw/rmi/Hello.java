package com.lzw.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author Administrator
 */
public interface Hello extends Remote {
    /**
     * 打印hello语句
     * @return
     * */
    public String sayHello() throws RemoteException;;

}
