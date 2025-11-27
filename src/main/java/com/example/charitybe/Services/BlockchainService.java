package com.example.charitybe.Services;

import com.example.charitybe.Config.BlockchainConfig;
import com.example.charitybe.entities.GiaiNgan;
import com.example.charitybe.entities.QuyenGop;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;

/**
 * Service for interacting with blockchain smart contract
 */
@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "blockchain.enabled", havingValue = "true")
public class BlockchainService {

    private final Web3j web3j;
    private final Credentials credentials;
    private final BlockchainConfig blockchainConfig;
    private final DefaultGasProvider gasProvider;

    /**
     * Record a donation on blockchain
     *
     * @param quyenGop The donation entity
     * @return Transaction hash
     */
    public String recordDonation(QuyenGop quyenGop) {
        try {
            log.info("Recording donation {} on blockchain", quyenGop.getId());

            // Check if credentials are available
            if (credentials == null) {
                log.warn("Blockchain credentials not configured. Skipping blockchain recording.");
                return null;
            }

            // Get contract address
            String contractAddress = blockchainConfig.getContract().getAddress();
            if (contractAddress == null || contractAddress.isEmpty() ||
                contractAddress.equals("DEPLOYED_CONTRACT_ADDRESS_HERE")) {
                log.warn("Contract address not configured. Skipping blockchain recording.");
                return null;
            }

            // Prepare function call: recordDonation(uint256,address,uint256,uint256,string,string,string)
            Function function = new Function(
                "recordDonation",
                Arrays.asList(
                    new Uint256(quyenGop.getId()),                                           // _id
                    new Address(credentials.getAddress()),                                   // _donor (using wallet address)
                    new Uint256(quyenGop.getMaDuAn()),                                      // _projectId
                    new Uint256(quyenGop.getSoTien().toBigInteger()),                       // _amount
                    new Utf8String(quyenGop.getDonViTienTe() != null ?
                        quyenGop.getDonViTienTe() : "VND"),                                 // _currency
                    new Utf8String(quyenGop.getMaGiaoDich() != null ?
                        quyenGop.getMaGiaoDich() : ""),                                     // _transactionCode
                    new Utf8String(quyenGop.getPhuongThucThanhToan().name())                // _paymentMethod
                ),
                Arrays.asList()
            );

            // Encode function
            String encodedFunction = FunctionEncoder.encode(function);

            // Get nonce
            BigInteger nonce = web3j.ethGetTransactionCount(
                credentials.getAddress(),
                DefaultBlockParameterName.PENDING
            ).send().getTransactionCount();

            // Create raw transaction
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasProvider.getGasPrice("recordDonation"),
                gasProvider.getGasLimit("recordDonation"),
                contractAddress,
                encodedFunction
            );

            // Sign transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            // Send transaction
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

            if (ethSendTransaction.hasError()) {
                log.error("Error sending transaction: {}", ethSendTransaction.getError().getMessage());
                return null;
            }

            String transactionHash = ethSendTransaction.getTransactionHash();
            log.info("Donation {} recorded on blockchain. TxHash: {}", quyenGop.getId(), transactionHash);

            return transactionHash;

        } catch (Exception e) {
            log.error("Error recording donation on blockchain: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Record a disbursement on blockchain
     *
     * @param giaiNgan The disbursement entity
     * @return Transaction hash
     */
    public String recordDisbursement(GiaiNgan giaiNgan) {
        try {
            log.info("Recording disbursement {} on blockchain", giaiNgan.getId());

            // Check if credentials are available
            if (credentials == null) {
                log.warn("Blockchain credentials not configured. Skipping blockchain recording.");
                return null;
            }

            // Get contract address
            String contractAddress = blockchainConfig.getContract().getAddress();
            if (contractAddress == null || contractAddress.isEmpty() ||
                contractAddress.equals("DEPLOYED_CONTRACT_ADDRESS_HERE")) {
                log.warn("Contract address not configured. Skipping blockchain recording.");
                return null;
            }

            // Determine approver address (use wallet address as placeholder)
            String approverAddress = credentials.getAddress();

            // Prepare function call: recordDisbursement(uint256,uint256,uint256,string,string,string,address)
            Function function = new Function(
                "recordDisbursement",
                Arrays.asList(
                    new Uint256(giaiNgan.getId()),                                          // _id
                    new Uint256(giaiNgan.getMaDuAn()),                                      // _projectId
                    new Uint256(giaiNgan.getSoTien().toBigInteger()),                       // _amount
                    new Utf8String(giaiNgan.getLoaiGiaiNgan().name()),                      // _disbursementType
                    new Utf8String(giaiNgan.getNguoiNhan()),                                // _recipient
                    new Utf8String(giaiNgan.getMucDichSuDung()),                            // _purpose
                    new Address(approverAddress)                                            // _approver
                ),
                Arrays.asList()
            );

            // Encode function
            String encodedFunction = FunctionEncoder.encode(function);

            // Get nonce
            BigInteger nonce = web3j.ethGetTransactionCount(
                credentials.getAddress(),
                DefaultBlockParameterName.PENDING
            ).send().getTransactionCount();

            // Create raw transaction
            RawTransaction rawTransaction = RawTransaction.createTransaction(
                nonce,
                gasProvider.getGasPrice("recordDisbursement"),
                gasProvider.getGasLimit("recordDisbursement"),
                contractAddress,
                encodedFunction
            );

            // Sign transaction
            byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, credentials);
            String hexValue = Numeric.toHexString(signedMessage);

            // Send transaction
            EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).send();

            if (ethSendTransaction.hasError()) {
                log.error("Error sending transaction: {}", ethSendTransaction.getError().getMessage());
                return null;
            }

            String transactionHash = ethSendTransaction.getTransactionHash();
            log.info("Disbursement {} recorded on blockchain. TxHash: {}", giaiNgan.getId(), transactionHash);

            return transactionHash;

        } catch (Exception e) {
            log.error("Error recording disbursement on blockchain: {}", e.getMessage(), e);
            return null;
        }
    }

    /**
     * Get transaction receipt by hash
     *
     * @param transactionHash Transaction hash
     * @return TransactionReceipt if found
     */
    public Optional<TransactionReceipt> getTransactionReceipt(String transactionHash) {
        try {
            EthGetTransactionReceipt receipt = web3j
                .ethGetTransactionReceipt(transactionHash)
                .send();

            return receipt.getTransactionReceipt();

        } catch (Exception e) {
            log.error("Error getting transaction receipt: {}", e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Check if blockchain service is properly configured
     *
     * @return true if configured
     */
    public boolean isConfigured() {
        String contractAddress = blockchainConfig.getContract().getAddress();
        return credentials != null &&
               contractAddress != null &&
               !contractAddress.isEmpty() &&
               !contractAddress.equals("DEPLOYED_CONTRACT_ADDRESS_HERE");
    }

    /**
     * Get current block number
     *
     * @return Block number
     */
    public BigInteger getCurrentBlockNumber() {
        try {
            return web3j.ethBlockNumber().send().getBlockNumber();
        } catch (Exception e) {
            log.error("Error getting block number: {}", e.getMessage());
            return BigInteger.ZERO;
        }
    }
}
