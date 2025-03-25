package site.easy.to.build.crm.service.csvImport;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;
import site.easy.to.build.crm.repository.TicketLeadImportRepository;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.Set;

@Service
public class TicketLeadImportService {
    @Autowired
    TicketLeadImportRepository ticketLeadImportRepository;
    @PersistenceContext
    EntityManager entityManager;

    public void saveAll(List<TicketLeadImport> ticketLeadImports, Set<String> exceptions, String file) {
        int i=1;
        for (TicketLeadImport ticketLeadImport : ticketLeadImports) {
            try{
                save(ticketLeadImport);
            }
            catch (ConstraintViolationException e){
                for (ConstraintViolation<?> constraintViolation : e.getConstraintViolations()) {
                    ConstraintViolationImpl violation = (ConstraintViolationImpl) constraintViolation;
                    exceptions.add("ERROR in file "+file+" at line "+i+": "+violation.getMessage());
                }
            }
            catch (Exception e){
                exceptions.add("ERROR in file "+file+" at line "+i+": "+e.getMessage());
            }
            i++;
        }
        entityManager.clear();
    }
    public void save(TicketLeadImport ticketLeadImport) {
        ticketLeadImportRepository.save(ticketLeadImport);
    }
    public void deleteAll(){
        ticketLeadImportRepository.deleteAll();
    }
}
