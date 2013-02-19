# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table url (
  id                        bigint not null,
  url                       varchar(255),
  constraint pk_url primary key (id))
;

create sequence url_seq;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists url;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists url_seq;

