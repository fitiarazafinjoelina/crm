package site.easy.to.build.crm.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class CsvService {

    public void readCsv(String filePath) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                System.out.println(String.join(", ", record));
            }
        }
    }
    public List<Object> readCsvObject(String filePath,Object clazz) throws IOException {
        try (FileReader reader = new FileReader(filePath)) {
            CsvToBean<Object> csvToBean = new CsvToBeanBuilder<Object>(reader)
                    .withType(clazz.getClass())
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }
}
