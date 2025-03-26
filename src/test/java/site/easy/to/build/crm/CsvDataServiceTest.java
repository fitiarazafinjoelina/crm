package site.easy.to.build.crm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.entity.csvImport.BudgetImport;
import site.easy.to.build.crm.entity.csvImport.CustomerImport;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;
import site.easy.to.build.crm.entity.exceptions.CsvException;
import site.easy.to.build.crm.service.csv.CsvDataService;
import site.easy.to.build.crm.service.csv.CsvService;
import site.easy.to.build.crm.service.csvImport.BudgetImportService;
import site.easy.to.build.crm.service.csvImport.CustomerImportService;
import site.easy.to.build.crm.service.csvImport.TicketLeadImportService;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class CsvDataServiceTest {

    @Mock
    private CsvDataService csvDataService;

    @Mock
    private CsvService csvService;
    @Mock
    private CustomerService customerService;
    @Mock
    private CustomerImportService customerImportService;
    @Mock
    private TicketLeadImportService ticketLeadImportService;
    @Mock
    private TicketService ticketService;
    @Mock
    private LeadService leadService;
    @Mock
    private BudgetImportService budgetImportService;
    @Mock
    private BudgetService budgetService;
    @Mock
    private AuthenticationUtils authenticationUtils;

    @Mock
    private MultipartFile customerFile;
    @Mock
    private MultipartFile budgetFile;
    @Mock
    private MultipartFile ticketLeadFile;
    @Mock
    private User loggedInUser;

    private Set<String> exceptions;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptions = new HashSet<>();
    }

   /* @Test
    void testProcessCsvFiles_withExceptions_shouldThrowCsvException() throws Exception {
        // Arrange
        when(csvService.readCsvObject(any(), eq(CustomerImport.class))).thenThrow(new RuntimeException("CSV parsing error"));

        // Act & Assert
        CsvException thrownException = assertThrows(CsvException.class, () -> {
            csvDataService.processCsvFiles(loggedInUser, customerFile, budgetFile, ticketLeadFile);
        });

        // Verify that the exception is added to the exceptions set
        assertTrue(thrownException.getCauses().contains("CSV parsing error"));
    }

    @Test
    void testProcessCsvFiles_withoutExceptions_shouldNotThrowCsvException() throws Exception {
        // Arrange
        when(csvService.readCsvObject(any(), eq(CustomerImport.class))).thenReturn(mockListOfCustomerImports());
        when(csvService.readCsvObject(any(), eq(BudgetImport.class))).thenReturn(mockListOfBudgetImports());
        when(csvService.readCsvObject(any(), eq(TicketLeadImport.class))).thenReturn(mockListOfTicketLeadImports());

        // Act
        csvDataService.processCsvFiles(loggedInUser, customerFile, budgetFile, ticketLeadFile);

        // Assert
        // Verify that no exception is thrown, and no exceptions are added
        assertTrue(exceptions.isEmpty());
    }

    @Test
    void testProcessCustomerImports_withException_shouldAddToExceptions() throws Exception {
        // Arrange
        when(csvService.readCsvObject(any(), eq(CustomerImport.class))).thenThrow(new RuntimeException("Customer import error"));

        // Act
        csvDataService.processCustomerImports(loggedInUser, customerFile, exceptions);

        // Assert
        assertTrue(exceptions.contains("Customer import error"));
    }

    @Test
    void testProcessBudgetImports_withException_shouldAddToExceptions() throws Exception {
        // Arrange
        when(csvService.readCsvObject(any(), eq(BudgetImport.class))).thenThrow(new RuntimeException("Budget import error"));

        // Act
        csvDataService.processBudgetImports(loggedInUser, budgetFile, exceptions);

        // Assert
        assertTrue(exceptions.contains("Budget import error"));
    }

    @Test
    void testProcessTicketLeadImports_withException_shouldAddToExceptions() throws Exception {
        // Arrange
        when(csvService.readCsvObject(any(), eq(TicketLeadImport.class))).thenThrow(new RuntimeException("Ticket lead import error"));

        // Act
        csvDataService.processTicketLeadImports(loggedInUser, ticketLeadFile, exceptions);

        // Assert
        assertTrue(exceptions.contains("Ticket lead import error"));
    }

    private List<CustomerImport> mockListOfCustomerImports() {
        // Return mocked customer imports
        return List.of(new CustomerImport());
    }

    private List<BudgetImport> mockListOfBudgetImports() {
        // Return mocked budget imports
        return List.of(new BudgetImport());
    }

    private List<TicketLeadImport> mockListOfTicketLeadImports() {
        // Return mocked ticket lead imports
        return List.of(new TicketLeadImport());
    }

    //deepseek
    @Test
    void processCsvFiles_CustomerValidationError_ThrowsWithErrorMessage() throws Exception {
        // Mock customer file validation error
        when(csvService.readCsvObject(any(), eq(CustomerImport.class)))
                .thenReturn(List.of(new CustomerImport()));
        doAnswer(inv -> {
            Set<String> errors = inv.getArgument(1);
            errors.add("customers.csv line 2: Invalid email");
            return null;
        }).when(customerImportService).saveAll(anyList(), anySet(), eq("customers.csv"));

        // Execute and verify
        CsvException ex = assertThrows(CsvException.class, () ->
                csvDataService.processCsvFiles(loggedInUser, customerFile, budgetFile, ticketLeadFile)
        );
        assertTrue(ex.getCauses().contains("customers.csv line 2: Invalid email"));
    }
    @Test
    void processBudgetImports_ConversionError_AddsToExceptions() throws Exception {
        // Mock budget conversion failure
        when(csvService.readCsvObject(any(), eq(BudgetImport.class)))
                .thenReturn(List.of(new BudgetImport()));
        when(budgetService.toBudgets(any(), anyList(),null,null))
                .thenThrow(new CsvException("Budget error", Set.of("budgets.csv line 5: Invalid amount")));

        Set<String> exceptions = new HashSet<>();
        csvDataService.processBudgetImports(loggedInUser, budgetFile, exceptions);

        assertTrue(exceptions.contains("budgets.csv line 5: Invalid amount"));
    }
    @Test
    void processCsvFiles_MultipleFileErrors_CombinesAllExceptions() throws Exception {
        // Customer error
        doAnswer(inv -> {
            ((Set<String>) inv.getArgument(1)).add("customers.csv line 3: Missing name");
            return null;
        }).when(customerImportService).saveAll(anyList(), anySet(), anyString());

        // Ticket/lead error
        doAnswer(inv -> {
            ((Set<String>) inv.getArgument(1)).add("tickets.csv line 7: Invalid priority");
            return null;
        }).when(ticketLeadImportService).saveAll(anyList(), anySet(), anyString());

        CsvException ex = assertThrows(CsvException.class, () ->
                csvDataService.processCsvFiles(loggedInUser, customerFile, budgetFile, ticketLeadFile)
        );

        assertAll(
                () -> assertTrue(ex.getCauses().contains("customers.csv line 3: Missing name")),
                () -> assertTrue(ex.getCauses().contains("tickets.csv line 7: Invalid priority"))
        );
    }
    @Test
    void processTicketLeadImports_WithExistingErrors_SkipsConversion() throws Exception {
        Set<String> exceptions = new HashSet<>();
        exceptions.add("Existing error"); // Simulate prior error

        csvDataService.processTicketLeadImports(loggedInUser, ticketLeadFile, exceptions);

        verify(ticketLeadImportService).saveAll(anyList(), anySet(), anyString());
        verify(ticketService, never()).toTickets(any(), anyList(),null,null); // Skipped
        verify(leadService, never()).toLeads(any(), anyList(),null,null);
    }
    @Test
    void processCustomerImports_ParsingError_AddsException() throws Exception {
        when(csvService.readCsvObject(any(), eq(CustomerImport.class)))
                .thenThrow(new IOException("Invalid CSV format"));

        Set<String> exceptions = new HashSet<>();
        csvDataService.processCustomerImports(loggedInUser, customerFile, exceptions);

        assertTrue(exceptions.stream()
                .anyMatch(e -> e.contains("customers.csv: Invalid CSV format")));
    }
    @Test
    void processCsvFiles_NoErrors_CompletesSuccessfully() throws Exception {
        // Mock valid processing
        when(csvService.readCsvObject(any(), any())).thenReturn(List.of());

        assertDoesNotThrow(() ->
                csvDataService.processCsvFiles(loggedInUser, customerFile, budgetFile, ticketLeadFile)
        );
    }*/
}
