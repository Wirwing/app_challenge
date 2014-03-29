# --- First database schema

# --- !Ups

create table alumno(
	id integer not null AUTO_INCREMENT,
	name text not null,
	password text not null,

	primary key( id ) 

);

create table plan(
	id integer not null AUTO_INCREMENT,
	name text not null,
	

	primary key( id ) 

);


create table asignatura(
	id integer not null AUTO_INCREMENT,
	name text not null,
	credits integer not null,
	primary key( id ) 

);



# --- !Downs