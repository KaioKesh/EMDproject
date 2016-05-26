<?php
/**
 * Creates new user, return new id
 */
    include_once 'db_functions.php';
    $db = new DB_Functions();
    $newid = $db->requestnewUser();
	//echo json_encode($newid, JSON_NUMERIC_CHECK);
	//echo $newid;
	$a = array('newid'=> $newid);
	echo json_encode($a);
?>	