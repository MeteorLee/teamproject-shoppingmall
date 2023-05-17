package project.finalproject1backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModifyRequestDTO {
    public String content;
    public String value;
}
/*
{
”content”:” 변경할 정보”
”value”:”변경할 정보 내용”
}
 */
