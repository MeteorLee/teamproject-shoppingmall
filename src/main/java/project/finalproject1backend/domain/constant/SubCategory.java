package project.finalproject1backend.domain.constant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import project.finalproject1backend.domain.AuditingFields;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="sub_category")
@Getter
@Setter
@RequiredArgsConstructor
public class SubCategory extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "main_category", nullable = false, length = 20)
    private MainCategory mainCategory;

    @OneToMany(mappedBy = "subCategory")
    private Set<DetailCategory> detailCategories = new HashSet<>();



}
