package com.lzw.rmi;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.rmi.ssl.SslRMIServerSocketFactory;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * @author lzw
 * 这个是hello的实现类，在构造方法和getCntext那里有些幺蛾子，需要细细看看，其他没什么了
 *
 * */
public class HelloImpl extends UnicastRemoteObject implements Hello {
    public HelloImpl() throws IllegalArgumentException, Exception {
        /**
         * 这个构造方法需要细细的说道一下了
         * 先说super
         *
         * 第一个参数指明了这个remoteObject接受请求的端口，我就设置成了0；
         *
         * 第二个参数设置客户端要使用的套接字工厂，我设置了自己实现的MySslRMIClientSocketFactory，这个具体是什么，后面介绍
         *
         * 第三个参数设置了服务端要是用的套接字工场，我是用了SslRMIServerSocketFactory；其中getContext在后面说，
         * new String[] { "SSL_RSA_WITH_RC4_128_MD5" }是加密算法，new String[] { "TLSv1" }使用的协议。
         *
         * 第四个参数是是否开启对客户端的验证，要做双向验证必然是true了
         *
         * */
        super(0, new MySslRMIClientSocketFactory(),
                new SslRMIServerSocketFactory(getContext(),
                        new String[] { "SSL_RSA_WITH_RC4_128_MD5" },
                        new String[] { "TLSv1" }, true));
    }

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Override
    public String sayHello() throws RemoteException {
        //在这里我没什么好说的··
        return "Hello World";
    }

    public static SSLContext getContext() throws Exception {
        String key = "D:\\keys\\server.jks";
        //服务器端的秘钥
        String trust = "D:\\keys\\trustserver.jks";
        //服务器端信任的证书
        KeyStore keyStore = KeyStore.getInstance("JKS");
        //使用JKS的keyStore
        /*加载服务器的秘钥开始*/
        keyStore.load(new FileInputStream(key), "123qwe,.".toCharArray());
        KeyStore trustStore = KeyStore.getInstance("JKS");
        /*加载服务器的秘钥结束*/
        /*加载服务器信任的证书开始*/
        trustStore.load(new FileInputStream(trust), "123qwe,.".toCharArray());
        /*加载服务器信任的证书结束*/

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        //创建了秘钥管理工厂
        kmf.init(keyStore, "123qwe,.".toCharArray());
        //用服务器的秘钥来初始化秘钥工厂
        TrustManagerFactory tmf = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
        //建立了信任证书工厂
        tmf.init(trustStore);
        //用服务器信任的证书来初始化
        SSLContext sslc = SSLContext.getInstance("TLSv1");
        //获得context实例
        sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(),
                new SecureRandom());
        //初始化context
        return sslc;
    }

    /**
     * 在这里我不使用Naming了，而是直接使用registry来绑定，其实功能是一样的，但是这个方式更适合不指定url的访问方式
     * */
    public static void main(String[] args) throws Exception {
        //获得注册的registry，因为我注册的registry也使用了ssl，所以这里也要在参数中加入new MySslRMIClientSocketFactory()
        Registry registry = LocateRegistry.getRegistry(null, 3000, new MySslRMIClientSocketFactory());
        HelloImpl helloImpl = new HelloImpl();
        //绑定
        registry.bind("HelloServer", helloImpl);
        System.out.println("HelloServer bound in registry");
    }

}
