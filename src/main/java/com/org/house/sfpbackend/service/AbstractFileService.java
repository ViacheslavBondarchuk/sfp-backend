package com.org.house.sfpbackend.service;

import com.mongodb.DBObject;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.org.house.sfpbackend.utils.AuthUtils;
import javassist.NotFoundException;
import org.springframework.data.mongodb.gridfs.GridFsOperations;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

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

    public Set<String> getFileByLike(final String filename) {
        Set<String> filenames = new HashSet<>();
        GridFSFindIterable gridFSFindIterable = gridFsOperations.find(query(where("filename")
                .regex(String.format(".*%s.*", filename)).and("metadata.user_id").is(AuthUtils.getUserId())));
        MongoCursor<GridFSFile> iterator = gridFSFindIterable.iterator();
        while (iterator.hasNext()) filenames.add(iterator.next().getFilename());
        return filenames;
    }

    public Set<String> getAllFile() {
        HashSet<String> filenames = new HashSet<>();
        MongoCursor<GridFSFile> iterator = gridFsOperations.find(query(where("metadata.user_id").is(AuthUtils.getUserId()))).iterator();
        while (iterator.hasNext()) filenames.add(iterator.next().getFilename());
        return filenames;
    }

    public void delete(final String filename) {
        gridFsOperations.delete(query(where("metadata.user_id").is(AuthUtils.getUserId()).and("filename").is(filename)));
    }

}
