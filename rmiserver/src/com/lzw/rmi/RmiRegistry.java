package com.lzw.rmi;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.FileInputStream;
import java.rmi.registry.LocateRegistry;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * @author Administrator
 */
public class RmiRegistry {
    public static void main(String[] args) throws Exception {
        String key = "D:\\keys\\server.jks";
        String trust = "D:\\keys\\trustserver.jks";
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(key), "123qwe,.".toCharArray());
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new FileInputStream(trust), "123qwe,.".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(keyStore, "123qwe,.".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        SSLContext sslc = SSLContext.getInstance("SSLv3");
        sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
                new SecureRandom());
        /*上面的一套代码就是生成一个指定了秘钥和信任证书的context，就不多说了*/

        /*下面我们在300端口创建一个registry，并且指定了客户端套接字和服务端套接字，和HelloImpl_1中的很像*/
        LocateRegistry.createRegistry(3000, new MySslRMIClientSocketFactory(),
                new SslRMIServerSocketFactory(sslc,
                        new String[] { "SSL_RSA_WITH_RC4_128_MD5" },
                        new String[] { "TLSv1" }, true));
        System.out.println("RMI Registry running on port 3000");
        Object object = new Object();
        synchronized (object) {
            object.wait();
        }

    }
}
