package site.easy.to.build.crm.entity.exceptions;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;
public class CsvException extends Exception {
    Set<String> causes;
    public CsvException() {
        super();
    }
    public CsvException(String message) {
        super(message);
    }
    public CsvException(Set<String> causes) {
        setCauses(causes);
    }
    public CsvException(String message,Set<String> causes) {
        super(message);
        setCauses(causes);
    }
    public CsvException(String message,Throwable cause) {
        super(message,cause);
    }

    public Set<String> getCauses() {
        return causes;
    }

    public void setCauses(Set<String> causes) {
        this.causes = causes;
    }

    @Override
    public String getMessage() {
        StringBuilder message= new StringBuilder(super.getMessage() + ": ");
        int i=0;
        for (String cause : causes) {
            if(i>0 && i!= causes.size()-1) message.append(",");
            message.append(cause);
            i++;
        }
        return message.toString();
    }

}
