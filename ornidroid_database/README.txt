AndroidAnnotations
https://github.com/excilys/androidannotations/wiki/Download

Steps

Put androidannotations-X.X.X-api.jar in the libs folder
Put androidannotations-X.X.X.jar in a different folder, such as compile-libs. androidannotations-X.X.X.jar must not go in the libs folder.
Right-click on your project, choose "Properties"
Go to Java Compiler and make sure that Compiler compliance level is set to 1.6, otherwise the processor won't be activated
Go to Java Compiler > Annotation Processing and choose Enable annotation processing
Go to Java Compiler > Annotation Processing > Factory Path and add the processor JAR : androidannotations-X.X.X.jar.
Confirm the workspace rebuild
Go to Java Build Path > Libraries and add the API JAR : androidannotations-X.X.X-api.jar, unless it's already in the build path (ADT 17 automatically adds JARs that are in the libs folder).
You can start using AndroidAnnotations
Troubleshooting

To check that AndroidAnnotations is activated, you should see AndroidAnnotations logs in the Eclipse Error Log (Window > Show View > Error Log) as soon as you save a file. If you do not see any log here, then it means AndroidAnnotations isn't activated yet.

If AndroidAnnotations isn't activated, this may be a Java version problem. You should check that eclipse.ini doesn't contain -Dosgi.requiredJavaVersion=1.5, and otherwise change it to -Dosgi.requiredJavaVersion=1.6.

When AndroidAnnotations is activated, You should now see any annotation problem as a regular error marker within the editor and in the Problem view.

Your project name should be identical to the name of the folder containing the project. When importing a project in Eclipse, the project name used is the name of the folder. Since the annotation jar is referenced by projectName/lib/androidannotations-X.X.X.jar, you may otherwise encounter errors when coworkers import your project.

If you get a java.lang.IllegalArgumentException: already added: Lorg/androidannotations/annotations/AfterInject; when compiling, please check that you did not put the processor jar androidannotations-X.X.X.jar in the libs folder.

If you get a java.lang.NoClassDefFoundError at runtime, please verify in project properties > Java Build Path > Order and Export that androidannotations-xxx-api.jar is checked.

Ornidroid_bdd:


Génération de la base de données sqlite
---------------------------------------
Script creation_bdd.sh
Prerequis : sqlite3, php, php-sqlite3, php-dom
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