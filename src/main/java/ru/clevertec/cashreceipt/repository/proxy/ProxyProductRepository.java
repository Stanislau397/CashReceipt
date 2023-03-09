package ru.clevertec.cashreceipt.repository.proxy;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import ru.clevertec.cashreceipt.entity.Product;
import ru.clevertec.cashreceipt.repository.ProductRepository;

@Repository
public class ProxyProductRepository implements ProductRepository {

    private final ProductRepository productRepository;

    public ProxyProductRepository(@Qualifier("createProductRepository") ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        return productRepository.update(product);
    }

    @Override
    public void deleteById(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public Product selectByName(String name) {
        return productRepository.selectByName(name);
    }

    @Override
    public Product selectById(Long productId) {
        return productRepository.selectById(productId);
    }

    @Override
    public Product selectProduct(Product product) {
        return productRepository.selectProduct(product);
    }
}
