package maluevartem.cloud_storage_backend.repository;

import maluevartem.cloud_storage_backend.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {

    File findFileByFileName(String fileName);

    Optional<File> findFileByHash(String hash);


    //List<File> findFilesByLimit(int limit);
}
