package com.pastpaperskenya.app.business.util;

import com.pastpaperskenya.app.BuildConfig;

/**
 * Created by Md Sahiul Islam on 23-Jan-19.
 */
    public class Constants {

    private static final String SITE_URL = "https://pastpaperskenya.com/"; //Change to your website url

    public static final String CONSUMER_KEY = BuildConfig.CONSUMER_KEY;
    public static final String CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET;

    private static final String API_URL = "wp-json/wc/v3/";    //Don't change api url
    public static final String BASE_URL = SITE_URL + API_URL;

    public static final String BASE_AUTH = "?consumer_key=" + CONSUMER_KEY + "&consumer_secret=" + CONSUMER_SECRET;

    public static final String PAYMENTS_URL= "https://us-central1-pastpaperskenya.cloudfunctions.net/payments/";
    public static final int RC_GOOGLE_IN= 9002;

    //KEY CONSTANT
    public static final String FIREBASE_DATABASE_COLLECTION_USER= "users";
    public static final String FIREBASE_DATABASE_COLLECTION_PAYMENTS= "payments";
    public static final String FIREBASE_DATABASE_COLLECTION_IMAGES= "images";
    public static final String USER_SERVER_ID="userServerId";
    public static final String KEY_ID = "id";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_FIRSTNAME = "first_name";
    public static final String KEY_LASTNAME = "last_name";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_PROFILE_PHOTO= "profile_photo";

    public static final String KEY_PRODUCT = "product";
    public static final String KEY_PRODUCT_ID = "product_id";
    public static final String KEY_SEARCH = "search";
    public static final String KEY_CATEGORY = "category";
    public static final String KEY_SLUG = "slug";
    public static final String KEY_TAG = "tag";
    public static final String KEY_AFTER = "after";
    public static final String KEY_ATTRIBUTE = "attribute";
    public static final String KEY_ATTRIBUTE_TERM = "attribute_term";
    public static final String KEY_PAGE = "page";
    public static final String KEY_PER_PAGE = "per_page";
    public static final String KEY_PARENT = "parent";
    public static final String KEY_HIDE_EMPTY = "hide_empty";
    public static final String KEY_FEATURED = "featured";
    public static final String KEY_ON_SALE = "on_sale";
    public static final String KEY_ORDER = "order";
    public static final String KEY_ORDER_BY = "orderby";
    public static final String KEY_MIN_PRICE = "min_price";
    public static final String KEY_MAX_PRICE = "max_price";
    public static final String KEY_ORDER_NOTE = "note";
    public static final String KEY_CUSTOMER_NOTE = "customer_note";
    public static final String KEY_ORDER_BILLING = "billing";
    public static final String KEY_ORDER_SHIPPING = "shipping";
    public static final String KEY_ORDER_LINE_ITEM = "line_items";
    public static final String KEY_COUPON_LINE_ITEM = "coupon_lines";
    public static final String KEY_SHIPPING_LINE_ITEM = "shipping_lines";
    public static final String KEY_PAYMENT_METHOD = "payment_method";
    public static final String KEY_PAYMENT_METHOD_TITLE = "payment_method_title";
    public static final String KEY_PAYMENT_SET_PAID = "set_paid";
    public static final String KEY_CUSTOMER_ID = "customer_id";
    public static final String KEY_FORCE = "force";
    public static final String KEY_TYPE = "type";
    public static final String KEY_NONCE = "nonce";
    public static final String KEY_AMOUNT = "amount";
    public static final String KEY_CURRENCY= "currency";
    public static final String KEY_CUSTOMER = "customer";
    public static final String KEY_REVIEW = "review";
    public static final String KEY_REVIEWER = "reviewer";
    public static final String KEY_REVIEWER_EMAIL = "reviewer_email";
    public static final String KEY_REVIEW_RATING = "rating";
    public static final String KEY_STRIPE_TOKEN = "token";
    public static final String KEY_PAYMENT_TITLE = "paymentTitle";
    public static final String KEY_CODE = "code";
    public static final String KEY_DISCOUNT = "discount";
    public static final String KEY_DISCOUNT_TOTAL = "discount_total";
    public static final String KEY_METHOD_ID = "method_id";
    public static final String KEY_METHOD_TITLE= "method_title";
    public static final String KEY_SHIPPING_TOTAL= "shipping_total";
    public static final String KEY_TOTAL= "total";
    public static final String KEY_TRANSACTION_ID= "transaction_id";
    public static final String KEY_EXCLUDE="exclude";



    // API END POINTS
    public static final String API_CUSTOMER = BASE_URL + "customers/" + BASE_AUTH;
    public static final String API_PRODUCTS = BASE_URL + "products/" + BASE_AUTH;
    public static final String API_PRODUCT_DETAIL = BASE_URL + "products/" + "{" + KEY_ID + "}" + BASE_AUTH;
    public static final String API_PRODUCT_CATEGORIES = "products/categories/" + BASE_AUTH;
    public static final String API_PRODUCT_TAGS = BASE_URL + "products/tags/" + BASE_AUTH;
    public static final String API_PRODUCT_TAGS_ID = BASE_URL + "products/tags/" + "{" + KEY_ID +"}" + BASE_AUTH;
    public static final String API_PRODUCT_FILTER_TAGS = BASE_URL + "products/" + BASE_AUTH;
    public static final String API_PRODUCT_REVIEWS = BASE_URL + "products/reviews/" + BASE_AUTH;
    public static final String API_PRODUCT_ATTRIBUTES = BASE_URL + "products/attributes/" + BASE_AUTH;
    public static final String API_PRODUCT_TERMS = BASE_URL + "products/attributes/" + "{" + KEY_ID + "}/terms/" + BASE_AUTH;
    public static final String API_COUPONS = BASE_URL + "coupons/" + BASE_AUTH;
   public static final String API_REQUEST_ORDER = BASE_URL + "orders" + BASE_AUTH;
    public static final String API_CANCEL_ORDER = BASE_URL + "orders/{" + KEY_ID + "}" + BASE_AUTH;
    public static final String API_ORDER_NOTES = BASE_URL + "orders/{" + KEY_ID + "}/notes/" + BASE_AUTH;
    public static final String API_DATA_CONTINENTS = "data/continents/" + BASE_AUTH;
    public static final String API_DATA_COUNTRIES = "data/countries/" + BASE_AUTH;
    public static final String API_DATA_CURRENCY = "data/currencies/current/" + BASE_AUTH;
    public static final String API_CURRENCY_SYMBOL = "data/currencies/" + "{" + KEY_ID + "}" + BASE_AUTH;
    public static final String API_DOWNLOAD = BASE_URL + "customers/" + "{" + KEY_ID + "}" + "/downloads/" + BASE_AUTH;


    //SlUG CONSTANT
    public static final String SLUG_TRENDING_DEAL = "trending";
    public static final String SLUG_HOT_DEAL = "hot_deal";
    public static final String SLUG_SPECIAL_SALE = "special_sale";
    public static final String SEARCH_KEY = "searchKey";
}
