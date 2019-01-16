package com.layar.islam;

/**
 * Created by melvin on 25/09/2016.
 */
public class Configurations {

    /**
     * I have put a lot of effort to ensure that everything is working fine. If you find any problem
     * feel free to contact me via the Codecanyon item's comment section or send me a personal
     * message from here: https://codecanyon.net/user/neurondigital?ref=neurondigital#contact
     * <p/>
     * Thanks for buying this Template!
     * Check out my other products on CodeCanyon: http://codecanyon.net/user/neurondigital/portfolio?ref=neurondigital
     */

    //GENERAL---------------------------------------------------------------------------------------
    //Server URL
    public static String SERVER_URL = "https://sholatdulu.com/layarislam/";

    //Image slideshow timer
    public final static int SLIDESHOW_TIME_SECONDS = 5;

    //categories displayed in navigation bar? (true/false)
    public final static boolean DISPLAY_CATEGORIES_IN_NAVIGATION_DRAWER = true;

    //Categories to show in navigation drawer
    public final static int CATEGORIES_TO_SHOW_IN_NAVIGATION_DRAWER = 10;

    //show the small icon next to the categories? (true/false)
    public final static boolean SHOW_CATEGORIES_ICONS = true;




    //IN-APP PURCHASE-------------------------------------------------------------------------------
    //To use the in-app purchase feature an EXTENDED LICENSE NEEDS to be purchased.
    //Only put in a public key if you purchased an extended license from
    //CODECANYON: http://codecanyon.net/user/neurondigital/portfolio?ref=neurondigital

    //OPTIONAL - Leave 'PUBLIC_KEY' empty to disable in-app purchase.
    final static String PUBLIC_KEY = "";

    // For testing use:  "android.test.purchased";  to make the purchase always accepted without an actual payment
    //Needs to be the same as the product id used in the Google Play Dashboard
    //final static String SKU_PREMIUM ="your_premium_upgrade_sku_here";
    public final static String SKU_PREMIUM = "android.test.purchased";//uncomment this for testing



    //FIREBASE PUSH NOTIFICATION--------------------------------------------------------------------
    public final static String FIREBASE_PUSH_NOTIFICATION_TOPIC = "news";
    public final static String FIREBASE_PUSH_NOTIFICATION_TOPIC_BREAKING = "breaking";



    //TEST DEVICES----------------------------------------------------------------------------------
    //This is use so that ads aren't shown on test devices. Just put a list of device uuids.
    public final static String[] TEST_DEVICES = new String[]{};

}
