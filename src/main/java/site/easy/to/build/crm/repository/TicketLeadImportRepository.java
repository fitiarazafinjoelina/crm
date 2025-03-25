package site.easy.to.build.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.easy.to.build.crm.entity.csvImport.TicketLeadImport;

public interface TicketLeadImportRepository extends JpaRepository<TicketLeadImport, Long> {
}
