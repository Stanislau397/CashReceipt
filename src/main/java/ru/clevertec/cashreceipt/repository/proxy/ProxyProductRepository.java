package ru.clevertec.cashreceipt.repository.proxy;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.repository.impl.ProductRepositoryImpl;

import java.util.Optional;

@Repository
@AllArgsConstructor
public class ProxyProductRepository extends ProductRepositoryImpl {

    private final Cache<Product> productCache;

    @Override
    public Product save(Product product) {
        Product savedProduct = super.save(product);
        Long productId = savedProduct.getProductId();
        if (productCache.get(productId).isEmpty()) {
            productCache.put(productId, savedProduct);
        }
        return savedProduct;
    }

    @Override
    public Product update(Product product) {
        Product updatedProduct = super.update(product);
        Long productId = updatedProduct.getProductId();
        productCache.put(productId, updatedProduct);
        return updatedProduct;
    }

    @Override
    public void deleteById(Long productId) {
        super.deleteById(productId);
        productCache.remove(productId);
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
