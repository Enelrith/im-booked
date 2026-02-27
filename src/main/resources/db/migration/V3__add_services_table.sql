create table services(
    id binary(16) primary key default (uuid_to_bin(uuid())),
    name varchar(100) not null,
    price decimal(10, 2) not null,
    duration_minutes integer not null,
    is_active boolean not null default true,
    business_id binary(16) not null,
    constraint ck_services_price_positive_or_zero check (price >= 0),
    constraint ck_services_duration_minutes_positive check (duration_minutes > 0),
    constraint fk_services_businesses foreign key (business_id) references businesses (id) on delete cascade
);