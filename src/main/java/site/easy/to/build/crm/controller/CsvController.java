package site.easy.to.build.crm.controller;

import com.opencsv.exceptions.CsvException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import site.easy.to.build.crm.service.csv.CsvService;

import java.io.IOException;

@RestController
@RequestMapping("/csv")
public class CsvController {

    @Autowired
    private CsvService csvService;

    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            // Process the file
            csvService.readCsv(file.getInputStream().toString());
            return "File uploaded and processed successfully!";
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return "Error while uploading the file!";
        }
    }
}
