package site.easy.to.build.crm.controller;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.beans.factory.annotation.Autowired;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.entity.csvImport.BudgetImport;
import site.easy.to.build.crm.entity.csvImport.CustomerImport;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;
import site.easy.to.build.crm.entity.exceptions.CsvException;
import site.easy.to.build.crm.entity.temp.*;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.cleanup.CleanUpService;
import site.easy.to.build.crm.service.csv.CsvDataService;
import site.easy.to.build.crm.service.csv.CsvDataServiceB;
import site.easy.to.build.crm.service.csv.CsvService;
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

@Controller
@RequestMapping("/csv")
public class CsvController {

    @Autowired
    private CsvService csvService;
    @Autowired
    private CsvDataService csvDataService;
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
    @Autowired
    private CleanUpService cleanUpService;

    @GetMapping("/form")
    public String form() {
        return "csv/import-csv";
    }
    @PostMapping("/upload")
    public String uploadCsv(@RequestParam("customerFile") MultipartFile customerFile, @RequestParam("ticketLeadFile") MultipartFile ticketLeadFile,@RequestParam("budgetFile") MultipartFile budgetFile, Authentication authentication,Model model) {
        /*Set<String> exceptions = new HashSet<>();
        try {
            int userId = authenticationUtils.getLoggedInUserId(authentication);
            User loggedInUser = userService.findById(userId);

            try{
                List<CustomerImport> customerImports = csvService.readCsvObject(customerFile.getInputStream(), CustomerImport.class);
                customerImportService.saveAll(customerImports,exceptions,customerFile.getName());
                List<CustomerTemp> customers = customerService.toCustomers(loggedInUser, customerImports);
                csvDataService.saveCsvData(customers,CustomerPersist.class,customerFile.getName());
            }
            catch (Exception e){
                e.printStackTrace();
                exceptions.add(e.getMessage());
            }
            try {

                List<BudgetImport> budgets = csvService.readCsvObject(budgetFile.getInputStream(), BudgetImport.class);
                budgetImportService.saveAll(budgets,exceptions,budgetFile.getName());
                List<BudgetTemp> temps = budgetService.toBudgets(loggedInUser, budgets);
                csvDataService.saveCsvData(temps,BudgetPersist.class,budgetFile.getName());

            }
            catch (Exception e){
                e.printStackTrace();
                exceptions.add(e.getMessage());
            }
            try{
                List<TicketLeadImport> ticketLeadImports = csvService.readCsvObject(ticketLeadFile.getInputStream(), TicketLeadImport.class);
                ticketLeadImportService.saveAll(ticketLeadImports,exceptions,ticketLeadFile.getName());

                try {
                    List<TicketTemp> tickets = ticketService.toTickets(loggedInUser, ticketLeadImports);
                    csvDataService.saveCsvData(tickets,TicketPersist.class,ticketLeadFile.getName());

                }
                catch (Exception e){
                    e.printStackTrace();
                    exceptions.add(e.getMessage());
                }
                try {
                    List<LeadTemp> leads = leadService.toLeads(loggedInUser, ticketLeadImports);
                    csvDataService.saveCsvData(leads,LeadPersist.class,ticketLeadFile.getName());
                }
                catch (Exception e){
                    e.printStackTrace();
                    exceptions.add(e.getMessage());
                }
            }
            catch (Exception e){
                e.printStackTrace();
                exceptions.add(e.getMessage());
            }

            if (exceptions.size() > 0) {
                throw new CsvException("CsvException",exceptions);
            }

            return "redirect:/";
        }
        catch (CsvException e) {
            model.addAttribute("error",e.getCauses());
            return "csv/import-csv";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error/500";
        }
        finally {
            customerImportService.deleteAll();
            ticketLeadImportService.deleteAll();
            budgetImportService.deleteAll();
        }*/

        try {
            int userId = authenticationUtils.getLoggedInUserId(authentication);
            User loggedInUser = userService.findById(userId);
            csvDataService.processCsvFiles(loggedInUser,customerFile,budgetFile,ticketLeadFile);
            return "redirect:/";
        }
        catch (CsvException e) {
            e.printStackTrace();
            cleanUpService.cleanupDatabase();
            model.addAttribute("error",e.getCauses());
            return "csv/import-csv";
        }
        finally {
            customerImportService.deleteAll();
            ticketLeadImportService.deleteAll();
            budgetImportService.deleteAll();
        }
    }
    /*@PostMapping("/uploadCustomerLogInfo")
    public String uploadCsvCustomerLogInfo(@RequestParam("file") MultipartFile file, Model model) {
        System.out.println(file.getOriginalFilename());
        try {
            //List<BudgetTemp> list=csvDataService.persistCsvData(file.getInputStream(), BudgetTemp.class, BudgetPersist.class);
            return "redirect:/";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "csv/import-csv";
        }
        catch (Exception e) {
            e.printStackTrace();
            return "error/500";
        }
    }*/
}
