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

    public void exportPdf(Object data, String filePath) {
        try {
            PdfWriter writer = new PdfWriter(filePath);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);


            if (data instanceof List<?> list) {
                document.add(new Paragraph(list.get(0).getClass().getSimpleName()));
                writePdfTableFromList((List<?>) data, document);
            } else {
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
    private static void writePdfTableFromList(List<?> objects, Document document) {
        if (objects.isEmpty()) {
            return;
        }

        Object firstObject = objects.get(0);
        Field[] fields = firstObject.getClass().getDeclaredFields();

        Table table = new Table(fields.length);

        for (Field field : fields) {
            table.addHeaderCell(field.getName());
        }


        for (Object object : objects) {
            for (Field field : fields) {
                field.setAccessible(true);
                try {
                    Object value = field.get(object);
                    table.addCell(value != null ? value.toString() : "");
                } catch (IllegalAccessException e) {
                    table.addCell("Error accessing field");
                }
            }
        }

        document.add(table);
    }

    private static void writePdfTableFromObject(Object object, Document document) {
        Field[] fields = object.getClass().getDeclaredFields();

        Table table = new Table(fields.length);

        for (Field field : fields) {
            table.addHeaderCell(field.getName());
        }

        for (Field field : fields) {
            field.setAccessible(true);
            try {
                Object value = field.get(object);
                table.addCell(value != null ? value.toString() : "");
            } catch (IllegalAccessException e) {
                table.addCell("Error accessing field");
            }
        }

        document.add(table);
    }
}
