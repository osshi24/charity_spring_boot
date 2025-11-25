package com.example.charitybe.Services;

import com.example.charitybe.Config.BlockchainConfig;
import com.example.charitybe.entities.GiaiNgan;
import com.example.charitybe.entities.QuyenGop;
import com.example.charitybe.repositories.GiaiNganRepository;
import com.example.charitybe.repositories.QuyenGopRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Optional;

/**
 * Asynchronous processor for blockchain operations
 * Handles background recording of transactions to blockchain
 */
@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(name = "blockchain.async.enabled", havingValue = "true")
public class AsyncBlockchainProcessor {

    private final BlockchainService blockchainService;
    private final QuyenGopRepository quyenGopRepository;
    private final GiaiNganRepository giaiNganRepository;
    private final BlockchainConfig blockchainConfig;

    /**
     * Asynchronously process donation and record on blockchain
     *
     * @param donationId ID of the donation
     */
    @Async
    public void processDonation(Long donationId) {
        log.info("Starting async blockchain processing for donation {}", donationId);

        try {
            // Check if blockchain is configured
            if (!blockchainService.isConfigured()) {
                log.warn("Blockchain not configured. Skipping donation {}", donationId);
                return;
            }

            // Fetch donation from database
            Optional<QuyenGop> optionalDonation = quyenGopRepository.findById(donationId);
            if (optionalDonation.isEmpty()) {
                log.error("Donation {} not found in database", donationId);
                return;
            }

            QuyenGop donation = optionalDonation.get();

            // Check if already recorded on blockchain
            if (donation.getBlockchainTxHash() != null &&
                "CONFIRMED".equals(donation.getBlockchainStatus())) {
                log.info("Donation {} already confirmed on blockchain", donationId);
                return;
            }

            // Record on blockchain with retry logic
            String txHash = recordWithRetry(() -> blockchainService.recordDonation(donation));

            if (txHash != null) {
                // Update entity with pending status
                donation.setBlockchainTxHash(txHash);
                donation.setBlockchainStatus("PENDING");
                quyenGopRepository.save(donation);

                log.info("Donation {} recorded on blockchain with txHash: {}", donationId, txHash);

                // Wait for transaction to be mined and update status
                waitAndUpdateDonationStatus(donation, txHash);

            } else {
                // Mark as failed
                donation.setBlockchainStatus("FAILED");
                quyenGopRepository.save(donation);
                log.error("Failed to record donation {} on blockchain", donationId);
            }

        } catch (Exception e) {
            log.error("Error processing donation {} on blockchain: {}", donationId, e.getMessage(), e);
        }
    }

    /**
     * Asynchronously process disbursement and record on blockchain
     *
     * @param disbursementId ID of the disbursement
     */
    @Async
    public void processDisbursement(Long disbursementId) {
        log.info("Starting async blockchain processing for disbursement {}", disbursementId);

        try {
            // Check if blockchain is configured
            if (!blockchainService.isConfigured()) {
                log.warn("Blockchain not configured. Skipping disbursement {}", disbursementId);
                return;
            }

            // Fetch disbursement from database
            Optional<GiaiNgan> optionalDisbursement = giaiNganRepository.findById(disbursementId);
            if (optionalDisbursement.isEmpty()) {
                log.error("Disbursement {} not found in database", disbursementId);
                return;
            }

            GiaiNgan disbursement = optionalDisbursement.get();

            // Check if already recorded on blockchain
            if (disbursement.getBlockchainTxHash() != null &&
                "CONFIRMED".equals(disbursement.getBlockchainStatus())) {
                log.info("Disbursement {} already confirmed on blockchain", disbursementId);
                return;
            }

            // Record on blockchain with retry logic
            String txHash = recordWithRetry(() -> blockchainService.recordDisbursement(disbursement));

            if (txHash != null) {
                // Update entity with pending status
                disbursement.setBlockchainTxHash(txHash);
                disbursement.setBlockchainStatus("PENDING");
                giaiNganRepository.save(disbursement);

                log.info("Disbursement {} recorded on blockchain with txHash: {}", disbursementId, txHash);

                // Wait for transaction to be mined and update status
                waitAndUpdateDisbursementStatus(disbursement, txHash);

            } else {
                // Mark as failed
                disbursement.setBlockchainStatus("FAILED");
                giaiNganRepository.save(disbursement);
                log.error("Failed to record disbursement {} on blockchain", disbursementId);
            }

        } catch (Exception e) {
            log.error("Error processing disbursement {} on blockchain: {}", disbursementId, e.getMessage(), e);
        }
    }

