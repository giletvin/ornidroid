Gestion des images
================


1ere étape : compression et génération des fichiers properties
----------------------------------------------------------------------------
./compression.sh

Ce script retaille et recompresse les images sources
Copie les fichiers properties quand il y en a.


2eme étape : génération des icônes
---------------------------------------------------------

Parcours des arbos pour trouver, répertoire par répertoire l'image icone de la fleur


3eme étape : generation des fichiers contents.properties
---------------------------------------------------------
Avant de faire le zip, et l'envoi en ligne, générer un ficheir contents.properties par répertoire de fleur


4eme étape: zip et zipsplit
------------------------------------------------------------------
zip -r images.zip images/
zipper le répertoire des images
puis splitter en fichiers de 28 Mo--> production de 2 fichiers.
zipsplit -n 28000000 images.zip 
