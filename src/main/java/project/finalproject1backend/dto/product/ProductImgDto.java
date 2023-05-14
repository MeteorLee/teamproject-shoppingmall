package project.finalproject1backend.dto.product;

import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import project.finalproject1backend.domain.ProductImg;

@Getter
@Setter
public class ProductImgDto {

    private Long id;

    private String imgName;

    private String oriImgName;

    private String imgUrl;

    private String repImgYn;

    private static ModelMapper modelMapper = new ModelMapper();

    public static ProductImgDto of(ProductImg productImg) {
        return modelMapper.map(productImg,ProductImgDto.class);
    }

}
