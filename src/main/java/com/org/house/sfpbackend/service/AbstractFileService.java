package com.org.house.sfpbackend.service;

import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.org.house.sfpbackend.utils.AuthUtils;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public abstract class AbstractFileService {
    private final String CONTENT_TYPE_FILE = "application/octet-stream";
    protected GridFsOperations gridFsOperations;

    public AbstractFileService(GridFsOperations gridFsOperations) {
        this.gridFsOperations = gridFsOperations;
    }

    public void save(final MultipartFile multipartFile, final DBObject dbObject) throws IOException {
        gridFsOperations.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), CONTENT_TYPE_FILE, dbObject);
    }

    public InputStream download(final String filename) throws IOException {
        return gridFsOperations.getResource(
                gridFsOperations.findOne(query(where("metadata.user_id").is(AuthUtils.getUserId()).and("filename").is(filename)))).getInputStream();
    }

    public GridFSFindIterable getAllFile() {
        return gridFsOperations.find(query(where("metadata.user_id").is(AuthUtils.getUserId())));
    }

    public void delete(final String filename) {
        gridFsOperations.delete(query(where("metadata.user_id").is(AuthUtils.getUserId()).and("filename").is(filename)));
    }

}
