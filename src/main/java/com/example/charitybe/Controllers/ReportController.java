package com.example.charitybe.Controllers;

import com.example.charitybe.Services.ReportService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports/donations")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/{format}") // Ví dụ: /api/reports/donations/pdf
    public ResponseEntity<byte[]> generateDonationReport(
            @PathVariable String format,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            byte[] reportBytes = reportService.generateDonationReport(startDate, endDate, format);

            HttpHeaders headers = new HttpHeaders();

            // Đặt Content Type và Header tải xuống
            if (format.equalsIgnoreCase("pdf")) {
                headers.setContentType(MediaType.APPLICATION_PDF);
            } else if (format.equalsIgnoreCase("xlsx")) {
                headers.setContentType(
                        MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            }

            headers.setContentDispositionFormData("filename",
                    "donation_report_" + startDate + "_" + endDate + "." + format);

            return new ResponseEntity<>(reportBytes, headers, HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}