package project.finalproject1backend.dto.user;

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
    private String ownerName;
    private LocalDate openingDate;
    private String corporateNumber;
    private String businessLicense;

    public boolean nullCheck(){
        if(this.openingDate==null)
            return false;
        if(this.ownerName==null)
            return false;
        if(this.corporateNumber==null)
            return false;
        if(this.businessLicense==null)
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
