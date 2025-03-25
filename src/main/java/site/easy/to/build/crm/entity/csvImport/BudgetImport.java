package site.easy.to.build.crm.entity.csvImport;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvNumber;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.groups.Default;

import java.math.BigDecimal;

@Entity
@Table(name = "budget_import")
public class BudgetImport {
    private interface BudgetValidator{}
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @CsvBindByName(column = "customer_email")
    @Column(name = "customer_email")
    private String customerEmail;

    @CsvBindByName(column = "budget")
    @Column(name = "budget")
    @NotNull(message = "Amount is required", groups = {Default.class, BudgetImport.class})
    @CsvNumber("#,##")
    @DecimalMin(value = "0.00", inclusive = true, message = "Budget must be greater than or equal to 0.00")
    private BigDecimal budget;

    public BudgetImport() {
    }

    public BudgetImport(Integer id, String customerEmail, BigDecimal budget) {
        this.id = id;
        this.customerEmail = customerEmail;
        this.budget = budget;
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

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
        this.budget = budget;
    }
}
