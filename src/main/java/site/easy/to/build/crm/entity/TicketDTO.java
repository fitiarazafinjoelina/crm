package site.easy.to.build.crm.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TicketDTO {

    int ticketId;
    String subject;
    String customerName;
    LocalDateTime createdAt;
    @JsonProperty("Amount")
    BigDecimal amount;

    public TicketDTO(Ticket ticket) {
        this.ticketId = ticket.getTicketId();
        this.subject = ticket.getSubject();
        this.customerName = ticket.getCustomer().getName();
        this.createdAt = ticket.getCreatedAt();
        this.amount = ticket.getAmount();
    }

    public TicketDTO() {
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
