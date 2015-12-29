<?php


$db = mysql_connect("localhost", "ccmp", "************");
mysql_select_db("ccmp");
mysql_query("SET NAMES 'utf8'");

//$c = mysql_query("SELECT * FROM rb_data WHERE id = 59");
$c = mysql_query("SELECT * FROM rb_data WHERE _app_id = '$_POST[_app_id]' AND _key = '$_POST[_key]';");
$result = mysql_fetch_array($c);

if (!$result) {
    echo 'FAILED==//invalid_data';
    return;
}

$exp_after_in_sec = $result['_exp_after'] * 60;
$created_sec = $result['ts_created'];
$time_now = time();

$diff_created_till_now = ($time_now - $created_sec);

$remaining = ($exp_after_in_sec - $diff_created_till_now);

$stringArray = array("appId" => $result['_key'], "exp_after_in_sec" => $exp_after_in_sec, "remaining" => $remaining, "userId" => $result['_value0'], "value1" => $result['_value1'], "value2" => $result['_value2'], "value3" => $result['_value3']);


/* Encode array to JSON string */
function encodeArray($resString) {
    header('Content-type: application/json');
    echo json_encode(array('posts' => $resString));
}

if (($exp_after_in_sec - $diff_created_till_now) < 0) {
    echo 'FAILED==//expired';
} else {
    if ($result) {
        encodeArray($stringArray);
    } else {
        echo 'FAILED==//no_data_found';
    }
}

?>

