package site.easy.to.build.crm.service.export;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import jakarta.mail.Header;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.List;

@Service
public class PdfExporter {

    // Function to export PDF from a list of objects or a single object
    public void exportPdf(Object data, String filePath) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            // Check if the input is a list or a single object
            if (data instanceof List<?> list) {
                // If it's a list, handle the list of objects
                document.add(new Paragraph(list.get(0).getClass().getSimpleName()));
                writePdfTableFromList((List<?>) data, document);
            } else {
                // If it's a single object, handle one object
                document.add(new Paragraph(data.getClass().getSimpleName()));
                writePdfTableFromObject(data, document);
            }

            document.close();
            System.out.println("PDF exported successfully to " + filePath);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error generating PDF");
        }
    }

    // Function to export a table from a list of objects
    private static void writePdfTableFromList(List<?> objects, Document document) {
        if (objects.isEmpty()) {
            return;
        }

        // Get the first object in the list to fetch the class type
        Object firstObject = objects.get(0);
        Field[] fields = firstObject.getClass().getDeclaredFields();

        // Create the table with the number of columns equal to the number of fields
        Table table = new Table(fields.length);

        // Add headers (field names)
        for (Field field : fields) {
            table.addHeaderCell(field.getName());
        }

        // Add rows (field values)
        for (Object object : objects) {
            for (Field field : fields) {
                field.setAccessible(true);  // Make private fields accessible
                try {
                    Object value = field.get(object);  // Get the value of the field
                    table.addCell(value != null ? value.toString() : "");  // Add value to table cell
                } catch (IllegalAccessException e) {
                    table.addCell("Error accessing field");
                }
            }
        }

        // Add the table to the document
        document.add(table);
    }

    // Function to export a table from a single object
    private static void writePdfTableFromObject(Object object, Document document) {
        Field[] fields = object.getClass().getDeclaredFields();

        // Create the table with the number of columns equal to the number of fields
        Table table = new Table(fields.length);

        // Add headers (field names)
        for (Field field : fields) {
            table.addHeaderCell(field.getName());
        }

        // Add row (field values from the single object)
        for (Field field : fields) {
            field.setAccessible(true);  // Make private fields accessible
            try {
                Object value = field.get(object);  // Get the value of the field
                table.addCell(value != null ? value.toString() : "");  // Add value to table cell
            } catch (IllegalAccessException e) {
                table.addCell("Error accessing field");
            }
        }

        // Add the table to the document
        document.add(table);
    }
}
