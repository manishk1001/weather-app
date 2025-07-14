package weatherApp.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import weatherApp.exception.ErrorModel;

import java.util.List;

@Builder
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class RestApiResponse<T> {
    private T data;

    @Singular
    private List<ErrorModel> errors;
}
