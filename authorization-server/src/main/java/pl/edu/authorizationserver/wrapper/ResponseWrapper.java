package pl.edu.authorizationserver.wrapper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ResponseWrapper<T> {
    private T data;
    private String error;
    private String status;
    private int statusCode;

    public static <T> ResponseWrapper<T> ok(T data) {
        ResponseWrapper<T> wrapper = new ResponseWrapper<>();
        wrapper.setData(data);
        wrapper.setStatus(HttpStatus.OK.name());
        wrapper.setStatusCode(HttpStatus.OK.value());
        return wrapper;
    }

    public static <T> ResponseWrapper<T> withError(HttpStatus status, String message) {
        ResponseWrapper<T> wrapper = new ResponseWrapper<>();
        wrapper.setError(message);
        wrapper.setStatus(status.name());
        wrapper.setStatusCode(status.value());
        return wrapper;
    }
}
