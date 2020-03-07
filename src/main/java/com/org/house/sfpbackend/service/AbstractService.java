package com.org.house.sfpbackend.service;

import javassist.NotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.mail.MessagingException;

public abstract class AbstractService<T, R extends JpaRepository<T, Long>> {
    protected R repository;

    public AbstractService(R repository) {
        this.repository = repository;
    }

    public T create(final T t) throws MessagingException {
        return repository.save(t);
    }

    public void update(final T t, final long id) throws NotFoundException {
        repository.findById(id).map(dbObj -> {
            BeanUtils.copyProperties(t, dbObj, "id");
            return repository.saveAndFlush(dbObj);
        }).orElseThrow(() -> new NotFoundException(String.format("Object by id: %d has been not found", id)));
    }

    public Iterable<T> readAll() {
        return repository.findAll();
    }

    public T read(final long id) throws NotFoundException {
        return repository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Object by id: %d has been not found", id)));
    }

    public void delete(final long id) {
        repository.deleteById(id);
    }
}
