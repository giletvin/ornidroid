<?php

/**
* suppression des caractères accentués d'une string
* @param $str string
* @return string sans les caractères accentués
*/
function removeDiacritics($str) {
	return str_replace(array (
		"à",
		"â",
		"ä",
		"å",
		"ã",
		"á",
		"Â",
		"Ä",
		"À",
		"Å",
		"Ã",
		"Á",
		"æ",
		"Æ",
		"ç",
		"Ç",
		"é",
		"è",
		"ê",
		"ë",
		"É",
		"Ê",
		"Ë",
		"È",
		"ï",
		"î",
		"ì",
		"í",
		"Ï",
		"Î",
		"Ì",
		"Í",
		"ñ",
		"Ñ",
		"ö",
		"ô",
		"ó",
		"ò",
		"õ",
		"Ó",
		"Ô",
		"Ö",
		"Ò",
		"Õ",
		"ù",
		"û",
		"ü",
		"ú",
		"Ü",
		"Û",
		"Ù",
		"Ú",
		"ý",
		"ÿ",
		"Ÿ",
		"ß",
		"ő",
		"ű",
		"ž",
		"đ",
		"Ž",
		"Ć",
		"š",
		"č"
	), array (
		"a",
		"a",
		"a",
		"a",
		"a",
		"a",
		"A",
		"A",
		"A",
		"A",
		"A",
		"A",
		"a",
		"A",
		"c",
		"C",
		"e",
		"e",
		"e",
		"e",
		"E",
		"E",
		"E",
		"E",
		"i",
		"i",
		"i",
		"i",
		"I",
		"I",
		"I",
		"I",
		"n",
		"N",
		"o",
		"o",
		"o",
		"o",
		"o",
		"O",
		"O",
		"O",
		"O",
		"O",
		"u",
		"u",
		"u",
		"u",
		"U",
		"U",
		"U",
		"U",
		"y",
		"y",
		"Y",
		"ss",
		"o",
		"u",
		"z",
		"d",
		"Z",
		"C",
		"s",
		"c"
	), $str);
}

/*
//INSERT INTO taxonomy(lang,taxon,searched_taxon,bird_fk) VALUES(?,?,?,?);
*/
function insertTraduction($dbh,$birdId,$birdName,$lang){
	//$qry = $dbh->prepare(
    	//	'INSERT INTO taxonomy(lang,taxon,searched_taxon,bird_fk) VALUES(?,?,?,'.intval($birdId).');');
//	$qry->execute(array($lang, $birdName,removeDiacritics($birdName)));
//	$dbh->exec('INSERT INTO taxonomy(lang,taxon,searched_taxon,bird_fk) VALUES($lang,$birdName,removeDiacritics($birdName),intval($birdId))')

	return "INSERT INTO taxonomy(lang,taxon,searched_taxon,bird_fk) VALUES('".$lang."',\"".$birdName."\",\"".removeDiacritics($birdName)."\",".intval($birdId).");\n";
}

/*
* MAIN
*/
$handlerFileInsertTraductions = fopen('generate_insert_data_traductions.sql', 'w');



$la=0;
$de=1;
$es=2;
$is=3;
$dk=4;
$cz=5;
$fi=6;
$it=7;
$jp=8;
$nl=9;
$no=10;
$po=11;
$pt=12;
$ru=13;
$sk=14;
$se=15;
$zh=16;
$la_code='la';
$de_code='de';
$es_code='es';
$is_code='is';
$dk_code='dk';
$cz_code='cz';
$fi_code='fi';
$it_code='it';
$jp_code='jp';
$nl_code='nl';
$no_code='no';
$po_code='po';
$pt_code='pt';
$ru_code='ru';
$sk_code='sk';
$se_code='se';
$zh_code='zh';
$gr_code='gr';
$bg_code='bg';
$ro_code='ro';
$tr_code='tr';
$hu_code='hu';
$sr_code='sr';
$sl_code='sl';
//catalan, basque, galicien
$ca_code='ca';
$eu_code='eu';
$gl_code='gl';

