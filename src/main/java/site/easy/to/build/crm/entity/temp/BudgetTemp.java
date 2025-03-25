package site.easy.to.build.crm.entity.temp;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "budget_temp")
public class BudgetTemp implements CsvClass{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

    @CsvBindByName(column = "amount")
    private BigDecimal amount;

    @Column(name = "created_at")
    @CsvBindByName(column = "created_at")
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @Column(name = "customer_id", nullable = false)
    @CsvBindByName(column = "customer_id")
    private Integer customerId;

    @Column(name = "user_id")
    @CsvBindByName(column = "user_id")
    private Integer managerId;

    public BudgetTemp() {
    }

    public BudgetTemp(Long budgetId, BigDecimal amount, LocalDateTime createdAt, Integer customerId, Integer managerId) {
        this.budgetId = budgetId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.customerId = customerId;
        this.managerId = managerId;
    }

    public Long getBudgetId() {
        return budgetId;
    }

    public void setBudgetId(Long budgetId) {
        this.budgetId = budgetId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getManagerId() {
        return managerId;
    }

    public void setManagerId(Integer managerId) {
        this.managerId = managerId;
    }

    @Override
    public String getTempTableName() {
        return "budget_temp";
    }

    @Override
    public String getTempTable() {
        return "create temporary table if not exists budget_temp\n" +
                "(\n" +
                "    budget_id   int auto_increment\n" +
                "        primary key,\n" +
                "    amount      decimal(15, 2) not null,\n" +
                "    customer_id int unsigned   not null,\n" +
                "    created_at  datetime       null,\n" +
                "    user_id     int            null\n" +
                ");";
    }

    @Override
    public boolean isValid() {
        return getAmount().compareTo(BigDecimal.ZERO) > 0 && getCustomerId() != null && getManagerId() != null ;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetTemp budget = (BudgetTemp) o;
        return budget.getAmount().compareTo(amount) == 0 &&
                Objects.equals(customerId, budget.getCustomerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, amount);
    }
}
