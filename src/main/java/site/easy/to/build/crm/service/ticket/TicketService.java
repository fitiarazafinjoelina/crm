package site.easy.to.build.crm.service.ticket;

import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;
import site.easy.to.build.crm.entity.temp.TicketTemp;

import java.math.BigDecimal;
import java.util.List;

public interface TicketService {
    public Ticket findByTicketId(int id);

    public Ticket save(Ticket ticket);

    public void delete(Ticket ticket);

    public List<Ticket> findManagerTickets(int id);

    public List<Ticket> findEmployeeTickets(int id);

    public List<Ticket> findAll();

    public List<Ticket> findCustomerTickets(int id);

    List<Ticket> getRecentTickets(int managerId, int limit);

    List<Ticket> getRecentEmployeeTickets(int employeeId, int limit);

    List<Ticket> getRecentCustomerTickets(int customerId, int limit);

    long countByEmployeeId(int employeeId);

    long countByManagerId(int managerId);

    long countByCustomerCustomerId(int customerId);

    void deleteAllByCustomer(Customer customer);
    public void deleteAll();
    public BigDecimal getTotalAmountTickets(int customerId);
    public Ticket updateTicket(int id, Ticket updatedTicket);
    public TicketTemp toTicket(User user, TicketLeadImport ticketTicketImport,List<User> employees);
    public List<TicketTemp> toTickets(User user, List<TicketLeadImport> ticketTicketImports);
}
