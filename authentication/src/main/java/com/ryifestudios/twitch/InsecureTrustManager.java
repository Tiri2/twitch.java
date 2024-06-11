package com.ryifestudios.twitch;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.net.ssl.SSLEngine;
import javax.net.ssl.X509ExtendedTrustManager;
import java.net.Socket;
import java.security.cert.X509Certificate;

public class InsecureTrustManager extends X509ExtendedTrustManager {

    private final Logger logger = LogManager.getLogger();


    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
        logger.traceEntry("certs={}, s={}, socket={}", x509Certificates, s, socket);
        logger.traceExit();
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) {
        logger.traceEntry("certs={}, s={}, socket={}", x509Certificates, s, socket);
        logger.traceExit();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
        logger.traceEntry("certs={}, s={}, sslEngine={}", x509Certificates, s, sslEngine);
        logger.traceExit();
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) {
        logger.traceEntry("certs={}, s={}, sslEngine={}", x509Certificates, s, sslEngine);
        logger.traceExit();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
        logger.traceEntry("certs={}, s={}", x509Certificates, s);
        logger.traceExit();
    }

    @Override
    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
        logger.traceEntry("certs={}, s={}", x509Certificates, s);
        logger.traceExit();
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
}

