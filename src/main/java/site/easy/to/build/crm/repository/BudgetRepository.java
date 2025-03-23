package site.easy.to.build.crm.repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.Ticket;

import java.math.BigDecimal;
import java.util.List;

public interface BudgetRepository extends JpaRepository<Budget, Long> {

    public Budget findByBudgetId(Long budgetId);

    public List<Budget> findByCustomer(Customer customer);

    List<Budget> findByCustomerCustomerId(int customerId);

    List<Budget> findByCustomerOrderByCreatedAtDesc(Customer customer, Pageable pageable);

    List<Budget> findByCustomerCustomerIdOrderByCreatedAtDesc(int customerId, Pageable pageable);

    long countByCustomer(Customer customer);

    BigDecimal getTotalBudgetSumByCustomer(Customer customer);

    long countByCustomerCustomerId(int customerId);
    BigDecimal getTotalBudgetSumByCustomerCustomerId(int customerId);

    void deleteAllByCustomer(Customer customer);
    void deleteAllByCustomerCustomerId(int customerId);
    public List<Budget> findByManagerId(int id);
    List<Budget> findByManagerIdOrderByCreatedAtDesc(int managerId, Pageable pageable);
    long countByManagerId(int managerId);
}
