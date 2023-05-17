package project.finalproject1backend.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserModifyLicenseRequestDTO {
    @Schema(defaultValue = "~~기업")
    private String companyName;
    @Schema(defaultValue = "서대표")
    private String ownerName;
    private LocalDate openingDate;
    @Schema(defaultValue = "corporateNumber")
    private String corporateNumber;


    public boolean nullCheck(){
        if(this.companyName==null)
            return false;
        if(this.openingDate==null)
            return false;
        if(this.ownerName==null)
            return false;
        if(this.corporateNumber==null)
            return false;
        return true;
    }
}
/*
”ownerName”:”대표자이름”
”openingDate”:”개업일시”
”corporateNumber”:”사업자등록번호”
”businessLicense”:”businessLicense”
 */
