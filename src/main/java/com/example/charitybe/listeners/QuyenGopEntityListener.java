package com.example.charitybe.listeners;

import com.example.charitybe.Services.AsyncBlockchainProcessor;
import com.example.charitybe.entities.QuyenGop;
import com.example.charitybe.enums.TrangThaiThanhToan;
import jakarta.persistence.PostPersist;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * JPA Entity Listener for QuyenGop
 * Automatically triggers blockchain recording after entity is persisted to database
 */
@Slf4j
@Component
public class QuyenGopEntityListener {

    private static ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext context) {
        QuyenGopEntityListener.applicationContext = context;
    }

    /**
     * Triggered after QuyenGop entity is persisted to database
     * This happens AFTER the transaction is committed
     */
    @PostPersist
    public void afterPersist(QuyenGop quyenGop) {
        // Only trigger blockchain for successful donations
        if (quyenGop.getTrangThai() != TrangThaiThanhToan.THANH_CONG) {
            log.debug("Skipping blockchain recording for donation {} - status: {}",
                quyenGop.getId(), quyenGop.getTrangThai());
            return;
        }

        try {
            // Get AsyncBlockchainProcessor from Spring context
            // We can't inject it directly because JPA listeners are not Spring beans
            AsyncBlockchainProcessor blockchainProcessor =
                applicationContext.getBean(AsyncBlockchainProcessor.class);

            log.info("Triggering blockchain recording for donation {} via EntityListener",
                quyenGop.getId());

            // Trigger async blockchain processing
            blockchainProcessor.processDonation(quyenGop.getId());

        } catch (Exception e) {
            log.error("Error triggering blockchain recording for donation {}: {}",
                quyenGop.getId(), e.getMessage(), e);
            // Don't throw exception - we don't want to rollback the transaction
        }
    }
}
