package site.easy.to.build.crm.entity;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class ErrorMessage implements ErrorResponse {
    String message;
    public ErrorMessage(String s) {
        super();
        setMessage(s);
    }

    @Override
    public HttpStatusCode getStatusCode() {
        return HttpStatusCode.valueOf(500);
    }

    @Override
    public ProblemDetail getBody() {
        return null;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
