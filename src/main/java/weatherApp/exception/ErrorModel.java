package weatherApp.exception;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ErrorModel {
    private String code;
    private String message;
}
