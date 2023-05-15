package project.finalproject1backend.dto.subCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubCategoryResponseDTO {
    private String subCategoryList;
}
