create table discount_cards
(
    discount_card_id    integer not null
        constraint discount_card_pkey
            primary key,
    discount_percentage integer not null
);

create table products
(
    product_id  integer               not null
        primary key,
    name        varchar               not null,
    price       numeric               not null,
    promotional boolean default false not null
);