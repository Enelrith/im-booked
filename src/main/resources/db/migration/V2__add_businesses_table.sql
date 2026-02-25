create table businesses(
    id binary(16) primary key default (uuid_to_bin(uuid())),
    name varchar(255) not null,
    email varchar(255) not null unique,
    phone varchar(20) null,
    description varchar(255) null,
    address varchar(255) null,
    city varchar(255) null,
    country varchar(255) null,
    is_active boolean not null default true,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    user_id binary(16) not null,
    constraint fk_businesses_users foreign key (user_id) references users (id) on delete cascade
);