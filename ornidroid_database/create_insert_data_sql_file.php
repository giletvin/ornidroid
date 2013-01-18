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
		"Ÿ"
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
		"Y"
	), $str);
}

/**
* nettoyage du nom français de l'oiseau pour créer le répertoire correspondant.
* le répertoire de l'oiseau est le nom francais sans les caractères accentués, sans apostrophe et sans espace, remplacés par des _
*/
function nettoie_nom($string){
	return trim(strtolower(str_replace(array (
		" ",
		"'"),array("_","_"),removeDiacritics($string))));

}
/*
* nettoie les string pour les insert sql. Suppression des ' et remplacement par \'
*/
function escapeSql($string){
	return str_replace(array("'"),array("\'"),$string);
}
/*
* renvoie une string en minuscule avec la premier lettre en maj
*/
function normalizeString($string){
	return ucfirst(strtolower($string));
}
/*
Retourne l'id de la catégorie de taille
INSERT INTO size_table(id,name,lang) VALUES(1,"Comme le moineau ou plus petit",'fr');
INSERT INTO size_table(id,name,lang) VALUES(2,"Entre le moineau et le merle",'fr');
INSERT INTO size_table(id,name,lang) VALUES(3,"Entre le merle et le pigeon",'fr');
INSERT INTO size_table(id,name,lang) VALUES(4,"Entre le pigeon et le canard colvert",'fr');
INSERT INTO size_table(id,name,lang) VALUES(5,"Plus grand que le canard colvert",'fr');
Les valeurs sont les tailles du moineau, du merle, du pigeon et du canard colvert.
*/
function getSizeFk($sizeIntValue){
	if ($sizeIntValue<=14){
		return 1;
	}
	if (($sizeIntValue>14)&&($sizeIntValue<=24)){
		return 2;
	}
	if ($sizeIntValue>24&&$sizeIntValue<=32){
		return 3;
	}
	if ($sizeIntValue>32&&$sizeIntValue<=43){
		return 4;
	}
	if ($sizeIntValue>43){
		return 5;
	}
}
/**
*Retourne l'id de la colour en base de données correspondant à la data passée en prm
*/
function getColourFk($data){
	$array_colours = array();
	//INSERT INTO colour(id,name,lang) VALUES(1,"Blanc",'fr');
	//INSERT INTO colour(id,name,lang) VALUES(2,"Bleu",'fr');
	//INSERT INTO colour(id,name,lang) VALUES(3,"Gris",'fr');
	//INSERT INTO colour(id,name,lang) VALUES(4,"Jaune",'fr');
	//INSERT INTO colour(id,name,lang) VALUES(5,"Noir",'fr');
	//INSERT INTO colour(id,name,lang) VALUES(6,"Orange",'fr');
	//INSERT INTO colour(id,name,lang) VALUES(7,"Rose",'fr');
	//INSERT INTO colour(id,name,lang) VALUES(8,"Rouge",'fr');
	//INSERT INTO colour(id,name,lang) VALUES(9,"Brun",'fr');
	//INSERT INTO colour(id,name,lang) VALUES(10,"Vert",'fr');

	//TODO : attention ! ce sont les valeurs en dur qu'on trouve dans le csv et qui sont dans le sql qui fait les inserts dans la table colours !
	$array_colours= array('blanc', 'bleu', 'gris', "jaune", "noir", "orange", "rose", "rouge", "brun", "vert");
	if ($data!=''){
		//retrouver dans la map colours l'id qui lui a été accordé dans la table des colours
		//id = index dans le tableau +1 car les id commencent à 1 et pas 0
		return array_search($data,$array_colours)+1;
	}
	else {
		return -1;
	}
}

