package likelion.demo.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.ALWAYS)
public class ApiResponse<T> {

    private final boolean success;
    private final int code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success(int code, String message, T data) {
        return new ApiResponse<>(true, code, message, data);
    }

    public static ApiResponse<Void> success(int code, String message) {
        return new ApiResponse<>(true, code, message, null);
    }

    public static ApiResponse<Void> error(int code, String message) {
        return new ApiResponse<>(false, code, message, null);
    }

    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<>(false, code, message, data);
    }
}
