<?php
chdir('/vhosts/app.boulderandroid.com/www/weather/');
include('../includes/db_connect.php');
define('T','Temperature');
define('RH','Relative Humidity');
define('DP','Dewpoint');
define('WC','Wind Chill');
define('P','Pressure');
define('AC', 'Aeronautical Correction');
define('WS','Wind Speed');
define('PG','Peak Gust');
define('WF','Wind From');
define('RA','Rain Accumulation');
$dbfields = array(T => "temp_deg_c",
                  RH => "relative_humidity",
                  DP => "dewpoint_deg_c",
                  WC => "wind_chill_deg_c",
                  P  => "pressure_millibars",
                  AC => "aeronautical_correction_millibars",
                  WS => "wind_speed_m_per_s",
                  PG => "peak_gust_m_per_s",
                  WF => array("wind_from_dir","wind_from_deg"),
                  RA => "rain_accumulation_mm");
$stations=array('fh','ml');

function fetch_station($station) {
	global $dbfields;
	$url="http://www.eol.ucar.edu/cgi-bin/weather.cgi?site=$station&sample=1";
	$handle=fopen($url, "r");
	$datatxt=stream_get_contents($handle);
	$data=preg_split("/\n/",$datatxt);
	//print_r($data);
	fclose($handle);
	$output = "";
	$date=$data[1];
	$output = "data: $date\n";
	$i=0;

	// Lets put everything in a nice hashmap
	$weather = array();
	foreach ($data as $key=>$value) {
		if ($i>2 && $i<13) {
			$hash = preg_split("/: /",$value);
	//		$output .= "$datarow    {$hash[0]} => {$hash[1]}\n";
			$weather[$hash[0]] = $hash[1];
		}
		$i++;
	}
//	print_r($weather);
	// Process Temp
	$temp_deg_c = (float)get_first_val($weather[T]);
	// Process Humidity
	$relative_humidity = (float)get_first_val($weather[RH]);
	$dewpoint_deg_c = (float)get_first_val($weather[DP]);
	$wind_chill_deg_c = (float)get_first_val($weather[WC]);
	$pressure_millibars = (float)get_first_val($weather[P]);
	$aeronautical_correction_millibars = (float)get_first_val($weather[AC]);
	$wind_speed_m_per_s = (float)get_first_val($weather[WS]);
	$peak_gust_m_per_s = (float)get_first_val($weather[PG]);
	$wind_from_dir = get_first_val($weather[WF]);
	$wind_from_deg = (float)get_third_val($weather[WF]);
	$rain_accumulation_mm = (float)get_first_val($weather[RA]);

	// Make our sql statement
	$sql = "insert into weather (station,{$dbfields[T]},{$dbfields[RH]},{$dbfields[DP]},{$dbfields[WC]},{$dbfields[P]},{$dbfields[AC]},{$dbfields[WS]},{$dbfields[PG]},{$dbfields[WF][0]},{$dbfields[WF][1]},{$dbfields[RA]},timestamp) values('$station', $temp_deg_c, $relative_humidity, $dewpoint_deg_c, $wind_chill_deg_c, $pressure_millibars, $aeronautical_correction_millibars, $wind_speed_m_per_s, $peak_gust_m_per_s, '$wind_from_dir', $wind_from_deg, $rain_accumulation_mm, NOW());";
	mysql_query($sql) or trigger_error(mysql_error());
	//	echo "Sql: $sql<br><br>";
}

// This will grab the first value before a space and return it
function get_first_val($input) {
	$arr=preg_split("/ /", $input,2);
	return $arr[0];
}
function get_third_val($input) {
        $arr=preg_split("/ /", $input,4);
        return $arr[2];
}

foreach ($stations as $station) {
	fetch_station($station);
}

//echo "<html><head><title>fetch</title><body><pre></pre></body></html>";


?>
