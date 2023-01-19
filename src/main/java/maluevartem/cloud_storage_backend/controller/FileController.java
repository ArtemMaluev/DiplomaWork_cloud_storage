package maluevartem.cloud_storage_backend.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import maluevartem.cloud_storage_backend.dto.FileDto;
import maluevartem.cloud_storage_backend.exception.FileNotFoundException;
import maluevartem.cloud_storage_backend.model.FileBody;
import maluevartem.cloud_storage_backend.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<Void> addFile(@NotNull @RequestParam("file") MultipartFile file, @RequestParam("filename") String fileName) {
        if (file.isEmpty()) {
            log.info("Файл для загрузки не выбран: {}", file.isEmpty());
            throw new FileNotFoundException("Файл не выбран", 0);
        }
        log.info("Файл для загрузки на сервер: {}", fileName);
        fileService.addFile(file, fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFile(@RequestParam("filename") String fileName) {
        log.info("Запрос файла для скачивания с сервера: {}", fileName);
        FileDto fileDto = fileService.getFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileDto.getFileData());
    }

    @PutMapping("/file")
    public ResponseEntity<Void> renameFile(@RequestParam("filename") String fileName, @Valid @RequestBody FileBody body) {
        log.info("Запрос файла на сервере для переименования: {}", fileName);
        fileService.renameFile(fileName, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestParam("filename") String fileName) {
        log.info("Запрос файла для удаления на сервере: {}", fileName);
        fileService.deleteFile(fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileDto>> getAllFiles(@Min(1) @RequestParam int limit) {
        log.info("Запрос для получения списка файлов. Лимит: {}", limit);
        return new ResponseEntity<>(fileService.getAllFiles(limit), HttpStatus.OK);
    }
}
