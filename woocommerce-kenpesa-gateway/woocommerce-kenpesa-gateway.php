<?php
/*
  Plugin Name: Kenpesa PB - WooCommerce Gateway
  Plugin URI: https://www.enet.co.ke/whmcs-mpesa-integration/
  Description: Extends WooCommerce by Adding the MPESA Gateway.
  Version: 2.6.0
  WC requires at least: 3.0
  WC tested up to: 5.6.0
  Author: Emmanuel Mbithi, Enet Online Solutions
  Author URI: https://www.enet.co.ke/whmcs-mpesa-integration/
 */

if (!defined('WC_MPESA_PLUGIN_DIR'))
    define('WC_MPESA_PLUGIN_DIR', plugin_dir_path(__FILE__));

include (WC_MPESA_PLUGIN_DIR . "/includes/functions.php");


autoload();

// Include our Gateway Class and register Payment Gateway with WooCommerce
add_action('plugins_loaded', 'kenpesapb_init', 0);

function kenpesapb_init() {
    // If the parent WC_Payment_Gateway class doesn't exist
    // it means WooCommerce is not installed on the site
    // so do nothing
    if (!class_exists('WC_Payment_Gateway'))
        return;

    // If we made it this far, then include our Gateway Class
    include_once( 'woocommerce-kenpesapb.php' );

    // Now that we have successfully included our class,
    // Lets add it too WooCommerce
    add_filter('woocommerce_payment_gateways', 'spyr_add_kenpesapb_gateway');

    function spyr_add_kenpesapb_gateway($methods) {
        $methods[] = 'kenpesapb';
        return $methods;
    }

}

// Add custom action links
add_filter('plugin_action_links_' . plugin_basename(__FILE__), 'kenpesapb_action_links');

function kenpesapb_action_links($links) {
    $plugin_links = array(
        '<a href="' . admin_url('admin.php?page=wc-settings&tab=checkout&section=spyr_kenpesapb') . '">' . __('Settings', 'spyr-kenpesapb') . '</a>',
    );

    // Merge our new link with the default ones
    return array_merge($plugin_links, $links);
}

function kenpesapb_install() {
    global $wpdb;

//    flushRules();
    flush_rewrite_rules();

    require_once( ABSPATH . 'wp-admin/includes/upgrade.php' );

    $table_name1 = $wpdb->prefix . "pbtransactions";
    $table_name2 = $wpdb->prefix . "mod_kenpesa";

    $sql1 = "CREATE TABLE IF NOT EXISTS {$table_name1} (
  id varchar(100) NOT NULL,
  orig varchar(20) NOT NULL,
  dest varchar(100) NOT NULL,
  tstamp TIMESTAMP NOT NULL,
  `text` varchar(255) NOT NULL,
  mpesa_code varchar(50) NOT NULL,
  mpesa_acc varchar(100) NOT NULL,
  mpesa_msisdn varchar(100) NOT NULL,
  mpesa_trx_date varchar(100) NOT NULL,
  mpesa_trx_time varchar(100) NOT NULL,
  mpesa_amt int(11) NOT NULL,
  mpesa_sender varchar(100) NOT NULL,
  `status` varchar(10) NOT NULL DEFAULT 'Open',
  invoiceid int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (id),
  UNIQUE KEY mpesa_code (mpesa_code)
)";


    $sql2 = "CREATE TABLE IF NOT EXISTS {$table_name2} (
id varchar(1) NOT NULL,
	  `localkey` text NULL
	)";


    dbDelta($sql1);
    dbDelta($sql2);
}

