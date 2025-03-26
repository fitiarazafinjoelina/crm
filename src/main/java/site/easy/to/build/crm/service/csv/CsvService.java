package site.easy.to.build.crm.service.csv;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    public <T> List<T> readCsvObject2(InputStream inputStream, Class<T> clazz,String file) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        }
    }
   /* public <T> List<T> readCsvObject(InputStream inputStream, Class<T> clazz,String file) throws Exception {
        List<T> validRecords = new ArrayList<>();
        Set<String> errors = new HashSet<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            int lineNumber = 0;

            // Read lines one by one
            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Skip header line if needed
                if (lineNumber == 1) {
                    continue; // Skip header or remove this if there's no header
                }

                // Parse the current line
                try {
                    T record = parseCsvLine(line, clazz);
                    if (record != null) {
                        validRecords.add(record);
                    } else {
                        errors.add("CSV parse error in file"+file+" at line "+lineNumber+":"+" Invalid format");
                    }
                } catch (Exception e) {
                    errors.add("CSV parse error in file"+file+" at line "+lineNumber+":"+ e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new Exception("Error reading CSV file: " + e.getMessage(), e);
        }
        if (!errors.isEmpty()) {
            throw new site.easy.to.build.crm.entity.exceptions.CsvException("CSV ERROR",errors);
        }

        return validRecords;
    }

    private <T> T parseCsvLine(String line, Class<T> clazz) throws Exception {
        try (StringReader stringReader = new StringReader(line)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(stringReader)
                    .withType(clazz)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            List<T> records = csvToBean.parse();
            return (records.isEmpty()) ? null : records.get(0);
        }
    }*/
    //line by line with header
    public <T> List<T> readCsvObject(InputStream inputStream, Class<T> clazz,String file) throws Exception {
        List<T> validRecords = new ArrayList<>();
        List<String> errors = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            // Read header line
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new Exception("CSV file is empty");
            }

            // Set up mapping strategy once
            HeaderColumnNameMappingStrategy<T> strategy = new HeaderColumnNameMappingStrategy<>();
            strategy.setType(clazz);

            int lineNumber = 1;
            String line;

            while ((line = reader.readLine()) != null) {
                lineNumber++;
                try {
                    T record = parseCsvLine(headerLine, line, strategy, lineNumber);
                    if (record != null) {
                        validRecords.add(record);
                    }
                } catch (Exception e) {
                    errors.add("File "+file+" Line " + lineNumber + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new Exception("Error reading CSV file: " + e.getMessage(), e);
        }

        if (!errors.isEmpty()) {
            throw new Exception("CSV errors:\n" + String.join("\n", errors));
        }

        return validRecords;
    }

    private <T> T parseCsvLine(String headerLine, String dataLine,
                               HeaderColumnNameMappingStrategy<T> strategy,
                               int lineNumber) throws Exception {

        String csvContent = headerLine + "\n" + dataLine;

        try (StringReader reader = new StringReader(csvContent)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withMappingStrategy(strategy)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .withThrowExceptions(true)
                    .build();

            List<T> results = csvToBean.parse();
            if (!results.isEmpty()) {
                return results.get(0);
            }
            throw new Exception("Empty record");
        } catch (RuntimeException e) {
            throw new Exception("Parsing failed: " + e.getMessage());
        }
    }
    public <T> List<T> readCsvObjectUnique(InputStream inputStream, Class<T> clazz) throws Exception {
        try (InputStreamReader reader = new InputStreamReader(inputStream)) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(clazz)
                    .withSeparator(',')
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            List<T> parsedList = csvToBean.parse();

            return new HashSet<>(parsedList).stream().toList();
        }
    }

    public <T> void writeCsv(List<T> objects, String filePath) {
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
