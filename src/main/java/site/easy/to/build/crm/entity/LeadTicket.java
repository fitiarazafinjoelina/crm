package site.easy.to.build.crm.entity;

import java.math.BigDecimal;

public class LeadTicket {
    String type;
    BigDecimal total;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
