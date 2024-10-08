drop database if exists db_p2p;
create database db_p2p;
use db_p2p;

create table client(
uuid varchar(36) primary key not null,
name varchar(100) not null,
email varchar(300) not null,
password varchar(60) not null
);

create table client_friend(
uuid varchar(36) primary key not null,
client_uuid varchar(36) not null,
friend_uuid varchar(36) not null
);

create table map_configuration(
	uuid varchar(36) primary key not null,
	map_url varchar(300) not null,
    seed varchar(130) not null,
    version varchar(10) not null

);

create table server(
uuid varchar(36) primary key not null,
map_config varchar(36) not null,
name varchar(100) not null,
static_ip varchar(150) not null,
last_volatile_ip varchar(150),

foreign key (map_config) references map_configuration(uuid)

);

create table server_access(
	uuid varchar(36) primary key not null,
    server_uuid varchar(36) not null,
    client_uuid varchar(36) not null,
    role varchar(20) not null,

    foreign key (client_uuid) references client(uuid),
    foreign key (server_uuid) references server(uuid)
);


select * from client;
select * from server;

