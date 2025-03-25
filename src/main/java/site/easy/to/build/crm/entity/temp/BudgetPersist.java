package site.easy.to.build.crm.entity.temp;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "budget")
public class BudgetPersist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long budgetId;

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

    public BudgetPersist() {
    }

    public BudgetPersist(Long budgetId, BigDecimal amount, LocalDateTime createdAt, Integer customerId, Integer managerId) {
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
}
