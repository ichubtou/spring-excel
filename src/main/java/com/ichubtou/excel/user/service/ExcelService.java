package com.ichubtou.excel.user.service;

import com.ichubtou.excel.user.entity.User;
import com.ichubtou.excel.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final UserRepository userRepository;

    public void uploadExcel(MultipartFile file) {
        List<User> memberList = new ArrayList<>();

        try {
            Workbook workbook = WorkbookFactory.create(file.getInputStream());
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() > 2) {
                    User user = User.builder()
                            .id(row.getCell(1).getStringCellValue())
                            .pw(row.getCell(2).getStringCellValue())
                            .storeName(row.getCell(3).getStringCellValue())
                            .positionName(row.getCell(4).getStringCellValue())
                            .userName(row.getCell(5).getStringCellValue())
                            .build();

                    memberList.add(user);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("");
        }

        memberList.remove(0);

        userRepository.saveAll(memberList);

    }

    public byte[] downloadExcel() throws IOException {
        List<User> all = userRepository.findAll();
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Users");

            // 헤더 스타일 생성
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setBorderTop(BorderStyle.THIN);
            headerStyle.setBorderBottom(BorderStyle.THIN);
            headerStyle.setBorderLeft(BorderStyle.THIN);
            headerStyle.setBorderRight(BorderStyle.THIN);

            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 10);
            font.setBold(true);
            headerStyle.setFont(font);

            // 헤더 행 생성
            String[] headers = {"User ID", "ID", "PW", "지점(점포)", "직책", "성명"};

            Row headerRow = sheet.createRow(0);
            for (int col = 0; col < headers.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(headers[col]);
                cell.setCellStyle(headerStyle);
            }

            // 데이터 행 스타일
            CellStyle dataStyle = workbook.createCellStyle();
            dataStyle.setBorderTop(BorderStyle.THIN);
            dataStyle.setBorderBottom(BorderStyle.THIN);
            dataStyle.setBorderLeft(BorderStyle.THIN);
            dataStyle.setBorderRight(BorderStyle.THIN);

            // 데이터 행 생성
            int rowIdx = 1;
            for (User user : all) {
                Row row = sheet.createRow(rowIdx++);

                createStyledCell(row, 0, user.getUserId().toString(), dataStyle);
                createStyledCell(row, 1, user.getId(), dataStyle);
                createStyledCell(row, 2, user.getPw(), dataStyle);
                createStyledCell(row, 3, user.getStoreName(), dataStyle);
                createStyledCell(row, 4, user.getPositionName(), dataStyle);
                createStyledCell(row, 5, user.getUserName(), dataStyle);
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 엑셀 파일 쓰기
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void createStyledCell(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

}
