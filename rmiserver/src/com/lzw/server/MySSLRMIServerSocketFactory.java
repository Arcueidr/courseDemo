package com.lzw.server;

import javax.net.ssl.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.rmi.server.RMIServerSocketFactory;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @author Administrator
 */
public class MySSLRMIServerSocketFactory implements RMIServerSocketFactory {

    private SSLServerSocketFactory sf = null;

    public MySSLRMIServerSocketFactory() {
        try {
            sf = getSSLServerSocketFactory();
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Override
    public ServerSocket createServerSocket(int port) throws IOException {
        // TODO Auto-generated method stub
        SSLServerSocket ss = (SSLServerSocket)sf.createServerSocket(port);
        ss.setNeedClientAuth(true);
        return ss;
    }

    private SSLServerSocketFactory getSSLServerSocketFactory()
            throws UnrecoverableKeyException, KeyStoreException,
            NoSuchAlgorithmException, CertificateException, IOException,
            KeyManagementException {
        SSLServerSocketFactory ssf = null;

        //服务端密钥加载
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("D:\\keys\\server.jks"),"serverpass".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "serverpass".toCharArray());

        //服务端信任证书加载
        KeyStore tks = KeyStore.getInstance("JKS");
        tks.load(new FileInputStream("D:\\keys\\server.jks"),"serverpass".toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(tks);


        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        ssf = sslContext.getServerSocketFactory();
        return ssf;
    }
}
