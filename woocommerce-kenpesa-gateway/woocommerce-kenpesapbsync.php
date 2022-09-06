<?php

require_once('../../../wp-config.php');

//require_once('../../../wp-blog-header.php');


$lc_result = $wpdb->get_row("SELECT localkey FROM {$wpdb->prefix}mod_kenpesa WHERE id =1");

$option_value = $wpdb->get_row("SELECT option_value FROM {$wpdb->prefix}options WHERE option_name like 'woocommerce_spyr_kenpesapb_settings'");


//$lc_data	= mysql_fetch_array($lc_result);
//$lc_data	= lc_result->localkey;


$localkey = $lc_result->localkey;

$plugin_data = unserialize($option_value->option_value);

$licensekey = $plugin_data['license'];
$user = $plugin_data['api_login'];
$pass = $plugin_data['trans_key'];

$notify_email = $plugin_data['notify_email'];

$license_check = check_license($licensekey, $localkey);

$sitename = get_bloginfo();

$domain_name = ltrim($_SERVER['HTTP_HOST'], 'www.');

///echo "<pre>";
//print_r($domain_name );
//echo "</pre>";
//exit();

if (isset($license_check['localkey'])) {
    if ($license_check['localkey'] !== $localkey) {

        $wpdb->update(
                "{$wpdb->prefix}mod_kenpesa", array(
            'localkey' => $license_check['localkey']
                ), array('id' => 1), array(
            '%s',
                ), array('%d')
        );
    }
}

if ($license_check['status'] !== 'Active') {
    die("Failed|License is {$license_check['status']}");
}


$postOrig = filter_var($_REQUEST['orig'], FILTER_SANITIZE_STRING);
$postId = filter_var($_REQUEST['id'], FILTER_SANITIZE_NUMBER_INT);
$postDest = filter_var($_REQUEST['dest'], FILTER_SANITIZE_STRING);
$postTstamp = filter_var($_REQUEST['tstamp'], FILTER_SANITIZE_STRING);
$postText = filter_var($_REQUEST['text'], FILTER_SANITIZE_STRING);
$postMpesa_code = filter_var($_REQUEST['mpesa_code'], FILTER_SANITIZE_STRING);
$postMpesa_acc = filter_var($_REQUEST['mpesa_acc'], FILTER_SANITIZE_STRING);
$postMpesa_msisdn = filter_var($_REQUEST['mpesa_msisdn'], FILTER_SANITIZE_STRING);
$postMpesa_trx_date = filter_var($_REQUEST['mpesa_trx_date'], FILTER_SANITIZE_STRING);
$postMpesa_trx_time = filter_var($_REQUEST['mpesa_trx_time'], FILTER_SANITIZE_STRING);
$postMpesa_amt = $_REQUEST['mpesa_amt'];
$postMpesa_sender = filter_var($_REQUEST['mpesa_sender'], FILTER_SANITIZE_STRING);
$debug = filter_var($_REQUEST['debug'], FILTER_SANITIZE_STRING);
$validate_at_client = filter_var($_REQUEST['validate_at_client'], FILTER_SANITIZE_STRING);
$base_string = filter_var($_REQUEST['base_string'], FILTER_SANITIZE_STRING);
$signature = filter_var($_REQUEST['signature'], FILTER_SANITIZE_STRING);

$base_string = base64_decode($base_string);

$err = '';

//echo "<pre>";
//print_r($_REQUEST);
//echo "</pre>";

if ($validate_at_client == 1) {
    $symmetric_key = $pass;
    $signature_created = base64_encode(hash_hmac("sha1", $base_string, $symmetric_key, true));
    //echo "$signature_created !== $signature";
    
    if($signature_created !== $signature){
        //die("Signature failed - API Key may have changed");
    }
    
} else {

    $postUser = filter_var($_REQUEST['user'], FILTER_SANITIZE_STRING);

    $postPass = filter_var($_REQUEST['pass'], FILTER_SANITIZE_STRING);

    if (empty($postUser)) {
        $err = $err . " Username Required";
    } else {
        if ($postUser != $user) {
            $err = $err . " Username didn't match.";
        }
    }
//}
//if (!empty($pass)) {
    if (empty($postPass)) {
        $err = $err . " Password Required";
    } else {
        if ($postPass != $pass) {
            $err = $err . " Password didn't match.";
        }
    }
//}
    if ($err != "") {



        die("Failed|Invalid or missing API login details");
    }
}

