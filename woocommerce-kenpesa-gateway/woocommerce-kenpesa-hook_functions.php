<?php

/*
  the function below will be executed after a successful insert of MPESA transaction into the database
  you can use it to carry functions like sending a confirmation SMS using your SMS Gateway
 */

function after_success_mpesa() {
    global $wpdb; //this wordpress db global variable;
//do not echo  or print_r - this will interfere with the response format that Kenpesa expect from your server

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

    $table_name = "{$wpdb->prefix}pbtransactions";

    /**
     * @todo add your custom code 
     * that will be executed after 
     * completion of a MPESA transaction 
     * in your account
     */
    //auto_create_order($postMpesa_acc, $postMpesa_code, $postMpesa_amt);
}

/**
 * allows clients to make orders without logging into your website
 * mpesa_acc = $SKU
 */
function auto_create_order($postMpesa_acc, $mpesa_code, $mpesa_amt) {
    global $wpdb;

    $product_id = get_product_by_sku($postMpesa_acc);

    if (!empty($product_id)) {

        $order = wc_create_order();

        $product = get_product($product_id);
        
        
        
        $order->add_product($product, 1,
                 array(
                                'subtotal'     => !empty($product->regular_price)?$product->regular_price:$mpesa_amt,
				'total'        => !empty($product->regular_price)?$product->regular_price:$mpesa_amt,
				'quantity'     => 1,
			)
                );

        // Set payment gateway
        $payment_gateways = WC()->payment_gateways->payment_gateways();
        $order->set_payment_method($payment_gateways['kenpesapb']);

        $order->calculate_totals();

//        $customer_order = new WC_Order($order->id);

        

        $kenpesapb_settings  = get_option("woocommerce_spyr_kenpesapb_settings");
        
        
        $order->update_status('wc-completed', 'Order created dynamically via MPESA. ', TRUE);

        $wpdb->update(
                $wpdb->prefix . 'pbtransactions', array(
            'status' => 'Closed',
            'invoiceid' => $order->id,
                ), array('mpesa_code' => $mpesa_code), array(
            '%s',
                )
        );
        $order->add_order_note(__('MPESA payment completed: ' . $mpesa_code, 'spyr-kenpesapb'));

        $order->payment_complete();
    }
}

function get_product_by_sku($sku) {

    global $wpdb;

    $product_id = $wpdb->get_var($wpdb->prepare("SELECT post_id FROM $wpdb->postmeta WHERE meta_key='_sku' AND meta_value='%s' LIMIT 1", $sku));



    return $product_id;
}

?>
