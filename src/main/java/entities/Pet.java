package entities;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder

public class Pet {
    private long id;
    private Category category;
    private String name;
    private List<String> photoUrls;
    private List<Category> tags;
    private String status;
}