$check = $wpdb->get_row("SELECT * FROM {$wpdb->prefix}pbtransactions WHERE mpesa_code = '{$postMpesa_code}'");


if (!$check->id) {
    //$insert = mysql_query("
    //INSERT INTO {$wpdb->prefix}pbtransactions (id,orig,dest,tstamp,text,mpesa_code,mpesa_acc,mpesa_msisdn,mpesa_trx_date,mpesa_trx_time,mpesa_amt,mpesa_sender) VALUES ('{$postId}','{$postOrig}','{$postDest}','{$postTstamp}','{$postText}','{$postMpesa_code}','{$postMpesa_acc}','{$postMpesa_msisdn}','{$postMpesa_trx_date}','{$postMpesa_trx_time}','{$postMpesa_amt}','{$postMpesa_sender}')
    //");


    $insert = $wpdb->query($wpdb->prepare(
                    "
		INSERT INTO {$wpdb->prefix}pbtransactions
		( id,orig,dest,tstamp,text,mpesa_code,mpesa_acc,mpesa_msisdn,
		mpesa_trx_date,mpesa_trx_time,mpesa_amt,mpesa_sender )
		VALUES ( %d, %s, %s , %s, %s, %s, %s, %s, %s, %s, %d, %s)
	", array(
                $postId, $postOrig, $postDest, $postTstamp,
                $postText, $postMpesa_code, $postMpesa_acc,
                $postMpesa_msisdn, $postMpesa_trx_date,
                $postMpesa_trx_time,
                $postMpesa_amt, $postMpesa_sender
                    )
    ));



    if ($insert) {

        if (!empty($notify_email)) {

            $headers = "From: MPESA API - WOOCOMMERCE <no-reply@{$domain_name}>" . "\r\n";

            wp_mail($notify_email, "MPESA API Notification {$postMpesa_code}", $postText, $headers);
        }

        if (is_readable('woocommerce-kenpesa-hook_functions.php')) {
            include_once 'woocommerce-kenpesa-hook_functions.php';
            if (function_exists('after_success_mpesa')) {
                if ($debug) {
                    @ob_start();
                }
                after_success_mpesa();
                if ($debug) {
                    @ob_end_clean();
                }
            }
        }

        echo "OK|API Successful";
    } else {
        echo("Failed|An error occurred during save.");
    }
    //echo "OK|API Successful";
} else {
    echo("Failed|Record exists.");
}

