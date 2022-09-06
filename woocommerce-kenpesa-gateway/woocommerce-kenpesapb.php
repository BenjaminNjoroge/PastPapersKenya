<?php

//ini_set("display_errors", 1);
/* Kenpesa PB Payment Gateway Class */

class kenpesapb extends WC_Payment_Gateway {

    private $check_stk_push_status_only = false;
    private $stk_response;
    private $stk_check_response = true;
    private $timestamp;
    private $message;
    private $base_url;

    // Setup our Gateway's id, description and other values
    function __construct() {

        $this->timestamp = date("YmdHis");
        // The global ID for this Payment method
        $this->id = "spyr_kenpesapb";

        // The Title shown on the top of the Payment Gateways Page next to all the other Payment Gateways
        $this->method_title = __("Kenpesa PB", 'spyr-kenpesapb');

        // The description for this Payment Gateway, shown on the actual Payment options page on the backend
        $this->method_description = __("Kenpesa PB Payment Gateway Plug-in for WooCommerce", 'spyr-kenpesapb');

        // The title to be used for the vertical tabs that can be ordered top to bottom
        $this->title = __("Kenpesa PB", 'spyr-kenpesapb');

        // If you want to show an image next to the gateway's name on the frontend, enter a URL to an image.
        $this->icon = null;

        // Bool. Can be set to true if you want payment fields to show on the checkout 
        // if doing a direct integration, which we are doing in this case
        $this->has_fields = true;

        // Supports the default credit card form
        //$this->supports = array( 'default_credit_card_form' );
        // This basically defines your settings which are then loaded with init_settings()
        $this->init_form_fields();

        // After init_settings() is called, you can get the settings and load them into variables, e.g:
        // $this->title = $this->get_option( 'title' );
        $this->init_settings();

        // Turn these settings into variables we can use
        foreach ($this->settings as $setting_key => $value) {
            $this->$setting_key = $value;
        }

        if ($this->transactiontype == 'KopokopoConnect') {
            $this->base_url = "https://{$this->environment}.kopokopo.com";
        } else {
            $this->base_url = "https://{$this->environment}.safaricom.co.ke";
        }
        // Lets check for SSL
        add_action('admin_notices', array($this, 'do_ssl_check'));

        // Save settings
        if (is_admin()) {
            // Versions over 2.0
            // Save our administration options. Since we are not going to be doing anything special
            // we have not defined 'process_admin_options' in this class so the method in the parent
            // class will be used instead
            add_action('woocommerce_update_options_payment_gateways_' . $this->id, array($this, 'process_admin_options'));
        }

        /*  if (!empty($this->consumer_key) && !empty($this->consumer_secret)) {
          $this->order_button_text = 'Complete Payment';
          } else {
          $this->order_button_text = 'Verify Payment and Place Order';
          }
         */

//        $this->order_button_text = "";

        $this->order_button_text = 'Proceed to Payment';

        $this->countries = array('KE');
    }

// End __construct()
    // Build the administration fields for this specific Gateway
    public function init_form_fields() {


        $pages = array();

        foreach (get_pages() as $page) {
            $pages[$page->ID] = $page->post_title;
        }

        $this->form_fields = array(
            'enabled' => array(
                'title' => __('Enable / Disable', 'spyr-kenpesapb'),
                'label' => __('Enable this payment gateway', 'spyr-kenpesapb'),
                'type' => 'checkbox',
                'default' => 'no',
            ),
            'title' => array(
                'title' => __('Title', 'spyr-kenpesapb'),
                'type' => 'text',
                'desc_tip' => __('Payment title the customer will see during the checkout process.', 'spyr-kenpesapb'),
                'default' => __('MPESA', 'spyr-kenpesapb'),
            ),
            'transactiontype' => array(
                'title' => __('Account Type', 'spyr-kenpesapb'),
                'type' => 'select',
                'options' => array(
                    'CustomerPayBillOnline' => 'Paybill',
                    'CustomerBuyGoodsOnline' => 'Buy Good & Services',
                    'KopokopoConnect' => 'Kopokopo Connect',
                ),
                'desc_tip' => __('This will be used by the MPESA STK Push', 'spyr-kenpesapb'),
                'class' => 'wc-enhanced-select',
            ),
            'business_number' => array(
                'title' => __('Paybill / Store Number / K2 Shortcode', 'spyr-kenpesapb'),
                'type' => 'text',
                'desc_tip' => __('Short code is the Paybill or the store number.', 'spyr-kenpesapb'),
            ),
            'till_number' => array(
                'title' => __('Till Number', 'spyr-kenpesapb'),
                'type' => 'text',
                'desc_tip' => __('Leave Blank for Paybill', 'spyr-kenpesapb'),
            ),
            'default_account_no' => array(
                'title' => __('Account No', 'spyr-kenpesapb'),
                'type' => 'select',
                'options' => array('na' => 'Not Applicable (Till Numbers)', 'order' => 'Order No', 'order_encoded' => 'Encoded Order No'),
                'desc_tip' => __('Specify the default order status for all orders paid via MPESA', 'spyr-kenpesapb'),
                'class' => 'wc-enhanced-select',
            ),
            'shortdesc' => array(
                'title' => __('Short Description', 'spyr-kenpesapb'),
                'type' => 'textarea',
                'desc_tip' => __('This will appear just below the gateway name in the checkout page. HTML Allowed.', 'spyr-kenpesapb'),
                'default' => __('Lipa Na M-PESA', 'spyr-kenpesapb'),
            ),
            'description' => array(
                'title' => __('Payment Instructions', 'spyr-kenpesapb'),
                'type' => 'textarea',
                'desc_tip' => __('Payment description the customer will at the payment page. To add account for Pay bill use [ACCOUNTNO]. HTML Allowed.', 'spyr-kenpesapb'),
                'default' => __('<ul style="text-align:left;">
<li>Please confirm your phone number below, then click on "SEND PAYMENT REQUEST TO PHONE". </li>
<li>Check your phone number for the M-PESA PIN prompt.</li>
<li>Key in your M-PESA PIN and submit</li>
<li>Wait for this page to refresh to move you to the next page</li>
</ul>', 'spyr-kenpesapb'),
            ),
            /* 'enable_verification_input_box' => array(
              'title' => __('Verification Box'),
              'label' => __('Show an input box to enter MPESA Transaction code at checkout. (Disable this if your wish to use Express Checkout)', 'spyr-kenpesapb'),
              'type' => 'checkbox',
              'desc_tip' => __('If disabled, the gateway will check if there are MPESA payments received from the clients phone number', 'spyr-kenpesapb'),
              'default' => 'no'
              ),
              'enable_verification_label' => array(
              'title' => __('Verification Label'),
              'type' => 'text',
              'desc_tip' => __('If verification input box is enabled, specify the label', 'spyr-kenpesapb'),
              'default' => 'Enter the MPESA Confirmation Code received from MPESA. If you made more than one transaction list the code separated with commas; e.g. LCR4HCBJS2, LCR4HCBJS3'
              ), */
            'license' => array(
                'title' => __('License Key', 'spyr-kenpesapb'),
                'type' => 'text',
                'desc_tip' => __('You need to get this from www.enet.co.ke', 'spyr-kenpesapb'),
            ),
            /* 'api_login' => array(
              'title' => __('API Login', 'spyr-kenpesapb'),
              'type' => 'text',
              'desc_tip' => __('', 'spyr-kenpesapb'),
              ),
              'trans_key' => array(
              'title' => __('API Password', 'spyr-kenpesapb'),
              'type' => 'password',
              'desc_tip' => __('', 'spyr-kenpesapb'),
              ), */
            'consumer_key' => array(
                'title' => __('Consumer Key / K2 Client ID', 'spyr-kenpesapb'),
                'type' => 'text',
                'desc_tip' => __('', 'spyr-kenpesapb'),
            ),
            'consumer_secret' => array(
                'title' => __('Consumer Secret / K2 Secret', 'spyr-kenpesapb'),
                'type' => 'text',
                'desc_tip' => __('', 'spyr-kenpesapb'),
            ),
            /* 'api_key' => array(
              'title' => __('K2 API Key', 'spyr-kenpesapb'),
              'type' => 'text',
              'desc_tip' => __('', 'spyr-kenpesapb'),
              ), */
            'lipa_na_mpesa_pass_key' => array(
                'title' => __('Lipa na MPESA Pass Key / K2 API Key', 'spyr-kenpesapb'),
                'type' => 'text',
                'desc_tip' => __('', 'spyr-kenpesapb'),
            ),
            'kenpesa_checkout_page' => array(
                'title' => __('M-PESA Checkout Page', 'spyr-kenpesapb'),
                'type' => 'select',
                'options' => $pages,
                'desc_tip' => __('Create a page with the shortcode [mpesa_checkout] and set it here.', 'spyr-kenpesapb'),
                'class' => 'wc-enhanced-select',
            ),
            'defualt_order_status' => array(
                'title' => __('Default order status', 'spyr-kenpesapb'),
                'type' => 'select',
                'options' => wc_get_order_statuses(),
                'desc_tip' => __('Specify the default order status for all orders paid via MPESA', 'spyr-kenpesapb'),
                'class' => 'wc-enhanced-select',
            ),
            /*  'notify_email' => array(
              'title' => __('Notify email', 'spyr-kenpesapb'),
              'type' => 'text',
              'desc_tip' => __('Notify email for every MPESA Transaction. Leave blank not to send', 'spyr-kenpesapb'),
              'default' => __('', 'spyr-kenpesapb'),
              ) */
            'environment' => array(
                'title' => __('Environment', 'spyr-kenpesapb'),
                'type' => 'select',
                'options' => array(
                    'sandbox' => 'Sandbox / Testing',
                    'api' => 'Live / Production',
                ),
                'desc_tip' => __('This will be used by the MPESA STK Push', 'spyr-kenpesapb'),
                'class' => 'wc-enhanced-select',
            ),
        );
    }

    public function print_pre($s) {
        echo "<pre>";
        print_r($s);
        echo "</pre>";
    }

    // Submit payment and handle response

    public function process_payment($order_id) {

        $mpesa = new kenpesapb();

        $page_url = get_permalink($mpesa->kenpesa_checkout_page);

//        exit($page_url);

        return array(
            'result' => 'success',
            'redirect' => $page_url . "?id=" . base64_encode($order_id),
//            'redirect' => get_site_url() . "/mpesa/" . base64_encode($order_id),
        );
    }

    public function validate_currency() {
        if (isset($_COOKIE['wmc_current_currency'])) {
            $current_currency = $_COOKIE['wmc_current_currency'];
        } else {
            $current_currency = get_option('woocommerce_currency');
        }

        if ($current_currency != 'KES') {

            $message = "Your currency is <b>{$current_currency}</b>, which is not supported by MPESA. Please check your currency to Kenya Shillings";

            throw new Exception(__($message, 'spyr-kenpesapb'));
        }
    }

    public function process_payment_online_checkout($order_id, $customer_order) {


        $this->send($order_id, $customer_order);

        sleep(30); //




        $time = array();

        $this->enable_verification_input_box = 'no';

        for ($i = 1; $i <= 12; $i++) {
            $time[] = date("Y-m-d H:i:s");

            if ($this->check_stk_push_status_only) {
                $this->checkSTK($customer_order);
                if (isset($this->stk_check_response->ResultCode) && $this->stk_check_response->ResultCode == 0) {
                    return;
                } elseif (isset($this->stk_check_response->ResultCode) && $this->stk_check_response->ResultCode == 1032) {
                    $customer_order->add_order_note(__($this->message, 'spyr-kenpesapb'));
                    throw new Exception(__("You cancalled the request from your phone.", 'spyr-kenpesapb'));
                }
            } else {
                if ($this->process_payment_verify($order_id, $customer_order) == true) {
                    return;
                }
            }
            sleep(5);
        }

        $message = "We haven't received the payment yet. Please try again";

        throw new Exception(__($message, 'spyr-kenpesapb'));
    }

    public function GenerateToken() {

        $url = 'https://api.safaricom.co.ke/oauth/v1/generate?grant_type=client_credentials';

//        echo "$url";

        $curl = curl_init();
        curl_setopt($curl, CURLOPT_URL, $url);
        curl_setopt($curl, CURLOPT_HTTPHEADER, array('Authorization: Basic ' . base64_encode("{$this->consumer_key}:{$this->consumer_secret}"))); //setting a custom header
        curl_setopt($curl, CURLOPT_HEADER, false);
        curl_setopt($curl, CURLOPT_SSL_VERIFYPEER, false);
        curl_setopt($curl, CURLOPT_RETURNTRANSFER, 1);

        $curl_response = curl_exec($curl);

        $data = json_decode($curl_response);


        if (is_object($data) && count($data) && !empty($data->access_token)) {
            return $data->access_token;
        } else {
            return "";
        }
    }

    public function send($order_id, $customer_order) {
        $access_token = $this->GenerateToken();

        if (!empty($access_token)) {

            $order_total = $customer_order->get_total();

//            $this->print_pre($customer_order);
//            exit();
//        if (isset($this->request->post['orderid'])) {
            $url = 'https://api.safaricom.co.ke/mpesa/stkpush/v1/processrequest';


            $BusinessShortCode = $this->business_number;
            $LipaNaMpesaPasskey = $this->lipa_na_mpesa_pass_key;
            $TransactionType = $this->transactiontype;
            ;
            $PartyA = $this->fixphonenumber($customer_order->get_billing_phone());
            $PartyB = $this->transactiontype == "CustomerPayBillOnline" ? $BusinessShortCode : $this->till_number;
            $PhoneNumber = $PartyA;
            $Amount = ceil($order_total) * 1;
            $AccountReference = $order_id;
            $TransactionDesc = "Order {$AccountReference}";
            $Remark = "Order {$AccountReference}";

            $CallBackURL = "https://my.enet.co.ke/test";

            $access_token = $this->GenerateToken();

//            exit("\$Amount$Amount");

            $password = base64_encode($BusinessShortCode . $LipaNaMpesaPasskey . $this->timestamp);

            $curl = curl_init();
//        echo "\$url$url";

            curl_setopt($curl, CURLOPT_URL, $url);
            curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type:application/json', 'Authorization:Bearer ' . $access_token));


            $curl_post_data = array(
                'BusinessShortCode' => $BusinessShortCode,
                'Password' => $password,
                'Timestamp' => $this->timestamp,
                'TransactionType' => $TransactionType,
                'Amount' => $Amount,
                'PartyA' => $PartyA,
                'PartyB' => $PartyB,
                'PhoneNumber' => $PhoneNumber,
                'CallBackURL' => $CallBackURL,
                'AccountReference' => $AccountReference,
                'TransactionDesc' => $TransactionDesc,
                'Remark' => $Remark
            );

//            $this->print_pre($curl_post_data);
//            exit();

            $data_string = json_encode($curl_post_data);

            curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($curl, CURLOPT_POST, true);
            curl_setopt($curl, CURLOPT_POSTFIELDS, $data_string);
            curl_setopt($curl, CURLOPT_HEADER, false);
            $curl_response = curl_exec($curl);

            $httpcode = curl_getinfo($curl, CURLINFO_HTTP_CODE);

            curl_close($curl);

            $this->stk_response = json_decode($curl_response);

            $customer_order->add_order_note(__('MPESA STK Push ID: ' . $this->stk_response->CheckoutRequestID, 'spyr-kenpesapb'));
            $customer_order->add_order_note(__('MPESA STK Response: ' . $this->stk_response->ResponseDescription, 'spyr-kenpesapb'));



//            echo 'HTTP code: ' . $httpcode;
//
//            echo $curl_response;
//
//            echo "<pre>";
//            print_r($curl_response);
//            echo "</pre>";
//            echo $curl_response;
//        }
        }
    }

    public function checkSTK($customer_order) {
        global $woocommerce;

        $access_token = $this->GenerateToken();

        if (!empty($access_token)) {

            $url = 'https://api.safaricom.co.ke/mpesa/stkpushquery/v1/query';


            $BusinessShortCode = $this->business_number;
            $LipaNaMpesaPasskey = $this->lipa_na_mpesa_pass_key;

            $access_token = $this->GenerateToken();

//            exit("\$Amount$Amount");

            $password = base64_encode($BusinessShortCode . $LipaNaMpesaPasskey . $this->timestamp);

            $curl = curl_init();
//        echo "\$url$url";

            curl_setopt($curl, CURLOPT_URL, $url);
            curl_setopt($curl, CURLOPT_HTTPHEADER, array('Content-Type:application/json', 'Authorization:Bearer ' . $access_token));


            $curl_post_data = array(
                'BusinessShortCode' => $this->business_number,
                'CheckoutRequestID' => $this->stk_response->CheckoutRequestID,
                'Password' => $password,
                'Timestamp' => $this->timestamp,
            );

//            $this->print_pre($curl_post_data);
//            exit();

            $data_string = json_encode($curl_post_data);

            curl_setopt($curl, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($curl, CURLOPT_POST, true);
            curl_setopt($curl, CURLOPT_POSTFIELDS, $data_string);
            curl_setopt($curl, CURLOPT_HEADER, false);
            $curl_response = curl_exec($curl);

//            $httpcode = curl_getinfo($curl, CURLINFO_HTTP_CODE);

            curl_close($curl);

            $this->stk_check_response = json_decode($curl_response);

//            $this->print_pre($this->stk_check_response);

            if (isset($this->stk_check_response->ResultCode) && $this->stk_check_response->ResultCode == 0) {
                $this->message = 'MPESA STK Completed: Successfully';

                $customer_order->update_status($this->defualt_order_status, '', TRUE);

                if ($this->defualt_order_status == 'wc-completed') {
                    wc_reduce_stock_levels($customer_order->get_id());
                }
                // Mark order as Paid
                $customer_order->payment_complete();

                // Empty the cart (Very important step)
                $woocommerce->cart->empty_cart();
            } elseif ($this->stk_check_response->ResultCode == 1032) {
                $this->message = 'MPESA STK Completed: Cancelled by User';
            } else {

                $this->message = 'MPESA STK Completed: ' . $this->stk_check_response->errorMessage;
            }
        }
    }

    public function fixphonenumber($phonenumber, $countrycode = 'KE') {

        $country = !empty($country) ? $country : 'KE';

        $phonenumber = str_replace(array("+", " "), "", $phonenumber);

        if (substr($phonenumber, 0, 1) == '0') {
            $callingcode = '254';
            $phonenumber = $callingcode . ltrim($phonenumber, '0');
        }

        return $phonenumber;
    }

    public function process_payment_verify($order_id, $customer_order) {
        global $woocommerce, $wpdb;


        $mpesa_code = sanitize_text_field($_POST['mpesa_code']);


        $customer_order = new WC_Order($order_id);


        $phone = substr($customer_order->get_billing_phone(), -9);

        if ($this->enable_verification_input_box == 'yes') {

            $mpesa_codes = explode(",", $mpesa_code);

            $mpesa_code_array = array();

            if (count($mpesa_codes)) {
                foreach ($mpesa_codes as $mpesa_trx) {
                    $mpesa_code_array[] = trim($mpesa_trx);
                }
            }

            $mpesa_trx_str = "'" . implode("','", $mpesa_code_array) . "'";

            $mpesa_trxs = $wpdb->get_results("SELECT * FROM {$wpdb->prefix}pbtransactions WHERE mpesa_code  IN ({$mpesa_trx_str}) AND `status` = 'Open'");
        } else {
            $mpesa_trxs = $wpdb->get_results("SELECT * FROM {$wpdb->prefix}pbtransactions WHERE mpesa_msisdn LIKE '%{$phone}%' AND `status` = 'Open'");
        }


//        $this->print_pre($mpesa_trxs);
//        exit("here");

        $refs_array = array();

        $amount = 0;

        if (count($mpesa_trxs)) {

            foreach ($mpesa_trxs as $trx) {
                $refs_array[] = $trx->mpesa_code;
                $amount += $trx->mpesa_amt;
            }
        }

        $refs = implode(',', $refs_array);

//        exit("\$refs$refs");

        if (empty($refs)) {
            if ($this->enable_verification_input_box == 'yes') {
                throw new Exception(__('Invalid MPESA Confirmation code(s)', 'spyr-kenpesapb'));
            } else {
                throw new Exception(__('We couldn\'t find any MPESA transaction from ' . $customer_order->get_billing_phone() . '. Please confirm that you have used the same phone number you have registered with us.', 'spyr-kenpesapb'));
            }
        }

        $order_total = $customer_order->get_total();

        if ($amount < $order_total) {

            $difference = $order_total - $amount;

            throw new Exception(__('You paid Ksh.' . number_format($amount, 2) . ' instead of Ksh' . number_format($order_total, 2) . '. Please top up the difference of Ksh.' . number_format($difference, 2), 'spyr-kenpesapb'));
        } else {


            foreach ($refs_array as $key => $mpesa_code) {
                $wpdb->update(
                        $wpdb->prefix . 'pbtransactions', array(
                    'status' => 'Closed',
                    'invoiceid' => $order_id,
                        ), array('mpesa_code' => $mpesa_code), array(
                    '%s',
                        )
                );
            }


            $customer_order->add_order_note(__('MPESA payment completed: ' . $refs, 'spyr-kenpesapb'));



            $customer_order->update_status($this->defualt_order_status, '', TRUE);

            if ($this->defualt_order_status == 'wc-completed') {
                wc_reduce_stock_levels($customer_order->get_id());
            }
            // Mark order as Paid
            $customer_order->payment_complete();

            // Empty the cart (Very important step)
            $woocommerce->cart->empty_cart();

            // Redirect to thank you page
        }

        return true;
    }

    // Validate fields
    public function validate_fields() {
        return true;
    }

    // Check if we are forcing SSL on checkout pages
    // Custom function not required by the Gateway
    public function do_ssl_check() {
        if ($this->enabled == "yes") {
            if (get_option('woocommerce_force_ssl_checkout') == "no") {
                //  echo "<div class=\"error\"><p>" . sprintf(__("<strong>%s</strong> is enabled and WooCommerce is not forcing the SSL certificate on your checkout page. Please ensure that you have a valid SSL certificate and that you are <a href=\"%s\">forcing the checkout pages to be secured.</a>"), $this->method_title, admin_url('admin.php?page=wc-settings&tab=checkout')) . "</p></div>";
            }
        }
    }

    public function payment_fields() {
        global $woocommerce;


        if ($this->default_account_no == 'order' || $this->default_account_no == 'order_encoded') {
            if (!isset($woocommerce->session->order_awaiting_payment)) {
                $order_id = $this->get_last_order_id() + 1;
            } else {
                $order_id = $woocommerce->session->order_awaiting_payment;
            }

            if ($this->default_account_no == 'order_encoded') {
                $account_number = str_replace("=", "", strtoupper(base64_encode(str_pad($order_id, 4, '0', STR_PAD_LEFT))));
            } else {
                $account_number = str_pad($order_id, 4, '0', STR_PAD_LEFT);
            }
        } else {
            $account_number = $this->randomize_account();
        }

        $instructions = $this->get_description();

//        if (!isset($woocommerce->session->order_awaiting_payment)) {
//            $account_number = $this->randomize_account();
//        } else {
//            $order_id = $woocommerce->session->order_awaiting_payment;
//            $account_number = str_pad($order_id, 4, '0', STR_PAD_LEFT);
//        }
        // $amount = $woocommerce->cart->get_cart_total();
        $amount = $woocommerce->cart->get_total();




        if (isset($_COOKIE['wmc_current_currency'])) {
            $current_currency = $_COOKIE['wmc_current_currency'];
        } else {
            $current_currency = get_option('woocommerce_currency');
        }



        $instructions = str_replace('[BUSINESSNO]', $this->business_number, $instructions);
        $instructions = str_replace('[ACCOUNTNO]', $account_number, $instructions);
        $instructions = str_replace('[AMOUNT]', $amount, $instructions);



        if ($current_currency != 'KES') {

            echo "<div class=\"woocommerce-NoticeGroup woocommerce-NoticeGroup-checkout\">"
            . "<ul class=\"woocommerce-error\">
			<li>Your currency is <b>{$current_currency}</b>, which is not supported by MPESA. Please check your currency to Kenya Shillings. </li>
                        </ul>
                </div>";
        }

        echo $this->shortdesc;

//        echo wpautop(wptexturize($instructions));

        /* if ($this->enable_verification_input_box == 'yes') {

          $label = !empty($this->enable_verification_label) ? $this->enable_verification_label : 'MPESA Confirmation Code(s).';
          echo '
          <p class="form-row form-row form-row-wide woocommerce-validated" id="mpesa_code_field" data-o_class="form-row form-row form-row-wide">
          <label for="mpesa_code" class="">' . $label . '  <abbr class="required" title="required">*</abbr></label>
          <input type="hidden" class="input-text " name="account_no" id="account_no" value="' . base64_encode($account_number) . '"/>
          <input type="text" style="max-width:50%" autocomplete="off" class="input-text " name="mpesa_code" id="mpesa_code" placeholder="LCR4HCBJS2, LCR4HCBJS3" />
          </p>
          ';
          } */
    }

    public function randomize_account() {
        $characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';

        $string = '';
        $max = strlen($characters) - 1;
        for ($i = 0; $i < 8; $i++) {
            $string .= $characters[mt_rand(0, $max)];
        }

        return $string;
    }

    public function get_cart_total($formatted = 1) {
        global $woocommerce;
        if ($formatted) {
            return $woocommerce->cart->get_total();
        } else {
            return $woocommerce->cart->get_total($formatted);
        }
    }

    public function get_last_order_id() {
        global $wpdb;
        $statuses = array_keys(wc_get_order_statuses());
        $statuses = implode("','", $statuses);

        // Getting last Order ID (max value)
        $results = $wpdb->get_col("
        SELECT MAX(ID) FROM {$wpdb->prefix}posts
        WHERE post_type LIKE 'shop_order'
        AND post_status IN ('$statuses')
    ");
        return reset($results);
    }

    function cl($key) {
        global $wpdb;

        $lc_result = $wpdb->get_row("SELECT localkey FROM {$wpdb->prefix}mod_kenpesa WHERE id =1");

//        $localkey = $lc_result->localkey;
//        
//        $this->print_pre($lc_result);exit;

        $whmcsurl = "https://my.enet.co.ke/";

        $licensing_secret_key = "287811c077b90af0b013cddf47dcfd69"; # Unique value, should match what is set in the product configuration for MD5 Hash Verification
        $check_token = time() . md5(mt_rand(1000000000, 9999999999) . $key);
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
            $postfields["licensekey"] = $key;
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
            } else {
                throw new Exception(__(base64_decode("QW4gZXJyb3Igb2NjdXJyZWQuIFBsZWFzZSBjb250YWN0IHVz"), 'spyr-kenpesapb'));
            }
            $results["remotecheck"] = true;
        }

//        if (isset($results['localkey'])) {
        if ($results["remotecheck"] == 1) {

            $wpdb->update(
                    "{$wpdb->prefix}mod_kenpesa", array(
                'localkey' => $results['localkey']
                    ), array('id' => 1), array(
                '%s',
                    ), array('%d')
            );
        }
//        }

        unset($postfields, $data, $matches, $whmcsurl, $licensing_secret_key, $checkdate, $usersip, $localkeydays, $allowcheckfaildays, $md5hash);
        return $results;
    }

    public function stk_confirm($params) {
        if ($this->transactiontype == 'KopokopoConnect') {
            $this->stk_confirm_k2($params);
        } else {
            $this->stk_confirm_mpesa($params);
        }
    }

    public function stk_confirm_k2($params) {
       
        
        $params_parts = explode("/", $params);
//        print_r($params_parts);

        $order_id = isset($params_parts[0]) ? $params_parts[0] : '';
        $order_key = isset($params_parts[1]) ? $params_parts[1] : '';


        $order = wc_get_order($order_id);

//        print_pre($order->status);exit;

        if ($order->order_key != $order_key || $order->status != 'pending') {
            die("Failed");
        } else {
            $ipn_data = file_get_contents("php://input");

            $decode_ipn = json_decode($ipn_data);

           

            add_post_meta($order_id, '_wc_stkresult_response', $ipn_data);

            $customer_order = new WC_Order($order_id);

            $mpesa_post_id = get_post_meta($order_id, 'kenpesawc_mpesa_post_id', 1);
            add_post_meta($mpesa_post_id, 'kenpesawc_stk_ipn_response', $ipn_data);



            if ($decode_ipn->data->attributes->status == 'Success') { //success
                $current_timestamp = current_time('timestamp');
                
                $trx = array();
                
                $trx['MpesaReceiptNumber'] = $decode_ipn->data->attributes->event->resource->reference;
                $trx['Amount'] = $decode_ipn->data->attributes->event->resource->amount;
                $trx['PhoneNumber'] = $decode_ipn->data->attributes->event->resource->sender_phone_number;

               
                add_post_meta($mpesa_post_id, 'kenpesawc_MpesaReceiptNumber', $trx['MpesaReceiptNumber']);
                add_post_meta($mpesa_post_id, 'kenpesawc_Amount', $trx['Amount']);
                add_post_meta($mpesa_post_id, 'kenpesawc_PhoneNumber', $trx['PhoneNumber']);


                $customer_order->add_order_note(__("MPESA Express payment completed: <a href=\"post.php?post={$mpesa_post_id}&action=edit\">{$trx['MpesaReceiptNumber']}</a>", 'spyr-kenpesapb'));



                $customer_order->update_status($this->defualt_order_status, '', TRUE);

                if ($this->defualt_order_status == 'wc-completed') {
                    wc_reduce_stock_levels($customer_order->get_id());
                }
                // Mark order as Paid
                $customer_order->payment_complete();

                wp_update_post(
                        array(
                            'ID' => trim($mpesa_post_id),
                            'post_status' => 'kenpesa_success'
                        )
                );


                die("Success");
            } else {
                wp_update_post(
                        array(
                            'ID' => trim($mpesa_post_id),
                            'post_status' => 'kenpesa_failed'
                        )
                );

//                exit("\$mpesa_post_id$mpesa_post_id");

                $customer_order->add_order_note(__("MPESA Express payment Failed: <a href=\"post.php?post={$mpesa_post_id}&action=edit\">{$decode_ipn->Body->stkCallback->ResultDesc}</a>", 'spyr-kenpesapb'));
            }
        }
    }

    public function stk_confirm_mpesa($params) {
        $params_parts = explode("/", $params);
//        print_r($params_parts);

        $order_id = isset($params_parts[0]) ? $params_parts[0] : '';
        $order_key = isset($params_parts[1]) ? $params_parts[1] : '';


        $order = wc_get_order($order_id);

//        print_pre($order->status);exit;

        if ($order->order_key != $order_key || $order->status != 'pending') {
            die("Failed");
        } else {
            $ipn_data = file_get_contents("php://input");

            $decode_ipn = json_decode($ipn_data);

//            print_pre();exit;

            add_post_meta($order_id, '_wc_stkresult_response', $ipn_data);

            $customer_order = new WC_Order($order_id);

            $mpesa_post_id = get_post_meta($order_id, 'kenpesawc_mpesa_post_id', 1);
            add_post_meta($mpesa_post_id, 'kenpesawc_stk_ipn_response', $ipn_data);



            if ($decode_ipn->Body->stkCallback->ResultCode == 0) { //success
                $current_timestamp = current_time('timestamp');

                $trx = array();

                if (isset($decode_ipn->Body->stkCallback->CallbackMetadata->Item)) {
                    foreach ($decode_ipn->Body->stkCallback->CallbackMetadata->Item as $item) {
                        $trx[$item->Name] = isset($item->Value) ? $item->Value : '0.0';
                    }
                }



                add_post_meta($mpesa_post_id, 'kenpesawc_MpesaReceiptNumber', $trx['MpesaReceiptNumber']);
                add_post_meta($mpesa_post_id, 'kenpesawc_Amount', $trx['Amount']);
                add_post_meta($mpesa_post_id, 'kenpesawc_PhoneNumber', $trx['PhoneNumber']);


                $customer_order->add_order_note(__("MPESA Express payment completed: <a href=\"post.php?post={$mpesa_post_id}&action=edit\">{$trx['MpesaReceiptNumber']}</a>", 'spyr-kenpesapb'));



                $customer_order->update_status($this->defualt_order_status, '', TRUE);

                if ($this->defualt_order_status == 'wc-completed') {
                    wc_reduce_stock_levels($customer_order->get_id());
                }
                // Mark order as Paid
                $customer_order->payment_complete();

                wp_update_post(
                        array(
                            'ID' => trim($mpesa_post_id),
                            'post_status' => 'kenpesa_success'
                        )
                );


                die("Success");
            } else {
                wp_update_post(
                        array(
                            'ID' => trim($mpesa_post_id),
                            'post_status' => 'kenpesa_failed'
                        )
                );

//                exit("\$mpesa_post_id$mpesa_post_id");

                $customer_order->add_order_note(__("MPESA Express payment Failed: <a href=\"post.php?post={$mpesa_post_id}&action=edit\">{$decode_ipn->Body->stkCallback->ResultDesc}</a>", 'spyr-kenpesapb'));
            }
        }
    }

}
