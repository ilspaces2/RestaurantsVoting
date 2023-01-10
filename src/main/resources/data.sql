insert into USERS (EMAIL, NAME, PASSWORD)
values ('admin@admin.com', 'admin','admin' );
insert into USERS (EMAIL, NAME, PASSWORD)
values ('user@user.com', 'user','user' );

insert into user_roles (role, user_id)
values ('ADMIN', 1),
       ('USER', 2);

insert into DISHES (NAME, PRICE) values ('beer', 10.5);
insert into DISHES (NAME, PRICE) values ('pizza', 15.25);

insert into VOTES (date_time, user_id) values ('2020-01-30 10:00:00', 1);

insert into RESTAURANTS (NAME, VOTE_ID, DISH_ID) values ('macdonalds', 1, 1);
