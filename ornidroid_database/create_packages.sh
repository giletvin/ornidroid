#!/bin/sh
# script de creation de packages zippés des médias
#@param 1 : chemin du père du repertoire à zipper
#@param 2 : nom  du repertoire à zipper.
#exemple sh create_packages.sh ../ornidroid_images/ images

EXPECTED_ARGS=2
E_BADARGS=65

if [ $# -ne $EXPECTED_ARGS ]
then
  echo "script de creation de packages zippés des médias"
  echo "Usage: `basename $0` {dir père du dir à zipper} {nom_repertoire (images ou audio)}"
  exit $E_BADARGS
fi

export HOME_DIR=`pwd`
export DIR_PERE=$1
export DIR_ZIP=$2
#aller sur le repertoire pere du repertoire à zipper
cd $DIR_PERE

#zipper
zip -r $DIR_ZIP.zip $DIR_ZIP
#deplacer dans ornidroid_bdd
mv $DIR_ZIP.zip $HOME_DIR
