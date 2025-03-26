package site.easy.to.build.crm.service.csv;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.hibernate.validator.internal.engine.ConstraintViolationImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import site.easy.to.build.crm.entity.exceptions.CsvException;
import site.easy.to.build.crm.entity.temp.CsvClass;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CsvDataServiceB<T, R> {

    @Autowired
    private CsvService csvService;

    @PersistenceContext
    private EntityManager entityManager;

    public <E extends CsvClass, M> void saveCsvData(List<E> temps, Class<M> mainClass,String fileName) throws CsvException {
        HashSet<String> exceptions = new HashSet<>();
        String tempTableName = null;
        boolean exceptionFlag = false;
        int index = 1;
        int mainIndex = 1;
        try {
            String tempTable = temps.get(0).getTempTable();
            tempTableName = temps.get(0).getTempTableName();

            entityManager.createNativeQuery(tempTable).executeUpdate();


            for (E entity : temps) {
                System.out.println("i");
                try{
                    saveEntity(entity,exceptions,index,fileName);

                }
                catch (Exception e){
                    exceptionFlag = true;
                    System.out.println("Exception here at save temp at line "+index);
                    entityManager.clear();
                }
                index++;

            }
            if(!exceptions.isEmpty()){
                throw new CsvException("ERROR", exceptions);
            }
            for (E entity : temps) {
                try{
                    M mainEntity = mainClass.getDeclaredConstructor().newInstance();
                    saveMainEntity(entity, mainEntity,exceptions,mainIndex,fileName);
                    mainIndex+=1;
                }
                catch (Exception e){
                    exceptionFlag = true;
                    System.out.println("Exception here at save main at line "+mainIndex);

                    if(e instanceof ConstraintViolationException){
                        ConstraintViolationException ee = (ConstraintViolationException)e;
                        for (ConstraintViolation<?> constraintViolation : ee.getConstraintViolations()) {
                            ConstraintViolationImpl violation = (ConstraintViolationImpl) constraintViolation;
                            exceptions.add("ERROR in file "+fileName+" at line "+mainIndex+": "+violation.getMessage()+" "+entity.toString());
                        }
                    }
                    else{
                        String mess = e.getMessage();
                        if(e.getCause()!=null){
                            mess = e.getCause().getMessage();
                        }
                        exceptions.add("SQL ERROR at line "+mainIndex+" of file"+fileName+":"+mess+" "+entity.toString());
                    }
                    mainIndex+=1;
                }

            }
            if(!exceptions.isEmpty()){
                exceptionFlag = true;
                throw new CsvException("ERROR", exceptions);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception here at exception");
            System.out.println(e.getMessage());
            exceptionFlag = true;
            entityManager.clear();
            //exceptions.add("SQL ERROR in file "+fileName+": "+e.getMessage());
            throw new CsvException("ERROR", exceptions);
        }
        finally {
            if(exceptions.isEmpty() && !exceptionFlag){
                entityManager.createNativeQuery("DROP TABLE if exists " + tempTableName).executeUpdate();
                entityManager.flush();
            }
        }
    }

    private <E extends CsvClass, M> void saveEntity(E source,Set<String>exceptions,int i,String fileName) {
        if (source.isValid()){
            entityManager.persist(source);
        }
        else {
            exceptions.add("not valid object at line " + i+" of file " + fileName);
        }
    }
    private <E extends CsvClass, M> void saveMainEntity(E source, M main,Set<String>exceptions,int i,String file) {
        if (source.isValid()){
            copyToMain(source, main,exceptions);
            entityManager.merge(main);

        }
        else {
            exceptions.add("Not valid object at line " + i+" of file: "+file);
        }
    }

    private <E extends CsvClass, M> void copyToMain(E source, M main,Set<String>exceptions) {
        try {
            Field[] sourceFields = source.getClass().getDeclaredFields();
            for (Field sourceField : sourceFields) {
                sourceField.setAccessible(true);
                Field mainField = main.getClass().getDeclaredField(sourceField.getName());
                mainField.setAccessible(true);
                mainField.set(main, sourceField.get(source));
            }
        }
        catch (Exception e) {
            String mess = e.getMessage();
            if(e.getCause()!=null){
                mess = e.getCause().getMessage();
            }
            exceptions.add(mess);
            e.printStackTrace();
        }

    }

    private <E extends CsvClass> E createEntityInstance(Class<E> entityClass) throws SQLException {
        try {
            return entityClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new SQLException("Error creating entity instance for " + entityClass.getSimpleName(), e);
        }
    }


   /* private <E extends CsvClass> List<E> readCsvData(InputStream inputStream, Class<E> entityClass) {
        try {
            return csvService.readCsvObject(inputStream, entityClass);
        } catch (Exception e) {
            throw new RuntimeException("Error reading CSV data for " + entityClass.getSimpleName(), e);
        }
    }*/

     /*@Transactional(rollbackFor = CsvException.class)
    public <E extends CsvClass, M> List<E> persistCsvData(InputStream inputStream, Class<E> entityClass, Class<M> mainClass) throws CsvException {
        HashSet<String> exceptions = new HashSet<>();
        String tempTableName = null;
        try {
            E entityInstance = createEntityInstance(entityClass);

            String tempTable = entityInstance.getTempTable();
            tempTableName = entityInstance.getTempTableName();

            entityManager.createNativeQuery(tempTable).executeUpdate();

            List<E> list = readCsvData(inputStream, entityClass);

            int index = 1;
            for (E entity : list) {
                M mainEntity = mainClass.getDeclaredConstructor().newInstance();
                saveEntity(entity, mainEntity,exceptions);
                if (exceptions.isEmpty()) entityManager.flush();
            }
            System.out.println("antso pas ok");
            if(!exceptions.isEmpty()){
                throw new CsvException("CSV ERROR", exceptions);
            }
            System.out.println("antso pre ok");
            for (E entity : list) {
                M mainEntity = mainClass.getDeclaredConstructor().newInstance();
                saveMainEntity(entity, mainEntity,exceptions);
                if (exceptions.isEmpty()) entityManager.flush();
            }

            if(!exceptions.isEmpty()){
                throw new CsvException("CSV ERROR", exceptions);
            }

            System.out.println("antso ok");
            return new ArrayList<>(list);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException |SQLException e) {
            e.printStackTrace();
            exceptions.add(e.getMessage());
            System.out.println("antso exception");
            throw new CsvException("CSV ERROR", exceptions);
        }
        finally {
            if(exceptions.isEmpty()){
                entityManager.createNativeQuery("DROP TABLE if exists " + tempTableName).executeUpdate();
            }
        }
    }*/
}
