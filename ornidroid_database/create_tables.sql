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
	oiseaux_net_link,
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
insert into application_info(id,key,value,date,comments) VALUES(4,"ornidroid_version","4.0.0","28/05/2013","ajout de la recherche par pays");
insert into application_info(id,key,value,date,comments) VALUES(5,"ornidroid_version","5.0.0","25/08/2013","trad grec + liens oiseaux.net");
insert into application_info(id,key,value,date,comments) VALUES(6,"ornidroid_version","5.0.1","14/09/2013","correction issue 79");
insert into application_info(id,key,value,date,comments) VALUES(7,"ornidroid_version","6.0.0","29/11/2013","roumain, bulgare, hongrois");



create table release_notes(id integer,version_code integer,date,comments_de,comments_en,comments_fr,read integer);

insert into release_notes(id,version_code,date,comments_en,comments_de,comments_fr,read) VALUES(1,16,"09/09/2014","","","",0);
update release_notes set comments_fr="Evolutions : 
 - intégration des pages wikipedia
 - refonte de l''écran de détail des oiseaux
 - téléchargement des packages complets d''images, sons et pages wikipedia depuis l''application
 - améliorations de la base de données de sons : ajout de nombreux fichiers mp3
 - notification sur les nouveautés lors de mises à jour (cet écran !)

Corrections de bugs:

 - image zoomée déformée sur certains appareils
 - anomalies sur la recherche par nom
 - crashs aléatoires" where version_code=16;

update release_notes set comments_en="New features : 
 - Wikipedia files embedded in the app
 - redesign of the main screen
 - download of full packages of images, sounds or wikipedia files from the app
 - More mp3 files from Xeno Canto available
 - Release notes screen

Bug fixes :

 - distorsion on zoomed images
 - bugs in the search by name
 - random crashes" where version_code=16;

update release_notes set comments_de="New features : 
 - Wikipedia files embedded in the app
 - redesign of the main screen
 - download of full packages of images, sounds or wikipedia files from the app
 - More mp3 files from Xeno Canto available
 - Release notes screen

Bug fixes :

 - distorsion on zoomed images
 - bugs in the search by name
 - random crashes" where version_code=16;
