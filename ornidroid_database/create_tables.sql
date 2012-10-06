-- creation de la base ornidroid
-- creation de la table taxonomy en jointure avec la table bird


create table bird(
	id integer,
	scientific_name,
	directory_name,
	scientific_order_fk,
	scientific_family_fk,
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
