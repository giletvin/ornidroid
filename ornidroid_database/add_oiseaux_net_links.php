<?php



/*
* MAIN
*/
/***
	<espece>
		<nom_lat>Hieraaetus pennatus</nom_lat>
		<nom_fr>Aigle botté</nom_fr>
		<url>http://www.oiseaux.net/oiseaux/aigle.botte.html</url>
	</espece>
*/
$handlerFileUpdateOiseauNetLinks = fopen('generate_update_oiseaux_net_links.sql', 'w');
try {
	$sqliteFile = "ornidroid.jpg";
	$dbh = new PDO('sqlite:'.$sqliteFile);
	$birdId=-1;

	$doc = DOMDocument :: load("fiches.xml");
	$listeEspecesXML=$doc->getElementsByTagName('espece');
	foreach ($listeEspecesXML as $especeXML) {
		$especeSubNodes = $especeXML->childNodes;
		$birdId=-1;
		foreach($especeSubNodes as $subnode){
			
			if ($subnode->nodeType == XML_ELEMENT_NODE) {
				//nom lat ou nom_fr ou url
				$nodeName = $subnode->nodeName;
				if ($nodeName=='nom_lat'){
					
					$findBirdIdQuery = "select id from bird where scientific_name='".$subnode->nodeValue."' or scientific_name2='".$subnode->nodeValue."'";
					///echo $findBirdIdQuery."\n";
					foreach ($dbh->query($findBirdIdQuery) as $row){
						$birdId = $row["id"];
					}

					if ($birdId>=1){
						echo "######insertion du lien pour : " . $subnode->nodeValue."\n";
					}
					else {
						//echo "nom latin non référencé : " . $subnode->nodeValue."\n";
					}
				}

				if ($nodeName=='url'&&$birdId>=1){
					echo $subnode->nodeValue."\n";
					$updateQuery="update bird set oiseaux_net_link='".$subnode->nodeValue."' where id=".$birdId.";\n";
					fwrite($handlerFileUpdateOiseauNetLinks,$updateQuery);
				}
			}
		}
	}
	$dbh = null;
}
catch(PDOException $e) {
    echo $e->getMessage();
}
fclose($handlerFileUpdateOiseauNetLinks);
?>
