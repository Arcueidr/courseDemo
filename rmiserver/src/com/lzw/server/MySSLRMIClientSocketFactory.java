package com.lzw.server;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.rmi.server.RMIClientSocketFactory;
import java.security.*;
import java.security.cert.CertificateException;

/**
 * @author Administrator
 */
public class MySSLRMIClientSocketFactory implements RMIClientSocketFactory,Serializable {
    private static final long serialVersionUID = -4926828809111173712L;
    private SocketFactory sf = null;

    public MySSLRMIClientSocketFactory() {
// if using default socket factory here, you need specify the trust store as argument in java command like following:
// java -cp . -Djavax.net.ssl.trustStore="c:\\keystore4test" net.gy.java.rmi.ssl.SSLRMIClient

// sf = SSLSocketFactory.getDefault();

        try {
            sf = buildSSLSocketFactory();
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        // TODO Auto-generated method stub
        Socket sc = sf.createSocket(host, port);
        return sc;
    }

    private SSLSocketFactory buildSSLSocketFactory()
            throws NoSuchAlgorithmException, KeyStoreException, CertificateException,
            IOException, KeyManagementException, UnrecoverableKeyException {
        String passwd = "clientpass";

        //客户端密钥加载
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream("D:\\keys\\client.jks"),"clientpass".toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
        kmf.init(ks, "clientpass".toCharArray());

        //加载信任证书
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        KeyStore tks = KeyStore.getInstance("JKS");
        tks.load(new FileInputStream("D:\\keys\\clienttrust.jks"),passwd.toCharArray());
        tmf.init(tks);

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        return sslContext.getSocketFactory();
    }
}
