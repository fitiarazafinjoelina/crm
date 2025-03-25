package site.easy.to.build.crm.service.lead;

import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.entity.csvImport.CustomerImport;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;
import site.easy.to.build.crm.entity.temp.LeadTemp;

import java.math.BigDecimal;
import java.util.List;

public interface LeadService {
    public Lead findByLeadId(int id);

    public List<Lead> findAll();

    public List<Lead> findAssignedLeads(int userId);

    public List<Lead> findCreatedLeads(int userId);

    public Lead findByMeetingId(String meetingId);

    public Lead save(Lead lead);

    public void delete(Lead lead);

    public List<Lead> getRecentLeads(int mangerId, int limit);
    public List<Lead> getCustomerLeads(int customerId);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);
    long countByCustomerId(int customerId);

    List<Lead> getRecentLeadsByEmployee(int employeeId, int limit);
    List<Lead> getRecentCustomerLeads(int customerId, int limit);
    public void deleteAllByCustomer(Customer customer);
    public void deleteAll();
    public BigDecimal getTotalAmountLeads(int customerId);
    public Lead updateLead(int id, Lead updatedLead);
    public LeadTemp toLead(User user, TicketLeadImport ticketLeadImport, List<User> employees);
    public List<LeadTemp> toLeads(User user,List<TicketLeadImport> ticketLeadImports);


}
