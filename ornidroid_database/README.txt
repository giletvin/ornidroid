Ornidroid_bdd:


Génération de la base de données sqlite
---------------------------------------
Script creation_bdd.sh
Input : fichier oiseaux_europe_avibase_ss_rares.csv
Output : génération du fichier SQLITE "ornidroid.jpg" (extension JPG pour la gestion des assets dans le sdk) et ornidroid.jpg.size (et de fichiers .sql temporaires qu'on peut effacer).
Suite à la génération, poser les fichiers ornidroid.jpg et ornidroid.jpg.size dans le répertoire assets du projet Android

Audio
-----
dans ornidroid_sons
compression.sh src_audio audio

Images
------
dans ornidroid_images
compression.sh src_images images

Icônes
------
creation_icones.sh images ../test/assets/bird_icons

Génération des fichiers contents.properties
-------------------------------------------

Fichiers décrivant le contenu des répertoires pour le téléchargement des médias (images ou sons)
generate_contents_properties.sh ../ornidroid_images/images
generate_contents_properties.sh ../ornidroid_audio/audio

Finalement, déposer les répertoires audio et images générés sur http://ornidroid.free.fr/ornidroid

Création des packages
---------------------
dans ornidroid_database 
create_packages.sh  ../ornidroid_images/ images
create_packages.sh  ../ornidroid_audio/ audio

Placer ornidroid.sqlite dans le répertoire assets de l'application pour Android
Placer les zip générés sur http://ornidroid.free.fr/ornidroid/packages/
