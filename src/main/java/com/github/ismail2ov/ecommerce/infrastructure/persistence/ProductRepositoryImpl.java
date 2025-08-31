package com.github.ismail2ov.ecommerce.infrastructure.persistence;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import jakarta.persistence.EntityManager;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.github.ismail2ov.ecommerce.domain.Product;
import com.github.ismail2ov.ecommerce.domain.ProductRepository;
import com.github.ismail2ov.ecommerce.domain.exception.CrossSellRelationAlreadyExistsException;
import com.github.ismail2ov.ecommerce.domain.exception.ProductNotFoundException;
import com.github.ismail2ov.ecommerce.domain.exception.ProductPersistenceException;
import com.github.ismail2ov.ecommerce.infrastructure.mapper.ProductMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProductRepositoryImpl implements ProductRepository {

    private final EntityManager entityManager;
    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAll() {
        return entityManager.createQuery("SELECT p FROM ProductEntity p", ProductEntity.class)
            .getResultList()
            .stream()
            .map(productMapper::fromEntity)
            .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(entityManager.find(ProductEntity.class, id))
            .map(productMapper::fromEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> getCrossSellProducts(Long id) {
        String jpql = """
            SELECT p FROM ProductEntity p
            WHERE p.id IN (
                SELECT cs.xsellId FROM CrossSaleEntity cs WHERE cs.productId = :productId
            )
            """;

        return entityManager.createQuery(jpql, ProductEntity.class)
            .setParameter("productId", id)
            .getResultList()
            .stream()
            .map(productMapper::fromEntity)
            .toList();
    }

    @Override
    public Product save(Product product) {
        Assert.notNull(product, "Product must not be null");

        try {
            ProductEntity productEntity = productMapper.entityFrom(product);

            if (Objects.isNull(productEntity.getId())) {
                entityManager.persist(productEntity);
                entityManager.flush();
                return productMapper.fromEntity(productEntity);
            } else {
                ProductEntity savedEntity = entityManager.merge(productEntity);
                return productMapper.fromEntity(savedEntity);
            }

        } catch (Exception e) {
            log.error("Error saving product: {}", product, e);
            throw new ProductPersistenceException("Failed to save product", e);
        }
    }

    @Override
    public void addCrossSellProduct(Long productId, Long xsellId) {
        if (productNotFound(productId)) {
            log.info("CrossSellProduct: Product not found with productId {}", productId);
            throw new ProductNotFoundException("Product not found with id: " + productId);
        }

        if (productNotFound(xsellId)) {
            log.info("CrossSellProduct: Product not found with xsellId {}", xsellId);
            throw new ProductNotFoundException("Cross-sell product not found with id: " + xsellId);
        }

        if (crossSellRelationExists(productId, xsellId)) {
            log.info("Cross-sell relation already exists between {} and {}", productId, xsellId);
            throw new CrossSellRelationAlreadyExistsException("Cross-sell relation already exists between product " + productId + " and " + xsellId);
        }

        CrossSaleEntity cs = new CrossSaleEntity();
        cs.setProductId(productId);
        cs.setXsellId(xsellId);

        entityManager.persist(cs);
        entityManager.flush();
    }

    private boolean productNotFound(long productId) {
        try {
            Long count = entityManager.createQuery("SELECT COUNT(p) FROM ProductEntity p WHERE p.id = :id", Long.class)
                .setParameter("id", productId)
                .getSingleResult();
            return count == 0;
        } catch (Exception e) {
            log.error("Error checking if product exists: {}", productId, e);
            return true;
        }
    }

    private boolean crossSellRelationExists(long productId, long xsellId) {
        try {
            Long count = entityManager.createQuery(
                "SELECT COUNT(cs) FROM CrossSaleEntity cs WHERE cs.productId = :productId AND cs.xsellId = :xsellId",
                Long.class)
                .setParameter("productId", productId)
                .setParameter("xsellId", xsellId)
                .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            log.error("Error checking cross-sell relation: {} -> {}", productId, xsellId, e);
            return false;
        }
    }
}
