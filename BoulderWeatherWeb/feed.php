<?php
include ("../includes/db_connect.php");
ini_set('display_errors', 1);
ini_set('log_errors', 1);  
ini_set('display_startup_errors', 1);
error_reporting(E_ALL);
$sql = "select * from weather order by timestamp desc, station desc limit 2";
$result = mysql_query($sql) or trigger_error(mysql_error());
$output = array();
$output[0]= mysql_fetch_assoc($result);
$output[1]= mysql_fetch_assoc($result);
echo json_encode($output);
?>
