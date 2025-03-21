package site.easy.to.build.crm.service.csv;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import site.easy.to.build.crm.entity.temp.CsvClass;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CsvDataService<T, R> {

    @Autowired
    private CsvService csvService;  // Service for reading CSV data into entity objects

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional(rollbackFor = SQLException.class)
    public <E extends CsvClass, M> List<E> persistCsvData(InputStream inputStream, Class<E> entityClass, Class<M> mainClass) throws SQLException {
        E entityInstance = createEntityInstance(entityClass);

        String tempTable = entityInstance.getTempTable();
        String tempTableName = entityInstance.getTempTableName();

        try {
            entityManager.createNativeQuery(tempTable).executeUpdate();

            List<E> list = readCsvData(inputStream, entityClass);
            Set<E> set = new HashSet<>(list); //to remove doublons

            for (E entity : list) { //soloina set eto raha manala doublons
                M mainEntity = mainClass.getDeclaredConstructor().newInstance();
                saveEntity(entity, mainEntity);
            }

            return new ArrayList<>(list);
        } catch (Exception e) {
            throw new SQLException("Error during CSV processing and persistence", e);
        } finally {
            entityManager.createNativeQuery("DROP TABLE " + tempTableName).executeUpdate();
        }
    }

    private <E extends CsvClass> List<E> readCsvData(InputStream inputStream, Class<E> entityClass) {
        try {
            return csvService.readCsvObject(inputStream, entityClass);
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV data for " + entityClass.getSimpleName(), e);
        }
    }

    private <E extends CsvClass, M> void saveEntity(E source, M main) throws SQLException {
        try {
            // Persist source (temporary entity)
            entityManager.persist(source);
            // Copy fields from source to main entity
            copyToMain(source, main);
            // Persist main entity (final entity)
            entityManager.merge(main);
            // Flush changes to ensure they are saved immediately
            entityManager.flush();
        } catch (Exception e) {
            throw new SQLException("Error saving entity to temporary table", e);
        }
    }

    private <E extends CsvClass, M> void copyToMain(E source, M main) throws Exception {
        Field[] sourceFields = source.getClass().getDeclaredFields();
        for (Field sourceField : sourceFields) {
            sourceField.setAccessible(true);
            Field mainField = main.getClass().getDeclaredField(sourceField.getName());
            mainField.setAccessible(true);
            mainField.set(main, sourceField.get(source));
        }
    }

    private <E extends CsvClass> E createEntityInstance(Class<E> entityClass) throws SQLException {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new SQLException("Error creating entity instance for " + entityClass.getSimpleName(), e);
        }
    }
}
