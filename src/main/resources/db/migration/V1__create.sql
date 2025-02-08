create table account
(
    id serial primary key,
    name varchar(50) not null,
    login varchar(50) not null unique,
    password varchar(255) not null,
    date_register timestamp default CURRENT_TIMESTAMP
);

alter table account
    owner to postgres;

create table category
(
    id serial primary key,
    name varchar(50) not null unique,
    description text
);

alter table category
    owner to postgres;

create table author
(
    id serial primary key,
    name varchar(100) not null unique
);

alter table author
    owner to postgres;

create table quote
(
    id serial primary key,
    text text not null,
    author_id integer references author on delete cascade,
    date_added timestamp default CURRENT_TIMESTAMP,
    rating integer default 0,
    category_id integer references category on delete set null,
    adder_id integer references account on delete set null
);

alter table quote
    owner to postgres;

create table liked_quote
(
    id serial primary key,
    account_id integer references account on delete cascade,
    quote_id integer references quote on delete cascade,
    date_added timestamp default CURRENT_TIMESTAMP,
    unique (account_id, quote_id)
);

alter table liked_quote
    owner to postgres;


CREATE TABLE quiz (
    id SERIAL PRIMARY KEY,
    question_text TEXT NOT NULL,
    option_a TEXT NOT NULL,
    option_b TEXT NOT NULL,
    option_c TEXT NOT NULL,
    option_d TEXT NOT NULL,
    correct_option text NOT NULL
);