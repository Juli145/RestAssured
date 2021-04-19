package entities;

import lombok.*;


@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Order {
    private long id;
    private long petId;
    private int quantity;
    private String shipDate;
    private String status;
    private boolean complete;
}