function kenpesapb_install_data() {
    global $wpdb;

    $localkey = '9tjIxIzNwgDMwIjI6gjOztjIlRXYkt2Ylh2YioTO6M3OicmbpNnblNWasx1cyVmdyV2ccNXZsVHZv1GX
zNWbodHXlNmc192czNWbodHXzN2bkRHacBFUNFEWcNHduVWb1N2bExFd0FWTcNnclNXVcpzQioDM4ozc
7ISey9GdjVmcpRGZpxWY2JiO0EjOztjIx4CMuAjL3ITMioTO6M3OiAXaklGbhZnI6cjOztjI0N3boxWY
j9Gbuc3d3xCdz9GasF2YvxmI6MjM6M3Oi4Wah12bkRWasFmdioTMxozc7ISeshGdu9WTiozN6M3OiUGb
jl3Yn5WasxWaiJiOyEjOztjI3ATL4ATL4ADMyIiOwEjOztjIlRXYkVWdkRHel5mI6ETM6M3OicDMtcDM
tgDMwIjI6ATM6M3OiUGdhR2ZlJnI6cjOztjIlNXYlxEI5xGa052bNByUD1ESXJiO5EjOztjIl1WYuR3Y
1R2byBnI6ETM6M3OicjI6EjOztjIklGdjVHZvJHcioTO6M3Oi02bj5ycj1Ga3BEd0FWbioDNxozc7ICb
pFWblJiO1ozc7IyUD1ESXBCd0FWTioDMxozc7ISZtFmbkVmclR3cpdWZyJiO0EjOztjIlZXa0NWQiojN
6M3OiMXd0FGdzJiO2ozc7pjMxoTY8baca0885830a33725148e94e693f3f073294c0558d38e31f844
c5e399e3c16a';

    $table_name = $wpdb->prefix . 'mod_kenpesa';

    $wpdb->insert(
            $table_name, array(
        'id' => 1,
        'localkey' => $localkey,
            )
    );
}

//checkout page
/* add_action('init', function() {
  add_rewrite_endpoint('mpesa', EP_ROOT);
  }); */



add_shortcode('mpesa_checkout', function() {
//    if ($order_id = get_query_var('mpesa')) {
    include_once( WC_MPESA_PLUGIN_DIR . 'includes/kenpesa-template.php' );
    //then stop processing
//        die();
//    }

    return get_kenpesa_template();
});

//ipn page
add_action('init', function() {
    add_rewrite_endpoint('wc_mpesa_ipn', EP_ROOT);
});

add_action('template_redirect', function() {
    if ($params = get_query_var('wc_mpesa_ipn')) {
        $kenpesapb = new kenpesapb();
        $kenpesapb->stk_confirm($params);
        die();
    }
});

function flushRules() {
    global $wp_rewrite;
//    $wp_rewrite->flush_rules();
}

register_activation_hook(__FILE__, 'kenpesapb_install');
register_activation_hook(__FILE__, 'kenpesapb_install_data');

function my_scripts() {
    wp_register_style('style.css', plugin_dir_url(__FILE__) . 'assets/css/style.css');
    wp_enqueue_style('style.css');

    wp_register_script('wc-mpesa.js', plugin_dir_url(__FILE__) . 'assets/js/wc-mpesa.js');
    wp_enqueue_script('wc-mpesa.js');
}

add_action('wp_enqueue_scripts', 'my_scripts', 9999);


add_action('wp_head', 'kenpesa_ajaxurl');

function kenpesa_ajaxurl() {

    echo '<script type="text/javascript">
           var kenpesa_ajaxurl = "' . admin_url('admin-ajax.php') . '";
         </script>';
}

