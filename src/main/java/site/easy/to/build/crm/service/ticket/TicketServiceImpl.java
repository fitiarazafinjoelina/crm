package site.easy.to.build.crm.service.ticket;

import com.github.javafaker.Faker;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;
import site.easy.to.build.crm.entity.temp.TicketTemp;
import site.easy.to.build.crm.repository.*;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class TicketServiceImpl implements TicketService{

    private final TicketRepository ticketRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public TicketServiceImpl(TicketRepository ticketRepository, CustomerRepository customerRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Ticket findByTicketId(int id) {
        return ticketRepository.findByTicketId(id);
    }

    @Override
    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    @Override
    public void delete(Ticket ticket) {
        ticketRepository.delete(ticket);
    }

    @Override
    public List<Ticket> findManagerTickets(int id) {
        return ticketRepository.findByManagerId(id);
    }

    @Override
    public List<Ticket> findEmployeeTickets(int id) {
        return ticketRepository.findByEmployeeId(id);
    }

    @Override
    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    @Override
    public List<Ticket> findCustomerTickets(int id) {
        return ticketRepository.findByCustomerCustomerId(id);
    }

    @Override
    public List<Ticket> getRecentTickets(int managerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByManagerIdOrderByCreatedAtDesc(managerId, pageable);
    }

    @Override
    public List<Ticket> getRecentEmployeeTickets(int employeeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId, pageable);
    }

    @Override
    public List<Ticket> getRecentCustomerTickets(int customerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return ticketRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId, pageable);
    }

    @Override
    public long countByEmployeeId(int employeeId) {
        return ticketRepository.countByEmployeeId(employeeId);
    }

    @Override
    public long countByManagerId(int managerId) {
        return ticketRepository.countByManagerId(managerId);
    }

    @Override
    public long countByCustomerCustomerId(int customerId) {
        return ticketRepository.countByCustomerCustomerId(customerId);
    }

    @Override
    public void deleteAllByCustomer(Customer customer) {
        ticketRepository.deleteAllByCustomer(customer);
    }

    @Override
    public void deleteAll() {
        ticketRepository.deleteAll();
    }

    @Override
    public BigDecimal getTotalAmountTickets(int customerId) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Ticket> tickets = findCustomerTickets(customerId);
        for (Ticket ticket : tickets) {
            totalAmount = totalAmount.add(ticket.getAmount());
        }
        return totalAmount;
    }
    @Override
    public Ticket updateTicket(int id, Ticket updatedTicket) {
        if (!ticketRepository.existsById(id)) {
            return null;  // Return null if the ticket does not exist
        }

        updatedTicket.setTicketId(id);
        return ticketRepository.save(updatedTicket);  // Save the updated ticket
    }

    @Override
    public TicketTemp toTicket(User user, TicketLeadImport ticketLeadImport, List<User> employees) {
        if(ticketLeadImport.getType().equals("ticket")){
            TicketTemp ticket = new TicketTemp();
            Faker faker = new Faker();
            Customer customer = customerRepository.findCustomerByEmail(ticketLeadImport.getCustomerEmail());
            ticket.setCustomerId(customer.getCustomerId());
            ticket.setManagerId(user.getId());
            ticket.setEmployeeId(employees.get(faker.number().numberBetween(0, employees.size())).getId());
            ticket.setDescription(faker.lorem().paragraph());
            ticket.setSubject(ticketLeadImport.getSubjectOrName());
            ticket.setPriority(Ticket.getAllPriority()[faker.number().numberBetween(0, Ticket.getAllPriority().length)]);
            ticket.setStatus(ticketLeadImport.getStatus());
            ticket.setAmount(ticketLeadImport.getExpense());
            //ticket.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            Date daty = Date.from(customer.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
            ticket.setCreatedAt(faker.date().future(365, TimeUnit.DAYS,daty).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            return ticket;
        }
        return null;
    }

    @Override
    public List<TicketTemp> toTickets(User user, List<TicketLeadImport> ticketLeadImports, String file, Set<String> exceptions) {
        List<TicketTemp> tickets = new ArrayList<>();
        Role role = roleRepository.findByName("ROLE_EMPLOYEE");
        List<User> users = userRepository.findByRoles(role);
        int i=1;
        for (TicketLeadImport ticketLeadImport : ticketLeadImports) {
            try{
                TicketTemp ticket = toTicket(user, ticketLeadImport,users);
                if(ticket != null){
                    tickets.add(ticket);
                }
            }catch (Exception e){
                exceptions.add("ERROR at line "+i+" of file "+file+": "+e.getMessage());
                e.printStackTrace();
            }
        }
        return tickets;
    }

}