    /**
     * Wait for transaction to be mined and update donation status
     */
    private void waitAndUpdateDonationStatus(QuyenGop donation, String txHash) {
        try {
            // Wait for transaction receipt (poll for up to 2 minutes)
            int maxAttempts = 24; // 24 attempts * 5 seconds = 2 minutes
            int attempt = 0;

            while (attempt < maxAttempts) {
                Thread.sleep(5000); // Wait 5 seconds

                Optional<TransactionReceipt> receiptOpt = blockchainService.getTransactionReceipt(txHash);

                if (receiptOpt.isPresent()) {
                    TransactionReceipt receipt = receiptOpt.get();

                    // Check if transaction was successful
                    boolean isSuccess = receipt.isStatusOK();

                    if (isSuccess) {
                        donation.setBlockchainStatus("CONFIRMED");
                        donation.setBlockchainBlockNumber(receipt.getBlockNumber().longValue());

                        // Convert timestamp if available
                        if (receipt.getBlockNumber() != null) {
                            donation.setBlockchainTimestamp(OffsetDateTime.now());
                        }

                        log.info("Donation {} confirmed on blockchain at block {}",
                            donation.getId(), receipt.getBlockNumber());
                    } else {
                        donation.setBlockchainStatus("FAILED");
                        log.error("Donation {} transaction failed on blockchain", donation.getId());
                    }

                    quyenGopRepository.save(donation);
                    return;
                }

                attempt++;
            }

            // Timeout - transaction not mined yet
            log.warn("Timeout waiting for donation {} transaction confirmation", donation.getId());

        } catch (Exception e) {
            log.error("Error waiting for donation {} transaction: {}", donation.getId(), e.getMessage());
        }
    }

    /**
     * Wait for transaction to be mined and update disbursement status
     */
    private void waitAndUpdateDisbursementStatus(GiaiNgan disbursement, String txHash) {
        try {
            // Wait for transaction receipt (poll for up to 2 minutes)
            int maxAttempts = 24; // 24 attempts * 5 seconds = 2 minutes
            int attempt = 0;

            while (attempt < maxAttempts) {
                Thread.sleep(5000); // Wait 5 seconds

                Optional<TransactionReceipt> receiptOpt = blockchainService.getTransactionReceipt(txHash);

                if (receiptOpt.isPresent()) {
                    TransactionReceipt receipt = receiptOpt.get();

                    // Check if transaction was successful
                    boolean isSuccess = receipt.isStatusOK();

                    if (isSuccess) {
                        disbursement.setBlockchainStatus("CONFIRMED");
                        disbursement.setBlockchainBlockNumber(receipt.getBlockNumber().longValue());

                        // Convert timestamp if available
                        if (receipt.getBlockNumber() != null) {
                            disbursement.setBlockchainTimestamp(OffsetDateTime.now());
                        }

                        log.info("Disbursement {} confirmed on blockchain at block {}",
                            disbursement.getId(), receipt.getBlockNumber());
                    } else {
                        disbursement.setBlockchainStatus("FAILED");
                        log.error("Disbursement {} transaction failed on blockchain", disbursement.getId());
                    }

                    giaiNganRepository.save(disbursement);
                    return;
                }

                attempt++;
            }

            // Timeout - transaction not mined yet
            log.warn("Timeout waiting for disbursement {} transaction confirmation", disbursement.getId());

        } catch (Exception e) {
            log.error("Error waiting for disbursement {} transaction: {}", disbursement.getId(), e.getMessage());
        }
    }

    /**
     * Execute blockchain recording with retry logic
     */
    private String recordWithRetry(BlockchainOperation operation) {
        int maxAttempts = blockchainConfig.getRetry().getMaxAttempts();
        long delayMs = blockchainConfig.getRetry().getDelay();

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            try {
                String result = operation.execute();
                if (result != null) {
                    return result;
                }

                if (attempt < maxAttempts) {
                    log.warn("Attempt {} failed. Retrying in {} ms...", attempt, delayMs);
                    Thread.sleep(delayMs);
                }

            } catch (Exception e) {
                log.error("Attempt {} failed with exception: {}", attempt, e.getMessage());

                if (attempt < maxAttempts) {
                    try {
                        Thread.sleep(delayMs);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        return null;
    }

    @FunctionalInterface
    private interface BlockchainOperation {
        String execute() throws Exception;
    }
}
