package site.easy.to.build.crm.service.cleanup;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CleanUpService {
    @Autowired
    private EntityManager entityManager;
    @Transactional
    public void cleanupDatabase() {
        entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 0").executeUpdate();
        try {
            entityManager.createNativeQuery("DELETE FROM employee").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM email_template").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM customer_login_info").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM customer").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM trigger_lead").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM trigger_ticket").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM trigger_contract").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM contract_settings").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM lead_action").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM lead_settings").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM ticket_settings").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM file").executeUpdate();
            entityManager.createNativeQuery("DELETE FROM google_drive_file").executeUpdate();
        } finally {
            entityManager.createNativeQuery("SET FOREIGN_KEY_CHECKS = 1").executeUpdate();
        }
    }
}
