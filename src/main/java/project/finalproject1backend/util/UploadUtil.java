package project.finalproject1backend.util;

import net.coobird.thumbnailator.Thumbnailator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import project.finalproject1backend.dto.UploadDTO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
@Component
public class UploadUtil {


    //파일 업로드
    public UploadDTO upload(MultipartFile multipartFile, String uploadPath){
        if(multipartFile.isEmpty() || multipartFile==null){
            throw new IllegalArgumentException("checkMultiPartFile");
        }
        String originalName = multipartFile.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        Path savePath = Paths.get(uploadPath, uuid+"_"+originalName);
        String fileName =uuid+"_"+originalName;
        try{
            multipartFile.transferTo(savePath);
            //이미지 파일이면 썸네일 파일도 생성
            if(Files.probeContentType(savePath).startsWith("image")){
                File thumbFile = new File(uploadPath,"s_"+uuid+"_"+originalName);
                Thumbnailator.createThumbnail(savePath.toFile(),thumbFile,200,200);
                return new UploadDTO(uuid,originalName,fileName,"s_"+uuid+"_"+originalName);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UploadDTO(uuid,originalName,fileName);
    }

    //파일 조회
    public ResponseEntity<?> viewFile(String fileName,String path){
        Resource resource = new FileSystemResource(path+File.separator+fileName);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();

        try{
            headers.add("Content-Type",Files.probeContentType(resource.getFile().toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    public Map<String,Boolean> deleteFile(String fileName,String path){
        Resource resource = new FileSystemResource(path+File.separator+fileName);
        String resourceName = resource.getFilename();
        Map<String,Boolean> resultMap = new HashMap<>();
        boolean removed = false;
        try {
            String contentType = Files.probeContentType(resource.getFile().toPath());
            removed = resource.getFile().delete();
            //이미지 파일이면 썸네일 파일도 생성
            if(contentType.startsWith("image")){
                File thumbFile = new File(path+File.separator+"s_"+fileName);
                thumbFile.delete();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        resultMap.put("result",removed);
        return resultMap;
    }

}
