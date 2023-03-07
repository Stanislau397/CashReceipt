package ru.clevertec.cashreceipt.entity;

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

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "discount_percentage")
    private Integer discountPercent;
}
