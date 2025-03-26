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
        try {
            int userId = authenticationUtils.getLoggedInUserId(authentication);
            User loggedInUser = userService.findById(userId);
            csvDataService.processCsvFiles(loggedInUser,customerFile,budgetFile,ticketLeadFile);
            return "redirect:/";
        }
        catch (CsvException e) {
            e.printStackTrace();
            cleanUpService.cleanupDatabaseImport();
            model.addAttribute("error",e.getCauses());
            return "csv/import-csv";
        }
        finally {
            customerImportService.deleteAll();
            ticketLeadImportService.deleteAll();
            budgetImportService.deleteAll();
        }
    }

}
