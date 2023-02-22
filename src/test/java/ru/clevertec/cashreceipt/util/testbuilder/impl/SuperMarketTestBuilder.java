package ru.clevertec.cashreceipt.util.testbuilder.impl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;
import ru.clevertec.cashreceipt.entity.Supermarket;
import ru.clevertec.cashreceipt.util.testbuilder.TestBuilder;

@AllArgsConstructor
@NoArgsConstructor(staticName = "aSupermarket")
@With
@EqualsAndHashCode
@ToString
public class SuperMarketTestBuilder implements TestBuilder<Supermarket> {

    private String name = "SUPERMARKET 123";
    private String address = "12 MILKYWAY Galaxy/Earth";
    private String phoneNumber = "123-456-7890";

    @Override
    public Supermarket build() {
        return Supermarket.builder()
                .name(name)
                .address(address)
                .phoneNumber(phoneNumber)
                .build();
    }
}
