package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import jakarta.persistence.EntityManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public abstract class BaseRepository<T> {

    protected final EntityManager entityManager;

    protected T saveOrUpdateEntity(T entity) {
        if (isNew(entity)) {
            entityManager.persist(entity);
            entityManager.flush();
            return entity;
        } else {
            return entityManager.merge(entity);
        }
    }

    protected abstract boolean isNew(T entity);

}
