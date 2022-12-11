package maluevartem.cloud_storage_backend.controller;

import lombok.RequiredArgsConstructor;
import maluevartem.cloud_storage_backend.dto.FileDto;
import maluevartem.cloud_storage_backend.model.FileBody;
import maluevartem.cloud_storage_backend.service.FileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping("/file")
    public ResponseEntity<Void> addFile(@NotNull @RequestParam("file") MultipartFile file, @RequestParam("filename") String fileName) {
        if (file.isEmpty()) {
            // TODO Exception "Файл не выбран!"
        }
        fileService.addFile(file, fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/file")
    public ResponseEntity<byte[]> getFile(@RequestParam("filename") String fileName) {
        FileDto fileDto = fileService.getFile(fileName);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(fileDto.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDto.getFileName() + "\"")
                .body(fileDto.getData());
    }

    @PutMapping("/file")
    public ResponseEntity<Void> renameFile(@RequestParam("filename") String fileName, @Valid @RequestBody FileBody body) {

        fileService.renameFile(fileName, body);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/file")
    public ResponseEntity<Void> deleteFile(@RequestParam("filename") String fileName) {
        fileService.deleteFile(fileName);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<FileDto>> getAllFiles(@RequestParam int limit) {
        return new ResponseEntity<>(fileService.getAllFiles(limit), HttpStatus.OK);
    }
}
