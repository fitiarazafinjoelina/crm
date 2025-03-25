package site.easy.to.build.crm.service.csv;

import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.easy.to.build.crm.entity.CustomerLoginInfo;
import site.easy.to.build.crm.repository.CustomerLoginInfoRepository;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CsvPersist {
    @Autowired
    private CustomerLoginInfoRepository customerLoginInfoRepository;
    @Autowired
    CsvService csvService;
    @Autowired
    private EntityManager entityManager;

    /*@Transactional(rollbackFor = SQLException.class)
    public List<CustomerLoginInfo> persistCustomerLoginInfo(InputStream inputStream) throws SQLException {
        try {
            entityManager.createNativeQuery(new CustomerLoginInfoTemp().getTempTable()).executeUpdate();

            List<CustomerLoginInfoTemp> list = null;

            try {
                list = csvService.readCsvObject(inputStream, CustomerLoginInfoTemp.class);
            } catch (Exception e) {
                throw new RuntimeException("Error reading CSV data", e);
            }

            for (CustomerLoginInfoTemp customerLoginInfoTemp : list) {
                try {
                    customerLoginInfoTempRepository.save(customerLoginInfoTemp);
                } catch (Exception e) {
                    throw new SQLException("Error saving CustomerLoginInfoTemp", e);
                }
                try {
                    CustomerLoginInfo customerLoginInfo = new CustomerLoginInfo(customerLoginInfoTemp);
                    customerLoginInfoRepository.save(customerLoginInfo);
                } catch (Exception e) {
                    throw new SQLException("Error saving CustomerLoginInfo", e);
                }
            }
            return list.stream()
                    .map(CustomerLoginInfo::new)
                    .collect(Collectors.toList());
        }
        finally {
            entityManager.createNativeQuery("DROP TABLE "+new CustomerLoginInfoTemp().getTempTableName()).executeUpdate();
        }
    }*/
}