function check_license($licensekey, $localkey = "") {
    $whmcsurl = "https://my.enet.co.ke/";

    $licensing_secret_key = "287811c077b90af0b013cddf47dcfd69"; # Unique value, should match what is set in the product configuration for MD5 Hash Verification
    $check_token = time() . md5(mt_rand(1000000000, 9999999999) . $licensekey);
    $checkdate = date("Ymd"); # Current date
    $usersip = isset($_SERVER['SERVER_ADDR']) ? $_SERVER['SERVER_ADDR'] : $_SERVER['LOCAL_ADDR'];
    $localkeydays = 2; # How long the local key is valid for in between remote checks
    $allowcheckfaildays = 3; # How many days to allow after local key expiry before blocking access if connection cannot be made
    $localkeyvalid = false;
    if ($localkey) {
        $localkey = str_replace("\n", '', $localkey); # Remove the line breaks
        $localdata = substr($localkey, 0, strlen($localkey) - 32); # Extract License Data
        $md5hash = substr($localkey, strlen($localkey) - 32); # Extract MD5 Hash
        if ($md5hash == md5($localdata . $licensing_secret_key)) {
            $localdata = strrev($localdata); # Reverse the string
            $md5hash = substr($localdata, 0, 32); # Extract MD5 Hash
            $localdata = substr($localdata, 32); # Extract License Data
            $localdata = base64_decode($localdata);
            $localkeyresults = unserialize($localdata);
            $originalcheckdate = $localkeyresults["checkdate"];
            if ($md5hash == md5($originalcheckdate . $licensing_secret_key)) {
                $localexpiry = date("Ymd", mktime(0, 0, 0, date("m"), date("d") - $localkeydays, date("Y")));
                if ($originalcheckdate > $localexpiry) {
                    $localkeyvalid = true;
                    $results = $localkeyresults;
                    $validdomains = explode(",", $results["validdomain"]);
                    if (!in_array($_SERVER['SERVER_NAME'], $validdomains)) {
                        $localkeyvalid = false;
                        $localkeyresults["status"] = "Invalid";
                        $results = array();
                    }
                    $validips = explode(",", $results["validip"]);
                    if (!in_array($usersip, $validips)) {
                        $localkeyvalid = false;
                        $localkeyresults["status"] = "Invalid";
                        $results = array();
                    }
                    if ($results["validdirectory"] != dirname(__FILE__)) {
                        $localkeyvalid = false;
                        $localkeyresults["status"] = "Invalid";
                        $results = array();
                    }
                }
            }
        }
    }
    if (!$localkeyvalid) {
        $postfields["licensekey"] = $licensekey;
        $postfields["domain"] = $_SERVER['SERVER_NAME'];
        $postfields["ip"] = $usersip;
        $postfields["dir"] = dirname(__FILE__);

        if ($check_token)
            $postfields["check_token"] = $check_token;
        if (function_exists("curl_exec")) {
            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, $whmcsurl . "modules/servers/licensing/verify.php");
            curl_setopt($ch, CURLOPT_POST, 1);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $postfields);
            curl_setopt($ch, CURLOPT_TIMEOUT, 30);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
            $data = curl_exec($ch);
            curl_close($ch);
        } else {
            $fp = fsockopen($whmcsurl, 80, $errno, $errstr, 5);
            if ($fp) {
                $querystring = "";
                foreach ($postfields AS $k => $v) {
                    $querystring .= "$k=" . urlencode($v) . "&";
                }
                $header = "POST " . $whmcsurl . "modules/servers/licensing/verify.php HTTP/1.0\r\n";
                $header .= "Host: " . $whmcsurl . "\r\n";
                $header .= "Content-type: application/x-www-form-urlencoded\r\n";
                $header .= "Content-length: " . @strlen($querystring) . "\r\n";
                $header .= "Connection: close\r\n\r\n";

                $header .= $querystring;
                $data = "";
                @stream_set_timeout($fp, 20);
                @fputs($fp, $header);
                $status = @socket_get_status($fp);
                while (!@feof($fp) && $status) {
                    $data .= @fgets($fp, 1024);
                    $status = @socket_get_status($fp);
                }
                @fclose($fp);
            }
        }
        if (!$data) {
            $localexpiry = date("Ymd", mktime(0, 0, 0, date("m"), date("d") - ($localkeydays + $allowcheckfaildays), date("Y")));
            if ($originalcheckdate > $localexpiry) {
                $results = $localkeyresults;
            } else {
                $results["status"] = "Invalid";
                $results["description"] = "Remote Check Failed";
                return $results;
            }
        } else {
            preg_match_all('/<(.*?)>([^<]+)<\/\\1>/i', $data, $matches);
            $results = array();
            foreach ($matches[1] AS $k => $v) {
                $results[$v] = $matches[2][$k];
            }
        }



        if ($results["md5hash"]) {
            if ($results["md5hash"] != md5($licensing_secret_key . $check_token)) {
                $results["status"] = "Invalid";
                $results["description"] = "MD5 Checksum Verification Failed";
                return $results;
            }
        }

        if ($results["status"] == "Active") {
            $results["checkdate"] = $checkdate;
            $data_encoded = serialize($results);
            $data_encoded = base64_encode($data_encoded);
            $data_encoded = md5($checkdate . $licensing_secret_key) . $data_encoded;
            $data_encoded = strrev($data_encoded);
            $data_encoded = $data_encoded . md5($data_encoded . $licensing_secret_key);
            $data_encoded = wordwrap($data_encoded, 80, "\n", true);
            $results["localkey"] = $data_encoded;
        }
        $results["remotecheck"] = true;
    }
    unset($postfields, $data, $matches, $whmcsurl, $licensing_secret_key, $checkdate, $usersip, $localkeydays, $allowcheckfaildays, $md5hash);
    return $results;
}

?>
