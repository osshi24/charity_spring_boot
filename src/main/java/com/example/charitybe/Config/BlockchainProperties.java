package com.example.charitybe.Config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "blockchain")
public class BlockchainProperties {
    private boolean enabled = false;
    private String rpcUrl;
    private String privateKey;
    private String contractAddress;
    private String functionName = "recordDonation";
    private String readTotalFunctionName = "getProjectTotalCents";
    private BigInteger gasLimit; // optional
    private Long gasPriceGwei; // optional
}
