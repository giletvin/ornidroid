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

-- release 10.0.0 version 19
insert into release_notes(id,version_code,date,comments_en,comments_de,comments_fr,read) VALUES(5,21,"23/01/2015","","","",0);
update release_notes set comments_fr=" * Version 11.0.0 (23/01/2015)

Evolutions : 
 - ajout de la base de données en Serbe
Corrections de bugs :
 - réécriture complète de l''application : gestion de la rotation de l''écran, meilleure gestion des téléchargements ...

 * Version 10.0.0 (15/10/2014)

Evolutions : 
 - améliorations du lecteur de sons (barre de progression, surlignage du fichier écouté)

+ quelques corrections de bugs.

 * Version 9.1.1 (04/10/2014)

Corrections de bugs:

 - crashs après le téléchargement de packages de photos, sons ou pages wikipedia

 * Version 9.1.0 (23/09/2014)

Evolutions : 
 - ajout de la base de données en turc

Corrections de bugs:

 - crashs aléatoires de l''appli au redémarrage

 * Version 9.0.0 (09/09/2014)

Evolutions : 
 - intégration des pages wikipedia
 - refonte de l''écran de détail des oiseaux
 - téléchargement des packages complets d''images, sons et pages wikipedia depuis l''application
 - améliorations de la base de données de sons : ajout de nombreux fichiers mp3
 - notification sur les nouveautés lors de mises à jour (cet écran !)

Corrections de bugs:

 - image zoomée déformée sur certains appareils
 - anomalies sur la recherche par nom
 - crashs aléatoires
" where version_code=21;

update release_notes set comments_en=" * Version 11.0.0 (01/23/2015)

New features : 
 - database in serb

Bug fixes :
 - Complete code refactoring : better handling of download processes, better handling of screen rotations, ... 

 * Version 10.0.0 (10/15/2014)

New features : 
 - enhancements on the mp3 player : progress bar, played item is now highlighted

+ bug fixes.


 * Version 9.1.1 (10/04/2014) 

Bug fixes :

 - crashes after downloading and installing zip packages

 * Version 9.1.0 (09/23/2014)

New features : 
 - database in turkish

Bug fixes :

 - random crashes when restarting app

 * Version 9.0.0 (09/09/2014)

New features : 
 - Wikipedia files embedded in the app
 - redesign of the main screen
 - download of full packages of images, sounds or wikipedia files from the app
 - More mp3 files from Xeno Canto available
 - Release notes screen

Bug fixes :

 - distorsion on zoomed images
 - bugs in the search by name
 - random crashes
" where version_code=21;

update release_notes set comments_de=" * Version 11.0.0 (01/23/2015)

New features : 
 - database in serb

Bug fixes :
 - Complete code refactoring : better handling of download processes, better handling of screen rotations, ... 

 * Version 10.0.0 (10/15/2014)

New features : 
 - enhancements on the mp3 player : progress bar, played item is now highlighted

+ bug fixes.


 * Version 9.1.1 (10/04/2014) 

Bug fixes :

 - crashes after downloading and installing zip packages

 * Version 9.1.0 (09/23/2014)

New features : 
 - database in turkish

Bug fixes :

 - random crashes when restarting app

 * Version 9.0.0 (09/09/2014)

New features : 
 - Wikipedia files embedded in the app
 - redesign of the main screen
 - download of full packages of images, sounds or wikipedia files from the app
 - More mp3 files from Xeno Canto available
 - Release notes screen

Bug fixes :

 - distorsion on zoomed images
 - bugs in the search by name
 - random crashes
" where version_code=21;

