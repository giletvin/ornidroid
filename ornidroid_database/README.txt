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
Changer le n° de version d'Ornidroid dans le fichier pom.xml
Verifier le version code et version name dans le manifest.xml
Vérifier que la dernière version de la db est dans le répertoire assets.
Compiler : mvn clean install

Bidouille pour le packaging
----------------------------
pb : dézipper, vider le répertoire meta inf et lancer la commande.
http://stackoverflow.com/questions/5089042/jarsigner-unable-to-sign-jar-java-util-zip-zipexception-invalid-entry-compres
zipalign -v 4 ornidroid.apk ornidroid-1.0.0.apk

Signature de l'apk avec la clé
-------------------------------
jarsigner -verbose -sigalg MD5withRSA -digestalg SHA1 -keystore [path_to_key] ornidroid-2.0.0.apk ornidroid_key

Refaire un coup la commande zipalign

zipalign -v 4 ornidroid-1.0.1.apk ornidroid-1.0.1-FINAL.apk


Tag git
-------

git tag -a v2.0.0 -m 'v2.0.0'
git push origin v2.0.0

Préparer la version à venir
----------------------------
Modifier pom.xml et manifest pour les n° de version