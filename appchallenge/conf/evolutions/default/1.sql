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



create table oferta(
	idAsignatura integer not null,
	idProfesor integer not null,
	periodo date not null,
	constraint pk_oferta primary key ( idAsignatura, idProfesor, periodo), 
	constraint fk_oferta_1 foreign key (idAsignatura) references asignatura(id) on delete cascade
	
);

create table planasignatura(
	idAsignatura integer not null,
	idPlan integer not null,

	constraint pk_planasignatura primary key ( idAsignatura, idPlan), 
	constraint fk_planasignatura_1 foreign key (idAsignatura) references asignatura(id) on delete cascade,
	constraint fk_planasignatura_2 foreign key (idPlan) references asignatura(id) on delete cascade
	
);




# --- !Downs