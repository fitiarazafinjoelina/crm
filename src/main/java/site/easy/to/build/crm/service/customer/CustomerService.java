package site.easy.to.build.crm.service.customer;

import org.checkerframework.checker.units.qual.C;
import site.easy.to.build.crm.entity.Customer;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.entity.csvImport.CustomerImport;
import site.easy.to.build.crm.entity.temp.CustomerTemp;

import java.util.List;
import java.util.Set;

public interface CustomerService {

    public Customer findByCustomerId(int customerId);

    public List<Customer> findByUserId(int userId);

    public Customer findByEmail(String email);
    public Customer findByCustomerEmail(String customerEmail);

    public List<Customer> findAll();

    public Customer save(Customer customer);

    public void delete(Customer customer);

    public List<Customer> getRecentCustomers(int userId, int limit);

    long countByUserId(int userId);
    public void deleteAll();

    List<CustomerTemp> toCustomers(User user, List<CustomerImport> customerImports, String file, Set<String> exceptions);


    CustomerTemp toCustomer(User user, CustomerImport customerImport);
}
