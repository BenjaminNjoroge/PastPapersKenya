<?php

if(!function_exists('get_kenpesa_template')){
function get_kenpesa_template() {
    global $woocommerce;
    $mpesa = new kenpesapb();

    $id = ($_GET['id']) ? $_GET['id'] : '';
//    exit(wc_get_cart_url());

       
        
    $return = "";

    if (!empty($id)) {

        $order_id = base64_decode($id);

        $customer_order = new WC_Order($order_id);
        
//        $phone_number = '';

        //@$phone_number = $mpesa->fixphonenumber($customer_order->data['billing']['phone']);
        $phone_number = is_callable( array( $customer_order, 'get_billing_phone' ) ) ? $customer_order->get_billing_phone() : @$customer_order->data['billing']['phone'];
//        $order_key = is_callable( array( $customer_order, 'get_order_key' ) ) ? $customer_order->get_order_key() : @$customer_order->data['order_key'];
//        $order_total = is_callable( array( $customer_order, 'get_total' ) ) ? $customer_order->get_total() : @$customer_order->data['get_order_total'];
        
       
//        echo "<pre>";
//        print_r("\$order_total$order_total");
////        print_r($customer_order);
//        echo "</pre>";
//exit;
        $return .= <<<HTML
                <span id="result"></span>
            <form id="frmSTK" name="frmSTK">
<label for="phoneno"> {$mpesa->description} <br/> 
    <input type="text" id="phoneno" name="phoneno"  placeholder="254722xxxxxx"  maxlength="12" autocomplete="off" style="text-align:center;" required="" value="{$phone_number}"/></label>
<input type="hidden" id="order_id" name="order_id" placeholder="" value="{$order_id}" readonly="readonly" >
<input type="hidden" id="CheckoutRequestID" name="CheckoutRequestID"> <br/>
        <input type="button" class="button alt" id="btnRequest" value="Send Payment Request to phone" style="margin: 5px;"><input type="button" id="btnChange" class="button alt"  value="Change Payment Method" style="margin: 5px;" onclick="changePayMtd();">
       
    </form>
HTML;
    }
    return $return;
}
}
