package project.finalproject1backend.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import project.finalproject1backend.domain.constant.Delivery;
import project.finalproject1backend.domain.constant.DetailCategory;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.domain.constant.SubCategory;
import project.finalproject1backend.dto.product.ProductFormDto;
import project.finalproject1backend.exception.OutOfStockException;

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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(nullable = false, length = 50)
    private String productName;   //상품명

    @Setter
    @Column(nullable = false, length = 50)
    private int consumerPrice;   //소비자가

    @Setter
    @Column(nullable = false, length = 50)
    private int productPrice;   //판매가

    @Setter
    @Column(nullable = false, length = 50)
    private String manufacturer;   //제조사

    @Setter
    @Column(nullable = false)
    private Delivery delivery;    //배송방법

    @Setter
    @Column(nullable = false)
    private int stockNumber;     //재고수량

    @Setter
    private int deliveryCharge;    //배송비

    @Setter
    @Column(nullable = false)
    private int minimumQuantity;     //최소수량

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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public void updateProduct(ProductFormDto productFormDto){
        this.productName = productFormDto.getProductName();
        this.consumerPrice = productFormDto.getConsumerPrice();
        this.productPrice = productFormDto.getProductPrice();
        this.manufacturer = productFormDto.getManufacturer();
        this.delivery = productFormDto.getDelivery();
        this.stockNumber = productFormDto.getStockNumber();
        this.deliveryCharge = productFormDto.getDeliveryCharge();
        this.minimumQuantity = productFormDto.getMinimumQuantity();
        this.maincategory = productFormDto.getMaincategory();
        this.subcategory = productFormDto.getSubcategory();
        this.detailcategory = productFormDto.getDetailcategory();
    }


    public void removeStock(int stockNumber){ //재고 수량 감소
        int restStock = this.stockNumber - stockNumber;
        if(restStock<0){
            throw new OutOfStockException("상품의 재고가 부족 합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    public void addStock(int stockNumber){
        this.stockNumber += stockNumber;
    }


}
