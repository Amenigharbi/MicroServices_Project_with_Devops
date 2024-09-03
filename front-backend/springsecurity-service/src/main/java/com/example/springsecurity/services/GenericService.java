package com.example.springsecurity.services;

import java.util.List;

public interface GenericService<T> {
    T create(T entity);
    T update(T entity);
    void delete(Long id);
    T getById(Long id);
    List<T> getAll();

}
