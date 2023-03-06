package ru.clevertec.cashreceipt.repository.proxy;

import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.factory.CustomCashFactory;
import ru.clevertec.cashreceipt.repository.impl.ProductRepositoryImpl;

import java.util.Optional;

@Repository
public class ProxyProductRepository extends ProductRepositoryImpl {

    private Cache<Product> productCache = new CustomCashFactory<Product>().create();

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
    public Product delete(Product product) {
        Product deletedProduct = super.delete(product);
        Long productId = deletedProduct.getProductId();
        productCache.remove(productId);
        return deletedProduct;
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
