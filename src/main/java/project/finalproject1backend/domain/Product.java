package project.finalproject1backend.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import project.finalproject1backend.domain.constant.Delivery;
import project.finalproject1backend.domain.constant.DetailCategory;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.domain.constant.SubCategory;

import javax.persistence.*;
import java.util.Objects;

@Table(name="product")
@ToString
@Getter
@Builder
@Where(clause = "is_deleted = false")
@SQLDelete(sql = "UPDATE user SET is_deleted = true, deleted_at=now() WHERE id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product extends AuditingFields{
    @Id
    @Column(name="product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false, length = 50)
    private String product_Name;   //상품명

    @Setter
    @Column(name="price", nullable = false)
    private int consumer_price;   //소비자가

    @Setter
    @Column(name="price", nullable = false)
    private int product_price;   //판매가

    @Setter
    @Column(nullable = false, length = 50)
    private String Manufacturer;   //제조사

    @Setter
    @Column(nullable = false)
    private Delivery delivery;    //배송방법

    @Setter
    @Column(nullable = false)
    private int stockNumber;     //재고수량

    @Setter
    private int delivery_charge;    //배송비

    @Setter
    @Column(nullable = false)
    private int Minimum_quantity;     //최소수량
    @Setter
    private String product_image;    //상품이미지

    @Setter
    private MainCategory maincategory;  //대분류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sub_category_id")
    private SubCategory subcategory;  //중분류

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "detail_category_id")
    private DetailCategory detailcategory; //소분류


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;s
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
