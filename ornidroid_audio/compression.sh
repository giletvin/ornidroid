#!/bin/bash
#script de compression de sons mp3.
#ce script convertit à la volée l'encoding des fichiers de properties vers du ASCII avec la commande native2ascii
#@param 1 : repertoire source
#@param 2 : repertoire destination

EXPECTED_ARGS=2
E_BADARGS=65

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Script de compression de sons mp3"
  echo "Usage: `basename $0` {dir_src} {dir_dest}"
  exit $E_BADARGS
fi

export DIR_COMPRESSION=$2
export DIR_SRC=$1

rm -rf $DIR_COMPRESSION
mkdir $DIR_COMPRESSION

export MAX_BIT_RATE=64

for repertoire in `ls $DIR_SRC`
do
	echo $repertoire
	#copie du repertoire source
	cp -r $DIR_SRC/$repertoire $DIR_COMPRESSION
	for soundfile in `ls $DIR_COMPRESSION/$repertoire/*.mp3`
	do
		echo "### " "$soundfile"
		# compression du fichier si necessaire
		kbps=`file $soundfile | cut -d "," -f 5 | cut -d " " -f 2`
		#si le fichier a un encoding < 100, il faut faire un cut different pour recupering le bitrate
		if [ "$kbps" == "" ]; then
			kbps=`file $soundfile | cut -d "," -f 5 | cut -d " " -f 3`
		fi
		if [ "$kbps" -gt "$MAX_BIT_RATE" ]; then
			echo "compression!"
			avconv -i $soundfile -acodec libmp3lame -ab ${MAX_BIT_RATE}k ${soundfile}_new.mp3 2>&1 
			mv ${soundfile}_new.mp3 $soundfile
		else
			echo "pas de compression !" 
		fi
		#conversion en ascii du fichier de properties associé
		native2ascii ${soundfile}.properties ${soundfile}.properties
	done
done


