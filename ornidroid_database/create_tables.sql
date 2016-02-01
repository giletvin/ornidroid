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

-- release 11.2.0 version 29
insert into release_notes(id,version_code,date,comments_en,comments_de,comments_fr,read) VALUES(5,29,"31/01/2016","","","",0);

update release_notes set comments_fr=" * Version 11.2.0 (31/01/2016)
Evolutions : 
 - ajout des traductions en catalan, basque et galicien. Merci à Maria pour son aide !

 * Version 11.1.0 (20/07/2015)
Evolutions :
 - amélioration de l''écran de zoom de photo
 - qques corrections dans la base de données
 - amélioration de la recherche multicritères

 * Version 11.0.0 (23/01/2015)

Evolutions : 
 - ajout de la base de données en Serbe et Slovène
Corrections de bugs :
 - réécriture complète de l''application : gestion de la rotation de l''écran, meilleure gestion des téléchargements ...

 * Version 10.0.0 (15/10/2014)

Evolutions : 
 - améliorations du lecteur de sons (barre de progression, surlignage du fichier écouté)

 * Version 9.1.0 (23/09/2014)

Evolutions : 
 - ajout de la base de données en turc

 * Version 9.0.0 (09/09/2014)

Evolutions : 
 - intégration des pages wikipedia
 - refonte de l''écran de détail des oiseaux
 - téléchargement des packages complets d''images, sons et pages wikipedia depuis l''application
 - améliorations de la base de données de sons : ajout de nombreux fichiers mp3
" where version_code=29;

update release_notes set comments_en=" * Version 11.2.0 (01/31/2016)
New features:
 - database in catalan, basque and galician, thanks to Maria !

 * Version 11.1.0 (07/20/2015)
New features:
 - enhancements on the zoomed images screen
 - few fixes in the database
 - enhancements in the multi criteria search

 * Version 11.0.0 (01/23/2015)

New features : 
 - database in serbian and slovenian

Bug fixes :
 - Complete code refactoring : better handling of download processes, better handling of screen rotations, ... 

 * Version 10.0.0 (10/15/2014)

New features : 
 - enhancements on the mp3 player : progress bar, played item is now highlighted

 * Version 9.1.0 (09/23/2014)

New features : 
 - database in turkish

 * Version 9.0.0 (09/09/2014)

New features : 
 - Wikipedia files embedded in the app
 - redesign of the main screen
 - download of full packages of images, sounds or wikipedia files from the app
 - More mp3 files from Xeno Canto available
 - Release notes screen

" where version_code=29;

update release_notes set comments_de=" * Version 11.2.0 (01/31/2016)
New features:
 - database in catalan, basque and galician, thanks to Maria !

 * Version 11.1.0 (07/20/2015)
New features:
 - enhancements on the zoomed images screen
 - few fixes in the database
 - enhancements in the multi criteria search

 * Version 11.0.0 (01/23/2015)

New features : 
 - database in serbian and slovenian

Bug fixes :
 - Complete code refactoring : better handling of download processes, better handling of screen rotations, ... 

 * Version 10.0.0 (10/15/2014)

New features : 
 - enhancements on the mp3 player : progress bar, played item is now highlighted

 * Version 9.1.0 (09/23/2014)

New features : 
 - database in turkish

 * Version 9.0.0 (09/09/2014)

New features : 
 - Wikipedia files embedded in the app
 - redesign of the main screen
 - download of full packages of images, sounds or wikipedia files from the app
 - More mp3 files from Xeno Canto available
 - Release notes screen

" where version_code=27;
