package com.example.charitybe.Config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Configuration class for blockchain integration
 * Loads blockchain properties from application.properties
 */
@Configuration
@ConfigurationProperties(prefix = "blockchain")
@EnableAsync
@Data
public class BlockchainConfig {

    private boolean enabled;
    private Network network;
    private Rpc rpc;
    private Wallet wallet;
    private Contract contract;
    private Long gasPrice;
    private Long gasLimit;
    private Async async;
    private Retry retry;

    @Data
    public static class Network {
        private String name;
        private Long chainId;
    }

    @Data
    public static class Rpc {
        private String url;
    }

    @Data
    public static class Wallet {
        private String privateKey;
        private String address;
    }

    @Data
    public static class Contract {
        private String address;
    }

    @Data
    public static class Async {
        private boolean enabled;
    }

    @Data
    public static class Retry {
        private int maxAttempts;
        private long delay;
    }
}
