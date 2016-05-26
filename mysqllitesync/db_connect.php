<?php
 
class DB_Connect {
 
	var $myconn;
    // constructor
    function __construct() {
 
    }
 
    // destructor
    function __destruct() {
        // $this->close();
    }
 
    // Connecting to database
    public function connect() {
        require_once 'config.php';
        // connecting to mysql
        $con = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD,DB_DATABASE);
		if (!$con) {
            die('Could not connect to database!');
        } else {
			$this->myconn = $con;
			//echo "connected";
		}
		        // return database handler
        return $this->myconn;
    }
 
    // Closing database connection
    public function close() {
        mysqli_close();
    }
} 
?>