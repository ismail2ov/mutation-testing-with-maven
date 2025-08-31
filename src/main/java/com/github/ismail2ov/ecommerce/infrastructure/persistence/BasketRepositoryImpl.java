package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.ismail2ov.ecommerce.domain.Basket;
import com.github.ismail2ov.ecommerce.domain.BasketRepository;
import com.github.ismail2ov.ecommerce.domain.exception.BasketPersistenceException;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.BasketMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BasketRepositoryImpl implements BasketRepository {

    private final EntityManager entityManager;
    private final BasketMapper basketMapper;

    @Override
    public Basket save(Basket basket) {
        Assert.notNull(basket, "Basket must not be null");

        try {
            BasketEntity basketEntity = basketMapper.entityFrom(basket);

            if (Objects.isNull(basketEntity.getId())) {
                entityManager.persist(basketEntity);
                entityManager.flush();
                return basketMapper.fromEntity(basketEntity);
            } else {
                BasketEntity savedEntity = entityManager.merge(basketEntity);
                return basketMapper.fromEntity(savedEntity);
            }

        } catch (Exception e) {
            log.error("Error saving basket: {}", basket, e);
            throw new BasketPersistenceException("Failed to save basket", e);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Basket> getByUserId(long userId) {
        String jpql = "SELECT b FROM BasketEntity b WHERE b.userId = :userId";

        List<BasketEntity> results = entityManager.createQuery(jpql, BasketEntity.class)
            .setParameter("userId", userId)
            .getResultList();

        Optional<BasketEntity> basketEntity = results.stream().findFirst();
        return basketEntity.map(basketMapper::fromEntity);
    }
}