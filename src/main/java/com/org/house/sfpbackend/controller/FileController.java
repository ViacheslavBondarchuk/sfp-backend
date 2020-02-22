package com.org.house.sfpbackend.controller;

import com.org.house.sfpbackend.service.impl.FileService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@RestController
@RequestMapping("/sfp/api/files")
public class FileController {
    private FileService fileService;

    @Autowired
    public void setFileService(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping
    public void save(@RequestParam("file") MultipartFile multipartFile) throws IOException {
        fileService.save(multipartFile);
    }

    @GetMapping
    public Set<String> getAll() {
        return fileService.getAllFile();
    }

    @GetMapping("/{filename}")
    public Set<String> getFileByLike(@PathVariable String filename) {
        return fileService.getFileByLike(filename);
    }

    @GetMapping("/download/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse httpServletResponse) throws IOException, NotFoundException {
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("filename=%s", filename));
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        httpServletResponse.getOutputStream().write(fileService.download(filename).readAllBytes());
        httpServletResponse.flushBuffer();
    }

    @DeleteMapping(value = "/{filename}")
    public void delete(@PathVariable String filename) {
        fileService.delete(filename);
    }
}
