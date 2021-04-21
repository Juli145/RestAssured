package entities;

import lombok.*;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder

public class Pet {
    private BigInteger id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Category> tags;
    private String status;
}
