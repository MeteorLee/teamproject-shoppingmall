package project.finalproject1backend.domain.constant;

import lombok.Getter;
import lombok.Setter;
import project.finalproject1backend.domain.AuditingFields;

import javax.persistence.*;

@Entity
@Table(name="detail_category")
@Getter
@Setter
public class DetailCategory extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subCategory;

}