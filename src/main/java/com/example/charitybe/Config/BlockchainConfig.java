package com.example.charitybe.Config;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class BlockchainConfig {

    private final BlockchainProperties props;

    @Bean
    @ConditionalOnProperty(prefix = "blockchain", name = "enabled", havingValue = "true")
    public Web3j web3j() {
        if (props.getRpcUrl() == null || props.getRpcUrl().isBlank()) {
            throw new IllegalStateException("Missing blockchain.rpcUrl configuration");
        }

        OkHttpClient httpClient = new OkHttpClient.2Builder()
                .connectTimeout(Duration.ofSeconds(10))
                .readTimeout(Duration.ofSeconds(30))
                .writeTimeout(Duration.ofSeconds(30))
                .build();

        HttpService httpService = new HttpService(props.getRpcUrl(), httpClient, false);
        return Web3j.build(httpService);
    }
}

