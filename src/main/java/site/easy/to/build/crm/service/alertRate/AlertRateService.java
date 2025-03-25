package site.easy.to.build.crm.service.alertRate;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ValidationException;
import site.easy.to.build.crm.entity.AlertRate;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Lead;
import site.easy.to.build.crm.entity.Ticket;
import site.easy.to.build.crm.repository.AlertRateRepository;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class AlertRateService {

    @Autowired
    private AlertRateRepository alertRateRepository;
    @Autowired
    private LeadService leadService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private CustomerService customerService;

    public List<AlertRate> getAllAlertRates() {
        return alertRateRepository.findAll();
    }

    public AlertRate saveAlertRate(AlertRate alertRate) {
        if (alertRate.getPercentage().compareTo(BigDecimal.ZERO) < 0 ||
                alertRate.getPercentage().compareTo(new BigDecimal("100.00")) > 0) {
            throw new ValidationException("Percentage must be between 0 and 100.");
        }
        return alertRateRepository.save(alertRate);
    }

    public Optional<AlertRate> getAlertRateById(Long id) {
        return alertRateRepository.findById(id);
    }
    public void deleteAlertRate(Long id) {
        alertRateRepository.deleteById(id);
    }
    public void deleteAll() {
        alertRateRepository.deleteAll();
    }
    public void updateAlertRate(AlertRate alertRate) {
        alertRateRepository.save(alertRate);
    }
    public boolean checkAlert(int customerId, Lead lead, AlertRate alertRate) {
        System.out.println("holo");
        BigDecimal totalAmount = leadService.getTotalAmountLeads(customerId);
        System.out.println("totalAmount: " + totalAmount);
        BigDecimal totalTicketAmount = ticketService.getTotalAmountTickets(customerId);
        System.out.println("totalTicketAmount: " + totalTicketAmount);
        BigDecimal totalBudget = budgetService.getTotalAmountBudget(customerId);
        totalAmount = totalAmount.add(totalTicketAmount);
        System.out.println("totalAmount: " + totalAmount);
        return totalAmount.add(lead.getAmount()).compareTo((alertRate.getPercentage().divide(BigDecimal.valueOf(100))).multiply(totalBudget)) >= 0;
    }
    public boolean checkAlert(int customerId, Ticket ticket, AlertRate alertRate) {
        System.out.println("holo");
        BigDecimal totalAmount = ticketService.getTotalAmountTickets(customerId);
        BigDecimal totalLeadAmount = leadService.getTotalAmountLeads(customerId);
        System.out.println("totalLeadAmount: " + totalLeadAmount);
        BigDecimal totalBudget = budgetService.getTotalAmountBudget(customerId);
        totalAmount = totalAmount.add(totalLeadAmount);
        System.out.println("totalAmount: " + totalAmount);
        return totalAmount.add(ticket.getAmount()).compareTo((alertRate.getPercentage().divide(BigDecimal.valueOf(100))).multiply(totalBudget)) >= 0;
    }

    public boolean checkDepasse(int customerId, Lead lead) {
        BigDecimal totalAmount = leadService.getTotalAmountLeads(customerId);
        BigDecimal totalTicketAmount = ticketService.getTotalAmountTickets(customerId);
        totalAmount = totalAmount.add(totalTicketAmount);
        BigDecimal totalBudget = budgetService.getTotalAmountBudget(customerId);
        return totalAmount.add(lead.getAmount()).compareTo(totalBudget) >= 0;
    }
    public boolean checkDepasse(int customerId, Ticket ticket) {
        BigDecimal totalAmount = ticketService.getTotalAmountTickets(customerId);
        BigDecimal totalLeadAmount = leadService.getTotalAmountLeads(customerId);
        totalAmount = totalAmount.add(totalLeadAmount);
        BigDecimal totalBudget = budgetService.getTotalAmountBudget(customerId);
        return totalAmount.add(ticket.getAmount()).compareTo(totalBudget) >= 0;
    }
    public BigDecimal getTotalAmountSpendings(){
        List<Customer> customers = customerService.findAll();
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (Customer customer : customers) {
            totalAmount = totalAmount.add(leadService.getTotalAmountLeads(customer.getCustomerId()));
            totalAmount = totalAmount.add(ticketService.getTotalAmountTickets(customer.getCustomerId()));
        }
        return totalAmount;
    }
}
