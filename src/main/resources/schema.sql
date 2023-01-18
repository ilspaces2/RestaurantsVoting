drop table if exists RESTAURANT_DISHES;
drop table if exists restaurants;
drop table if exists dishes;
drop table if exists votes;
drop table if exists user_roles;
drop table if exists users;

create table users(
    id serial primary key,
    email varchar unique not null,
    name varchar not null,
    password varchar not null,
    voted boolean
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

create table restaurants(
    id serial primary key,
    name varchar unique not null,
    votes int default null
);

create table RESTAURANT_DISHES(
    restaurant_id int references restaurants(id),
    dish_id int references dishes(id)
);
