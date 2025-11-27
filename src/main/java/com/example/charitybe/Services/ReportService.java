package com.example.charitybe.Services;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.example.charitybe.dto.quyengop.QuyenGopReportData;
import com.example.charitybe.entities.QuyenGop;
import com.example.charitybe.mapper.QuyenGopMapper;
import com.example.charitybe.repositories.QuyenGopRepository;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;

@Service
@RequiredArgsConstructor
public class ReportService {

    @Autowired
    private ResourceLoader resourceLoader;
    @Autowired
    private QuyenGopRepository quyenGopRepository;
    private final QuyenGopMapper quyenGopMapper;

    private JasperReport loadAndCompile(String templateName) throws Exception {
        // Tìm file .jrxml trong thư mục src/main/resources/reports/

        // Lưu ý: Nếu bạn sử dụng file đã được biên dịch sẵn (.jasper), bạn có thể tải
        // trực tiếp
        // nhưng đây là cách phổ biến để biên dịch từ JRXML khi cần.

        // Tải file .jrxml
        String path = "reports/" + templateName;

        // Lấy tài nguyên từ classpath
        java.io.InputStream inputStream = resourceLoader.getResource("classpath:" + path).getInputStream();

        // Biên dịch JRXML thành JasperReport
        JasperReport jasperReport = net.sf.jasperreports.engine.JasperCompileManager.compileReport(inputStream);

        return jasperReport;
    }

    public byte[] generateDonationReport(LocalDate startDate, LocalDate endDate, String format) throws Exception {
        // 1. Lấy dữ liệu từ DB, lọc theo ngày
        List<QuyenGop> rawData = quyenGopRepository.findByNgayTaoBetween(startDate, endDate);

        // 2. Chuyển đổi sang ReportDataModel (bao gồm cả logic mapping tên dự án/người
        // dùng)
        List<QuyenGopReportData> reportData = quyenGopMapper.convertToReportData(rawData);

        // 3. Tải và biên dịch (chỉ biên dịch nếu chưa có file .jasper)
        JasperReport jasperReport = loadAndCompile("donation_report.jrxml");

        // 4. Điền dữ liệu
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportData);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("ReportTitle", "BÁO CÁO TỪ NGÀY " + startDate.toString() + " ĐẾN " + endDate.toString());
        // Thêm các tham số khác...

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        // 5. Xuất file
        if (format.equalsIgnoreCase("pdf")) {
            return JasperExportManager.exportReportToPdf(jasperPrint);
        } else if (format.equalsIgnoreCase("xlsx")) {
            // Cần thư viện JExcelApiExporter hoặc POIExporter cho XLSX
            // Tạm thời dùng thư viện khác cho XLSX nếu cần:
            JRXlsxExporter exporter = new JRXlsxExporter();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
            exporter.exportReport();
            return outputStream.toByteArray();
        }
        throw new IllegalArgumentException("Định dạng không hợp lệ");
    }
}