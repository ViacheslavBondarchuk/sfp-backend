package com.org.house.sfpbackend.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.org.house.sfpbackend.service.AbstractFileService;
import com.org.house.sfpbackend.utils.AuthUtils;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class FileService extends AbstractFileService {
    @Autowired
    public FileService(GridFsOperations gridFsOperations) {
        super(gridFsOperations);
    }

    public void save(MultipartFile multipartFile) throws IOException {
        super.save(multipartFile, new BasicDBObject() {{
            put("user_id", AuthUtils.getUserId());
        }});
    }

    public InputStream download(String filename) throws IOException, NotFoundException {
        return super.download(filename);
    }

    @Override
    public GridFSFindIterable getFileByLike(String filename) {
        return super.getFileByLike(filename);
    }

    public GridFSFindIterable getAllFile() {
        return super.getAllFile();
    }

    public void delete(String filename) {
        super.delete(filename);
    }
}
