package com.org.house.sfpbackend.service;

import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.org.house.sfpbackend.utils.AuthUtils;
import javassist.NotFoundException;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

public abstract class AbstractFileService {
    protected GridFsOperations gridFsOperations;

    public AbstractFileService(GridFsOperations gridFsOperations) {
        this.gridFsOperations = gridFsOperations;
    }

    public void save(final MultipartFile multipartFile, final DBObject dbObject) throws IOException {
        gridFsOperations.store(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), MediaType.APPLICATION_OCTET_STREAM_VALUE, dbObject);
    }

    public InputStream download(final String filename) throws IOException, NotFoundException {
        GridFSFile gridFSFile = gridFsOperations.findOne(query(where("metadata.user_id").is(AuthUtils.getUserId()).and("filename").is(filename)));
        if (gridFSFile == null)
            throw new NotFoundException(String.format("File by name: %s has been not found", filename));
        return gridFsOperations.getResource(gridFSFile).getInputStream();
    }

    public GridFSFindIterable getFileByLike(final String filename) {
        return gridFsOperations.find(query(where("filename").regex(String.format(".*%s.*", filename))));
    }

    public GridFSFindIterable getAllFile() {
        return gridFsOperations.find(query(where("metadata.user_id").is(AuthUtils.getUserId())));
    }

    public void delete(final String filename) {
        gridFsOperations.delete(query(where("metadata.user_id").is(AuthUtils.getUserId()).and("filename").is(filename)));
    }

}
