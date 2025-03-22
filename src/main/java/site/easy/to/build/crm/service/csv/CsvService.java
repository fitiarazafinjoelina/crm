package site.easy.to.build.crm.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.List;

@Service
public class CsvService {

    public void readCsv(InputStream inputStream) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            List<String[]> records = reader.readAll();
            for (String[] record : records) {
                System.out.println(String.join(", ", record));
            }
        }
    }
    public <T> List<T> readCsvObject(InputStream inputStream, Class<T> clazz) throws IOException, CsvException {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }public <T> void writeCsv(List<T> objects, String filePath) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))) {


            if (!objects.isEmpty()) {
                Class<?> clazz = objects.get(0).getClass();
                Field[] fields = clazz.getDeclaredFields();

                String[] header = new String[fields.length];
                for (int i = 0; i < fields.length; i++) {
                    header[i] = fields[i].getName();
                }
                writer.writeNext(header);


                for (T object : objects) {
                    String[] row = new String[fields.length];
                    for (int i = 0; i < fields.length; i++) {
                        fields[i].setAccessible(true);  // Allow access to private fields
                        try {
                            row[i] = String.valueOf(fields[i].get(object));  // Get field value as String
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                    writer.writeNext(row);
                }
            }

            System.out.println("CSV file written successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error writing CSV file.");
        }
    }
}
