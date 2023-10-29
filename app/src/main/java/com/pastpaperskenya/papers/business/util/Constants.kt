package com.pastpaperskenya.papers.business.util

import com.pastpaperskenya.papers.BuildConfig

/**
 * Created by Benja on 30-Nov-22.
 */
object Constants {
    private const val SITE_URL = "https://pastpaperskenya.com/"
    private const val CONSUMER_KEY = BuildConfig.CONSUMER_KEY
    private const val CONSUMER_SECRET = BuildConfig.CONSUMER_SECRET
    private const val API_URL = "wp-json/wc/v3/"
    const val BASE_URL = SITE_URL + API_URL
    private const val BASE_AUTH = "?consumer_key=$CONSUMER_KEY&consumer_secret=$CONSUMER_SECRET"
    const val RC_GOOGLE_IN = 9002

    //KEY CONSTANT
    const val FIREBASE_DATABASE_COLLECTION_USER = "users"
    const val FIREBASE_DATABASE_COLLECTION_PAYMENTS = "payments"
    const val FIREBASE_DATABASE_COLLECTION_ORDER= "orders"
    const val FIREBASE_DATABASE_COLLECTION_IMAGES = "images"
    const val USER_SERVER_ID = "userServerId"
    const val KEY_ID = "id"
    const val ORDER_ID= "order_id"
    const val KEY_EMAIL = "email"
    const val KEY_FIRSTNAME = "first_name"
    const val KEY_LASTNAME = "last_name"
    const val KEY_USERNAME = "username"
    const val KEY_PASSWORD = "password"
    const val BACKGROUND_IMAGE_NAME = "background_images"
    const val KEY_PRODUCT = "product"
    const val KEY_PRODUCT_ID = "product_id"
    const val KEY_SEARCH = "search"
    const val KEY_CATEGORY = "category"
    const val KEY_SLUG = "slug"
    const val KEY_TAG = "tag"
    const val KEY_AFTER = "after"
    const val KEY_ATTRIBUTE = "attribute"
    const val KEY_ATTRIBUTE_TERM = "attribute_term"
    const val KEY_PAGE = "page"
    const val KEY_PER_PAGE = "per_page"
    const val KEY_PARENT = "parent"
    const val KEY_HIDE_EMPTY = "hide_empty"
    const val KEY_FEATURED = "featured"
    const val KEY_ON_SALE = "on_sale"
    const val KEY_ORDER = "order"
    const val KEY_ORDER_BY = "orderby"
    const val KEY_MIN_PRICE = "min_price"
    const val KEY_MAX_PRICE = "max_price"
    const val KEY_ORDER_NOTE = "note"
    const val KEY_CUSTOMER_NOTE = "customer_note"
    const val KEY_ORDER_BILLING = "billing"
    const val KEY_ORDER_SHIPPING = "shipping"
    const val KEY_ORDER_LINE_ITEM = "line_items"
    const val KEY_COUPON_LINE_ITEM = "coupon_lines"
    const val KEY_SHIPPING_LINE_ITEM = "shipping_lines"
    const val KEY_PAYMENT_METHOD = "payment_method"
    const val KEY_PAYMENT_METHOD_TITLE = "payment_method_title"
    const val KEY_PAYMENT_SET_PAID = "set_paid"
    const val KEY_CUSTOMER_ID = "customer_id"
    const val KEY_FORCE = "force"
    const val KEY_TYPE = "type"
    const val KEY_NONCE = "nonce"
    const val KEY_AMOUNT = "amount"
    const val KEY_CURRENCY = "currency"
    const val KEY_CUSTOMER = "customer"
    const val KEY_REVIEW = "review"
    const val KEY_REVIEWER = "reviewer"
    const val KEY_REVIEWER_EMAIL = "reviewer_email"
    const val KEY_REVIEW_RATING = "rating"
    const val KEY_PAYMENT_TITLE = "paymentTitle"
    const val KEY_CODE = "code"
    const val KEY_DISCOUNT = "discount"
    const val KEY_DISCOUNT_TOTAL = "discount_total"
    const val KEY_METHOD_ID = "method_id"
    const val KEY_METHOD_TITLE = "method_title"
    const val KEY_SHIPPING_TOTAL = "shipping_total"
    const val KEY_TOTAL = "total"
    const val KEY_TRANSACTION_ID = "transaction_id"
    const val KEY_EXCLUDE = "exclude"
    const val PAYMENT_METHOD_CARD = "card"
    const val RESET_PASSWORD = "reset_password"

