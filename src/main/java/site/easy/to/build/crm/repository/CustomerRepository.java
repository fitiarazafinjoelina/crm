package site.easy.to.build.crm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import site.easy.to.build.crm.entity.Customer;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    public Customer findByCustomerId(int customerId);
    public Customer findCustomerByEmail(String customerEmail);

    public List<Customer> findByUserId(int userId);

    public Customer findByEmail(String email);

    public List<Customer> findAll();

    public List<Customer> findByUserIdOrderByCreatedAtDesc(int userId, Pageable pageable);

    long countByUserId(int userId);
}