function stk_request() {

    $kenpesapb = new kenpesapb();

    $BusinessShortCode = $kenpesapb->business_number;
    $LipaNaMpesaPasskey = $kenpesapb->lipa_na_mpesa_pass_key;
    $TransactionType = $kenpesapb->transactiontype;
    $app_consumer_key = $kenpesapb->consumer_key;
    $app_consumer_secret = $kenpesapb->consumer_secret;
    $till_number = $kenpesapb->till_number;
    $environment = $kenpesapb->environment;

    $PartyA = filter_input(INPUT_POST, 'phoneno', FILTER_SANITIZE_STRING);
    $order_id = filter_input(INPUT_POST, 'order_id', FILTER_SANITIZE_STRING);

//    $order = wc_get_order($order_id);
    $customer_order = new WC_Order($order_id);
    $order_key = is_callable(array($customer_order, 'get_order_key')) ? $customer_order->get_order_key() : @$customer_order->data['order_key'];
    $order_total = is_callable(array($customer_order, 'get_total')) ? $customer_order->get_total() : @$customer_order->data['get_order_total'];

//    echo "<pre>";
//    print_r($order->total);
//    echo "</pre>";
//    exit("\$order_id$order_id");

    $Amount = $order_total * 1;
    $PartyB = $TransactionType == "CustomerPayBillOnline" ? $BusinessShortCode : $till_number;
    $PhoneNumber = $PartyA;

    $AccountReference = $order_id;
    $TransactionDesc = $Remark = "Order #" . $order_id;

//    $_give_payment_purchase_key = give_get_meta($donation_id, '_give_payment_purchase_key', true);

    $CallBackURL = get_site_url() . "/wc_mpesa_ipn/{$order_id}/{$order_key}";
//    exit($CallBackURL);
    if (empty($PartyA)) {
        echo (
        json_encode(
                array(
                    'errorMessage' => 'Please enter your phonenumber',
                )
        )
        );

        wp_die();
    }

//    delete_post_meta($order_id, 'kenpesawc_stkrequest_response');
//    delete_post_meta($order_id, '_wc_stkresult_response');
//    exit("\$order_id$order_id");

    $mpesa = new mpesa($app_consumer_key, $app_consumer_secret, $TransactionType, $environment);
    $mpesa->GenerateToken();
    $stk = $mpesa->STKPushSimulation($BusinessShortCode, $LipaNaMpesaPasskey, $TransactionType, $Amount, $PartyA, $PartyB, $PhoneNumber, $CallBackURL, $AccountReference, $TransactionDesc, $Remark);

    add_post_meta($order_id, '_wc_stkrequest_response', $stk);

    $mpesa_post_id = wp_insert_post(
            array(
                'post_title' => "Payment Request for Order #{$order_id}",
//                    'post_content' => "Response: " . json_encode($purchase_data),
                'post_status' => isset(json_decode($stk)->errorCode) ? 'kenpesa_failed' : 'kenpesa_pending',
                'post_type' => 'kenpesawc_mpesa',
                'post_author' => 1,
            )
    );

    delete_post_meta($order_id, 'kenpesawc_mpesa_post_id');

//    add_post_meta($order_id, 'kenpesawc_stkrequest_response', $stk);
    add_post_meta($order_id, 'kenpesawc_mpesa_post_id', $mpesa_post_id);

    add_post_meta($mpesa_post_id, 'kenpesawc_stkrequest_response', $stk);
    add_post_meta($mpesa_post_id, 'kenpesawc_Order_No', $order_id);

    header('Content-Type: application/json');
    die($stk); // this is required to terminate immediately and return a proper response
}

add_action('wp_ajax_stk_request', 'stk_request');
add_action('wp_ajax_nopriv_stk_request', 'stk_request');

function check_stk() {
    global $woocommerce;


    $order_id = filter_input(INPUT_POST, 'order_id', FILTER_SANITIZE_STRING);

    $mpesa_post_id = get_post_meta($order_id, 'kenpesawc_mpesa_post_id', true);

    $stk_result = get_post_meta($mpesa_post_id, 'kenpesawc_stk_ipn_response', true);
    $stk_request_response = get_post_meta($mpesa_post_id, 'kenpesawc_stkrequest_response', true);

//    print_pre($_SESSION);
//    $stk_result = "";
//exit("\$stk_result$stk_result\$order_id$order_id");
//exit("\$mpesa_post_id$mpesa_post_id");
//exit("\$stk_result$stk_result");
//exit("\$stk_request_response$stk_request_response");

    $results = array();

    $kenpesapb = new kenpesapb();
//     print_pre($kenpesapb);exit;

      $customer_order = new WC_Order($order_id);
      
    if ($kenpesapb->settings['transactiontype'] == 'KopokopoConnect') {

        $decoded_response = json_decode($stk_request_response);

//        print_pre($decoded_response);

        $mpesa = new mpesa($kenpesapb->settings['consumer_key'], $kenpesapb->settings['consumer_secret'], $kenpesapb->settings['transactiontype'], $kenpesapb->settings['environment']);
        $response = $mpesa->STKPushQuery_K2($decoded_response->location);

        $decoded_result = json_decode($response);

        if (isset($decoded_result->data->attributes->status) && $decoded_result->data->attributes->status == 'Success') {
          
//          print_pre($decoded_result);  
            $kenpesapb = new kenpesapb();

            $woocommerce->cart->empty_cart();
            
            $results['ResultCode'] = 0;
            $results['ResultDesc'] = "Payment has been processed successfully via Kopokopo.";
            $results['redirect'] = $kenpesapb->get_return_url($customer_order);
        } elseif ($decoded_result->data->attributes->status == 'Failed') {
            
            add_post_meta($mpesa_post_id, 'kenpesawc_stk_ipn_response', $response);
            
            $customer_order->add_order_note(__($decoded_result->data->attributes->event->errors, 'spyr-kenpesapb'));

            
            $results['ResultCode'] = 1;
            $results['ResultDesc'] = $decoded_result->data->attributes->event->errors;
        }

//        print_pre($decoded_result->data->attributes->status);
//        print_pre($decoded_result->data->attributes->event->errors);
//        print_pre($decoded_result);
//        exit("xxx");
    } else {

        if (!empty($stk_result)) {
            $decoded_result = json_decode($stk_result);
//exit($decoded_result->Body->stkCallback->ResultCode);


            $results = array(
                'ResultCode' => $decoded_result->Body->stkCallback->ResultCode,
                'ResultDesc' => $decoded_result->Body->stkCallback->ResultDesc,
            );

            if ($decoded_result->Body->stkCallback->ResultCode == 0) {
                $customer_order = new WC_Order($order_id);
//          print_pre($decoded_result);  
                $kenpesapb = new kenpesapb();

                $woocommerce->cart->empty_cart();

                $results['redirect'] = $kenpesapb->get_return_url($customer_order);
            }
        }
    }

    header('Content-Type: application/json');
    die(json_encode($results));
}

