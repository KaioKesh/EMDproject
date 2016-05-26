<?php
/**
 * DB operations functions
 */
class DB_Functions {
 
    private $db;
 
    // constructor
    function __construct() {
        include_once './db_connect.php';
        // connecting to database
        $this->db = new DB_Connect();
        $this->db->connect();
    }
 
    // destructor
    function __destruct() {
 
    }
 
    public function requestnewUser() {
        // Insert user into database
        $newuser = "INSERT INTO users(`_company`, `_superior`) VALUES(-1,1);";
		$newprofile = "INSERT INTO profiles(`name`, `email`, `title`) VALUES('not_set','not_set','not_set');";
		if ($this->db->myconn->query($newuser) === TRUE && $this->db->myconn->query($newprofile)==TRUE) {
			return $this->db->myconn->insert_id;
		} else {
			echo "Error: " . $newprofile . "<br>" . $this->db->myconn->error;
			return false;
		}
    }
    /**
     * Storing new user
     * returns user details
     */
    public function storeUser($name,$email,$title) {
        // Insert user into database
        $result = mysqli_query($this->db->myconn,"INSERT INTO users(`_company`, `_superior`) VALUES(-1,-1);INSERT INTO profiles(`name`, `email`, `title`) VALUES($name,$email,$title);");
 
        if ($result) {
            return true;
        } else {            
                // For other errors
                return false;
        }
    }
     /**
     * Getting all users
     */
    public function getAllUsers() {
        $result = mysqli_query($this->db->myconn,"SELECT * FROM users");
        return $result;
    }
	public function getAllProfiles() {
        $result = mysqli_query($this->db->myconn,"SELECT * FROM profiles");
        return $result;
    }
	public function getAll(){
		$result = mysqli_query($this->db->myconn, "SELECT * FROM users LEFT JOIN profiles ON users._id=profiles._id;");
        return $result;
	}
	
	/**
	* Update user Profile
	*/
	public function updateProfile($id, $name, $email, $title){
		$intid = (int)$id;
        $result = mysqli_query($this->db->myconn, "UPDATE profiles SET name = '$name', email = '$email', title = '$title' WHERE _id = $intid");
        return $result;
    }
}
 
?>