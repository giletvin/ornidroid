#!/bin/sh


export DATABASE_NAME=../ornidroid_database/ornidroid.jpg


echo 'select scientific_name from bird;'|sqlite3 $DATABASE_NAME >bird_names.txt

underscore="_"
while read line
do
	scientific_name=`echo $line | sed 's/\ /_/g'`
	echo $scientific_name
	wget -k http://fr.wikipedia.org/wiki/$scientific_name
	php transform_wikipedia_file.php $scientific_name fr > $scientific_name.tmp
	mv $scientific_name.tmp $scientific_name
	mv $scientific_name fr/
	wget -k http://en.wikipedia.org/wiki/$scientific_name
	php transform_wikipedia_file.php $scientific_name en > $scientific_name.tmp
	mv $scientific_name.tmp $scientific_name
	mv $scientific_name en/
	wget -k http://de.wikipedia.org/wiki/$scientific_name
	php transform_wikipedia_file.php $scientific_name de > $scientific_name.tmp
	mv $scientific_name.tmp $scientific_name
	mv $scientific_name de/
done < bird_names.txt

