package com.lzw.rmi;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * @author Administrator
 */
public class MySslRMIClientSocketFactory implements RMIClientSocketFactory {
    /**
     * 这个类继承了SslRMIClientSocketFactory，我也是看文档的，
     * 那个什么什么说SslRMIClientSocketFactory为了避免各种各种的问题
     * 都是使用默认的jre中配置的证书来创建socket，要是你自己的客户端能验证服务器
     * ，服务器验证你的客户端，而且是使用自己的证书，就重写一下createSocket这个方法。。。云云 那就重写吧
     */
    private static final long serialVersionUID = 1L;

    public MySslRMIClientSocketFactory() {
        super();
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        String key = "D:\\keys\\trustclient.jks";
        String client = "D:\\keys\\client.jks";
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(new FileInputStream(key), "123qwe,.".toCharArray());
            KeyStore clientStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            clientStore.load(new FileInputStream(client),
                    "123qwe,.".toCharArray());
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            KeyManagerFactory kmf = KeyManagerFactory
                    .getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, "123qwe,.".toCharArray());
            SSLContext sslc = SSLContext.getInstance("TLSv1");
            sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
                    new SecureRandom());
            /*上面的没什么好说的了。。。就是要说说，证书换成了客户端要信任的证书和客户端的秘钥*/

            SSLSocketFactory sslSocketFactory = sslc.getSocketFactory();//通过sslc获得socketFactory
            SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket(host,
                    port);//建立socket
            return socket;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
