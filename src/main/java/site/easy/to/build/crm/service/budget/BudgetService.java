package site.easy.to.build.crm.service.budget;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.entity.csvImport.BudgetImport;
import site.easy.to.build.crm.entity.temp.BudgetTemp;
import site.easy.to.build.crm.repository.BudgetRepository;
import site.easy.to.build.crm.service.customer.CustomerService;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;
    @Autowired
    private CustomerService customerService;

    public Budget findByBudgetId(Long id) {
        return budgetRepository.findByBudgetId(id);
    }
    public List<Budget> findByCustomerCustomerId(int customerId){
        return budgetRepository.findByCustomerCustomerId(customerId);
    }
    List<Budget> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable){
        return budgetRepository.findByCustomerCustomerIdOrderByCreatedAtDesc(customerId,pageable);
    }
    long countByCustomerCustomerId(int customerId){
        return budgetRepository.countByCustomerCustomerId(customerId);
    }
    BigDecimal getTotalBudgetSumByCustomerCustomerId(int customerId){
        return budgetRepository.getTotalBudgetSumByCustomerCustomerId(customerId);
    }

    public Budget save(Budget budget) {
        return budgetRepository.save(budget);
    }

    public void delete(Budget budget) {
        budgetRepository.delete(budget);
    }

    public List<Budget> findAll() {
        return budgetRepository.findAll();
    }
    void deleteAllByCustomerCustomerId(int customerId){
        budgetRepository.deleteAllByCustomerCustomerId(customerId);
    }

    public void deleteAll() {
        budgetRepository.deleteAll();
    }
    public List<Budget> findByManagerId(int id){
        return budgetRepository.findByManagerId(id);
    }
    List<Budget> findByManagerIdOrderByCreatedAtDesc(int managerId, Pageable pageable){
        return budgetRepository.findByManagerIdOrderByCreatedAtDesc(managerId,pageable);
    }
    long countByManagerId(int managerId){
        return budgetRepository.countByManagerId(managerId);
    }
    public BigDecimal getTotalAmountBudget(int customerId) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Budget> budgets = findByCustomerCustomerId(customerId);
        for (Budget budget : budgets) {
            totalAmount = totalAmount.add(budget.getAmount());
        }
        return totalAmount;
    }
    public BigDecimal getTotalAmountBudget() {
        BigDecimal totalAmount = BigDecimal.ZERO;
        List<Customer> customers = customerService.findAll();
        for (Customer customer : customers) {
            totalAmount = totalAmount.add(getTotalAmountBudget(customer.getCustomerId()));
        }
        return totalAmount;
    }
    public BudgetTemp toBudget(User user, BudgetImport budgetImport){
        BudgetTemp budgetTemp = new BudgetTemp();
        Faker faker = new Faker();
        Customer customer = customerService.findByEmail(budgetImport.getCustomerEmail());

        budgetTemp.setAmount(budgetImport.getBudget());
        budgetTemp.setManagerId(user.getId());
        budgetTemp.setCustomerId(customer.getCustomerId());
        Date daty = Date.from(customer.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant());
        budgetTemp.setCreatedAt(faker.date().future(365, TimeUnit.DAYS,daty).toInstant().atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());

        return budgetTemp;
    }
    public List<BudgetTemp> toBudgets(User user,List<BudgetImport> budgetImports, String file, Set<String> exceptions){
        List<BudgetTemp> budgetTemps = new ArrayList<>();
        int i =1;
        for (BudgetImport budgetImport : budgetImports) {
            try{
            budgetTemps.add(toBudget(user,budgetImport));
            }catch (Exception e){
                exceptions.add("ERROR at line "+i+" of file "+file+": "+e.getMessage());
            }
            i++;
        }
        return budgetTemps;
    }
}
