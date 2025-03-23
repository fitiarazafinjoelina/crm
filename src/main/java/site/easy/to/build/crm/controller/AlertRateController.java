package site.easy.to.build.crm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.AlertRate;
import site.easy.to.build.crm.entity.CustomerDetailSpending;
import site.easy.to.build.crm.service.alertRate.AlertRateService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/alertrates")
public class AlertRateController {
    @Autowired
    private AlertRateService alertRateService;

    @PostMapping
    public ResponseEntity<?> configureAlertRate(@RequestBody AlertRate alertRate) {
        try {
            AlertRate curr = alertRateService.getAllAlertRates().get(0);
            curr.setPercentage(alertRate.getPercentage());
            alertRateService.updateAlertRate(curr);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/total-amount-spendings")
    public BigDecimal getTotalAmountSpendings() {
        return alertRateService.getTotalAmountSpendings();
    }

}
