insert into USERS (EMAIL, NAME, PASSWORD,VOTED)
values ('admin@admin.com', 'admin','admin', false );
insert into USERS (EMAIL, NAME, PASSWORD,VOTED)
values ('user@user.com', 'user','user', false );

insert into user_roles (role, user_id)
values ('ADMIN', 1),
       ('USER', 2);

insert into DISHES (NAME, PRICE) values ('beer', 10.5);
insert into DISHES (NAME, PRICE) values ('pizza', 15.25);

insert into RESTAURANTS (NAME, DISH_ID, VOTES) values ('macdonalds', 1, 1);
