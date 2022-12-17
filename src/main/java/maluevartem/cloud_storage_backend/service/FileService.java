package maluevartem.cloud_storage_backend.service;

import lombok.RequiredArgsConstructor;
import maluevartem.cloud_storage_backend.dto.FileDto;
import maluevartem.cloud_storage_backend.entity.FileEntity;
import maluevartem.cloud_storage_backend.exception.FileNotFoundException;
import maluevartem.cloud_storage_backend.exception.IncorrectDataEntry;
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

//        TODO добавить ID
        fileRepository.findFileByFileName(fileName).ifPresent(s -> {
            throw new IncorrectDataEntry("Файл с таким именем: { " + fileName + " } уже существует", 0);
        });
        String hash = null;
        byte[] fileData = null;
        try {
            hash = checksum(file);
            fileData = file.getBytes();
        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }

        if (fileRepository.findFileByHash(hash).isPresent()) {
            return;
        }
        FileEntity createdFile = FileEntity.builder()
                .fileName(fileName)
                .fileType(file.getContentType())
                .fileData(fileData)
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

//        TODO добавить ID
        FileEntity file = fileRepository.findFileByFileName(fileName).orElseThrow(() -> new FileNotFoundException(
                        "Файл с именем: { " + fileName + " } не найден", 0));

        return FileDto.builder()
                .fileName(file.getFileName())
                .fileType(file.getFileType())
                .fileData(file.getFileData())
                .hash(file.getHash())
                .size(file.getSize())
                .build();
    }

    public void renameFile(String fileName, FileBody fileBody) {

//        TODO добавить ID
        FileEntity fileToRename = fileRepository.findFileByFileName(fileName).orElseThrow(() -> new FileNotFoundException(
                        "Файл с именем: { " + fileName + " } не найден", 0));

//        TODO добавить ID
        fileRepository.findFileByFileName(fileName).ifPresent(s -> {
            throw new IncorrectDataEntry("Файл с таким именем: { " + fileName + " } уже существует", 0);
        });

        fileToRename.setFileName(fileBody.getFileName());
        fileRepository.save(fileToRename);
    }

    public void deleteFile(String fileName) {
//        TODO добавить ID
        FileEntity fileFromStorage = fileRepository.findFileByFileName(fileName).orElseThrow(() -> new FileNotFoundException(
                "Файл с именем: { " + fileName + " } не найден", 0));
        fileRepository.deleteById(fileFromStorage.getId());
    }

    public List<FileDto> getAllFiles(int limit) {

        List<FileEntity> listFiles = fileRepository.findFilesByLimit(limit);

        return listFiles.stream()
                .map(file -> FileDto.builder()
                        .fileName(file.getFileName())
                        .fileType(file.getFileType())
                        .fileData(file.getFileData())
                        .hash(file.getHash())
                        .size(file.getSize())
                        .build()).collect(Collectors.toList());
    }
}

