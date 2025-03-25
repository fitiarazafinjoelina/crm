package site.easy.to.build.crm.service.csvImport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.checkerframework.checker.units.qual.A;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.csvImport.CustomerImport;
import site.easy.to.build.crm.repository.CustomerImportRepository;
import site.easy.to.build.crm.repository.CustomerRepository;

import java.util.List;
import java.util.Set;

@Service
public class CustomerImportService {
    @Autowired
    private CustomerImportRepository customerImportRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    public void saveAll(List<CustomerImport> customerImports, Set<String> exceptions, String filename) {
        int i=1;
        for (CustomerImport customerImport : customerImports) {
            try{
                save(customerImport);
            }
            catch (ConstraintViolationException e){
                for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                    ConstraintViolationImpl violation = (ConstraintViolationImpl) constraintViolation;
                    exceptions.add("ERROR in file "+filename+" at line "+i+": "+violation.getMessage());
                }
            }
            catch (Exception e){
                exceptions.add("ERROR in file "+filename+" at line "+i+": "+e.getMessage());
            }
            i++;
        }
        entityManager.clear();

    }
    public void save(CustomerImport customerImport) {
        customerImportRepository.save(customerImport);
    }
    public void deleteAll() {
        customerImportRepository.deleteAll();
    }
}
