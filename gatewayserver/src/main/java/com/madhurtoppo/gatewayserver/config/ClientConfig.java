package com.madhurtoppo.gatewayserver.config;

import com.madhurtoppo.gatewayserver.service.client.CustomerSummaryClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;


@Configuration
public class ClientConfig {

    @Value("${app.baseUrl}")
    private String baseUrl;


    /**
     * A bean to create a reactive web client to invoke the customer summary service.
     *
     * @return a reactive web client that implements the
     * {@link CustomerSummaryClient} interface
     */
    @Bean
    CustomerSummaryClient customerSummaryClient() {
        WebClient webClient = WebClient.builder().baseUrl(baseUrl).build();
        WebClientAdapter adapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
        return factory.createClient(CustomerSummaryClient.class);

    }

}
