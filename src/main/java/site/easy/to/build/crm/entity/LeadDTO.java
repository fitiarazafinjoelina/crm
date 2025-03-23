package site.easy.to.build.crm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class LeadDTO {

    int leadId;
    String name;
    String customerName;
    LocalDateTime createdAt;
    @JsonProperty("Amount")
    BigDecimal amount;

    public LeadDTO(Lead lead) {
        this.leadId = lead.getLeadId();
        this.name = lead.getName();
        this.customerName = lead.getCustomer().getName();
        this.createdAt = lead.getCreatedAt();
        this.amount = lead.getAmount();
    }

    public LeadDTO() {
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

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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
