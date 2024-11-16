package com.example.projekt_arbete.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.netty.http.client.*;
import reactor.netty.tcp.SslProvider;

import javax.net.ssl.SSLException;


@Component
public class WebClientConfig {

    @Bean
    public WebClient.Builder webClientBuilder () {

        // Configuring the client to ignore self-signed certificates, such as mykeystore.p12, enabling https calls
        HttpClient httpClient = HttpClient.create()
                .secure(sslContextSpec -> {
                    try {
                        sslContextSpec.sslContext(SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE));
                    } catch (SSLException e) {
                        throw new RuntimeException(e);
                    }
                });

        return WebClient.builder().clientConnector(new ReactorClientHttpConnector(httpClient));
    }

}
