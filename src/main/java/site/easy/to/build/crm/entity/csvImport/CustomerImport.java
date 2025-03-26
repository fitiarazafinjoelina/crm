package site.easy.to.build.crm.entity.csvImport;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import site.easy.to.build.crm.customValidations.customer.UniqueEmail;

@Entity
@Table(name = "customer_import")
public class CustomerImport {

    public interface CustomerImportValidator{}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "customer_email")
    //@CsvBindByPosition(position = 0)
    @CsvBindByName(column = "customer_email")
    @NotBlank(message = "Email required", groups = CustomerImportValidator.class)
    @UniqueEmail(message = "Email must be unique", groups = CustomerImportValidator.class)
    private String customerEmail;

    @Column(name = "customer_name")
    //@CsvBindByPosition(position = 1)
    @CsvBindByName(column = "customer_name")
    @NotBlank(message = "Name required", groups = CustomerImportValidator.class)
    private String customerName;



    public CustomerImport() {
    }

    public CustomerImport(Integer id, String customerEmail, String customerName) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.customerName = customerName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}

