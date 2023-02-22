package ru.clevertec.cashreceipt.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Supermarket {

    private String name;
    private String address;
    private String phoneNumber;
}
