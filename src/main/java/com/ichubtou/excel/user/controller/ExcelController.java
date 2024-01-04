package com.ichubtou.excel.user.controller;

import com.ichubtou.excel.user.service.ExcelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelController {

    private final ExcelService excelService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(@RequestPart("file") MultipartFile file/*,
                                         @RequestPart("data") DataDto dataDto*/) {
        excelService.uploadExcel(file);

        return ResponseEntity.ok("");
    }

    @GetMapping("/download")
    public ResponseEntity<?> downloadExcel() throws IOException {
        byte[] bytes = excelService.downloadExcel();

        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        headers.setContentDispositionFormData("attachment", "users.xlsx");

        return ResponseEntity.ok().headers(headers).body(bytes);
    }

}
