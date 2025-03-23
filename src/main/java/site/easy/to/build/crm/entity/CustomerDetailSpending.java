package site.easy.to.build.crm.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CustomerDetailSpending {
    int customerId;
    LocalDateTime spendingTime;
    BigDecimal spendingAmount;
    String spendingType;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public LocalDateTime getSpendingTime() {
        return spendingTime;
    }

    public void setSpendingTime(LocalDateTime spendingTime) {
        this.spendingTime = spendingTime;
    }

    public BigDecimal getSpendingAmount() {
        return spendingAmount;
    }

    public void setSpendingAmount(BigDecimal spendingAmount) {
        this.spendingAmount = spendingAmount;
    }

    public String getSpendingType() {
        return spendingType;
    }

    public void setSpendingType(String spendingType) {
        this.spendingType = spendingType;
    }
}
