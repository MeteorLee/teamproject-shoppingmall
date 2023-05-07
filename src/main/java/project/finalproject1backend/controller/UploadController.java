package project.finalproject1backend.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.dto.UploadDTO;
import project.finalproject1backend.util.UploadUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@RequestMapping("/upload/")
@RestController
public class UploadController {
    UploadUtil attachmentUtil =new UploadUtil();
    @Tag(name = "테스트용 컨트롤러", description = "테스트용 컨트롤러")
    @Operation(summary = "upload 메서드", description = "업로드 메서드입니다.")
    @PostMapping(value = "/", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> upload(@RequestPart MultipartFile[] multipartFiles){

        String path= "/home/ubuntu/FinalProject/upload/test";
        List<UploadDTO> result = new ArrayList<>();
        for (MultipartFile i: multipartFiles){
            result.add(attachmentUtil.upload(i,path));

        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Tag(name = "테스트용 컨트롤러", description = "테스트용 컨트롤러")
    @Operation(summary = "view 메서드", description = "view 메서드입니다.")
    @GetMapping("/view/{fileName}")
    public ResponseEntity<?> viewFile(@PathVariable String fileName){
       return attachmentUtil.viewFile(fileName);
    }

    @Tag(name = "테스트용 컨트롤러", description = "테스트용 컨트롤러")
    @Operation(summary = "delete 메서드", description = "delete 메서드입니다.")
    @DeleteMapping("/remove/{fileName}")
    public Map<String,Boolean> deleteFile(@PathVariable String fileName){
        return attachmentUtil.deleteFile(fileName);
    }
}
