create table users(
    id serial primary key,
    email varchar not null,
    name varchar not null,
    password varchar not null,
    registration timestamp
);

create table user_roles
(
    user_id int not null,
    role varchar not null ,
    constraint user_roles_idx unique (user_id, role),
    foreign key (user_id) references users (id) on delete cascade
);

create table dishes(
    id serial primary key,
    name varchar not null,
    price float not null
);

create table restaurant(
    id serial primary key,
    name varchar not null,
    vote int,
    dishes_id int references dishes(id)
);