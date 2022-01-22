create table role (
    id      serial primary key,
    name    text not null unique,
    created timestamp default now(),
    updated timestamp default now()
);

create table person (
    id       serial primary key,
    username text not null unique,
    role_id  int  not null references role (id),
    created  timestamp default now(),
    updated  timestamp default now()
);

create table room (
    id      serial primary key,
    name    text not null unique,
    created timestamp default now(),
    updated timestamp default now()
);

create table message (
    id        serial primary key,
    body      text not null,
    person_id int  not null references person (id),
    room_id   int  not null references room (id),
    created   timestamp default now(),
    updated   timestamp default now()
);