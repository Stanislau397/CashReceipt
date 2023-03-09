package ru.clevertec.cashreceipt.repository.handler;

import ru.clevertec.cashreceipt.cache.Cache;
import ru.clevertec.cashreceipt.entity.DiscountCard;
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
        Object invoke = method.invoke(discountCardRepository, args);

        if (invoke != null) {

            if (!Integer.class.isAssignableFrom(invoke.getClass())) {

                DiscountCard discountCard = (DiscountCard) invoke;
                Long cardId = discountCard.getDiscountCardId();

                if (SELECT_BY_ID.equals(method.getName())) {
                    Optional<Object> discountCardInCache = discountCardCache.get(cardId);
                    if (discountCardInCache.isPresent()) {
                        invoke = discountCardInCache.get();
                    }
                    discountCardCache.put(cardId, invoke);
                }

                if (SAVE.equals(method.getName())) {
                    if (discountCardCache.get(cardId).isEmpty()) {
                        discountCardCache.put(cardId, invoke);
                    }
                }

                if (DELETE_BY_ID.equals(method.getName())) {
                    if (discountCardCache.get(cardId).isPresent()) {
                        discountCardCache.remove(cardId);
                    }
                }

                if (UPDATE.equals(method.getName())) {
                    discountCardCache.put(cardId, invoke);
                }
            }
        }
        return invoke;
    }
}
