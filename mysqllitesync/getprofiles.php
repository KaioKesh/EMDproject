<?php
/**
 * Creates profile data as JSON
 */
    include_once 'db_functions.php';
    $db = new DB_Functions();
    $profiles = $db->getAllProfiles();
    $a = array();
    $b = array();
    if ($profiles != false){
        while ($row = mysqli_fetch_array($profiles)) {      
            $b["userId"] = $row["_id"];
            $b["userName"] = $row["name"];
			$b["userEmail"] = $row["email"];
            $b["userTitle"] = $row["title"];
            array_push($a,$b);
        }
        echo json_encode($a);
    }
?>