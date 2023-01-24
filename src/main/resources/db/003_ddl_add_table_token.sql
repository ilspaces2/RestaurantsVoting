drop table if exists tokens;
create table tokens(
    id serial primary key,
    email varchar not null,
    refresh_token varchar not null
);