$latin_index_in_greek_file=1;
$greek_index_in_greek_file=2;
$latin_index_in_bulgarian_file=1;
$bulgarian_index_in_bulgarian_file=0;
$latin_index_in_romanian_file=0;
$romanian_index_in_romanian_file=1;
$latin_index_in_turkish_file=0;
$turkish_index_in_turkish_file=1;
$latin_index_in_hungarian_file=1;
$hungarian_index_in_hungarian_file=2;
$latin_index_in_serb_file=0;
$serb_index_in_serb_file=1;
$latin_index_in_slovenian_file=0;
$slovenian_index_in_slovenian_file=1;
$latin_index_in_catalan_basque_galician_file=0;
$catalan_index_in_catalan_basque_galician_file=1;
$basque_index_in_catalan_basque_galician_file=3;
$galician_index_in_catalan_basque_galician_file=2;


try {
	$sqliteFile = "ornidroid.jpg";
	$dbh = new PDO('sqlite:'.$sqliteFile); // success
/*
	//create the database
	$dbh->exec("CREATE TABLE lang (Id INTEGER PRIMARY KEY, code TEXT, label TEXT)"); 
	$qry = $dbh->prepare(
    		'INSERT INTO lang(id,code,label) VALUES(?,?,?);');
	$qry->execute(array($la,$la_code,"Latin"));
	$qry->execute(array(1,"fr","Français"));
	$qry->execute(array(2,"en","English"));
	$qry->execute(array(3,"de","Deutsch"));
	$qry->execute(array(4,"es","Español"));
	$qry->execute(array(5,"is","Íslenska"));
	$qry->execute(array(6,"dk","Dansk"));
	$qry->execute(array(7,"cz","Čeština"));
	$qry->execute(array(8,"fi","Suomi"));
	$qry->execute(array(9,"it","Italiano"));
	$qry->execute(array(10,"jp","日本語"));
	$qry->execute(array(11,"nl","Nederlands"));
	$qry->execute(array(12,"no","Norsk"));
	$qry->execute(array(13,"po","Polski"));
	$qry->execute(array(14,"pt","Português"));
	$qry->execute(array(15,"ru","Русский язык"));
	$qry->execute(array(16,"sk","Slovenčina"));
	$qry->execute(array(17,"se","Svenska"));
	$qry->execute(array(18,"zh","普通话"));
*/

	if (($handle = fopen("traductions.csv", "r")) !== FALSE) {
	    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
		//si latin non vide
		if (""!=$data[$la]){
			$findBirdIdQuery = "select id from bird where scientific_name='".$data[$la]."'";
			echo $findBirdIdQuery."\n";
			$birdId=-1;
			foreach ($dbh->query($findBirdIdQuery) as $row){
				$birdId = $row["id"];
			}
			if ($birdId>=1){
				echo "######insertion des traductions pour : " . $data[$la]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$la],$la_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$de],$de_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$es],$es_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$is],$is_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$dk],$dk_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$cz],$cz_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$fi],$fi_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$it],$it_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$jp],$jp_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$nl],$nl_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$no],$no_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$po],$po_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$pt],$pt_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$ru],$ru_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$sk],$sk_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$se],$se_code));
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,$data[$zh],$zh_code));
			}
			else {
				echo "nom latin non référencé : " . $data[$la]."\n";
			}
		}
	    }
	    fclose($handle);
	}

	//greek
	if (($handle = fopen("traductions_greek.csv", "r")) !== FALSE) {
	    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
		//si latin non vide et 4 colonnes = un oiseau !
		if (""!=$data[$latin_index_in_greek_file]&&count($data)==4){
			$findBirdIdQuery = "select id from bird where scientific_name='".trim($data[$latin_index_in_greek_file])."' or scientific_name2='".trim($data[$latin_index_in_greek_file])."'";
			echo $findBirdIdQuery."\n";
			$birdId=-1;
			foreach ($dbh->query($findBirdIdQuery) as $row){
				$birdId = $row["id"];
			}
			if ($birdId>=1){
				echo "######insertion du grec pour : " . $data[$latin_index_in_greek_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$greek_index_in_greek_file]),$gr_code));
			}
			else {
				echo "GREC : nom latin non référencé : " . $data[$latin_index_in_greek_file]."\n";
			}
		}
	    }
	    fclose($handle);
	}
	//bulgare
	if (($handle = fopen("traductions_bulgare.csv", "r")) !== FALSE) {
	    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
		//si latin non vide et 2 colonnes = un oiseau !
		if (""!=$data[$latin_index_in_bulgarian_file]&&count($data)==2){
			$findBirdIdQuery = "select id from bird where scientific_name='".trim($data[$latin_index_in_bulgarian_file])."' or scientific_name2='".trim($data[$latin_index_in_bulgarian_file])."'";
			echo $findBirdIdQuery."\n";
			$birdId=-1;
			foreach ($dbh->query($findBirdIdQuery) as $row){
				$birdId = $row["id"];
			}
			if ($birdId>=1){
				echo "######insertion du bulgare pour : " . $data[$latin_index_in_bulgarian_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$bulgarian_index_in_bulgarian_file]),$bg_code));
			}
			else {
				echo "Bulgare : nom latin non référencé : " . $data[$latin_index_in_bulgarian_file]."\n";
			}
		}
	    }
	    fclose($handle);
	}
	//roumain
	if (($handle = fopen("traductions_roumain.csv", "r")) !== FALSE) {
	    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
		//si latin non vide et 2 colonnes = un oiseau !
		if (""!=$data[$latin_index_in_romanian_file]&&count($data)==2){
			$findBirdIdQuery = "select id from bird where scientific_name='".trim($data[$latin_index_in_romanian_file])."' or scientific_name2='".trim($data[$latin_index_in_romanian_file])."'";
			echo $findBirdIdQuery."\n";
			$birdId=-1;
			foreach ($dbh->query($findBirdIdQuery) as $row){
				$birdId = $row["id"];
			}
			if ($birdId>=1){
				echo "######insertion du romanian pour : " . $data[$latin_index_in_romanian_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$romanian_index_in_romanian_file]),$ro_code));
			}
			else {
				echo "romanian : nom latin non référencé : " . $data[$latin_index_in_romanian_file]."\n";
			}
		}
	    }
	    fclose($handle);
	}
	//turc
	if (($handle = fopen("traductions_turc.csv", "r")) !== FALSE) {
	    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
		//si latin non vide et 2 colonnes = un oiseau !
		if (""!=$data[$latin_index_in_turkish_file]&&count($data)==2){
			$findBirdIdQuery = "select id from bird where scientific_name='".trim($data[$latin_index_in_turkish_file])."' or scientific_name2='".trim($data[$latin_index_in_turkish_file])."'";
			echo $findBirdIdQuery."\n";
			$birdId=-1;
			foreach ($dbh->query($findBirdIdQuery) as $row){
				$birdId = $row["id"];
			}
			if ($birdId>=1){
				echo "######insertion du turkish pour : " . $data[$latin_index_in_turkish_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$turkish_index_in_turkish_file]),$tr_code));
			}
			else {
				echo "turkish : nom latin non référencé : " . $data[$latin_index_in_turkish_file]."\n";
			}
		}
	    }
	    fclose($handle);
	}
	//hungarian
	if (($handle = fopen("traductions_hongrois.csv", "r")) !== FALSE) {
	    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
		//si latin non vide et 3 colonnes = un oiseau !
		if (""!=$data[$latin_index_in_hungarian_file]&&count($data)==3){
			$findBirdIdQuery = "select id from bird where scientific_name='".trim($data[$latin_index_in_hungarian_file])."' or scientific_name2='".trim($data[$latin_index_in_hungarian_file])."'";
			echo $findBirdIdQuery."\n";
			$birdId=-1;
			foreach ($dbh->query($findBirdIdQuery) as $row){
				$birdId = $row["id"];
			}
			if ($birdId>=1){
				echo "######insertion du hungarian pour : " . $data[$latin_index_in_hungarian_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$hungarian_index_in_hungarian_file]),$hu_code));
			}
			else {
				echo "hungarian : nom latin non référencé : " . $data[$latin_index_in_hungarian_file]."\n";
			}
		}
	    }
	    fclose($handle);
	}

	//Serbe
	if (($handle = fopen("traductions_serbe.csv", "r")) !== FALSE) {
	    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
		//si latin non vide et 2 colonnes = un oiseau !
		if (""!=$data[$latin_index_in_serb_file]&&count($data)==2){
			$findBirdIdQuery = "select id from bird where scientific_name='".trim($data[$latin_index_in_serb_file])."' or scientific_name2='".trim($data[$latin_index_in_serb_file])."'";
			echo $findBirdIdQuery."\n";
			$birdId=-1;
			foreach ($dbh->query($findBirdIdQuery) as $row){
				$birdId = $row["id"];
			}
			if ($birdId>=1){
				echo "######insertion du serb pour : " . $data[$latin_index_in_serb_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$serb_index_in_serb_file]),$sr_code));
			}
			else {
				echo "serb : nom latin non référencé : " . $data[$latin_index_in_serb_file]."\n";
			}
		}
	    }
	    fclose($handle);
	}
	//Slovene
	if (($handle = fopen("traductions_slovene.csv", "r")) !== FALSE) {
	    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
		//si latin non vide et 2 colonnes = un oiseau !
		if (""!=$data[$latin_index_in_slovenian_file]&&count($data)==2){
			$findBirdIdQuery = "select id from bird where scientific_name='".trim($data[$latin_index_in_slovenian_file])."' or scientific_name2='".trim($data[$latin_index_in_slovenian_file])."'";
			echo $findBirdIdQuery."\n";
			$birdId=-1;
			foreach ($dbh->query($findBirdIdQuery) as $row){
				$birdId = $row["id"];
			}
			if ($birdId>=1){
				echo "######insertion du slovenian pour : " . $data[$latin_index_in_slovenian_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$slovenian_index_in_slovenian_file]),$sl_code));
			}
			else {
				echo "slovenian : nom latin non référencé : " . $data[$latin_index_in_slovenian_file]."\n";
			}
		}
	    }
	    fclose($handle);
	}

	//Catalan, basque et galicien
	if (($handle = fopen("traductions_catalan_basque_galicien.csv", "r")) !== FALSE) {
	    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
		$findBirdIdQuery = "select id from bird where scientific_name='".trim($data[$latin_index_in_catalan_basque_galician_file])."' or scientific_name2='".trim($data[$latin_index_in_catalan_basque_galician_file])."'";
		echo $findBirdIdQuery."\n";
		$birdId=-1;
		foreach ($dbh->query($findBirdIdQuery) as $row){
			$birdId = $row["id"];
		}
		if ($birdId>=1){
			if (""!=trim($data[$catalan_index_in_catalan_basque_galician_file])){
				echo "######insertion du catalan pour : " . $data[$latin_index_in_catalan_basque_galician_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$catalan_index_in_catalan_basque_galician_file]),$ca_code));
			}
			if (""!=trim($data[$galician_index_in_catalan_basque_galician_file])){
				echo "######insertion du galicien pour : " . $data[$latin_index_in_catalan_basque_galician_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$galician_index_in_catalan_basque_galician_file]),$gl_code));
			}
			if (""!=trim($data[$basque_index_in_catalan_basque_galician_file])){
				echo "######insertion du basque pour : " . $data[$latin_index_in_catalan_basque_galician_file]."\n";
				fwrite($handlerFileInsertTraductions,insertTraduction($dbh,$birdId,trim($data[$basque_index_in_catalan_basque_galician_file]),$eu_code));
			}
		}
		else {
			echo "catalan, galicien, basque : nom latin non référencé : " . $data[$latin_index_in_catalan_basque_galician_file]."\n";
		}
	    }
	    fclose($handle);
	}

	$dbh = null;
}
catch(PDOException $e) {
    echo $e->getMessage();
}
fclose($handlerFileInsertTraductions);
?>