/*
* Genere la commande insert dans la table bird
* insert into bird (id,scientific_name,directory_name,scientific_order_fk,scientific_family_fk, category_fk,beak_form_fk, size_value,size_fk, feather_colour_fk,feather_colour_2_fk,beak_colour_fk,beak_colour_2_fk,paw_colour_fk,paw_colour_2_fk,remarkable_sign_fk) values (1,'morus bassanus','morus_bassanus',1,1,1,1,115,5,1,1,1,1,1,1,1,1);
*/
function genereInsertTableBird($array_sign,$array_beak_form,$array_habitat,$array_category,$array_scientific_orders,$array_scientific_family,$id,$csvLine){

        $directory_name = nettoie_nom($csvLine[0]);
	$scientific_name = $csvLine[2];
	$data = explode(": ",$csvLine[4]);
	//id = index dans le tableau +1 car les id commencent à 1 et pas 0
	$ordre= array_search(normalizeString($data[0]),$array_scientific_orders)+1;
	$size_value=intval(trim($csvLine[5]));
	$size_fk=getSizeFk($size_value);
	//id = index dans le tableau +1 car les id commencent à 1 et pas 0
	$famille=array_search($data[1],$array_scientific_family)+1;
	//retrouver dans la map des categories l'id qui lui a été accordé dans la table des category
	//id = index dans le tableau +1 car les id commencent à 1 et pas 0
	$category_fk=array_search($csvLine[6],$array_category)+1;

	//retrouver dans la map des forms de bec l'id qui lui a été accordé dans la table des beak_form
	//id = index dans le tableau +1 car les id commencent à 1 et pas 0
	$beak_form_fk=array_search($csvLine[8],$array_beak_form)+1;

	//retrouver dans la map colours l'id qui lui a été accordé dans la table des colours
	//id = index dans le tableau +1 car les id commencent à 1 et pas 0
	$feather_colour_fk=getColourFk($csvLine[14]);
	$feather_colour_2_fk=getColourFk($csvLine[15]);
	$paw_colour_fk=getColourFk($csvLine[11]);
	$paw_colour_2_fk=getColourFk($csvLine[12]);
	$beak_colour_fk=getColourFk($csvLine[9]);
	$beak_colour_2_fk=getColourFk($csvLine[10]);

	$sqlQuery = "insert into bird (id,scientific_name,directory_name,scientific_order_fk,scientific_family_fk, category_fk, beak_form_fk,size_value,size_fk,feather_colour_fk,feather_colour_2_fk,beak_colour_fk,beak_colour_2_fk,paw_colour_fk,paw_colour_2_fk) values (".$id.",'".$scientific_name."','".$directory_name."',".$ordre.",".$famille.",".$category_fk.",".$beak_form_fk.",".$size_value.",".$size_fk.",".$feather_colour_fk.",".$feather_colour_2_fk.",".$beak_colour_fk.",".$beak_colour_2_fk.",".$paw_colour_fk.",".$paw_colour_2_fk.");\n";

	//meme chose avec les habitats
	$habitat1_fk='';
	$habitat2_fk='';
	if($csvLine[18]!=''){
		$habitat1_fk=array_search($csvLine[18],$array_habitat)+1;
		$sqlQuery .="update bird set habitat1_fk=".$habitat1_fk." where id=".$id.";\n";
	}
	if ($csvLine[19]!=''){
		$habitat2_fk=array_search($csvLine[19],$array_habitat)+1;
		$sqlQuery .="update bird set habitat2_fk=".$habitat2_fk." where id=".$id.";\n";
	}
	if ($csvLine[13]!=''){
		//retrouver dans la map des forms des signs l'id qui lui a été accordé dans la table des remarkable_sign
		//id = index dans le tableau +1 car les id commencent à 1 et pas 0
		$sign_fk=array_search($csvLine[13],$array_sign)+1;
		$sqlQuery .="update bird set remarkable_sign_fk=".$sign_fk." where id=".$id.";\n";
	}

	return $sqlQuery;
}

/*
* Genere la commande insert dans la table taxonomy
* //INSERT INTO taxonomy(lang,taxon,bird_fk) VALUES('fr','fou de bassan',1);
*/
function genereInsertTableTaxonomy($id,$csvLine){
	//nom francais
	$insertTaxons="INSERT INTO taxonomy(lang,taxon,searched_taxon, bird_fk) VALUES('fr',\"".$csvLine[0]."\",\"".removeDiacritics($csvLine[0])."\",".$id.");\n";
	if ($csvLine[1]!=''){
		//deuxieme nom francais si existe
		$insertTaxons=$insertTaxons."INSERT INTO taxonomy(lang,taxon,searched_taxon,bird_fk) VALUES('fr',\"".$csvLine[1]."\",\"".removeDiacritics($csvLine[1])."\",".$id.");\n";
	}
	//nom anglais
	$insertTaxons=$insertTaxons."INSERT INTO taxonomy(lang,taxon,searched_taxon,bird_fk) VALUES('en',\"".$csvLine[3]."\",\"".removeDiacritics($csvLine[3])."\",".$id.");\n";
	return $insertTaxons;
}

/*
* Genere la commande insert dans la table description
* //INSERT INTO bird_description(lang,description,distribution, bird_fk) VALUES('en','English description','distribution',1);
*/
function genereInsertTableDescription($id,$csvLine){
	if ($csvLine[16]!=''||$csvLine[17]!=''){
		return "INSERT INTO bird_description(lang,description,distribution,bird_fk) VALUES('fr',\"".$csvLine[16]."\",\"".$csvLine[17]."\",".$id.");\n";
	}
	else{
		return "";
	}
}

