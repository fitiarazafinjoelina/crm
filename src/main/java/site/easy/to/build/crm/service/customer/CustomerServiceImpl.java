package site.easy.to.build.crm.service.customer;

import com.github.javafaker.Faker;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.User;
import site.easy.to.build.crm.entity.csvImport.CustomerImport;
import site.easy.to.build.crm.entity.temp.CustomerTemp;
import site.easy.to.build.crm.repository.CustomerRepository;
import site.easy.to.build.crm.entity.Customer;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer findByCustomerId(int customerId) {
        return customerRepository.findByCustomerId(customerId);
    }

    @Override
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer findByCustomerEmail(String customerEmail) {
        return customerRepository.findCustomerByEmail(customerEmail);
    }

    @Override
    public List<Customer> findByUserId(int userId) {
        return customerRepository.findByUserId(userId);
    }

    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public List<Customer> getRecentCustomers(int userId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return customerRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    @Override
    public long countByUserId(int userId) {
        return customerRepository.countByUserId(userId);
    }
    @Override
    public void deleteAll(){
        customerRepository.deleteAll();
    }

    @Override
    public List<CustomerTemp> toCustomers(User user, List<CustomerImport> customerImports, String file, Set<String> exceptions) {
        List<CustomerTemp> customers = new ArrayList<>();
        int i = 1;
        for (CustomerImport customerImport : customerImports) {
            try {
                customers.add(toCustomer(user, customerImport));
            }
            catch (Exception e) {
                exceptions.add("ERROR at line "+i+" of file "+file+": "+e.getMessage());
            }
            i++;
        }
        return customers;
    }

    @Override
    public CustomerTemp toCustomer(User user, CustomerImport customerImport) {
            Faker faker = new Faker();
            CustomerTemp customer = new CustomerTemp();
            customer.setEmail(customerImport.getCustomerEmail());
            customer.setName(customerImport.getCustomerName());
            customer.setPhone(faker.phoneNumber().cellPhone());
            customer.setAddress(faker.address().streetAddress());
            customer.setCity(faker.address().city());
            customer.setCountry(faker.address().country());
            customer.setState(faker.address().state());
            customer.setUserId(user.getId());
            customer.setDescription(faker.lorem().paragraph());
            customer.setPosition(faker.job().position());
            customer.setCreatedAt(faker.date().past(365, TimeUnit.DAYS).toInstant()
                    .atZone(ZoneId.systemDefault()).toLocalDate().atStartOfDay());
            return customer;
    }
}
