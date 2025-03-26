package site.easy.to.build.crm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.api.ApiService;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApiServiceTest {

    @Mock
    private LeadService leadService;

    @Mock
    private CustomerService customerService;

    @Mock
    private TicketService ticketService;

    @Mock
    private BudgetService budgetService;

    @InjectMocks
    private ApiService apiService;

    private Customer customer1;
    private Customer customer2;
    private Lead lead1;
    private Lead lead2;
    private Ticket ticket1;

    @BeforeEach
    void setUp() {
        customer1 = new Customer();
        customer1.setCustomerId(1);
        customer1.setName("Customer A");

        customer2 = new Customer();
        customer2.setCustomerId(2);
        customer2.setName("Customer B");

        lead1 = new Lead();
        lead1.setAmount(new BigDecimal("1000.00"));
        lead1.setCreatedAt(LocalDateTime.now());

        lead2 = new Lead();
        lead2.setAmount(new BigDecimal("2000.00"));
        lead2.setCreatedAt(LocalDateTime.now().minusDays(1));

        ticket1 = new Ticket();
        ticket1.setAmount(new BigDecimal("500.00"));
        ticket1.setCreatedAt(LocalDateTime.now().minusHours(3));
    }

    @Test
    void findBudgetCustomers_ShouldReturnAllCustomersWithBudgets() {
        // Arrange
        when(customerService.findAll()).thenReturn(Arrays.asList(customer1, customer2));
        when(budgetService.getTotalAmountBudget(1)).thenReturn(new BigDecimal("5000.00"));
        when(budgetService.getTotalAmountBudget(2)).thenReturn(new BigDecimal("7500.00"));

        // Act
        List<BudgetCustomer> result = apiService.findBudgetCustomers();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Customer A", result.get(0).getCustomerName());
        assertEquals(new BigDecimal("5000.00"), result.get(0).getBudgetAmount());
        assertEquals(new BigDecimal("7500.00"), result.get(1).getBudgetAmount());
    }

    @Test
    void findLeadTicketContrib_ShouldSumAllLeadsAndTickets() {
        // Arrange
        when(leadService.findAll()).thenReturn(Arrays.asList(lead1, lead2));
        when(ticketService.findAll()).thenReturn(Collections.singletonList(ticket1));

        // Act
        List<LeadTicket> result = apiService.findLeadTicketContrib();

        // Assert
        assertEquals(2, result.size());

        // Verify leads total
        assertEquals("lead", result.get(0).getType());
        assertEquals(new BigDecimal("3000.00"), result.get(0).getTotal());

        // Verify tickets total
        assertEquals("ticket", result.get(1).getType());
        assertEquals(new BigDecimal("500.00"), result.get(1).getTotal());
    }

    @Test
    void findSpendingDistribution_ShouldCombineLeadAndTicketAmounts() {
        // Arrange
        when(customerService.findAll()).thenReturn(Collections.singletonList(customer1));
        when(leadService.getTotalAmountLeads(1)).thenReturn(new BigDecimal("1500.00"));
        when(ticketService.getTotalAmountTickets(1)).thenReturn(new BigDecimal("750.00"));

        // Act
        List<CustomerSpending> result = apiService.findSpendingDistribution();

        // Assert
        assertEquals(1, result.size());
        assertEquals(new BigDecimal("2250.00"), result.get(0).getSpendingAmount());
        assertEquals("Customer A", result.get(0).getCustomerName());
    }

    @Test
    void findSpendingDistribution_WithCustomerId_ShouldReturnDetailedEntries() {
        // Arrange
        when(customerService.findByCustomerId(1)).thenReturn(customer1);
        when(leadService.getCustomerLeads(1)).thenReturn(Arrays.asList(lead1, lead2));
        when(ticketService.findCustomerTickets(1)).thenReturn(Collections.singletonList(ticket1));

        // Act
        List<CustomerDetailSpending> result = apiService.findSpendingDistribution(1);

        // Assert
        assertEquals(3, result.size());

        // Verify leads
        assertEquals("lead", result.get(0).getSpendingType());
        assertEquals(new BigDecimal("1000.00"), result.get(0).getSpendingAmount());

        assertEquals("lead", result.get(1).getSpendingType());
        assertEquals(new BigDecimal("2000.00"), result.get(1).getSpendingAmount());

        // Verify ticket
        assertEquals("ticket", result.get(2).getSpendingType());
        assertEquals(new BigDecimal("500.00"), result.get(2).getSpendingAmount());
    }

    @Test
    void findBudgetCustomers_NoCustomers_ShouldReturnEmptyList() {
        when(customerService.findAll()).thenReturn(Collections.emptyList());
        assertTrue(apiService.findBudgetCustomers().isEmpty());
    }

    @Test
    void findLeadTicketContrib_NoData_ShouldReturnZeroTotals() {
        when(leadService.findAll()).thenReturn(Collections.emptyList());
        when(ticketService.findAll()).thenReturn(Collections.emptyList());

        List<LeadTicket> result = apiService.findLeadTicketContrib();

        assertEquals(new BigDecimal("0"), result.get(0).getTotal());
        assertEquals(new BigDecimal("0"), result.get(1).getTotal());
    }

    @Test
    void findSpendingDistribution_NoSpending_ShouldReturnZeroAmount() {
        when(customerService.findAll()).thenReturn(Collections.singletonList(customer1));
        when(leadService.getTotalAmountLeads(1)).thenReturn(BigDecimal.ZERO);
        when(ticketService.getTotalAmountTickets(1)).thenReturn(BigDecimal.ZERO);

        CustomerSpending spending = apiService.findSpendingDistribution().get(0);
        assertEquals(BigDecimal.ZERO, spending.getSpendingAmount());
    }

    @Test
    void findSpendingDistribution_InvalidCustomerId_ShouldHandleGracefully() {
        when(customerService.findByCustomerId(999)).thenReturn(null);
        assertTrue(apiService.findSpendingDistribution(999).isEmpty());
    }
}