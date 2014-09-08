#!/bin/sh


export DATABASE_NAME=../ornidroid_database/ornidroid.jpg
export oiseaux_csv=Ois_Xeno.csv
export DEST_DIR=xc_oiseaux
rm -rf $DEST_DIR/
mkdir $DEST_DIR
rm bird_names.txt
echo 'select scientific_name from bird;'|sqlite3 $DATABASE_NAME >bird_names.txt
#echo 'Turdus merula' > bird_names.txt

while read line
do
	rm current_bird.tmp
	scientific_name_brut=`echo $line`
	scientific_name=`echo $line | sed 's/\ /;/g'`
	echo $scientific_name
	grep "$scientific_name" $oiseaux_csv > current_bird.tmp
	if [ $? != 0 ];
	then
		echo "pas trouve"
	else 
		while read xc_line
		do
			echo $xc_line

			code_xc=`echo $xc_line | cut -d ";" -f 1`
			echo $code_xc
			directory_name=`echo $xc_line | cut -d ";" -f 2 | sed 's/\./_/g'`
			echo $directory_name
			title=`echo $xc_line | cut -d ";" -f 7`
			author=`echo $xc_line | cut -d ";" -f 5`
			#http://www.xeno-canto.org/193667/download
			wget http://www.xeno-canto.org/$code_xc/download -O $code_xc.mp3
			mkdir $DEST_DIR/$directory_name
			mv $code_xc.mp3 $DEST_DIR/$directory_name
			#properties file
			echo "scientific_name=$scientific_name_brut">$DEST_DIR/$directory_name/$code_xc.mp3.properties
			echo "mp3_url=http://www.xeno-canto.org/$code_xc/download">>$DEST_DIR/$directory_name/$code_xc.mp3.properties
			echo "audio_recordist=$author">>$DEST_DIR/$directory_name/$code_xc.mp3.properties
			echo "audio_title=$title">>$DEST_DIR/$directory_name/$code_xc.mp3.properties
			echo "audio_remarks=">>$DEST_DIR/$directory_name/$code_xc.mp3.properties
			echo "audio_duration=">>$DEST_DIR/$directory_name/$code_xc.mp3.properties
			echo "audio_ref=XC$code_xc">>$DEST_DIR/$directory_name/$code_xc.mp3.properties
		done < current_bird.tmp
	fi
done < bird_names.txt

