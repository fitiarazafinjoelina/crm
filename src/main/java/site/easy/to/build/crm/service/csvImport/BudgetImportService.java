package site.easy.to.build.crm.service.csvImport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.entity.Budget;
import site.easy.to.build.crm.entity.csvImport.BudgetImport;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;
import site.easy.to.build.crm.repository.BudgetImportRepository;

import java.util.List;
import java.util.Set;

@Service
public class BudgetImportService {
    @Autowired
    private BudgetImportRepository budgetImportRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public void saveAll(List<BudgetImport> budgetImports, Set<String> exceptions, String file) {
        int i=1;
        for (BudgetImport budgetImport : budgetImports) {
            try{
                save(budgetImport);
            }
            catch (ConstraintViolationException e){
                for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                    ConstraintViolationImpl violation = (ConstraintViolationImpl) constraintViolation;
                    exceptions.add("ERROR in file "+file+" at line "+i+": "+violation.getMessage());
                }
            }
            catch (Exception e){
                e.printStackTrace();
                exceptions.add("ERROR in file "+file+" at line "+i+": "+e.getMessage());
            }
            i++;
        }
        entityManager.clear();

    }
    public void save(BudgetImport budgetImport) {
        budgetImportRepository.save(budgetImport);
    }
    public void deleteAll(){
        budgetImportRepository.deleteAll();
    }
}