/*
* Genere la commande insert dans la table des ordres scientifiques et des familles
* //INSERT INTO scientific_order(id,name,lang) VALUES(1,'Passeriformes','fr');
* //5eme colonne
*/
function genereInsertTableScientificOrderAndFamily(&$array_scientific_orders,&$array_scientific_family,$csvLine){
	if ($csvLine[4]!=''){
		$data = explode(": ",$csvLine[4]);
		$ordre= normalizeString($data[0]);
		$famille=$data[1];
		$sqlquery="";
		if (!in_array($ordre,$array_scientific_orders)){
			$id=array_push($array_scientific_orders,$ordre);
			$sqlquery .= "INSERT INTO scientific_order(id,name,lang) VALUES(".$id.",\"".$ordre."\",'fr');\n";
			$sqlquery .= "INSERT INTO scientific_order(id,name,lang) VALUES(".$id.",\"".$ordre."\",'de');\n";
			$sqlquery .= "INSERT INTO scientific_order(id,name,lang) VALUES(".$id.",\"".$ordre."\",'en');\n";
		}
		if (!in_array($famille,$array_scientific_family)){
			$id =array_push($array_scientific_family,$famille);

			$sqlquery .= "INSERT INTO scientific_family(id,name,lang) VALUES(".$id.",\"".$famille."\",'fr');\n";
			$sqlquery .= "INSERT INTO scientific_family(id,name,lang) VALUES(".$id.",\"".$famille."\",'de');\n";
			$sqlquery .= "INSERT INTO scientific_family(id,name,lang) VALUES(".$id.",\"".$famille."\",'en');\n";
		}
		return $sqlquery;
	}
	else{
		return "";
	}
}

/*
* Genere la commande insert dans la table des remarkable signs
* //INSERT INTO remarkable_sign(id,name,lang) VALUES(1,'huppe','fr');
* //14e colonne
*/
function genereInsertTableSign(&$array_sign,$csvLine){
	$sign=$csvLine[13];
	if ($sign!=''){
		$sqlquery="";
		if (!in_array($sign,$array_sign)){
			$id=array_push($array_sign,$sign);
			$sqlquery .= "INSERT INTO remarkable_sign(id,name,lang) VALUES(".$id.",\"".$sign."\",'fr');\n";
		}
		return $sqlquery;
	}
	else{
		return "";
	}
}
/*
initialisation du tab des categories telles qu'elles sont stockées en bdd
INSERT INTO category(id,name,lang) VALUES(1,"Passereaux",'fr');
INSERT INTO category(id,name,lang) VALUES(2,"Rapaces",'fr');
INSERT INTO category(id,name,lang) VALUES(3,"Grands échassiers",'fr');
INSERT INTO category(id,name,lang) VALUES(4,"Cygnes, oies, canards",'fr');
INSERT INTO category(id,name,lang) VALUES(5,"Limicoles",'fr');
INSERT INTO category(id,name,lang) VALUES(6,"Gallinacés",'fr');
INSERT INTO category(id,name,lang) VALUES(7,"Corvidés",'fr');
INSERT INTO category(id,name,lang) VALUES(8,"Chouettes, hiboux",'fr');
INSERT INTO category(id,name,lang) VALUES(9,"Fous, cormorans, pélicans",'fr');
INSERT INTO category(id,name,lang) VALUES(10,"Autres",'fr');
INSERT INTO category(id,name,lang) VALUES(11,"Râles, marouettes, poules d'eau",'fr');
INSERT INTO category(id,name,lang) VALUES(12,"Mouettes, goéland, fulmars",'fr');
INSERT INTO category(id,name,lang) VALUES(13,"Labbes",'fr');
INSERT INTO category(id,name,lang) VALUES(14,"Grèbes",'fr');
INSERT INTO category(id,name,lang) VALUES(15,"Sternes",'fr');
INSERT INTO category(id,name,lang) VALUES(16,"Pingouins, guillemots, macareux",'fr');
INSERT INTO category(id,name,lang) VALUES(17,"Martinets et hirondelles",'fr');
INSERT INTO category(id,name,lang) VALUES(18,"Pics",'fr');
INSERT INTO category(id,name,lang) VALUES(19,"Pigeons et tourterelles",'fr');
INSERT INTO category(id,name,lang) VALUES(20,"Plongeons",'fr');
*/
function initializeCategories(){
	return array('Passereaux', 'Rapaces', 'Grands échassiers', "Cygnes, oies, canards", "Limicoles", "Gallinacés", "Corvidés", "Chouettes, hiboux", "Fous, cormorans, pélicans","Autres","Râles, marouettes, poules d'eau","Mouettes, goéland, fulmars","Labbes","Grèbes","Sternes","Pingouins, guillemots, macareux","Martinets et hirondelles","Pics","Pigeons et tourterelles","Plongeons");
}
/*
init des habitats tels que dans la bdd
INSERT INTO habitat(id,name,lang) VALUES(1,"Montagnes et roches",'fr');
INSERT INTO habitat(id,name,lang) VALUES(2,"Villes, villages, jardins",'fr');
INSERT INTO habitat(id,name,lang) VALUES(3,"Milieu sec ou désertique",'fr');
INSERT INTO habitat(id,name,lang) VALUES(4,"Forêts et landes",'fr');
INSERT INTO habitat(id,name,lang) VALUES(5,"Champs et bocages",'fr');
INSERT INTO habitat(id,name,lang) VALUES(6,"Littoral",'fr');
INSERT INTO habitat(id,name,lang) VALUES(7,"Bords de rivières, zones humides",'fr');
INSERT INTO habitat(id,name,lang) VALUES(8,"Milieu méditerranéen",'fr');
INSERT INTO habitat(id,name,lang) VALUES(9,"Toundra",'fr');
*/
function initializeHabitats(){
	return array('Montagnes et roches', 'Villes, villages, jardins', 'Milieu sec ou désertique', "Forêts et landes", "Champs et bocages", "Littoral", "Bords de rivières, zones humides", "Milieu méditerranéen", "Toundra");
}
/*
init les formes de becs
//INSERT INTO beak_form(id,name,lang) VALUES(1,"autres becs droits",'fr');
// INSERT INTO beak_form(id,name,lang) VALUES(2,"épais et court",'fr');
// INSERT INTO beak_form(id,name,lang) VALUES(3,"autre",'fr');
// INSERT INTO beak_form(id,name,lang) VALUES(4,"courbé",'fr');
// INSERT INTO beak_form(id,name,lang) VALUES(5,"droit et long",'fr');
// INSERT INTO beak_form(id,name,lang) VALUES(6,"crochu",'fr');
// INSERT INTO beak_form(id,name,lang) VALUES(7,"fin et court",'fr');
// INSERT INTO beak_form(id,name,lang) VALUES(8,"canard",'fr');
// INSERT INTO beak_form(id,name,lang) VALUES(9,"mouette",'fr');

*/
function initializeBeakForms(){
	return array('autres becs droits', 'épais et court', 'autre', "courbé", "droit et long", "crochu", "fin et court", "canard", "mouette");
}
/*
INSERT INTO remarkable_sign(id,name,lang) VALUES(1,"queue longue",'fr');
INSERT INTO remarkable_sign(id,name,lang) VALUES(2,"huppe",'fr');
INSERT INTO remarkable_sign(id,name,lang) VALUES(3,"moucheté",'fr');
INSERT INTO remarkable_sign(id,name,lang) VALUES(4,"queue bifide",'fr');
*/
function initializeSigns(){
	return array('queue longue', 'huppe', 'moucheté', "queue bifide");
}

