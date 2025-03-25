package site.easy.to.build.crm.entity.csvImport;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.validation.groups.Default;
import java.math.BigDecimal;

@Entity
@Table(name = "ticket_lead_import")
public class TicketLeadImport {

    public interface TicketLeadImportValidator {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(name = "customer_email")
    @CsvBindByName(column = "customer_email")
    @NotBlank(message = "Customer email is required", groups = {Default.class, TicketLeadImportValidator.class})
    private String customerEmail;

    @Column(name = "subject_or_name")
    @CsvBindByName(column = "subject_or_name")
    private String subjectOrName;

    @Column
    @CsvBindByName
    @NotBlank(message = "Type is required", groups = {Default.class, TicketLeadImportValidator.class})
    @Pattern(regexp = "^(lead|ticket)$", message = "Invalid type")
    private String type;

    @Column
    @CsvBindByName
    @NotBlank(message = "Status is required", groups = {Default.class, TicketLeadImportValidator.class})
    @Pattern(regexp = "^(open|assigned|on-hold|in-progress|resolved|closed|reopened|pending-customer-response|escalated|meeting-to-schedule|scheduled|archived|success|assign-to-sales)$", message = "Invalid status")
    private String status;

    @Column
    @CsvBindByName
    @NotNull(message = "Amount is required", groups = {Default.class, TicketLeadImportValidator.class})
    @CsvNumber("#,##")
    @DecimalMin(value = "0.00", inclusive = true, message = "Expense must be greater than or equal to 0.00")
    private BigDecimal expense;

    public TicketLeadImport() {
    }

    public TicketLeadImport(Integer id, String customerEmail, String subjectOrName, String type, String status, BigDecimal expense) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.subjectOrName = subjectOrName;
        this.type = type;
        this.status = status;
        this.expense = expense;
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

    public String getSubjectOrName() {
        return subjectOrName;
    }

    public void setSubjectOrName(String subjectOrName) {
        this.subjectOrName = subjectOrName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getExpense() {
        return expense;
    }

    public void setExpense(BigDecimal expense) {
        this.expense = expense;
    }
}
