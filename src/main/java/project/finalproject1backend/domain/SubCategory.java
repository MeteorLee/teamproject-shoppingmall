package project.finalproject1backend.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import project.finalproject1backend.domain.AuditingFields;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.constant.MainCategory;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubCategory extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_category", nullable = false, length = 20)
    private MainCategory mainCategory;

    @JsonIgnore
    @OneToMany(mappedBy = "productSubcategory", cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @ToString.Exclude
    @Builder.Default
    private List<Product> productList = new ArrayList<>();


}
