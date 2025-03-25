package site.easy.to.build.crm.entity.temp;


import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import site.easy.to.build.crm.entity.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "trigger_lead_temp")
public class LeadTemp implements CsvClass{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_id")
    private int leadId;

    @Column(name = "name")
    @CsvBindByName(column = "name")
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "status")
    @CsvBindByName(column = "status")
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(meeting-to-schedule|scheduled|archived|success|assign-to-sales)$", message = "Invalid status")
    private String status;

    @Column(name = "phone")
    @CsvBindByName(column = "phone")
    private String phone;

    @Column(name = "meeting_id")
    private String meetingId;

    @Column(name = "google_drive")
    @CsvBindByName(column = "google_drive")
    private Boolean googleDrive;

    @Column(name = "google_drive_folder_id")
    @CsvBindByName(column = "google_drive_folder_id")
    private String googleDriveFolderId;

    @Column(name = "user_id")
    @CsvBindByName(column = "user_id")
    private Integer userId;

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

    public LeadTemp() {
    }

    public static String[] getAllStatus(){
        String be = "meeting-to-schedule|scheduled|archived|success|assign-to-sales";
        return be.split("\\|");
    }

    public LeadTemp(int leadId, String name, String status, String phone, String meetingId, Boolean googleDrive, String googleDriveFolderId, Integer userId, Integer employeeId, Integer customerId, LocalDateTime createdAt, BigDecimal amount) {
        this.leadId = leadId;
        this.name = name;
        this.status = status;
        this.phone = phone;
        this.meetingId = meetingId;
        this.googleDrive = googleDrive;
        this.googleDriveFolderId = googleDriveFolderId;
        this.userId = userId;
        this.employeeId = employeeId;
        this.customerId = customerId;
        this.createdAt = createdAt;
        this.amount = amount;
    }

    public int getLeadId() {
        return leadId;
    }

    public void setLeadId(int leadId) {
        this.leadId = leadId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMeetingId() {
        return meetingId;
    }

    public Boolean getGoogleDrive() {
        return googleDrive;
    }

    public void setGoogleDrive(Boolean googleDrive) {
        this.googleDrive = googleDrive;
    }

    public String getGoogleDriveFolderId() {
        return googleDriveFolderId;
    }

    public void setGoogleDriveFolderId(String googleDriveFolderId) {
        this.googleDriveFolderId = googleDriveFolderId;
    }

    public void setMeetingId(String meetingId) {
        this.meetingId = meetingId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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
        return "trigger_lead_temp";
    }

    @Override
    public String getTempTable() {
        return "create temporary table if not exists trigger_lead_temp\n" +
                "(\n" +
                "    lead_id                int unsigned auto_increment\n" +
                "        primary key,\n" +
                "    customer_id            int unsigned   not null,\n" +
                "    user_id                int            null,\n" +
                "    name                   varchar(255)   null,\n" +
                "    phone                  varchar(20)    null,\n" +
                "    employee_id            int            null,\n" +
                "    status                 varchar(50)    null,\n" +
                "    meeting_id             varchar(255)   null,\n" +
                "    google_drive           tinyint(1)     null,\n" +
                "    google_drive_folder_id varchar(255)   null,\n" +
                "    created_at             datetime       null,\n" +
                "    amount                 decimal(16, 2) null"+
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
        LeadTemp lead = (LeadTemp) o;
        return lead.getAmount().compareTo(amount) == 0 &&
                Objects.equals(name, lead.getName()) &&
                Objects.equals(customerId, lead.getCustomerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, customerId, amount);
    }
}


