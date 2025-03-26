package site.easy.to.build.crm;

import jakarta.validation.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.repository.AlertRateRepository;
import site.easy.to.build.crm.service.alertRate.AlertRateService;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlertRateServiceTest {

    @Mock
    private AlertRateRepository alertRateRepository;

    @Mock
    private LeadService leadService;

    @Mock
    private TicketService ticketService;

    @Mock
    private BudgetService budgetService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AlertRateService alertRateService;

    private AlertRate validAlertRate;
    private Customer testCustomer;
    private Lead testLead;
    private Ticket testTicket;

    @BeforeEach
    void setUp() {
        validAlertRate = new AlertRate();
        validAlertRate.setPercentage(new BigDecimal("50.00"));

        testCustomer = new Customer();
        testCustomer.setCustomerId(1);

        testLead = new Lead();
        testLead.setAmount(new BigDecimal("1000.00"));

        testTicket = new Ticket();
        testTicket.setAmount(new BigDecimal("500.00"));
    }

    @Test
    void saveAlertRate_ValidPercentage_ShouldSave() {
        AlertRate input = new AlertRate();
        input.setPercentage(new BigDecimal("75.50"));

        when(alertRateRepository.save(input)).thenReturn(input);

        AlertRate result = alertRateService.saveAlertRate(input);

        assertNotNull(result);
        verify(alertRateRepository).save(input);
    }

    @Test
    void saveAlertRate_InvalidPercentage_ShouldThrowValidationException() {
        AlertRate invalidAlertRate = new AlertRate();
        invalidAlertRate.setPercentage(new BigDecimal("-5.00"));

        assertThrows(ValidationException.class, () -> {
            alertRateService.saveAlertRate(invalidAlertRate);
        });

        invalidAlertRate.setPercentage(new BigDecimal("150.00"));
        assertThrows(ValidationException.class, () -> {
            alertRateService.saveAlertRate(invalidAlertRate);
        });
    }

    @Test
    void checkAlert_WithLead_ShouldReturnTrueWhenThresholdExceeded() {
        // Setup
        when(leadService.getTotalAmountLeads(1)).thenReturn(new BigDecimal("4000.00"));
        when(ticketService.getTotalAmountTickets(1)).thenReturn(new BigDecimal("1000.00"));
        when(budgetService.getTotalAmountBudget(1)).thenReturn(new BigDecimal("10000.00"));

        // Calculation: (4000 + 1000 + 1000) >= (50% of 10000) → 6000 >= 5000 → true
        boolean result = alertRateService.checkAlert(1, testLead, validAlertRate);

        assertTrue(result);
    }

    @Test
    void checkAlert_WithTicket_ShouldReturnFalseWhenBelowThreshold() {
        when(ticketService.getTotalAmountTickets(1)).thenReturn(new BigDecimal("2000.00"));
        when(leadService.getTotalAmountLeads(1)).thenReturn(new BigDecimal("1500.00"));
        when(budgetService.getTotalAmountBudget(1)).thenReturn(new BigDecimal("10000.00"));

        // (2000 + 1500 + 500) = 4000 < 5000
        boolean result = alertRateService.checkAlert(1, testTicket, validAlertRate);

        assertFalse(result);
    }

    @Test
    void checkDepasse_WithLead_ShouldReturnTrueWhenBudgetExceeded() {
        when(leadService.getTotalAmountLeads(1)).thenReturn(new BigDecimal("8000.00"));
        when(ticketService.getTotalAmountTickets(1)).thenReturn(new BigDecimal("2000.00"));
        when(budgetService.getTotalAmountBudget(1)).thenReturn(new BigDecimal("9000.00"));

        // 8000 + 2000 + 1000 = 11000 > 9000
        boolean result = alertRateService.checkDepasse(1, testLead);

        assertTrue(result);
    }

    @Test
    void getTotalAmountSpendings_ShouldSumAllCustomers() {
        Customer customer1 = new Customer();
        customer1.setCustomerId(1);
        Customer customer2 = new Customer();
        customer2.setCustomerId(2);

        when(customerService.findAll()).thenReturn(Arrays.asList(customer1, customer2));
        when(leadService.getTotalAmountLeads(1)).thenReturn(new BigDecimal("1000.00"));
        when(ticketService.getTotalAmountTickets(1)).thenReturn(new BigDecimal("500.00"));
        when(leadService.getTotalAmountLeads(2)).thenReturn(new BigDecimal("2000.00"));
        when(ticketService.getTotalAmountTickets(2)).thenReturn(new BigDecimal("1500.00"));

        BigDecimal result = alertRateService.getTotalAmountSpendings();

        assertEquals(new BigDecimal("5000.00"), result);
    }

    @Test
    void getAllAlertRates_ShouldReturnAllEntries() {
        AlertRate rate1 = new AlertRate();
        AlertRate rate2 = new AlertRate();
        when(alertRateRepository.findAll()).thenReturn(Arrays.asList(rate1, rate2));

        List<AlertRate> result = alertRateService.getAllAlertRates();

        assertEquals(2, result.size());
    }

    @Test
    void deleteAlertRate_ShouldCallRepository() {
        alertRateService.deleteAlertRate(1L);
        verify(alertRateRepository).deleteById(1L);
    }

    @Test
    void getAlertRateById_ShouldReturnWhenExists() {
        AlertRate expected = new AlertRate();
        when(alertRateRepository.findById(1L)).thenReturn(Optional.of(expected));

        Optional<AlertRate> result = alertRateService.getAlertRateById(1L);

        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    void updateAlertRate_ShouldCallSave() {
        AlertRate rate = new AlertRate();
        alertRateService.updateAlertRate(rate);
        verify(alertRateRepository).save(rate);
    }

    @Test
    void checkAlert_EdgeCaseZeroPercentage_ShouldHandleCorrectly() {
        AlertRate zeroAlert = new AlertRate();
        zeroAlert.setPercentage(BigDecimal.ZERO);

        when(leadService.getTotalAmountLeads(1)).thenReturn(BigDecimal.ZERO);
        when(ticketService.getTotalAmountTickets(1)).thenReturn(BigDecimal.ZERO);
        when(budgetService.getTotalAmountBudget(1)).thenReturn(new BigDecimal("1000.00"));

        // 0 + 0 + 1000 >= (0% of 1000) → 1000 >= 0 → true
        boolean result = alertRateService.checkAlert(1, testLead, zeroAlert);
        assertTrue(result);
    }
}