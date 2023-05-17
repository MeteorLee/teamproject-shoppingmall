package project.finalproject1backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.finalproject1backend.domain.AttachmentFile;

public interface AttachmentFileRepository extends JpaRepository<AttachmentFile,Long> {
    void deleteByFileName(String fileName);
    void deleteAllByUserBusinessLicense(Long id);
}
