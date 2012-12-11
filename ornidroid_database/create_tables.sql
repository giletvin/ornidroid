-- creation de la base ornidroid
-- creation de la table taxonomy en jointure avec la table bird


create table bird(
	id integer,
	scientific_name,
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