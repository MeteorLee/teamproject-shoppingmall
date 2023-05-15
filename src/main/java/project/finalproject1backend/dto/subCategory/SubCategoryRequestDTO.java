package project.finalproject1backend.dto.subCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.constant.MainCategory;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryRequestDTO {
    private String subCategoryName;
    private MainCategory mainCategory;
}
