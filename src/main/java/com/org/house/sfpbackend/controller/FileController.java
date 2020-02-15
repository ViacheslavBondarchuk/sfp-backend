package com.org.house.sfpbackend.controller;

import com.mongodb.client.gridfs.GridFSFindIterable;
import com.org.house.sfpbackend.service.impl.FileService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    public GridFSFindIterable getAll() {
        return fileService.getAllFile();
    }

    @GetMapping("/{filename}")
    public void download(@PathVariable String filename, HttpServletResponse httpServletResponse) throws IOException {
        httpServletResponse.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("filename=%s", filename));
        IOUtils.copy(fileService.download(filename), httpServletResponse.getOutputStream());
        httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        httpServletResponse.flushBuffer();
    }

    @DeleteMapping(value = "/{filename}")
    public void delete(@PathVariable String filename) {
        fileService.delete(filename);
    }
}
