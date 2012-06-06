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
		"ß"
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
		"ss"
	), $str);
}

/*
//INSERT INTO taxonomy(lang,taxon,searched_taxon,bird_fk) VALUES(?,?,?,?);
*/
function insertTraduction($dbh,$birdId,$birdName,$lang){
	$qry = $dbh->prepare(
    		'INSERT INTO taxonomy(lang,taxon,searched_taxon,bird_fk) VALUES(?,?,?,'.intval($birdId).');');
	$qry->execute(array($lang, $birdName,removeDiacritics($birdName)));
//	$dbh->exec('INSERT INTO taxonomy(lang,taxon,searched_taxon,bird_fk) VALUES($lang,$birdName,removeDiacritics($birdName),intval($birdId))')
}

/*
* MAIN
*/




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

try {
	$sqliteFile = "ornidroid.sqlite";
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
				insertTraduction($dbh,$birdId,$data[$la],$la_code);
				insertTraduction($dbh,$birdId,$data[$de],$de_code);
				insertTraduction($dbh,$birdId,$data[$es],$es_code);
				insertTraduction($dbh,$birdId,$data[$is],$is_code);
				insertTraduction($dbh,$birdId,$data[$dk],$dk_code);
				insertTraduction($dbh,$birdId,$data[$cz],$cz_code);
				insertTraduction($dbh,$birdId,$data[$fi],$fi_code);
				insertTraduction($dbh,$birdId,$data[$it],$it_code);
				insertTraduction($dbh,$birdId,$data[$jp],$jp_code);
				insertTraduction($dbh,$birdId,$data[$nl],$nl_code);
				insertTraduction($dbh,$birdId,$data[$no],$no_code);
				insertTraduction($dbh,$birdId,$data[$po],$po_code);
				insertTraduction($dbh,$birdId,$data[$pt],$pt_code);
				insertTraduction($dbh,$birdId,$data[$ru],$ru_code);
				insertTraduction($dbh,$birdId,$data[$sk],$sk_code);
				insertTraduction($dbh,$birdId,$data[$se],$se_code);
				insertTraduction($dbh,$birdId,$data[$zh],$zh_code);
			}
			else {
				echo "nom latin non référencé : " . $data[$la]."\n";
			}
		}
	    }
	    fclose($handle);
	}

	$dbh = null;
}
catch(PDOException $e) {
    echo $e->getMessage();
}

?>
