<?php

function print_pre($s) {
    echo "<pre>";
    print_r($s);
    echo "</pre>";
}

function autoload() {
    $dirs = scandir(WC_MPESA_PLUGIN_DIR . "/includes");

    unset($dirs[0], $dirs[1]);

    foreach ($dirs as $dir) {
        $class = WC_MPESA_PLUGIN_DIR . "includes/{$dir}/class.{$dir}.php";

//        echo "\$dir$dir<br/>";
        
        $parts = explode(".", $dir);
      
        if (count($parts) == 1 && file_exists($class)) {
            include $class;
        }
    }
}

function validate_token() {

    $token = filter_input(INPUT_GET, 'token', FILTER_SANITIZE_STRING);

    if ($token != md5(session_id())) {

        exit(
                json_encode(
                        array('errorMessage' => 'Session not valid, refresh the page and try again.')
                )
        );
    }
}