add_action('wp_ajax_check_stk', 'check_stk');
add_action('wp_ajax_nopriv_check_stk', 'check_stk');


/* * mpesa post * */

add_action('init', function () {
    register_post_type('kenpesawc_mpesa',
            array(
                'labels' => array(
                    'name' => __('M-PESA Payments', 'kenpesawc_mps'),
                    'singular_name' => __('M-PESA Payment', 'kenpesawc_mps'),
                    'menu_name' => __('M-PESA', 'kenpesawc_mps'),
                    'name_admin_bar' => __('M-PESA Payment', 'kenpesawc_mps'),
                    'archives' => __('M-PESA Payment Archives', 'kenpesawc_mps'),
                    'attributes' => __('M-PESA Payment Attributes', 'kenpesawc_mps'),
                    'parent_item_colon' => __('Parent M-PESA Payment:', 'kenpesawc_mps'),
                    'all_items' => __('M-PESA Payments', 'kenpesawc_mps'),
                    'add_new_item' => __('Record New Manual M-PESA Payment', 'kenpesawc_mps'),
                    'add_new' => __('Add M-PESA Payment', 'kenpesawc_mps'),
                    'new_item' => __('New Manual M-PESA', 'kenpesawc_mps'),
                    'edit_item' => __('Edit M-PESA Payment', 'kenpesawc_mps'),
                    'update_item' => __('Update M-PESA Payment', 'kenpesawc_mps'),
                    'view_item' => __('View M-PESA Payment', 'kenpesawc_mps'),
                    'view_items' => __('View M-PESA Payments', 'kenpesawc_mps'),
                    'search_items' => __('Search M-PESA Payments', 'kenpesawc_mps'),
                    'not_found' => __('Not found', 'kenpesawc_mps'),
                    'not_found_in_trash' => __('Not found in Trash', 'kenpesawc_mps'),
                    'items_list' => __('M-PESA Payments list', 'kenpesawc_mps'),
                    'items_list_navigation' => __('M-PESA Payments list navigation', 'kenpesawc_mps'),
                    'filter_items_list' => __('Filter payments list', 'kenpesawc_mps'),
                ),
                'public' => false,
                'show_ui' => true,
                'show_in_menu' => true,
                'exclude_from_search' => false,
                'has_archive' => true,
                'rewrite' => array('slug' => 'kenpesawc_mpesa'),
                'show_in_rest' => true,
//                'supports' => array('title','author','comments'),
                'supports' => array('title'),
                'menu_icon' => plugins_url('woocommerce-kenpesa-gateway/assets/images/mpesa.png'),
            )
    );
});


add_action('add_meta_boxes', function () {
    add_meta_box(
            'kenpesawc_mpesa_trx', 'M-PESA Details', 'kenpesawc_mpesa_posts_metaboxes_html', 'kenpesawc_mpesa'
    );
});

