package ru.clevertec.cashreceipt.repository.proxy;

import jakarta.persistence.EntityManager;
import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.factory.CacheFactory;
import ru.clevertec.cashreceipt.repository.impl.ProductRepositoryImpl;

import java.util.Optional;

public class ProxyProductRepository extends ProductRepositoryImpl {

    private final Cache<Product> productCache;

    public ProxyProductRepository(EntityManager entityManager) {
        super(entityManager);
        productCache = new CacheFactory<Product>().create();
    }

    @Override
    public Product save(Product product) {
        Product savedProduct = super.save(product);
        return super.save(product);
    }

    @Override
    public Product update(Product product) {
        return super.update(product);
    }

    @Override
    public Product delete(Product product) {
        return super.delete(product);
    }

    @Override
    public Optional<Product> selectById(Long productId) {
        Optional<Product> productFromCache = productCache.get(productId);
        if (productFromCache.isPresent()) {
            return productFromCache;
        }
        Optional<Product> productFromRepository = super.selectById(productId);
        if (productFromRepository.isPresent()) {
            Product product = productFromRepository.get();
            productCache.put(productId, product);
        }
        return productFromRepository;
    }
}
