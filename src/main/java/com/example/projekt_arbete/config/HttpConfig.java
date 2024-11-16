/*package com.example.projekt_arbete.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpConfig {

    // TODO - explore more about this
    private Connector createHttpConnector () {
        Connector connector = new Connector(TomcatServletWebServerFactory.DEFAULT_PROTOCOL);
        connector.setPort(8080);
        connector.setScheme("http");
        connector.setSecure(false);
        return connector;
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> servletContainer () {

        return factory -> factory.addAdditionalTomcatConnectors(createHttpConnector());
    }
}*/
