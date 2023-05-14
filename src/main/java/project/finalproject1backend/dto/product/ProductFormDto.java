package project.finalproject1backend.dto.product;

import lombok.*;
import org.modelmapper.ModelMapper;
import project.finalproject1backend.domain.Product;
import project.finalproject1backend.domain.constant.Delivery;
import project.finalproject1backend.domain.constant.MainCategory;
import project.finalproject1backend.domain.constant.ProductStatus;
import project.finalproject1backend.domain.SubCategory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductFormDto {


    private Long id;

    @NotBlank(message = "상품명은 필수 입력 값입니다.")
    private String productName;   //상품명

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int consumerPrice;   //소비자가

    @NotNull(message = "가격은 필수 입력 값입니다.")
    private int productPrice;   //판매가

    @NotBlank(message = "제조사는 필수 입력 값입니다.")
    private String manufacturer;   //제조사

    @NotBlank(message = "배송방법은 필수 입력 값입니다.")
    private Delivery delivery;    //배송방법

    @NotNull(message = "재고는 필수 입력 값입니다.")
    private int stockNumber;     //재고수량

    private ProductStatus productStatus;  //공개 비공개

    @NotNull(message = "배송비는 필수 입력 값입니다.")
    private int deliveryCharge;    //배송비

    @NotNull(message = "최소수량 필수 입력 값입니다.")
    private int minimumQuantity;     //최소수량

    @NotNull(message = "대분류는 필수 입력 항목입니다.")
    private MainCategory maincategory;  //대분류

    @NotBlank(message = "중분류는 필수 입력 항목입니다.")
    private String subcategory;  //중분류

    @NotBlank(message = "상품설명은 필수 입력 항목입니다.")
    private String message;  //상품설명

    private List<Integer> option; //옵션


    private static ModelMapper modelMapper = new ModelMapper();


    public Product createProduct(){
        return modelMapper.map(this, Product.class);
    }

    public static ProductFormDto of(Product product){
        return modelMapper.map(product, ProductFormDto.class);
    }

}
