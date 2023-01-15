insert into USERS (EMAIL, NAME, PASSWORD,VOTED)
values ('admin@admin.com', 'admin','{noop}admin', false );
insert into USERS (EMAIL, NAME, PASSWORD,VOTED)
values ('user1@user1.com', 'user1','{noop}user1', false );

insert into user_roles (role, user_id)
values ('ADMIN', 1),
       ('USER', 2);

insert into DISHES (NAME, PRICE) values ('beer', 10.5);
insert into DISHES (NAME, PRICE) values ('pizza', 15.25);

insert into RESTAURANTS (NAME, VOTES) values ('macdonalds', 1);
insert into RESTAURANTS (NAME, VOTES) values ('kfc', 0);

insert into RESTAURANT_DISHES (restaurant_id, dish_id) values (1,1);