function kenpesawc_mpesa_posts_metaboxes_html($post) {
    $MpesaReceiptNumber = get_post_meta($post->ID, 'kenpesawc_MpesaReceiptNumber', true);
    $Amount = get_post_meta($post->ID, 'kenpesawc_Amount', true);
    $PhoneNumber = get_post_meta($post->ID, 'kenpesawc_PhoneNumber', true);
    $Order_No = get_post_meta($post->ID, 'kenpesawc_Order_No', true);
    $request = get_post_meta($post->ID, 'kenpesawc_stkrequest_response', true);
    $ipn_response = get_post_meta($post->ID, 'kenpesawc_stk_ipn_response', true);

    $pages = array();
    ?>
    <input type="hidden" name="save_meta">
    <table width="100%">
        <tr>
            <th style="text-align: left; width: 30%"><label for="kenpesawc_MpesaReceiptNumber">M-PESA Receipt Number</label></th>
            <td><?php echo ' <input type="text" name="kenpesawc_MpesaReceiptNumber" value="' . esc_attr($MpesaReceiptNumber) . ' " />'; ?></td>
        </tr>
        <tr>
            <th style="text-align: left"><label for="kenpesawc_Amount">Amount</label></th>
            <td> <?php echo ' <input type="text" name="kenpesawc_Amount" value="' . esc_attr($Amount) . ' " />'; ?></td>
        </tr>
        <tr>
            <th style="text-align: left"><label for="kenpesawc_PhoneNumber">Phone Number</label></th>
            <td> <?php echo ' <input type="text" name="kenpesawc_PhoneNumber" value="' . esc_attr($PhoneNumber) . ' " />'; ?></td>
        </tr>
        <tr>
            <th style="text-align: left"><label for="kenpesawc_Order_No">Order No</label></th>
            <td> <?php echo ' <input type="text" name="kenpesawc_Order_No" value="' . esc_attr($Order_No) . ' " />'; ?> <a href="post.php?post=<?= esc_attr($Order_No); ?>&action=edit">View Order</a></td>
        </tr>

    <?php if (!empty($request)) { ?>
            <tr>
                <th style="text-align: left"><label for="stk_req">STK Request</label></th>
                <td> <?php echo '<textarea name="stk_req" style="width:100%" rows="8" readonly>' . $request . '</textarea>'; ?></td>
            </tr>
    <?php } ?>

        <?php if (!empty($ipn_response)) { ?>
            <tr>
                <th style="text-align: left"><label for="ipn">IPN Results</label></th>
                <td> <?php echo '<textarea name="ipn" style="width:100%" rows="8" readonly>' . $ipn_response . '</textarea>'; ?></td>
            </tr>
    <?php } ?>

    </table>




    <?php
}

add_action('save_post', function ($post_id) {
    if (isset($_POST['save_meta'])) {
        $MpesaReceiptNumber = trim($_POST['kenpesawc_MpesaReceiptNumber']);
        $Amount = strip_tags(trim($_POST['kenpesawc_Amount']));
        $Order_No = strip_tags(trim($_POST['kenpesawc_Order_No']));

        update_post_meta($post_id, 'kenpesawc_MpesaReceiptNumber', $MpesaReceiptNumber);
        update_post_meta($post_id, 'kenpesawc_Amount', $Amount);
        update_post_meta($post_id, 'kenpesawc_Order_No', $Order_No);

//        kenpesawc_update_payment_status($Order_No, 'publish');
//        kenpesawc_set_payment_transaction_id($Order_No, sanitize_text_field($MpesaReceiptNumber));
    }
});



add_filter('manage_kenpesawc_mpesa_posts_columns', 'kenpesawc_mpesa_posts_columns_heads', 10);
add_filter('manage_kenpesawc_mpesa_posts_custom_column', 'kenpesawc_mpesa_posts_columns_data', 10, 2);

function kenpesawc_mpesa_posts_columns_heads($columns) {
    return array_merge($columns, array(
        'MpesaReceiptNumber' => __('M-Pesa Trx.'),
        'Amount' => __('Amount'),
        'Request' => __('Request'),
        'Response' => __('Response'),
        'Order_No' => __('WC Order No')
            )
    );
}

