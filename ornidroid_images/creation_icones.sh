#!/bin/bash
#script de creation d'icones pour les oiseaux
#ce script analyse un repertoire contenant les photos d'oiseaux, classées dans des sous repertoires, prend la première image jpg du repertoire, la retaille pour en faire une vignette de 100 par 100
#et pose l'image generée dans un repertoire de sortie. L'icone porte le même nom que le repertoire
#ex : input/merle_noir/merle.jpg --> output/merle_noir.jpg
#@param 1 : repertoire source
#@param 2 : repertoire destination : si ce répertoire existe, il est effacé pour être recréé en début de script

#compression batch gimp
#@param 1 : path de l'image
#@param 2 : qualite (0.5 par exemple)
function compression_gimp {
	gimp -i -b "(simple-compress \""$1"\" $2)" -b '(gimp-quit 0)'
}

EXPECTED_ARGS=2
E_BADARGS=65

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "Script de création des icones d'oiseaux"
  echo "Usage: `basename $0` {dir_src} {dir_dest}"
  exit $E_BADARGS
fi


export DIR_SRC=$1
export DIR_OUTPUT=$2


rm -rf $DIR_OUTPUT
mkdir $DIR_OUTPUT

export QUALITY=50
export QUALITY_GIMP=0.5

export MAX_RESOLUTION=100


for repertoire in `ls $DIR_SRC`
do
	echo $repertoire

	for picturefile in `ls $DIR_SRC/$repertoire/*.jpg`
	do
		echo "### " "$picturefile"
		#copie de la photo dans le repertoire DESTINATION avec le nom du repertoire
		iconpath=$DIR_OUTPUT/$repertoire.jpg
		cp $picturefile $iconpath

		hauteur=`exiv2 $iconpath 2>/dev/null | grep "Taille de l'image" | cut -d "x" -f 2 | tr -d " "`
		largeur=`exiv2 $iconpath 2>/dev/null | grep "Taille de l'image" | cut -d "x" -f 1 | cut -d ":" -f 2 | tr -d " "`
		if [[ "$hauteur" -gt "$largeur"  ]]; then
			echo "format portrait"
			convert -resize 100x1000  $iconpath $iconpath
		else
			echo "format paysage"
			convert -resize 1000x100  $iconpath $iconpath
		fi

		echo "resize et crop de l'image $iconpath"
		convert -gravity Center  -crop ${MAX_RESOLUTION}x${MAX_RESOLUTION}+0+0 +repage  $iconpath $iconpath
		#compression avec Gimp
		compression_gimp $iconpath $QUALITY_GIMP

		break
	done

done

