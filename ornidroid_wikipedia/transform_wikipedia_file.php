<?php



/*
* MAIN
*/
$fileName = $argv[1];
$lang = $argv[2];

$wikipediaContent = file_get_contents($fileName);
$content = preg_replace("/<img[^>]+\>/i", "", $wikipediaContent);
$content = preg_replace("/<link[^>]+\>/i", "", $content);
//$content = preg_replace("/<script[^>]+script\>/i", "", $content);
$content = preg_replace('/<script>[^>]+<\/script>/eis',"",$content);
if ($lang=='fr'){
	$content = preg_replace("/>modifier</i", "><", $content);  
	$content = preg_replace("/>modifier le code</i", "><", $content);  
	
}
if ($lang=='en'){
	$content = preg_replace("/>edit</i", "><", $content);  
	
}
if ($lang=='de'){
	$content = preg_replace("/>Bearbeiten</i", "><", $content);  
}
echo $content;



?>