function kenpesawc_mpesa_posts_columns_data($column_name, $post_ID) {
//    echo "|$column_name|";
    if ($column_name == 'Amount') {
        $amount = (int) get_post_meta($post_ID, 'kenpesawc_' . $column_name, 1);
        echo "KES " . number_format($amount, 2);
    } elseif ($column_name == 'MpesaReceiptNumber') {
        echo get_post_meta($post_ID, 'kenpesawc_' . $column_name, 1);
    } elseif ($column_name == 'Request') {
        $response = get_post_meta($post_ID, 'kenpesawc_stkrequest_response', 1);
        if (!empty($response)) {
            $decoded = json_decode($response);
            
           

            if (isset($decoded->ResponseCode)) {
                echo $decoded->ResponseDescription;
            }

            if (isset($decoded->errorCode)) {
                echo $decoded->errorMessage;
            }
        }
    } elseif ($column_name == 'Response') {
        $response = get_post_meta($post_ID, 'kenpesawc_stk_ipn_response', 1);
        $decoded_response = json_decode($response);
        if (!isset($decoded_response->Body->stkCallback->ResultDesc)) {
            echo $decoded_response->Body->stkCallback->ResultDesc;
        }
        
        
        if(isset($decoded_response->data->attributes->event->errors)){
            echo $decoded_response->data->attributes->event->errors;
        }
        
    } elseif ($column_name == 'Order_No') {
        $order_no = get_post_meta($post_ID, 'kenpesawc_' . $column_name, 1);
        echo "<a href=\"post.php?post={$order_no}&action=edit\">{$order_no}</a>";
    } else {
        echo get_post_meta($post_ID, 'kenpesawc_' . $column_name, 1);
    }
}

/* * custom status * */

$post_status = array(
    'kenpesa_pending' => 'Pending Confirmation',
    'kenpesa_failed' => 'Failed',
    'kenpesa_success' => 'Successful',
);

function kenpesawc_mpesa_custom_status_creation() {
    global $post_status;
    foreach ($post_status as $status_key => $status_label) {
        register_post_status($status_key, array(
            'label' => _x($status_label, 'post'),
            'label_count' => _n_noop($status_label . ' <span class="count">(%s)</span>', $status_label . ' <span 
class="count">(%s)</span>'),
            'public' => false,
            'protected' => true,
            'exclude_from_search' => false,
            'show_in_admin_all_list' => true,
            'show_in_admin_status_list' => true
        ));
    }
}

add_action('init', 'kenpesawc_mpesa_custom_status_creation');

function kenpesawc_mpesa_add_to_post_status_dropdown() {
    global $post, $post_status;

    $scripts = "";


    foreach ($post_status as $status_key => $status_label) {
        if ($post->post_type != 'kenpesawc_mpesa') {
            return false;
        }
        $status = ($post->post_status == $status_key) ? "jQuery( '#post-status-display' ).text( '" . $status_label . "' );
jQuery( 'select[name=\"post_status\"]' ).val('" . $status_key . "');" : '';
        $scripts = "
jQuery(document).ready( function() {
jQuery( 'select[name=\"post_status\"]' ).append( '<option value=\"{$status_key}\">" . $status_label . "</option>' );
" . $status . "
});
";
    }

    if (!empty($scripts)) {
        echo '<script>';
        echo $scripts;
        echo '</script>';
    }
}

add_action('post_submitbox_misc_actions', 'kenpesawc_mpesa_add_to_post_status_dropdown');

function kenpesawc_mpesa_custom_status_add_in_quick_edit() {
    global $post, $post_status;
    echo '<script>';
    foreach ($post_status as $status_key => $status_label) {
        if ($post->post_type != 'kenpesawc_mpesa')
            return false;
        echo "
jQuery(document).ready( function() {
jQuery( 'select[name=\"_status\"]' ).append( '<option value=\"{$status_key}\">{$status_label}</option>' );
});
";
    }

    echo '</script>';
}

add_action('admin_footer-edit.php', 'kenpesawc_mpesa_custom_status_add_in_quick_edit');

function kenpesapb_gateway_icon($gateways) {
    if (isset($gateways['spyr_kenpesapb'])) {
        $gateways['spyr_kenpesapb']->icon = plugins_url('woocommerce-kenpesa-gateway/assets/images/lipa-na-mpesa.png');
    }

    return $gateways;
}

add_filter('woocommerce_available_payment_gateways', 'kenpesapb_gateway_icon');
