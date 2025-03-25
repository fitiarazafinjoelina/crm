package site.easy.to.build.crm.entity.temp;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trigger_ticket")
public class TicketPersist{
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

    public TicketPersist() {
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
}
