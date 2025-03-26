package site.easy.to.build.crm.service.lead;

import com.github.javafaker.Faker;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;
import site.easy.to.build.crm.entity.temp.LeadTemp;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.repository.LeadRepository;
import site.easy.to.build.crm.repository.RoleRepository;
import site.easy.to.build.crm.repository.UserRepository;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class LeadServiceImpl implements LeadService {

    private final LeadRepository leadRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public LeadServiceImpl(LeadRepository leadRepository, CustomerRepository customerRepository, RoleRepository roleRepository, UserRepository userRepository) {
        this.leadRepository = leadRepository;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Lead findByLeadId(int id) {
        return leadRepository.findByLeadId(id);
    }

    @Override
    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    @Override
    public List<Lead> findAssignedLeads(int userId) {
        return leadRepository.findByEmployeeId(userId);
    }

    @Override
    public List<Lead> findCreatedLeads(int userId) {
        return leadRepository.findByManagerId(userId);
    }

    @Override
    public Lead findByMeetingId(String meetingId){
        return leadRepository.findByMeetingId(meetingId);
    }
    @Override
    public Lead save(Lead lead) {
        return leadRepository.save(lead);
    }

    @Override
    public void delete(Lead lead) {
        leadRepository.delete(lead);
    }

    @Override
    public List<Lead> getRecentLeadsByEmployee(int employeeId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return leadRepository.findByEmployeeIdOrderByCreatedAtDesc(employeeId, pageable);
    }

    @Override
    public List<Lead> getRecentCustomerLeads(int customerId, int limit) {
        Pageable pageable = PageRequest.of(0,limit);
        return leadRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId, pageable);
    }

    @Override
    public void deleteAllByCustomer(Customer customer) {
        leadRepository.deleteAllByCustomer(customer);
    }

    @Override
    public void deleteAll() {
        leadRepository.deleteAll();
    }

    @Override
    public List<Lead> getRecentLeads(int managerId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return leadRepository.findByManagerIdOrderByCreatedAtDesc(managerId, pageable);
    }

    @Override
    public List<Lead> getCustomerLeads(int customerId) {
        return leadRepository.findByCustomerCustomerId(customerId);
    }

    @Override
    public long countByEmployeeId(int employeeId) {
        return leadRepository.countByEmployeeId(employeeId);
    }

    @Override
    public long countByManagerId(int managerId) {
        return leadRepository.countByManagerId(managerId);
    }

    @Override
    public long countByCustomerId(int customerId) {
        return leadRepository.countByCustomerCustomerId(customerId);
    }

    @Override
    public BigDecimal getTotalAmountLeads(int customerId) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Lead> tickets = getCustomerLeads(customerId);
        for (Lead ticket : tickets) {
            totalAmount = totalAmount.add(ticket.getAmount());
        }
        return totalAmount;
    }
    @Override
    public Lead updateLead(int id, Lead updatedLead) {
        if (!leadRepository.existsById(id)) {
            return null;  // Return null if the lead does not exist
        }

        updatedLead.setLeadId(id);
        return leadRepository.save(updatedLead);  // Save the updated lead
    }

    @Override
    public LeadTemp toLead(User user, TicketLeadImport ticketLeadImport, List<User> employees) {
        if(ticketLeadImport.getType().equals("lead")){
            LeadTemp lead = new LeadTemp();
            Faker faker = new Faker();
            Customer customer = customerRepository.findCustomerByEmail(ticketLeadImport.getCustomerEmail());
            lead.setCustomerId(customer.getCustomerId());
            lead.setUserId(user.getId());
            lead.setEmployeeId(employees.get(faker.number().numberBetween(0, employees.size())).getId());
            lead.setName(ticketLeadImport.getSubjectOrName());
            lead.setPhone(faker.phoneNumber().cellPhone());
            lead.setStatus(ticketLeadImport.getStatus());
            lead.setAmount(ticketLeadImport.getExpense());
            //lead.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            Date daty = Date.from(customer.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
            lead.setCreatedAt(faker.date().future(365, TimeUnit.DAYS,daty).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            return lead;
        }
        return null;
    }

    @Override
    public List<LeadTemp> toLeads(User user, List<TicketLeadImport> ticketLeadImports, String file, Set<String> exceptions) {
        List<LeadTemp> leads = new ArrayList<>();
        Role role = roleRepository.findByName("ROLE_EMPLOYEE");
        List<User> users = userRepository.findByRoles(role);
        int i=1;
        for (TicketLeadImport ticketLeadImport : ticketLeadImports) {
            try {
                LeadTemp lead = toLead(user, ticketLeadImport, users);
                if (lead != null) {
                    leads.add(lead);
                }
            }catch (Exception e){
                exceptions.add("ERROR at line "+i+" of file "+file+": "+e.getMessage());
                e.printStackTrace();
            }
        }
        return leads;
    }

}