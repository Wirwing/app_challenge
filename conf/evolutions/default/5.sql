# --- First database schema

# --- !Ups



insert into kardex values( 2, 1, '2012-08-01', 1, 1 );
insert into kardex values( 2, 2, '2012-08-01', 0, 1 );
insert into kardex values( 2, 3, '2012-08-01', 1, 1 );

insert into kardex values( 2, 4, '2013-01-01', 1, 1 );
insert into kardex values( 2, 2, '2013-01-01', 1, 0 );
insert into kardex values( 2, 5, '2013-01-01', 1, 1 );
insert into kardex values( 2, 6, '2013-01-01', 1, 1 );

insert into kardex values( 2, 7, '2013-08-01', 1, 1 );
insert into kardex values( 2, 8, '2013-08-01', 1, 1 );
insert into kardex values( 2, 9, '2013-08-01', 1, 1 );
insert into kardex values( 2, 1, '2013-08-01', 0, 0 );

# --- !Downs