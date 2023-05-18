package project.finalproject1backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.finalproject1backend.domain.AttachmentFile;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentDTO {
    private String fileName;
    private String filePath;
    private String originalFileName;

    public AttachmentDTO(AttachmentFile a) {
        this.fileName = a.getFileName();
        this.filePath = a.getFilePath();
        this.originalFileName = a.getOriginalFileName();
    }
}
