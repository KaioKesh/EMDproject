<?php
/**
 * Updates Sync status of Users
 */
include_once './db_functions.php';
//Create Object for DB_Functions clas
$db = new DB_Functions(); 
//Get JSON posted by Android Application
$json = $_POST["profile"];
//Remove Slashes
if (get_magic_quotes_gpc()){
$json = stripslashes($json);
}
$data = json_decode($json);
echo $data[0]->email;
$res = $db->updateProfile($data[0]->id, $data[0]->name, $data[0]->email, $data[0]->title);

?>