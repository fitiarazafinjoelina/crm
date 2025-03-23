package site.easy.to.build.crm.entity;

import java.math.BigDecimal;

public class CustomerSpending {
    int customerId;
    String customerName;
    BigDecimal spendingAmount;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public BigDecimal getSpendingAmount() {
        return spendingAmount;
    }

    public void setSpendingAmount(BigDecimal spendingAmount) {
        this.spendingAmount = spendingAmount;
    }
}
