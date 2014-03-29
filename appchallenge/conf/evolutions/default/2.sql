# --- First database schema

# --- !Ups

insert into plan values(1, 'matematicas' );
insert into alumno values(1, '10216287', '' );


insert into asignatura values(1, 'ALGEBRA SUPERIOR I', 10 );
insert into asignatura values(2, 'CALCULO 1', 23 );
insert into asignatura values(3, 'COMPUTACION 1', 10 );
insert into asignatura values(4, 'ALGEBRA SUPERIOR 2', 10 );
insert into asignatura values(5, 'CALCULO 2', 23 );
insert into asignatura values(6, 'COMPUTACION 2', 10 );
insert into asignatura values(7, 'ALGEBRA LINEAL 1', 10 );
insert into asignatura values(8, 'ANALISIS NUMERICO 1', 10 );
insert into asignatura values(9, 'ECUACIONES DIFERENCIALES 1', 10 );
insert into asignatura values(10, 'ALGEBRA LINEAL 2', 10 );
insert into asignatura values(11, 'ANALISIS NUMERICO 2', 10 );
insert into asignatura values(12, 'ECUACIONES DIFERENCIALES 2', 10 );	
insert into asignatura values(13, 'CALCULO IIO', 25 ); 


insert into dependencia values(4, 1);

insert into dependencia values(5, 2);

insert into dependencia values(6, 1);
insert into dependencia values(6, 3);
insert into dependencia values(6, 4);

insert into dependencia values(7, 1);
insert into dependencia values(7, 4);

insert into dependencia values(8, 2);
insert into dependencia values(8, 3);
insert into dependencia values(8, 4);
insert into dependencia values(8, 5);
insert into dependencia values(8, 6);
insert into dependencia values(8, 7);

insert into dependencia values(9, 2);
insert into dependencia values(9, 4);
insert into dependencia values(9, 5);
insert into dependencia values(9, 7);

insert into dependencia values(10, 1);
insert into dependencia values(10, 4);
insert into dependencia values(10, 7);

insert into dependencia values(11, 2);
insert into dependencia values(11, 5);
insert into dependencia values(11, 6);
insert into dependencia values(11, 8);
insert into dependencia values(11, 9);
  
insert into dependencia values(12, 1);
insert into dependencia values(12, 2);
insert into dependencia values(12, 4);
insert into dependencia values(12, 5);
insert into dependencia values(12, 7);
insert into dependencia values(12, 9);
insert into dependencia values(12, 13);

# --- !Downs