package com.example.charitybe.Config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.gas.DefaultGasProvider;

import java.math.BigInteger;

/**
 * Web3j Configuration
 * Creates Web3j beans for interacting with Ethereum blockchain
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
@ConditionalOnProperty(name = "blockchain.enabled", havingValue = "true")
public class Web3jConfig {

    private final BlockchainConfig blockchainConfig;

    /**
     * Create Web3j instance connected to Sepolia RPC
     */
    @Bean
    public Web3j web3j() {
        String rpcUrl = blockchainConfig.getRpc().getUrl();
        log.info("Connecting to Ethereum RPC: {}", rpcUrl);

        Web3j web3j = Web3j.build(new HttpService(rpcUrl));

        // Test connection
        try {
            String clientVersion = web3j.web3ClientVersion().send().getWeb3ClientVersion();
            log.info("Connected to Ethereum network: {}", clientVersion);
        } catch (Exception e) {
            log.error("Failed to connect to Ethereum network: {}", e.getMessage());
        }

        return web3j;
    }

    /**
     * Create Credentials from private key
     */
    @Bean
    public Credentials credentials() {
        String privateKey = blockchainConfig.getWallet().getPrivateKey();

        if (privateKey == null || privateKey.isEmpty() || privateKey.equals("YOUR_PRIVATE_KEY_HERE")) {
            log.warn("Blockchain private key not configured. Using dummy credentials.");
            // Return null or throw exception based on your preference
            return null;
        }

        // Remove "0x" prefix if present
        if (privateKey.startsWith("0x")) {
            privateKey = privateKey.substring(2);
        }

        Credentials credentials = Credentials.create(privateKey);
        log.info("Loaded wallet address: {}", credentials.getAddress());

        return credentials;
    }

    /**
     * Custom Gas Provider based on configuration
     */
    @Bean
    public DefaultGasProvider gasProvider() {
        BigInteger gasPrice = BigInteger.valueOf(blockchainConfig.getGasPrice());
        BigInteger gasLimit = BigInteger.valueOf(blockchainConfig.getGasLimit());

        log.info("Gas Provider configured - Price: {} wei, Limit: {}", gasPrice, gasLimit);

        return new DefaultGasProvider() {
            @Override
            public BigInteger getGasPrice(String contractFunc) {
                return gasPrice;
            }

            @Override
            public BigInteger getGasLimit(String contractFunc) {
                return gasLimit;
            }
        };
    }
}
