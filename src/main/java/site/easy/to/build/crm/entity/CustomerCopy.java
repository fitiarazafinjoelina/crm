package site.easy.to.build.crm.entity;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvNumber;
import jakarta.persistence.Column;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.groups.Default;
import site.easy.to.build.crm.entity.csvImport.BudgetImport;
import site.easy.to.build.crm.entity.csvImport.CustomerImport;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;

import java.math.BigDecimal;

public class CustomerCopy {
    @CsvBindByName(column = "customerEmail")
    @CsvBindByPosition(position = 0)
    private String customerEmail;

    @CsvBindByName(column = "customerName")
    @CsvBindByPosition(position = 1)
    private String customerName;

    @CsvBindByName
    @CsvBindByPosition(position = 2)
    private String type;

    @Column
    @CsvBindByName
    @CsvBindByPosition(position = 3)
    private String status;

    @CsvBindByName
    @CsvBindByPosition(position = 4)
    private BigDecimal amount;

    public static CustomerCopy copyCustomer(Customer customer){
        CustomerCopy customerCopy = new CustomerCopy();
        customerCopy.setCustomerEmail(customer.getEmail());
        customerCopy.setCustomerName(customer.getName());
        customerCopy.setType("customer");
        customerCopy.setAmount(BigDecimal.ZERO);
        return customerCopy;
    }
    public static CustomerCopy copyCustomer(Customer customer,Lead lead){
        CustomerCopy customerCopy = new CustomerCopy();
        customerCopy.setCustomerEmail("copy_"+customer.getEmail());
        customerCopy.setCustomerName(customer.getName());
        customerCopy.setType("lead");
        customerCopy.setStatus(lead.getStatus());
        customerCopy.setAmount(lead.getAmount());
        return customerCopy;
    }
    public static CustomerCopy copyCustomer(Customer customer,Ticket ticket){
        CustomerCopy customerCopy = new CustomerCopy();
        customerCopy.setCustomerEmail("copy_"+customer.getEmail());
        customerCopy.setCustomerName(customer.getName());
        customerCopy.setType("ticket");
        customerCopy.setStatus(ticket.getStatus());
        customerCopy.setAmount(ticket.getAmount());
        return customerCopy;
    }
    public static CustomerCopy copyCustomer(Customer customer,Budget budget){
        CustomerCopy customerCopy = new CustomerCopy();
        customerCopy.setCustomerEmail("copy_"+customer.getEmail());
        customerCopy.setCustomerName(customer.getName());
        customerCopy.setType("budget");
        customerCopy.setAmount(budget.getAmount());
        return customerCopy;
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

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
