package site.easy.to.build.crm.service.api;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.repository.UserRepository;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    @Autowired
    private UserRepository userRepository;

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

    public List<CustomerCopy> copyCustomer(int customerId){
        List<CustomerCopy> copies = new ArrayList<>();
        Customer customer = customerService.findByCustomerId(customerId);
        List<Lead> leads = leadService.getCustomerLeads(customer.getCustomerId());
        List<Ticket> tickets = ticketService.findCustomerTickets(customer.getCustomerId());
        List<Budget> budgets = budgetService.findByCustomerCustomerId(customer.getCustomerId());

        //set customer
        copies.add(CustomerCopy.copyCustomer(customer));

        for (Lead lead : leads){
            copies.add(CustomerCopy.copyCustomer(customer,lead));
        }
        for(Ticket ticket : tickets){
            copies.add(CustomerCopy.copyCustomer(customer,ticket));
        }
        for(Budget budget : budgets){
            copies.add(CustomerCopy.copyCustomer(customer,budget));
        }
        return copies;
    }

    public Customer fromCopy(CustomerCopy customerCopy){
        User user = userRepository.findById(60);
        Customer customer = new Customer();
        customer.setUser(user);
        customer.setEmail(customerCopy.getCustomerEmail());
        customer.setName(customerCopy.getCustomerName());
        return customer;
    }
    public Lead fromCopyLead(CustomerCopy customerCopy,Customer customer){
        if(customerCopy.getType().equals("lead")){
            Lead lead = new Lead();
            lead.setAmount(customerCopy.getAmount());
            lead.setStatus(customerCopy.getStatus());
            lead.setCreatedAt(LocalDateTime.now());
            lead.setCustomer(customer);
            lead.setName("Duplicate");
            return lead;
        }
        return null;

    }
    public Ticket fromCopyTicket(CustomerCopy customerCopy,Customer customer){
        if(customerCopy.getType().equals("ticket")){
            Ticket ticket = new Ticket();
            ticket.setAmount(customerCopy.getAmount());
            ticket.setStatus(customerCopy.getStatus());
            ticket.setCreatedAt(LocalDateTime.now());
            ticket.setCustomer(customer);
            ticket.setSubject("Duplicate");
            ticket.setPriority("low");
            return ticket;
        }
        return null;
    }
    public Budget fromCopyBudget(CustomerCopy customerCopy,Customer customer){
        if(customerCopy.getType().equals("budget")){
            Budget budget = new Budget();
            budget.setAmount(customerCopy.getAmount());
            budget.setCustomer(customer);
            return budget;
        }
        return null;
    }
    public void saveCustomerCopy(List<CustomerCopy> copies){
        Customer customerT = fromCopy(copies.get(0));
        customerService.save(customerT);

        Customer customer = customerService.findByEmail(customerT.getEmail());

        for (int i = 1; i < copies.size(); i++) {
            CustomerCopy customerCopy = copies.get(i);
            if(customerCopy.getType().equals("lead")){
                Lead l = fromCopyLead(customerCopy,customer);
                leadService.save(l);
            }
            else if(customerCopy.getType().equals("ticket")){
                Ticket t = fromCopyTicket(customerCopy,customer);
                ticketService.save(t);
            }
            else if(customerCopy.getType().equals("budget")){
                Budget b = fromCopyBudget(customerCopy,customer);
                budgetService.save(b);
            }
        }
    }
}
