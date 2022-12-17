package maluevartem.cloud_storage_backend.service;

import lombok.RequiredArgsConstructor;
import maluevartem.cloud_storage_backend.dto.FileDto;
import maluevartem.cloud_storage_backend.entity.FileEntity;
import maluevartem.cloud_storage_backend.model.FileBody;
import maluevartem.cloud_storage_backend.repository.FileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Transactional
    public void addFile(MultipartFile file, String fileName) {

//        TODO fileRepository.findFileByFileName(fileName);
        String hash = null;
        byte[] fileBytes = null;
        try {
            hash = checksum(file);
            fileBytes = file.getBytes();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

//        TODO fileRepository.findFileByHash(hash);
        FileEntity createdFile = FileEntity.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .fileData(fileBytes)
                .hash(hash)
                .size(file.getSize())
                .build();
        fileRepository.save(createdFile);
    }

    private String checksum(MultipartFile file) throws NoSuchAlgorithmException, IOException {

        MessageDigest md = MessageDigest.getInstance("SHA-256");

        try (var fis = file.getInputStream();
             var bis = new BufferedInputStream(fis);
             var dis = new DigestInputStream(bis, md)) {

            while (dis.read() != -1) ;
            md = dis.getMessageDigest();
        }

        var result = new StringBuilder();
        for (byte b : md.digest()) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }

    @Transactional
    public FileDto getFile(String fileName) {

        FileEntity fileFound = fileRepository.findFileByFileName(fileName);
//        TODO Exception "Файл не найден!"

        return FileDto.builder()
                .fileName(fileFound.getFileName())
                .fileType(fileFound.getFileType())
                .fileData(fileFound.getFileData())
                .hash(fileFound.getHash())
                .size(fileFound.getSize())
                .build();
    }

    public void renameFile(String fileName, FileBody fileBody) {

        FileEntity fileFoundForUpdate = fileRepository.findFileByFileName(fileName);

//        TODO fileRepository.findFileByFileName(fileName);

        fileFoundForUpdate.setFileName(fileBody.getFileName());
        fileRepository.save(fileFoundForUpdate);
    }

    public void deleteFile(String fileName) {
//        TODO проверка
        FileEntity fileFromStorage = fileRepository.findFileByFileName(fileName);
        fileRepository.deleteById(fileFromStorage.getId());
    }

    public List<FileDto> getAllFiles(int limit) {

        List<FileEntity> filesByUserIdWithLimit = null; //fileRepository.findFilesByLimit(limit);

        return filesByUserIdWithLimit.stream()
                .map(file -> FileDto.builder()
                        .fileName(file.getFileName())
                        .fileType(file.getFileType())
                        .fileData(file.getFileData())
                        .hash(file.getHash())
                        .size(file.getSize())
                        .build()).collect(Collectors.toList());
    }
}

