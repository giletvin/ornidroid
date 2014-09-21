Ornidroid_bdd:


Génération de la base de données sqlite
---------------------------------------
Script creation_bdd.sh
Input : fichier oiseaux_europe_avibase_ss_rares.csv
Output : génération du fichier SQLITE "ornidroid.jpg" (extension JPG pour la gestion des assets dans le sdk) et ornidroid.jpg.size (et de fichiers .sql temporaires qu'on peut effacer).
Suite à la génération, poser le fichier ornidroid.jpg  dans le répertoire assets du projet Android

Audio
-----
dans ornidroid_sons
compression.sh src_audio audio
Attention ! pour des mises à jour et éviter de tout regénérer, copier les répertoires à compresser dans audio_test et lancer compression.sh audio_test /tmp/audio
Puis generate_contents_properties.sh sur le répertoire audio

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
generate_contents_properties.sh ../ornidroid_images/images jpg
generate_contents_properties.sh ../ornidroid_audio/audio mp3

Finalement, déposer les répertoires audio et images générés sur http://ornidroid.free.fr/ornidroid

Création des packages
---------------------
dans ornidroid_database 
create_packages.sh  ../ornidroid_images/ images
create_packages.sh  ../ornidroid_audio/ audio

Placer ornidroid.sqlite dans le répertoire assets de l'application pour Android
Placer les zip générés sur http://ornidroid.free.fr/ornidroid/packages/


Mode opératoire pour la publication sur Google Play
---------------------------------------------------

Génération de la clé - à faire une seule fois
----------------------
keytool -genkey -v -keystore ornidroid_key.keystore -alias ornidroid_key -keyalg RSA -keysize 2048 -validity 10000



Une fois la version commitée, testée...
Ajouter     android:debuggable="false" dans le tag application

Changer le n° de version d'Ornidroid dans le fichier pom.xml
Verifier le version code et version name dans le manifest.xml
Vérifier que la dernière version de la db est dans le répertoire assets.

Clic droit sur le pj dans Eclipse. Android/Export signed application Package et laisser le wizard créer le fichier apk prêt à livrer sur GPlay.


Tag git
-------

git tag -a v2.0.0 -m 'v2.0.0'
git push origin v2.0.0

Préparer la version à venir
----------------------------
Modifier pom.xml et manifest pour les n° de version