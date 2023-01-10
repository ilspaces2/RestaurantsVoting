drop table if exists restaurants;
drop table if exists dishes;
drop table if exists votes;
drop table if exists user_roles;
drop table if exists users;

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

create table votes(
    id serial primary key,
    date_time timestamp,
    user_id int references users(id)
);

create table restaurants(
    id serial primary key,
    name varchar not null,
    dish_id int references dishes(id),
    vote_id int references votes(id)
);
