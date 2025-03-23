package site.easy.to.build.crm.service.api;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {
    @Autowired
    private LeadService leadService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private BudgetService budgetService;

    public List<BudgetCustomer> findBudgetCustomers(){
        List<Customer> customers = customerService.findAll();
        List<BudgetCustomer> budgetCustomers = new ArrayList<>();
        for (Customer customer : customers){
            BudgetCustomer budgetCustomer = new BudgetCustomer();
            budgetCustomer.setCustomerId(customer.getCustomerId());
            budgetCustomer.setCustomerName(customer.getName());
            budgetCustomer.setBudgetAmount(budgetService.getTotalAmountBudget(customer.getCustomerId()));
            budgetCustomers.add(budgetCustomer);
        }
        return budgetCustomers;
    }

    public List<LeadTicket> findLeadTicketContrib(){
        List<LeadTicket> leadTickets = new ArrayList<>();
        List<Lead> leads = leadService.findAll();
        LeadTicket leadd = new LeadTicket();
        leadd.setType("lead");
        leadd.setTotal(BigDecimal.ZERO);
        for (Lead lead : leads) {
            leadd.setTotal(leadd.getTotal().add(lead.getAmount()));
        }
        leadTickets.add(leadd);

        LeadTicket tickett = new LeadTicket();
        tickett.setType("ticket");
        tickett.setTotal(BigDecimal.ZERO);
        List<Ticket> tickets = ticketService.findAll();
        for (Ticket ticket : tickets) {
            tickett.setTotal(tickett.getTotal().add(ticket.getAmount()));
        }
        leadTickets.add(tickett);

        return leadTickets;
    }

    public List<CustomerSpending> findSpendingDistribution(){
        List<CustomerSpending> list = new ArrayList<>();
        List<Customer> customers = customerService.findAll();
        for (Customer customer : customers){
            CustomerSpending customerSpending = new CustomerSpending();
            BigDecimal total = leadService.getTotalAmountLeads(customer.getCustomerId());
            total = total.add(ticketService.getTotalAmountTickets(customer.getCustomerId()));
            customerSpending.setCustomerId(customer.getCustomerId());
            customerSpending.setCustomerName(customer.getName());
            customerSpending.setSpendingAmount(total);
            list.add(customerSpending);
        }
        return list;
    }
    public List<CustomerDetailSpending> findSpendingDistribution(int customerId){
        List<CustomerDetailSpending> list = new ArrayList<>();
        Customer customer = customerService.findByCustomerId(customerId);
        List<Lead> leads = leadService.getCustomerLeads(customer.getCustomerId());
        for (Lead lead : leads){
            CustomerDetailSpending customerDetailSpending = new CustomerDetailSpending();
            customerDetailSpending.setCustomerId(customer.getCustomerId());
            customerDetailSpending.setSpendingTime(lead.getCreatedAt());
            customerDetailSpending.setSpendingAmount(lead.getAmount());
            customerDetailSpending.setSpendingType("lead");
            list.add(customerDetailSpending);

        }
        List<Ticket> tickets = ticketService.findCustomerTickets(customer.getCustomerId());
        for (Ticket ticket : tickets){
            CustomerDetailSpending customerDetailSpending = new CustomerDetailSpending();
            customerDetailSpending.setCustomerId(customer.getCustomerId());
            customerDetailSpending.setSpendingTime(ticket.getCreatedAt());
            customerDetailSpending.setSpendingAmount(ticket.getAmount());
            customerDetailSpending.setSpendingType("ticket");
            list.add(customerDetailSpending);
        }
        return list;
    }
}
