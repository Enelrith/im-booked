create table appointments(
    id binary(16) primary key default (uuid_to_bin(uuid())),
    client_name varchar(255) not null,
    status varchar(20) not null,
    appointment_start timestamp not null,
    appointment_end timestamp not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp on update current_timestamp,
    service_id binary(16) null,
    business_id binary(16) not null,
    constraint ck_appointments_appointment_end_after_start check (appointment_end > appointment_start),
    constraint fk_appointments_services foreign key (service_id) references services (id) on delete set null,
    constraint fk_appointments_businesses foreign key (business_id) references businesses (id) on delete cascade
);