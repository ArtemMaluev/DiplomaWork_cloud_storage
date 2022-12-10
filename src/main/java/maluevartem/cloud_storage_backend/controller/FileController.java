package maluevartem.cloud_storage_backend.controller;

import maluevartem.cloud_storage_backend.service.FileService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class FileController {

    private FileService service;

    @PostMapping("/file")
    public String addFile() {
        return "file";
    }

    @GetMapping("/file")
    public String getFile() {
        return "GET file";
    }

    @PutMapping("/file")
    public void renameFile(@RequestParam String name) {
    }

    @DeleteMapping("/file")
    public void deleteFile() {
    }

    @GetMapping("/list")
    public List<String> getAllFile() {
        return null;
    }
}
