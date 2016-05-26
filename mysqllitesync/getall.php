<?php
/**
 * Creates profile data as JSON
 */
    include_once 'db_functions.php';
    $db = new DB_Functions();
    $fulldb = $db->getAll();
    $a = array();
    $b = array();
    if ($fulldb != false){
        while ($row = mysqli_fetch_array($fulldb)) {      
            $b["userId"] = $row["_id"];
			$b["companyId"] = $row["_company"];
			$b["superiorId"] = $row["_superior"];
            $b["userName"] = $row["name"];
			$b["userEmail"] = $row["email"];
            $b["userTitle"] = $row["title"];
            array_push($a,$b);
        }	
        echo json_encode($a);
    }
?>