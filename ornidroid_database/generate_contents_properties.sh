#!/bin/bash
#set -x
#script de génération de fichier contents.properties à partir d'un repertoire de media
#ex de contenu du fichier de contents.
#files=barge_a_queue_noire_1.jpg,barge_a_queue_noire_2.jpg

#@param 1 : chemin du repertoire à analyser et dans lequel les fichiers contents vont être générés. C'est le repertoire racine du media "audio" ou "images"
#@param 2 : extension à filtrer : jpg ou mp3

EXPECTED_ARGS=2
E_BADARGS=65

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Script de préparation des repertoires avant l'envoi en ligne : ajout des fichiers contents et nettoyage des meta svn ou git"
  echo "Usage: `basename $0` {dir} {extension (mp3 ou jpg)}"
  exit $E_BADARGS
fi

export DIR_SRC=$1
export EXTENSION_TO_FIND=$2
find $DIR_SRC -name ".svn" -exec rm -rf {} \; 2> /dev/null
find $DIR_SRC -name ".git" -exec rm -rf {} \; 2> /dev/null

#boucle sur tous les repertoires d'oiseaux
for repertoire in `ls $DIR_SRC`
do
	echo $repertoire
	content="files="
	#boucle sur les fichiers du repertoire. Collecte des fichiers et ecriture du fichier contents.properties
	for file in `ls $DIR_SRC/$repertoire/*.$EXTENSION_TO_FIND`
	do
		fichier=`basename $file`
		content=$content$fichier,
	done
	content=`echo $content | sed 's/,$//g'`
	echo $content > $DIR_SRC/$repertoire/contents.properties
done


