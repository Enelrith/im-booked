create table users(
    id binary(16) primary key default (uuid_to_bin(uuid())),
    email varchar(255) not null unique,
    password varchar(255) not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp
);

create table roles(
    id bigint auto_increment primary key,
    name varchar(20) not null unique
);

insert into roles (name) values ('USER'), ('ADMIN'), ('BUSINESS_OWNER');

create table user_roles(
    user_id binary(16) not null,
    role_id bigint not null,
    constraint pk_user_roles primary key (user_id, role_id),
    constraint fk_user_roles_users foreign key (user_id) references users (id) on delete cascade,
    constraint fk_user_roles_roles foreign key (role_id) references roles (id) on delete cascade
);