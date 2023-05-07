package project.finalproject1backend.dto;

import lombok.Data;

@Data
public class UploadDTO {
    private String uuid;
    private String originalName;
    private String fileName;
    private String thumbFileName;

    public UploadDTO(String uuid, String originalName, String fileName) {
        this.uuid = uuid;
        this.originalName = originalName;
        this.fileName = fileName;
    }

    public UploadDTO(String uuid, String originalName, String fileName, String thumbFileName) {
        this.uuid = uuid;
        this.originalName = originalName;
        this.fileName = fileName;
        this.thumbFileName = thumbFileName;
    }
}
