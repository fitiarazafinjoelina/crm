package site.easy.to.build.crm.entity.temp;

import com.opencsv.bean.CsvBindByName;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trigger_lead")
public class LeadPersist{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_id")
    private int leadId;

    @Column(name = "name")
    @NotBlank(message = "Name is required")
    private String name;

    @Column(name = "status")
    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(meeting-to-schedule|scheduled|archived|success|assign-to-sales)$", message = "Invalid status")
    private String status;

    @Column(name = "phone")
    private String phone;

    @Column(name = "meeting_id")
    private String meetingId;

    @Column(name = "google_drive")
    private Boolean googleDrive;

    @Column(name = "google_drive_folder_id")
    private String googleDriveFolderId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "employee_id")
    private Integer employeeId;

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @NotNull(message = "Amount is required")
    @Digits(integer = 16, fraction = 2, message = "Amount must be a valid number with up to 2 decimal places")
    @DecimalMin(value = "0.00", inclusive = true, message = "Amount must be greater than or equal to 0.00")
    //@DecimalMax(value = "9999999.99", inclusive = true, message = "Amount must be less than or equal to 9999999.99")
    @Column(name = "amount")
    private BigDecimal amount;

    public LeadPersist() {
    }

    public static String[] getAllStatus(){
        String be = "meeting-to-schedule|scheduled|archived|success|assign-to-sales";
        return be.split("\\|");
    }

    public LeadPersist(int leadId, String name, String status, String phone, String meetingId, Boolean googleDrive, String googleDriveFolderId, Integer userId, Integer employeeId, Integer customerId, LocalDateTime createdAt, BigDecimal amount) {
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
}


