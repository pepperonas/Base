<?php

$db = mysql_connect("localhost", "ccmp", "PASSWORD");
mysql_select_db("ccmp");
mysql_query("SET NAMES 'utf8'");

/*Werte aus JSON-Objekt in Variablen ablegen*/
$dev_id   = $_POST['dev_id'];
$app_id   = $_POST['app_id'];
$user_id  = $_POST['user_id'];
$stamp 	  = $_POST['stamp'];
$expires  = $_POST['expires'];
$any_text = $_POST['any_text'];
$any_int  = $_POST['any_int'];

$result = mysql_query("INSERT INTO app_registry (dev_id, app_id, user_id, stamp, expires, any_text, any_int) VALUES ('$dev_id', '$app_id', '$user_id', CURRENT_TIMESTAMP,  '$expires', '$any_text', '$any_int');");

	if ($result) {
    		echo 'SUCCESS==//<info>user_registered</info>';
	} else {
		$result_msg = "FAILED==//<info>user_exists</info>";
		$field = 'stamp';
		$query = mysql_query("SELECT $field FROM app_registry WHERE user_id='". mysql_real_escape_string( $user_id ) ."';");
		$retval = mysql_fetch_object($query)->$field;

		$registration_date = "<stamp>";
		$registration_date .= $retval;
		$registration_date .= "</stamp>";


		$extraString = 'any_text';
		$query = mysql_query("SELECT $extraString FROM app_registry WHERE user_id='". mysql_real_escape_string( $user_id ) ."';");
		$retxtra_string = mysql_fetch_object($query)->$extraString;

		$stringExtra = "<extra_string>";
		$stringExtra .= $retxtra_string;
		$stringExtra .= "</extra_string>";


		$extraInt = 'any_int';
		$query = mysql_query("SELECT $extraInt FROM app_registry WHERE user_id='". mysql_real_escape_string( $user_id ) ."';");
		$retxtra_int = mysql_fetch_object($query)->$extraInt;

		$intExtra = "<extra_int>";
		$intExtra .= $retxtra_int;
		$intExtra .= "</extra_int>";

        	echo $result_msg, $registration_date, $stringExtra, $intExtra;
    	}

?>
