package maluevartem.cloud_storage_backend.repository;

import maluevartem.cloud_storage_backend.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {

    FileEntity findFileByFileName(String fileName);

    Optional<FileEntity> findFileByHash(String hash);


    //List<File> findFilesByLimit(int limit);
}
