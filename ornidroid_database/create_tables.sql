-- creation de la base ornidroid
-- creation de la table taxonomy en jointure avec la table bird


create table bird(
	id integer,
	scientific_name,
	scientific_name2,
	directory_name,
	scientific_order_fk,
	scientific_family_fk,
	category_fk,
	habitat1_fk,
	habitat2_fk,
	beak_form_fk,
	size_value,
	size_fk,
	feather_colour_fk,
	feather_colour_2_fk,
	beak_colour_fk,
	beak_colour_2_fk,
	paw_colour_fk,
	paw_colour_2_fk,
	remarkable_sign_fk,
	PRIMARY KEY(id)
);

CREATE VIRTUAL TABLE taxonomy USING fts3(lang, taxon, searched_taxon, bird_fk);

CREATE TABLE bird_description (lang, description, distribution, bird_fk);

create table scientific_order(
		id,
		name,
		lang
);

create table scientific_family(
		id,
		name,
		lang
);


create table category(
	id integer,
	name,
	lang
);

create table habitat(
	id integer,
	name,
	lang
);
create table size_table(
	id integer,
	name,
	lang
);

create table beak_form(
		id integer,
		name,
		lang
);

create table colour(
		id integer,
		name,
		lang
);

create table remarkable_sign(
		id integer,
		name,
		lang
);


create VIRTUAL TABLE bird_country USING fts3(
		bird_fk,
		country_code,
		PRIMARY KEY(bird_fk,country_code)
);

create table country(
		id integer,
		code,
		name_fr,
		name_en,
		name_de,
		PRIMARY KEY(id)
);
		


create table application_info(id integer,key,value,date,comments);
insert into application_info(id,key,value,date,comments) VALUES(1,"ornidroid_version","2.0.1","18/01/2013","");
insert into application_info(id,key,value,date,comments) VALUES(2,"ornidroid_version","3.0.0","28/04/2013","correction habitat du tétras lyre");
insert into application_info(id,key,value,date,comments) VALUES(3,"ornidroid_version","3.0.1","01/05/2013","orange et rouge fusionnés en rouge/orange");
insert into application_info(id,key,value,date,comments) VALUES(4,"ornidroid_version","4.0.0","XX/XX/2013","ajout de la recherche par pays");

--create table temp(id integer,key,value,date);
