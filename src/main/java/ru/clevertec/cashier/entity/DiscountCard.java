package ru.clevertec.cashier.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "discount_cards")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class DiscountCard {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "discount_card_id")
    private Long discountCardId;

    @Column(name = "discount_percentage")
    private Integer discountPercent;
}
