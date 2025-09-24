package com.app_neighbrsnook.apiService;
public class UrlClass {
    public static final String BASE_URL="https://dev.neighbrsnook.com/oldadmin/";
    public static final String SIGN_UP= BASE_URL+"api/master?flag=createuser";
    public static final String LARAVEL_BASE_URL="https://dev.neighbrsnook.com/admin/";
    public static final String OTP=BASE_URL+"api/master?flag=sendotp";
    public static final String OTP_FORGOT=BASE_URL+"api/otpverification?flag=forget";
    public static final String MAIL_API=BASE_URL+"api/testuu?flag=hitmail";
    public static final String CHANGE_PASSWORD=BASE_URL+"api/master";
    public static final String DEACTIVATE_ACCOUNT=BASE_URL+"api/mobilesettings";
    public static final String ADDRESS_PROOF_API=BASE_URL+"api/master";
    public static final String LITTEL_MORE_ABOUT=BASE_URL+"api/master";
    public static final String PROFILE_UPDATE=BASE_URL+"api/master";
    public static final String EVENT_IMAGE_SEND=BASE_URL+"api/event";
    public static final String REQUEST_FOR_NEIGHBRHOOD=BASE_URL+"api/master";
    public static final String LOCATION_STATUS=LARAVEL_BASE_URL+"api/master";
    public static final String USERNAME_CHANGED=BASE_URL+"api/master";
    public static final String SAVE_NEIGHBRHOOD=BASE_URL+"api/master";
    public static final String CREATE_GROUP_URL=BASE_URL+"api/groups";
    public static final String CREATE_EVENT_URL=BASE_URL+"api/event";
    public static final String CREATE_PRODUCT_URL="api/mpk_product_add";
    public static final String HOMEPAGE_LIST_API="api/mpk_product_home";
    public static final String SELECT_NEIGHBRHOOD=BASE_URL+"api/master?flag=searchneighborhood";
    public static final String MARKET_PLACE_CHAT_LIST=LARAVEL_BASE_URL+"api/seller-chat-list/";
    public static final String MARKET_PLACE_DLT=LARAVEL_BASE_URL+"api/mpk_product_add/edit/";
    public static final String MARKET_PLACE_DELETE_WISHLIST=LARAVEL_BASE_URL+"api/wishlist/";
    public static final String CHAT_SENDER_RCVR_MARKET_PLACE=LARAVEL_BASE_URL+"api/messages/";
    public static final String NEIGHBRHOOD_LIST=BASE_URL+"api/nearneighlist?flag=neighbourlist&nbdname=";
    public static final String PROFILE=BASE_URL+"api/master?flag=userprofile&userid";
    public String getLoginUrl() {
        return BASE_URL+"api/login";
    }
}
