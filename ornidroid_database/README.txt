Ornidroid_bdd:


Génération de la base de données sqlite
---------------------------------------
Script creation_bdd.sh
Input : fichier oiseaux_europe_avibase_ss_rares.csv
Output : génération du fichier ornidroid.sqlite et de fichiers .sql temporaires.


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
Zipper ornidroid.sqlite et ornidroid.sqlite.properties dans ornidroid_database.zip
Placer les zip obtenus dans le répertoire packages sur ornidroid.free.fr
Poser le fichier ornidroid.sqlite et son fichier de properties dans http://ornidroid.free.fr/ornidroid
