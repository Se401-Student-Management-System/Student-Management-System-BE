package com.example.studentmanagement.designpattern.command;

import com.example.studentmanagement.dto.cashier.PaymentRecordDTO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ExportPaymentRecordToExcelCommand implements PaymentCommand {
    private List<PaymentRecordDTO> records;
    private byte[] result;

    public ExportPaymentRecordToExcelCommand(List<PaymentRecordDTO> records) {
        this.records = records;
    }

    @Override
    public void execute() {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Payment Records");

            // Header
            Row headerRow = sheet.createRow(0);
            String[] headers = {"Mã hóa đơn", "Mã học sinh", "Họ và tên", "Năm học", "Tổng học phí", "Đã thanh toán", "Số tiền còn nợ", "Trạng thái"};
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(getHeaderStyle(workbook));
            }

            // Data
            int rowNum = 1;
            for (PaymentRecordDTO record : records) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(record.getInvoiceId());
                row.createCell(1).setCellValue(record.getStudentId());
                row.createCell(2).setCellValue(record.getStudentName());
                row.createCell(3).setCellValue(record.getAcademicYear());
                row.createCell(4).setCellValue(record.getTotalFee());
                row.createCell(5).setCellValue(record.getPaidAmount());
                row.createCell(6).setCellValue(record.getOutstandingAmount());
                row.createCell(7).setCellValue(record.getStatus());
            }

            // Auto-size columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            result = out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo file Excel: " + e.getMessage());
        }
    }

    public byte[] getResult() {
        return result;
    }

    private CellStyle getHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }
}