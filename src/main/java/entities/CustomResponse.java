package entities;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder

public class CustomResponse {
    private long code;
    private String type;
    private String message;
}
