package site.easy.to.build.crm.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import site.easy.to.build.crm.entity.*;
import site.easy.to.build.crm.service.customer.CustomerService;
import site.easy.to.build.crm.service.budget.BudgetService;
import site.easy.to.build.crm.service.user.UserService;
import site.easy.to.build.crm.util.*;
import java.time.LocalDateTime;
import java.util.*;

@Controller
@RequestMapping("/employee/budget")
public class BudgetController {

    private final BudgetService budgetService;
    private final AuthenticationUtils authenticationUtils;
    private final UserService userService;
    private final CustomerService customerService;


    @Autowired
    public BudgetController(BudgetService budgetService, AuthenticationUtils authenticationUtils, UserService userService, CustomerService customerService) {
        this.budgetService = budgetService;
        this.authenticationUtils = authenticationUtils;
        this.userService = userService;
        this.customerService = customerService;
    }

    @GetMapping("/manager/all-budgets")
    public String showAllBudgets(Model model) {
        List<Budget> budgets = budgetService.findAll();
        model.addAttribute("budgets",budgets);
        return "budget/my-budgets";
    }

    @GetMapping("/created-budgets")
    public String showCreatedBudget(Model model, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        List<Budget> budgets = budgetService.findByManagerId(userId);
        model.addAttribute("budgets",budgets);
        return "budget/my-budgets";
    }

    @GetMapping("/create-budget")
    public String showBudgetCreationForm(Model model, Authentication authentication) {
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User user = userService.findById(userId);
        if(user.isInactiveUser()) {
            return "error/account-inactive";
        }
        List<Customer> customers;

        if(AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
            customers = customerService.findAll();
        } else {
            customers = customerService.findByUserId(user.getId());
        }

        model.addAttribute("customers",customers);
        model.addAttribute("budget", new Budget());
        return "budget/create-budget";
    }

    @PostMapping("/create-budget")
    public String createBudget(@ModelAttribute("budget") @Validated Budget budget, BindingResult bindingResult, @RequestParam("customerId") int customerId,
                               @RequestParam Map<String, String> formParams, Model model, Authentication authentication) {

        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User manager = userService.findById(userId);
        if(manager == null) {
            return "error/500";
        }
        if(manager.isInactiveUser()) {
            return "error/account-inactive";
        }
        if(bindingResult.hasErrors()) {
            List<Customer> customers;

            if(AuthorizationUtil.hasRole(authentication, "ROLE_MANAGER")) {
                customers = customerService.findAll();
            } else {
                customers = customerService.findByUserId(manager.getId());
            }
            model.addAttribute("customers",customers);
            return "budget/create-budget";
        }

        Customer customer = customerService.findByCustomerId(customerId);

        if(customer == null) {
            return "error/500";
        }
        if(AuthorizationUtil.hasRole(authentication, "ROLE_EMPLOYEE")) {
            if(customer.getUser().getId() != userId) {
                return "error/500";
            }
        }

        budget.setCustomer(customer);
        budget.setManager(manager);
        budget.setCreatedAt(LocalDateTime.now());

        budgetService.save(budget);

        return "redirect:/employee/budget/created-budgets";
    }
    @PostMapping("/delete-budget/{id}")
    public String deleteBudget(@PathVariable("id") int id, Authentication authentication){
        int userId = authenticationUtils.getLoggedInUserId(authentication);
        User loggedInUser = userService.findById(userId);
        if(loggedInUser.isInactiveUser()) {
            return "error/account-inactive";
        }

        Budget budget = budgetService.findByBudgetId((long) id);

        User employee = budget.getManager();
        if(!AuthorizationUtil.checkIfUserAuthorized(employee,loggedInUser)) {
            return "error/access-denied";
        }

        budgetService.delete(budget);
        return "redirect:/employee/budget/created-budgets";
    }
}
