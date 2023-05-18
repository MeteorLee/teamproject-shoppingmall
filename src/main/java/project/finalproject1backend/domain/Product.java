package project.finalproject1backend.domain;

import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import project.finalproject1backend.domain.Inquiry.BuyInquiry;
import project.finalproject1backend.domain.constant.Delivery;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.domain.constant.ProductStatus;
import project.finalproject1backend.dto.product.ProductFormDto;
import project.finalproject1backend.exception.OutOfStockException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Table(name="product")
@ToString
@Getter
@Builder
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
    @Enumerated(EnumType.STRING)
    private Delivery delivery;    //배송방법

    @Setter
    @Column(nullable = false)
    private int deliveryCharge;    //배송비
    @Setter
    @Column(nullable = false)
    private int stockNumber;     //재고수량

    @Setter
    @Column(nullable = false)
    private int minimumQuantity;     //최소수량

    @Setter
    @Enumerated(EnumType.STRING)
    private ProductStatus productStatus;  //판매상태

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Setter
    private MainCategory mainCategory;  //대분류

    @Setter
    private String message;  //상품설명
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private SubCategory productSubcategory;  //중분류

    //유저의 현황 관련
    @OneToMany(mappedBy = "cartItems", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Cart> carts = new ArrayList<>();

    //order 로 했다가 domain의 Order말고 다른 Order 들어가서 오류가 많이 나서 orders로 변경...
    @OneToMany(mappedBy = "buyInquiryProduct", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<BuyInquiry> inquiries = new ArrayList<>();

    @OneToMany(mappedBy = "productAttachment", cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<AttachmentFile> imgList = new ArrayList<>();


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
        this.productStatus = productFormDto.getProductStatus();
        this.minimumQuantity = productFormDto.getMinimumQuantity();
        this.delivery = productFormDto.getDelivery();
        this.stockNumber = productFormDto.getStockNumber();
        this.deliveryCharge = productFormDto.getDeliveryCharge();
        this.minimumQuantity = productFormDto.getMinimumQuantity();
        this.mainCategory = productFormDto.getMaincategory();
//        this.productSubcategory = productFormDto.getSubcategory();
        this.message = productFormDto.getMessage();

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
