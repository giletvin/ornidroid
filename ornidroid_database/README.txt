Ornidroid_bdd:


Génération de la base de données sqlite
---------------------------------------
Script creation_bdd.sh
Input : fichier oiseaux_europe_avibase_ss_rares.csv
Output : génération du fichier ornidroid.sqlite et de fichiers .sql temporaires.


Audio
-----
dans ornidroid_sons
compression.sh audio audio_compression

Images
------
dans ornidroid_images
compression.sh images images_compression

Icônes
------
creation_icones.sh images_compression ../test/assets/bird_icons

Génération des fichiers contents.properties
-------------------------------------------

Fichiers décrivant le contenu des répertoires pour le téléchargement des médias (images ou sons)
generate_contents_properties.sh ../ornidroid_images/images_compression
generate_contents_properties.sh ../ornidroid_audio/images_audio

Finalement, déposer les répertoires audio et images générés sur http://ornidroid.free.fr/ornidroid
Zipper le répertoire images et audio et les mettre dans le répertoire packages
Poser le fichier ornidroid.sqlite dans http://ornidroid.free.fr/ornidroid