/*
* MAIN
*/
$handlerTableBird = fopen('insert_data_table_bird.sql', 'w');

$handlerTableTaxonomy = fopen('insert_data_table_taxonomy.sql', 'w');

$handlerTableDescription = fopen('insert_data_table_bird_description.sql', 'w');

$handlerTableScientificOrderAndFamily = fopen('insert_data_table_scientific_order_and_family.sql', 'w');



$array_scientific_orders = array();
$array_scientific_family = array();
$array_category = initializeCategories();
$array_habitat = initializeHabitats();
$array_sign = initializeSigns();
$array_beak_form = initializeBeakForms();


$idBird=0;
if (($handle = fopen("oiseaux_europe_avibase_ss_rares.csv", "r")) !== FALSE) {
    while (($data = fgetcsv($handle, 1000, "|")) !== FALSE) {
	if ($idBird>0) {
		if (count($data)==21){
			$insertTableScientificOrderAndFamily=genereInsertTableScientificOrderAndFamily($array_scientific_orders,$array_scientific_family,$data);
			$insertTableBird=genereInsertTableBird($array_sign,$array_beak_form,$array_habitat,$array_category,$array_scientific_orders,$array_scientific_family,$idBird,$data);
			$insertTableTaxonomy=genereInsertTableTaxonomy($idBird,$data);
			$insertTableDescription=genereInsertTableDescription($idBird,$data);
			fwrite($handlerTableBird, $insertTableBird);
			fwrite($handlerTableTaxonomy, $insertTableTaxonomy);
			fwrite($handlerTableDescription, $insertTableDescription);
			fwrite($handlerTableScientificOrderAndFamily, $insertTableScientificOrderAndFamily);


		}
		else {
			echo "erreur sur la ligne : ".$idBird. " ". print_r($data);
		}
	}
	$idBird++;
    }
    fclose($handle);
}
fclose($handlerTableBird);
fclose($handlerTableTaxonomy);
fclose($handlerTableDescription);
fclose($handlerTableScientificOrderAndFamily);



?>
