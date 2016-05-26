<?php
/**
 * Creates Unsynced rows data as JSON
 */
    include_once 'db_functions.php';
    $db = new DB_Functions();
    $users = $db->getAllUsers();
    $a = array();
    $b = array();
    if ($users != false){
        while ($row = mysqli_fetch_array($users)) {      
            $b["userId"] = $row["_id"];
            $b["companyId"] = $row["_company"];
			$b["superiorId"] = $row["_superior"];
            array_push($a,$b);
        }
        echo json_encode($a);
    }
?>