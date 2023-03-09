package ru.clevertec.cashreceipt.repository.proxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.clevertec.cashreceipt.entity.DiscountCard;
import ru.clevertec.cashreceipt.repository.DiscountCardRepository;
import ru.clevertec.cashreceipt.repository.impl.DiscountCardRepositoryImpl;
import ru.clevertec.cashreceipt.util.testbuilder.impl.DiscountCardTestBuilder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

class ProxyDiscountCardRepositoryTest {

    private DiscountCardRepository discountCardRepository;
    private ProxyDiscountCardRepository proxyDiscountCardRepository;

    @BeforeEach
    void setUp() {
        discountCardRepository = Mockito.mock(DiscountCardRepositoryImpl.class);
        proxyDiscountCardRepository = new ProxyDiscountCardRepository(discountCardRepository);
    }


    @Test
    void checkShouldSaveDiscountCard() {
        DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();

        doReturn(discountCard)
                .when(discountCardRepository)
                .save(discountCard);

        DiscountCard actualDiscountCard = proxyDiscountCardRepository.save(discountCard);

        assertThat(actualDiscountCard).isEqualTo(discountCard);
    }

    @Test
    void checkShouldUpdateDiscountCard() {
        DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();

        doReturn(discountCard)
                .when(discountCardRepository)
                .update(discountCard);

        DiscountCard actualDiscountCard = proxyDiscountCardRepository.update(discountCard);

        assertThat(actualDiscountCard).isEqualTo(discountCard);
    }

    @Test
    void checkShouldDeleteDiscountCardById() {
        DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
        Long cardId = discountCard.getDiscountCardId();

        doNothing()
                .when(discountCardRepository)
                .deleteById(cardId);

        proxyDiscountCardRepository.deleteById(cardId);

        DiscountCard actualDiscountCard = proxyDiscountCardRepository.selectById(cardId);

        assertThat(actualDiscountCard).isNull();
    }

    @Test
    void checkShouldSelectDiscountCardById() {
        DiscountCard discountCard = DiscountCardTestBuilder.aDiscountCard().build();
        Long discountCardId = discountCard.getDiscountCardId();

        doReturn(discountCard)
                .when(discountCardRepository)
                .selectById(discountCardId);

        DiscountCard actualDiscountCard = proxyDiscountCardRepository.selectById(discountCardId);

        assertThat(actualDiscountCard).isNotNull();
    }
}