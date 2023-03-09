package ru.clevertec.cashreceipt.repository.handler;

import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.factory.CacheFactory;
import ru.clevertec.cashreceipt.repository.ProductRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;

public class ProductRepositoryHandler implements InvocationHandler {

    private static final String SELECT_BY_ID = "selectById";
    private static final String SAVE = "save";
    private static final String DELETE_BY_ID = "deleteById";
    private static final String UPDATE = "update";

    private final ProductRepository productRepository;
    private final Cache<Object, Object> productCache = new CacheFactory<>().createCache();

    public ProductRepositoryHandler(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object invoke = method.invoke(productRepository, args);
        if (SELECT_BY_ID.equals(method.getName())) {
            Optional<Object> cacheProduct = productCache.get(args[0]);
            if (cacheProduct.isPresent()) {
                invoke = cacheProduct.get();
            }
            productCache.put(args[0], invoke);
        }

        if (SAVE.equals(method.getName())) {
            if (productCache.get(args[0]).isEmpty()) {
                productCache.put(args[0], invoke);
            }
        }

        if (DELETE_BY_ID.equals(method.getName())) {
            if (productCache.get(args[0]).isPresent()) {
                productCache.remove(invoke);
            }
        }

        if (UPDATE.equals(method.getName())) {
            productCache.put(args[0], invoke);
        }
        return invoke;
    }
}
