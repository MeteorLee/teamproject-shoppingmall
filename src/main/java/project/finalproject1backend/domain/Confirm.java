package project.finalproject1backend.domain;

import lombok.*;
import project.finalproject1backend.util.Encrypt256;

import javax.persistence.*;

@ToString
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Confirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Setter
    @Convert(converter = Encrypt256.class)
    private String email;

    @Setter
    private String token;

}
