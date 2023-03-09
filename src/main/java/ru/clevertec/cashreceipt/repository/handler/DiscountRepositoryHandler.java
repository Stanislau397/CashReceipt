package ru.clevertec.cashreceipt.repository.handler;

import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.factory.CacheFactory;
import ru.clevertec.cashreceipt.repository.DiscountCardRepository;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Optional;


public class DiscountRepositoryHandler implements InvocationHandler {

    private static final String SELECT_BY_ID = "selectById";
    private static final String SAVE = "save";
    private static final String DELETE_BY_ID = "deleteById";
    private static final String UPDATE = "update";

    private final DiscountCardRepository discountCardRepository;
    private final Cache<Object, Object> discountCardCache = new CacheFactory<>().createCache();

    public DiscountRepositoryHandler(DiscountCardRepository discountCardRepository) {
        this.discountCardRepository = discountCardRepository;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object discountCard = method.invoke(discountCardRepository, args);
        if (SELECT_BY_ID.equals(method.getName())) {
            Optional<Object> discountCardInCache = discountCardCache.get(args[0]);
            if (discountCardInCache.isPresent()) {
                discountCard = discountCardInCache.get();
            }
            discountCardCache.put(args[0], discountCard);
        }

        if (SAVE.equals(method.getName())) {
            if (discountCardCache.get(args[0]).isEmpty()) {
                discountCardCache.put(args[0], discountCard);
            }
        }

        if (DELETE_BY_ID.equals(method.getName())) {
            if (discountCardCache.get(args[0]).isPresent()) {
                discountCardCache.remove(args[0]);
            }
        }

        if (UPDATE.equals(method.getName())) {
            discountCardCache.put(args[0], discountCard);
        }
        return discountCard;
    }
}
