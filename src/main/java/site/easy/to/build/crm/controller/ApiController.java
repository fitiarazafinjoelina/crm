package site.easy.to.build.crm.controller;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.alertRate.AlertRateService;
import site.easy.to.build.crm.service.api.ApiService;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ApiController {
    @Autowired
    private ApiService apiService;
    @Autowired
    private LeadService leadService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private BudgetService budgetService;
    @Autowired
    private CustomerService customerService;

    @GetMapping("/nb-customers")
    public int getNbCustomers() {
        return customerService.findAll().size();
    }
    @GetMapping("/total-amount-budgets")
    public BigDecimal getTotalAmountBudgets() {
        return budgetService.getTotalAmountBudget();
    }
    @GetMapping("/customer-spending-dists")
    public ResponseEntity<?> customerSpendingDist() {
        try {
            List<CustomerSpending> list = apiService.findSpendingDistribution();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/lead-ticket-contribs")
    public ResponseEntity<?> leadTicketContrib() {
        try {
            List<LeadTicket> list = apiService.findLeadTicketContrib();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/leads")
    public ResponseEntity<?> leads() {
        try {
            List<Lead> list = leadService.findAll();
            List<LeadDTO> lista = new ArrayList<>();
            for (Lead lead : list) {
                lista.add(new LeadDTO(lead));
            }
            return ResponseEntity.ok(lista);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/tickets")
    public ResponseEntity<?> tickets() {
        try {
            List<Ticket> list = ticketService.findAll();
            List<TicketDTO> lista = new ArrayList<>();
            for (Ticket ticket : list) {
                lista.add(new TicketDTO(ticket));
            }
            return ResponseEntity.ok(lista);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/leads/{id}")
    public ResponseEntity<String> deleteLead(@PathVariable("id") int leadId) {
        try {
        Lead lead = leadService.findByLeadId(leadId);
         leadService.delete(lead);
            return ResponseEntity.ok("Lead deleted successfully");
        }
        catch(Exception e){
            return ResponseEntity.status(404).body("Error "+e.getMessage());
        }
    }
    @DeleteMapping("/tickets/{id}")
    public ResponseEntity<String> deleteTicket(@PathVariable("id") int ticketId) {
        try {
            Ticket ticket = ticketService.findByTicketId(ticketId);
            ticketService.delete(ticket);
            return ResponseEntity.ok("Ticket deleted successfully");
        }
        catch(Exception e){
            return ResponseEntity.status(404).body("Error "+e.getMessage());
        }
    }
    @PutMapping("/leads/{id}")
    public ResponseEntity<?> updateLead(@PathVariable("id") int id, @RequestBody LeadDTO updatedLead) {

        try{
            Lead taloha = leadService.findByLeadId(id);
            taloha.setAmount(updatedLead.getAmount());
            Lead lead = leadService.updateLead(id, taloha);
            if (lead == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(lead);
        }
        catch(Exception e){
            e.printStackTrace();
            String message = e.getMessage();
            if(e.getCause().getCause()!=null){
                message = e.getCause().getCause().getMessage();
            }
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/tickets/{id}")
    public ResponseEntity<?> updateTicket(@PathVariable("id") int id, @RequestBody TicketDTO updatedTicket) {

        try{
            Ticket taloha = ticketService.findByTicketId(id);
            taloha.setAmount(updatedTicket.getAmount());
            Ticket ticket = ticketService.updateTicket(id, taloha);
            if (ticket == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(ticket);
        }
        catch(Exception e){
            e.printStackTrace();
            String message = e.getMessage();
            if(e.getCause().getCause()!=null){
                message = e.getCause().getCause().getMessage();
            }
            return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/customer-spending-dists/{id}")
    public ResponseEntity<?> customerSpendingDistDetail(@PathVariable("id") int customerId) {
        try {
            List<CustomerDetailSpending> list = apiService.findSpendingDistribution(customerId);
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/budget-customers")
    public ResponseEntity<?> budgetCustomers() {
        try {
            List<BudgetCustomer> list = apiService.findBudgetCustomers();
            return ResponseEntity.ok(list);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/budget-customers/{id}")
    public ResponseEntity<?> budgetCustomer(@PathVariable("id") int id) {
        try {
            List<Budget> budgets = budgetService.findByCustomerCustomerId(id);
            return ResponseEntity.ok(budgets);
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
