package project.finalproject1backend.domain;

import javax.persistence.*;

@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    private User cartUser;
    @ManyToOne(fetch = FetchType.EAGER)
    private Product cartProduct;
}
