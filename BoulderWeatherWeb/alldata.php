<?php
include ("../includes/db_connect.php");
$sql = "select * from weather order by id,station asc";
$result = mysql_query($sql) or trigger_error(mysql_error());

?>

<html><head><title>Boulder Weather Info</title>
<style>
TD { white-space: nowrap; }
</style></head>
<body>
<h1>Boulder Weather Data</h1>
<table>
<?php 
$i=0;
while ($row = mysql_fetch_assoc($result)) {
	if ($i==0) {
		$fields = array_keys($row);
		echo "<tr>";
		foreach($fields as $field) {
			echo "<td>$field</td>";
		}
		echo "</tr>\n";
	}
	echo "<tr>";
	foreach(array_values($row) as $value) {
		echo "<td>$value</td>";
	}
	echo "</tr>\n";
	$i++;

}
?>
</table>
</body>
</html>
