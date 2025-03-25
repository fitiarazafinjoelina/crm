package site.easy.to.build.crm.service.data;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.repository.*;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class DataGenerationService {
    private final Faker faker = new Faker();
    @Autowired
    private LeadRepository leadRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private BudgetRepository budgetRepository;

    public void generateLeads(User user,int nb) {
        List<Customer> customers = customerRepository.findAll();
        Role role = roleRepository.findByName("ROLE_EMPLOYEE");
        List<User> users = userRepository.findByRoles(role);
        for (int i = 0; i < nb; i++) {
            Lead lead = new Lead();
            lead.setName(faker.name().title());
            lead.setPhone(faker.phoneNumber().cellPhone());
            lead.setStatus(Lead.getAllStatus()[faker.number().numberBetween(0, Lead.getAllStatus().length)]);
            lead.setManager(user);
            lead.setEmployee(users.get(faker.number().numberBetween(0, users.size())));
            Customer customer = customers.get(faker.number().numberBetween(0, customers.size()));
            lead.setCustomer(customer);
            lead.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100000)));
            //lead.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            Date daty = Date.from(customer.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
            lead.setCreatedAt(faker.date().future(365, TimeUnit.DAYS,daty).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            leadRepository.save(lead);
        }
    }
    public void generateTickets(User user,int nb) {
        List<Customer> customers = customerRepository.findAll();
        Role role = roleRepository.findByName("ROLE_EMPLOYEE");
        List<User> users = userRepository.findByRoles(role);
        for (int i = 0; i < nb; i++) {
            Ticket ticket = new Ticket();
            ticket.setSubject(faker.name().title());
            ticket.setDescription(faker.lorem().paragraph());
            ticket.setStatus(Ticket.getAllStatus()[faker.number().numberBetween(0, Ticket.getAllStatus().length)]);
            ticket.setPriority(Ticket.getAllPriority()[faker.number().numberBetween(0, Ticket.getAllPriority().length)]);
            ticket.setManager(user);
            ticket.setEmployee(users.get(faker.number().numberBetween(0, users.size())));
            Customer customer =customers.get(faker.number().numberBetween(0, customers.size()));
            ticket.setCustomer(customer);
            ticket.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100000)));
            //ticket.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            Date daty = Date.from(customer.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
            ticket.setCreatedAt(faker.date().future(365, TimeUnit.DAYS,daty).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            ticketRepository.save(ticket);
        }
    }
    public void generateCustomers(User user,int nb) {

        for (int i = 0; i < nb; i++) {
            Customer customer = new Customer();
            customer.setName(faker.name().fullName());
            customer.setPhone(faker.phoneNumber().cellPhone());
            customer.setAddress(faker.address().streetAddress());
            customer.setCity(faker.address().city());
            customer.setState(faker.address().state());
            customer.setCountry(faker.address().country());
            customer.setDescription(faker.lorem().paragraph());
            customer.setPosition(faker.job().position());
            customer.setEmail(faker.name().username()+"@gmail.com");
            customer.setUser(user);
            customer.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            customerRepository.save(customer);
        }
    }
    public void generateBudgets(User user,int nb) {
        List<Customer> customers = customerRepository.findAll();
        for (int i = 0; i < nb; i++) {
            Budget budget = new Budget();
            budget.setManager(user);
            Customer customer =customers.get(faker.number().numberBetween(0, customers.size()));
            budget.setCustomer(customer);
            budget.setAmount(BigDecimal.valueOf(faker.number().randomDouble(2, 1, 250000)));
            //budget.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            Date daty = Date.from(customer.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
            budget.setCreatedAt(faker.date().future(365, TimeUnit.DAYS,daty).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            budgetRepository.save(budget);
        }
    }
    @Transactional
    public void generateData(User user,int nbCustomers,int nbBudgets,int nbLeads,int nbTickets) {
        generateCustomers(user,nbCustomers);
        generateBudgets(user,nbBudgets);
        generateLeads(user,nbLeads);
        generateTickets(user,nbTickets);
    }
}
