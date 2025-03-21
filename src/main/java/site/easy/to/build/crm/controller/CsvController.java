package site.easy.to.build.crm.controller;

import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.entity.temp.CustomerLoginInfoTemp;
import site.easy.to.build.crm.service.csv.CsvDataService;
import site.easy.to.build.crm.service.csv.CsvPersist;
import site.easy.to.build.crm.service.csv.CsvService;
import site.easy.to.build.crm.service.export.PdfExporter;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Controller
@RequestMapping("/csv")
public class CsvController {

    @Autowired
    private CsvService csvService;
    @Autowired
    private CsvPersist csvPersist;
    @Autowired
    private PdfExporter pdfExporter;
    @Autowired
    private CsvDataService<CustomerLoginInfoTemp, CustomerLoginInfo> csvDataGenericService;

    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println(file.getOriginalFilename());
            csvService.readCsv(file.getInputStream());
            return "redirect:/";
        } catch (IOException | CsvException e) {
            e.printStackTrace();
            return "error/500";
        }
    }
    @PostMapping("/uploadCustomerLogInfo")
    public String uploadCsvCustomerLogInfo(@RequestParam("file") MultipartFile file) {
        try {
            System.out.println(file.getOriginalFilename());
            //List<CustomerLoginInfo> list = csvService.readCsvObject(file.getInputStream(), CustomerLoginInfo.class);
            //List<CustomerLoginInfo> list=csvPersist.persistCustomerLoginInfo(file.getInputStream());
            List<CustomerLoginInfoTemp> list=csvDataGenericService.persistCsvData(file.getInputStream(), CustomerLoginInfoTemp.class,CustomerLoginInfo.class);
            csvService.writeCsv(list,"custom.csv");
            pdfExporter.exportPdf(list,"test.pdf");
            for (CustomerLoginInfoTemp customerLoginInfo : list) {
                System.out.println(customerLoginInfo.getUsername()+" "+customerLoginInfo.getPassword()+" "+customerLoginInfo.getPasswordSet());
            }
            return "redirect:/";
        } catch (IOException | SQLException e) {
            e.printStackTrace();
            return "error/500";
        }
    }
}