    // API END POINTS
    const val API_CUSTOMER = BASE_URL + "customers/" + BASE_AUTH
    const val API_CUSTOMER_ID = BASE_URL + "customers/" + "{" + KEY_ID + "}" + BASE_AUTH
    const val API_PRODUCTS = BASE_URL + "products/" + BASE_AUTH
    const val API_PRODUCT_DETAIL = BASE_URL + "products/" + "{" + KEY_ID + "}" + BASE_AUTH
    const val API_PRODUCT_CATEGORIES = "products/categories/" + BASE_AUTH
    const val API_PRODUCT_TAGS = BASE_URL + "products/tags/" + BASE_AUTH
    const val API_PRODUCT_TAGS_ID = BASE_URL + "products/tags/" + "{" + KEY_ID + "}" + BASE_AUTH
    const val API_PRODUCT_FILTER_TAGS = BASE_URL + "products/" + BASE_AUTH
    const val API_PRODUCT_REVIEWS = BASE_URL + "products/reviews/" + BASE_AUTH
    const val API_PRODUCT_ATTRIBUTES = BASE_URL + "products/attributes/" + BASE_AUTH
    const val API_PRODUCT_TERMS =
        BASE_URL + "products/attributes/" + "{" + KEY_ID + "}/terms/" + BASE_AUTH
    const val API_COUPONS = BASE_URL + "coupons/" + BASE_AUTH
    const val API_REQUEST_ORDER = BASE_URL + "orders" + BASE_AUTH
    const val API_UPDATE_ORDER = BASE_URL + "orders/{" + KEY_ID + "}" + BASE_AUTH
    const val API_CANCEL_ORDER = BASE_URL + "orders/{" + KEY_ID + "}" + BASE_AUTH
    const val API_ORDER_NOTES = BASE_URL + "orders/{" + KEY_ID + "}/notes/" + BASE_AUTH
    const val API_DATA_CONTINENTS = "data/continents/" + BASE_AUTH
    const val API_DATA_COUNTRIES = "data/countries/" + BASE_AUTH
    const val API_DATA_CURRENCY = "data/currencies/current/" + BASE_AUTH
    const val API_CURRENCY_SYMBOL = "data/currencies/" + "{" + KEY_ID + "}" + BASE_AUTH
    const val API_DOWNLOAD =
        BASE_URL + "customers/" + "{" + KEY_ID + "}" + "/downloads/" + BASE_AUTH

    //mpesa
    const val MPESA_TOKEN = BASE_URL + "generateToken"
    const val MPESA_STK_REQUEST = BASE_URL + "generateStkPush/"
    const val CHECK_PAYMENT_STATUS = BASE_URL + "order-payment/{" + ORDER_ID + "}"
    const val SEND_ORDER_DATA = BASE_URL + "sendOrderData/"

    //CARD PAYMENTS
    const val FLUTTER_PUBLIC_KEY = "FLWPUBK-0ff040c1844abe453dea0eb666b5ee27-X"
    const val FLUTTER_SECRET_KEY = "FLWSECK-4b6869c753de2ea4624d5fea4feb111e-X"
    const val FLUTTER_ENCRYPTION_KEY = "4b6869c753dee7d6cc7cf187"

    const val NOTIFICATION_CHANNEL= "pastpapers_channel"
}