package site.easy.to.build.crm.entity.temp;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "trigger_ticket_temp")
public class TicketTemp implements CsvClass{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private int ticketId;

    @Column(name = "subject")
    @NotBlank(message = "Subject is required")
    @CsvBindByName(column = "subject")
    private String subject;

    @Column(name = "description")
    @CsvBindByName(column = "description")
    private String description;

    @Column(name = "status")
    @CsvBindByName(column = "status")
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(open|assigned|on-hold|in-progress|resolved|closed|reopened|pending-customer-response|escalated|archived)$", message = "Invalid status")
    private String status;

    @Column(name = "priority")
    @CsvBindByName(column = "priority")
    @NotBlank(message = "Priority is required")
    @Pattern(regexp = "^(low|medium|high|closed|urgent|critical)$", message = "Invalid priority")
    private String priority;

    @Column(name = "manager_id")
    @CsvBindByName(column = "manager_id")
    private Integer managerId;

    @Column(name = "employee_id")
    @CsvBindByName(column = "employee_id")
    private Integer employeeId;

    @Column(name = "customer_id")
    @CsvBindByName(column = "customer_id")
    private Integer customerId;

    @Column(name = "created_at")
    @CsvBindByName(column = "created_at")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @NotNull(message = "Amount is required")
    @Digits(integer = 16, fraction = 2, message = "Amount must be a valid number with up to 2 decimal places")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than or equal to 0.00")
    //@DecimalMax(value = "9999999.99", inclusive = true, message = "Amount must be less than or equal to 9999999.99")
    @Column(name = "amount")
    @CsvBindByName(column = "amount")
    private BigDecimal amount;


    public static String[] getAllStatus(){
        String all = "open|assigned|on-hold|in-progress|resolved|closed|reopened|pending-customer-response|escalated|archived";
        return all.split("\\|");
    }
    public static String[] getAllPriority(){
        String all ="low|medium|high|closed|urgent|critical";
        return all.split("\\|");
    }

    public TicketTemp() {
    }

    public int getTicketId() {
        return ticketId;
    }

    public void setTicketId(int ticketId) {
        this.ticketId = ticketId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String getTempTableName() {
        return "trigger_ticket_temp";
    }

    @Override
    public String getTempTable() {
        return "create temporary table if not exists trigger_ticket_temp\n" +
                "(\n" +
                "    ticket_id   int unsigned auto_increment\n" +
                "        primary key,\n" +
                "    subject     varchar(255)   null,\n" +
                "    description text           null,\n" +
                "    status      varchar(50)    null,\n" +
                "    priority    varchar(50)    null,\n" +
                "    customer_id int unsigned   not null,\n" +
                "    manager_id  int            null,\n" +
                "    employee_id int            null,\n" +
                "    created_at  datetime       null,\n" +
                "    amount      decimal(16, 2) null\n" +
                ");";
    }

    @Override
    public boolean isValid() {
        return getAmount().compareTo(BigDecimal.ZERO) > 0 && getCustomerId()!=null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketTemp ticket = (TicketTemp) o;
        return ticket.getAmount().compareTo(amount) == 0 &&
                Objects.equals(subject, ticket.getSubject()) &&
                Objects.equals(customerId, ticket.getCustomerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(subject, customerId, amount);
    }
}
