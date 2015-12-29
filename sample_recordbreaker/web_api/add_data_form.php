<!DOCTYPE html>
<html lang="en">


<!--HEAD-->
<head>
    <meta charset="UTF-8">
    <title>RecordBreaker</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style type="text/css">
        body {
            background-color: fafafa;
            background-repeat: no-repeat;
            background-position: top left;
            background-attachment: fixed;
        }

        h1 {
            font-family: Helvetica;
            color: 212121;
            background-color: fafafa;
        }

        p {
            font-family: Cursive;
            font-size: 14px;
            font-style: normal;
            font-weight: normal;
            font-variant: small-caps;
            color: 000000;
            background-color: fafafa;
        }
    </style>
</head>
<body>
<h1>RecordBreaker-0.0.1</h1>

<p>Enter your application data and click submit. Please note: If you leave the duration field empty, the appId will expire after 24h automatically.</p>


<!--FORM-->

<form name="HtmlForm" method="POST">
    <table width="450px">

        <!--PACKAGE FIELD-->
        <tr>
            <td valign="top">
                <label for="_app_id">Application-Id</label>
            </td>
            <td valign="top">
                <input type="text" name="_app_id" maxlength="50" size="30">
            </td>
        </tr>

        <!--KEY FIELD-->
        <tr>
            <td valign="top">
                <label for="_key">Key*</label>
            </td>
            <td valign="top">
                <input type="text" name="_key" maxlength="50" size="30">
            </td>
        </tr>

        <!--VALUE FIELD-->
        <tr>
            <td valign="top">
                <label for="_value0">Value (0)*</label>
            </td>
            <td valign="top">
                <input type="text" name="_value0" maxlength="50" size="30">
            </td>
        </tr>

        <!--VALUE FIELD-->
        <tr>
            <td valign="top">
                <label for="_value1">Value (1)</label>
            </td>
            <td valign="top">
                <input type="text" name="_value1" maxlength="50" size="30">
            </td>
        </tr>

        <!--VALUE FIELD-->
        <tr>
            <td valign="top">
                <label for="_value2">Value (2)</label>
            </td>
            <td valign="top">
                <input type="text" name="_value2" maxlength="50" size="30">
            </td>
        </tr>

        <!--VALUE FIELD-->
        <tr>
            <td valign="top">
                <label for="_value3">Value (3)</label>
            </td>
            <td valign="top">
                <input type="text" name="_value3" maxlength="50" size="30">
            </td>
        </tr>

        <!--DURATION FIELD-->
        <tr>
            <td valign="top">
                <label for="_exp_after">Duration (default: 24h)</label>
            </td>
            <td valign="top">
                <input type="text" name="_exp_after" maxlength="30" size="30">
            </td>
        </tr>

        </br>
        <!--SUBMIT BUTTON-->
        <tr>
            <td colspan="0" style="text-align:center">
                <input type="submit" value="Submit">
            </td>
        </tr>
    </table>
</form>


<?php

if ((isset($_POST["_key"]) && !empty($_POST["_value0"]))) {

    $db = mysql_connect("localhost", "ccmp", "************");
    mysql_select_db("ccmp");
    mysql_query("SET NAMES 'utf8'");

    $time_sec = time();

    $_app_id = $_POST["_app_id"];
    $_key = $_POST["_key"];
    $_value0 = $_POST["_value0"];
    $_value1 = $_POST["_value1"];
    $_value2 = $_POST["_value2"];
    $_value3 = $_POST["_value3"];

    if (!empty($_POST["_exp_after"])) {
        $_exp_after = $_POST["_exp_after"] * 60;
    } else if (!empty($_POST["_exp_after_min"])) {
        $_exp_after = $_POST["_exp_after_min"];
    }
    if (empty($_POST["_exp_after"]) && empty($_POST["_exp_after_min"])) {
        $_exp_after = 24 * 60;
    }


    $result = mysql_query("INSERT INTO rb_data (_app_id, ts_created, _key, _value0, _value1, _value2, _value3, _exp_after) VALUES ('$_app_id', '$time_sec', '$_key', '$_value0', '$_value1', '$_value2', '$_value3', '$_exp_after');");

    if ($result) {
        ?>
        <div id="msg">Got it! Key expires in <?php echo $_exp_after / 60; ?> hours.</div>
        <?php
    } else {
        ?>
        <div id="msg">Failed!</div>
        <?php
    }
} else {
    ?>
    <div id="msg">Please enter some valid data.</div>
    <?php
}
?>

</body>

</html>
