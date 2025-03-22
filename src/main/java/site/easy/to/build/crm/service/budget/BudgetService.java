package site.easy.to.build.crm.service.budget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.repository.BudgetRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

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
}
