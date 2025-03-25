package site.easy.to.build.crm.service.csv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.entity.csvImport.BudgetImport;
import site.easy.to.build.crm.entity.csvImport.CustomerImport;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;
import site.easy.to.build.crm.entity.exceptions.CsvException;
import site.easy.to.build.crm.entity.temp.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.csvImport.BudgetImportService;
import site.easy.to.build.crm.service.csvImport.CustomerImportService;
import site.easy.to.build.crm.service.csvImport.TicketLeadImportService;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.lead.LeadService;
import site.easy.to.build.crm.service.ticket.TicketService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.AuthenticationUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CsvDataService {

    @Autowired
    private CsvService csvService;
    @Autowired
    private CsvDataServiceB csvDataService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationUtils authenticationUtils;
    @Autowired
    private CustomerImportService customerImportService;
    @Autowired
    private TicketLeadImportService ticketLeadImportService;
    @Autowired
    private TicketService ticketService;
    @Autowired
    private LeadService leadService;
    @Autowired
    private BudgetImportService budgetImportService;
    @Autowired
    private BudgetService budgetService;

    @Transactional(rollbackFor = CsvException.class)
    public void processCsvFiles(User loggedInUser, MultipartFile customerFile, MultipartFile budgetFile, MultipartFile ticketLeadFile) throws CsvException {
        Set<String> exceptions = new HashSet<>();

        try{
            List<CustomerImport> customerImports = csvService.readCsvObject(customerFile.getInputStream(), CustomerImport.class);
            customerImportService.saveAll(customerImports, exceptions, customerFile.getName());
            try {
                List<CustomerTemp> customers = customerService.toCustomers(loggedInUser, customerImports);
                csvDataService.saveCsvData(customers, CustomerPersist.class, customerFile.getName()); // This is part of the same transaction
            } catch (CsvException e) {
                e.printStackTrace();
                exceptions.addAll(e.getCauses());
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        // Handle Budget Import
        try{
            List<BudgetImport> budgets = csvService.readCsvObject(budgetFile.getInputStream(), BudgetImport.class);
            budgetImportService.saveAll(budgets, exceptions, budgetFile.getName());

            try {
                List<BudgetTemp> temps = budgetService.toBudgets(loggedInUser, budgets);
                csvDataService.saveCsvData(temps, BudgetPersist.class, budgetFile.getName()); // This is part of the same transaction
            } catch (CsvException e) {
                e.printStackTrace();
                exceptions.addAll(e.getCauses());
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }



        // Handle TicketLead Import
        try {
            List<TicketLeadImport> ticketLeadImports = csvService.readCsvObject(ticketLeadFile.getInputStream(), TicketLeadImport.class);
            ticketLeadImportService.saveAll(ticketLeadImports, exceptions, ticketLeadFile.getName());

            if(exceptions.isEmpty()){
                try {
                    List<TicketTemp> tickets = ticketService.toTickets(loggedInUser, ticketLeadImports);
                    csvDataService.saveCsvData(tickets, TicketPersist.class, ticketLeadFile.getName()); // This is part of the same transaction
                } catch (CsvException e) {
                    e.printStackTrace();
                    exceptions.addAll(e.getCauses());
                }

                try {
                    List<LeadTemp> leads = leadService.toLeads(loggedInUser, ticketLeadImports);
                    csvDataService.saveCsvData(leads, LeadPersist.class, ticketLeadFile.getName()); // This is part of the same transaction
                } catch (CsvException e) {
                    e.printStackTrace();
                    exceptions.addAll(e.getCauses());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!exceptions.isEmpty()) {
            throw new CsvException("CsvException", exceptions);
        }
    }

}
