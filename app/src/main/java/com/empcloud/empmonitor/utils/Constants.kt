package com.empcloud.empmonitor.utils

object Constants {

    const val AUTH_TOKEN = "auth_token"
    const val USER_ID = "USER_ID"
    //Sharedpref
    const val FIRST_RUN = "FIRST_RUN"

    const val LOCATION_LIST = "LOCATION_LIST"

    //end points api
    const val VERIFY_EMAIL = "open-user/verify-email"
    const val USER_LOGIN = "open-user/user-login"
    const val FORGOT_PASSWORD = "open-user/forgot-password"
    const val RESET_PASSWORD = "open-user/reset-password"
    const val VERIFY_PHONE = "open-user/verify-phone"
    const val FETCH_PROFILE = "profile/fetchProfile"
    const val UPDATE_PROFILE = "profile/updateProfile"
    const val MARK_ATTENDANCE = "attendance/mark-attendance"
    const val FETCH_ATTENDANCE = "attendance/attendance"
    const val UPLOAD_PROFILE_IMAGE = "profile/uploadProfileImage"
    const val HOLIDAT_FETCH ="holiday/get-holiday"
    const val FETCH_LEAVES = "leaves/get-leaves"
    const val ATTENDANCE = "attendance/fetch-attendance"
    const val CREATE_LEAVE = "leaves/create-leave"
    const val EDIT_LEAVE = "leaves/update-leaves"
    const val DELETE_LEAVE = "leaves/delete-leaves"
    const val CREATE_CLIENT = "client/create"
    const val FETCH_CLEINT = "client/fetch"
    const val EDIT_ATTENDANCE = "attendance/attendance-request"
    const val FETCH_TASK = "task/fetch"
    const val UPDATE_TASK = "task/update-taskStatus"
    const val CREATE_TASK = "task/create"
    const val CLIENT_UPLOAD_PROFLE_IMAGE = "client/clientUploadProfileImage"
    const val SEND_LOCATION = "track/get-location"
    const val UPDATE_TASK_FILES = "task/uploadTask-files"
    const val UPDATE_RESCHEDULE = "task/update"
    const val FILTER_TASK = "task/filterTask"
    const val UPDATE_CLIENT = "client/update"
    const val GET_LEAVE_TYPE = "leaves/fetch-leave-type"
    const val MODE_TRANSPORT_SELECTION = "profile/Update-Emp-mode-of-transport"
    const val NOTIFICATION = "task/getNotification"
    const val GET_TASK_STATE_TAGS = "tags/getTags"
    const val GET_TRACKING_SETTINGS = "open-user/get-tracking-settings"

    //Auto Check-In
    const val AUTO_CHECK_IN_BY_MOBILE = "AUTO_CHECK_IN_BY_MOBILE"
    const val AUTO_CHECK_IN_BY_GEO_FENCING = "AUTO_CHECK_IN_BY_GEO_FENCING"
    const val IS_GEO_FENCING_ON = "IS_GEO_FENCING_ON"
    const val ORG_LATITUDE = "ORG_LATITUDE"
    const val ORG_LONGITUDE = "ORG_LONGITUDE"
    const val ORG_RADIUS = "ORG_RADIUS"
    const val AUTO_CHECK_IN_TIME = "AUTO_CHECK_IN_TIME"
    const val AUTO_CHECK_OUT_PENDING = "AUTO_CHECK_OUT_PENDING"
    const val AUTO_CHECK_OUT_ACTION = "com.empcloud.empmonitor.AUTO_CHECK_OUT"
    const val CHECK_IN_METHOD = "CHECK_IN_METHOD"
    const val CHECK_IN_METHOD_MAP = "MAP"
    const val CHECK_IN_METHOD_MOBILE = "MOBILE"
    const val GEO_LOG_STATUS = "GEO_LOG_STATUS"

    //intent passing
    const val PHONE_NUMBER = "phone_number"
    const val EMAIL = "Email"
    const val PASSWORD = "password"
    const val FORGOT_PASS_OTP = "forgot_pass_otp"
    const val LOGIN_TYPE = "login_type"

    //map data
    const val CITY = "city"
    const val COUNTRY = "country"
    const val ADDRESS = "address"
    const val STATE = "state"
    const val ZIP = "zip"

    const val CITY_CLIENT = "city_CLIENT"
    const val COUNTRY_CLIENT = "country_CLIENT"
    const val ADDRESS_CLIENT = "address_CLIENT"
    const val ADDRESS_CLIENT_1 = "address_CLIENT_1"

    const val STATE_CLIENT = "state_CLIENT"
    const val ZIP_CLIENT = "zip_CLIENT"
    const val LATITUDE_CLIENT = "latitude_CLIENT"
    const val LONGITUDE_CLIENT = "longitude_CLIENT"
    const val COUNTRY_CLIENT_NEW = "COUNTRY_CLIENT"



    const val BITMAP_USER_PIC  = "user_pic"
    const val CREATE_PROFILE_BITMAP_PIC = "create_profile_bitmap"

    const val USER_FULL_NAME = "full_name"
    const val NAME_FULL = "NAME_FULL"
    const val ROLE = "role"
    const val CREATE_SECTION = "create_section"
    const val LEAVE_ID = "leave_id"
    const val NAME = "NAME"

    //client details
    const val CLIENT_DETAILS = "client_details"
    const val CLIENT_NAME = "client_name"
    const val CLIENT_EMAIL = "client_email"
    const val CLIENT_PHONE = "client_phone"
    const val CLIENT_CATEGORY = "client_category"
    const val CLIENT_ADDRES1 = "client_addres1"
    const val CLIENT_ADDRESS2 = "client_address2"
    const val CLIENT_PHONE_COUTRY_CODE = "CLIENT_PHONE_COUTRY_CODE"
    const val CLIENT_PHONE_COUTRY_NAME_CODE = "CLIENT_PHONE_COUTRY_NAME_CODE"

    //create client
    const val CREATE_USER_PROFILE = "create_user_client"
    const val CREATE_USERNAME = "username"
    const val USER_CREATE_EMAIL = "useremail"
    const val CREATE_USER_PHONE = "userphone"
    const val USER_CREATE_ADDRES1 = "user_create_addres1"
    const val USER_CREATE_ADDRESS2 = "user_create_address2"
    const val USER_CITY = "user_city"
    const val USER_OUNTRY = "user_country"
    const val USER_STATE = "user_state"
    const val USER_ZIP = "user_zip"
    const val USER_AGE = "user_age"
    const val USER_GENDER = "user_gender"
    const val USER_LAT = "user_lat"
    const val USER_LONG = "user_long"

    //
   const val DIAL_NUMBER = "Dial Number"
    const val DIRECTION_NAME = "direction_name"
    const val DIRECTION_ADDRESS = "direction_address"
    const val LAT = "direction_lat"
    const val LON = "direction_lon"


    const val LAT_SEC = "direction_lat_sec"
    const val LON_SEC = "direction_lon_sec"

    //selection client
    const val CLIENT_ID = "client_id"
    const val CLIENT_NAME_SELECTED = "client_name_selected"

    const val TASK_NAME = "task_name"
    const val CLIENT_NAME_START = "clinet_name_start"
    const val TASK_TIMING = "task_timing"
    const val TASK_ADDRESS = "task_address"
    const val ISTASK_STARTED = "istask_starteed"
    const val TASK_ID = "task_id"
    const val CLIENT_NAME_TASK = "client_name_task"

    //geo constants
    const val GEO_PREF = "geo_pref"
    const val GEO_LAT = "geo_lat"
    const val GEO_LON = "geo_lon"
    const val GEO_RADIUS = "geo_radius"

    const val STATUS_TASK = "status_Task"

    const val COUNTRY_CODE = "country_code"

    const val USER_NAME = "name"
    const val USER_MAIL_ID = "mail_id"
    const val USER_PHONE_MOBIILE = "phone_no"

    const val PROFILE_URL = "profile_url"
    const val  BITMAP_RECIEVE = "bitmap_recieve"
    const val  BITMAP_RECIEVE_UPDATE = "bitmap_recieve_update"

    const val  CLIENT_BITMAP_RECIEVE = "CLIENT_BITMAP_RECIEVE"

    const val MOBILE_LOGIN = "mobile_login"

    //UPDATE PROFILE
    const val UPDATE_PROFILE_DATA = "update_profile_data"
    const val UPDATE_ADDRESS = "update_address"
    const val UPDATE_CITY = "update_city"
    const val UPDATE_STATE = "update_state"
    const val UPDATE_ZIP = "update_zip"
    const val UPDATE_LAT = "update_lat"
    const val UPDATE_LON = "update_lon"
    const val UPDATE_NAME = "update_name"
    const val UPDATE_AGE = "update_age"
    const val UPDATE_MOBILENO = "update_mobile"
    const val UPDATE_EMAIL = "update_email"
    const val UPDATE_GENDER = "update_gender"
    const val UPDATE_PROFILE_NEW = "update_profile_new"

    const val STATUS_SETTINGS = "status_setitings"
    const val UMN = "UMN"

    const val PIC_RESPONSE = "pic_response"
    const val PIC_DESC = "pic_desc"

    const val TASK_NAME_ADD = "task_add"
    const val EDIT_BACK = "edit_back"
    const val EDIT_BACK_FULL = "edit_back_full"
    const val CLIENT_ID_CREATED = "created_client_id"

    const val CLIENT_DETIALS_BUNDLE = "client_details_bundle"

    const val STATE_UPDATE  = "STATE_UPDATE"
    const val ZIP_UPDATE  = "ZIP_UPDATE"
    const val CITY_UPDATE  = "CITY_UPDATE"
    const val LAT_UPDATE  = "LAT_UPDATE"
    const val LON_UPDATE  = "LON_UPDATE"
    const val ADDRESSSECOND ="ADDRESSSECOND"
    const val COUNTRYCODE = "COUNTRYCODE"
    const val COUNTRYNAME = "COUNTRYNAME"


    const val STATE_UPDATE_1  = "STATE_UPDATE_1"
    const val ZIP_UPDATE_1  = "ZIP_UPDATE_1"
    const val CITY_UPDATE_1  = "CITY_UPDATE_1"
    const val LAT_UPDATE_1  = "LAT_UPDATE_1"
    const val LON_UPDATE_1  = "LON_UPDAT_1"
    const val ADDRESSSECOND_1 ="ADDRESSSECOND_1"
    const val COUNTRYCODE_1 = "COUNTRYCODE_1"
    const val COUNTRYNAME_1 = "COUNTRYNAME_1"

    const val ISHANDLERSTOP = "ISHANDLERSTOP"
    const val FREQUENCY = "FREQUENCY"
    const val FENCE_RADIUS = "FENCE_RADIUS"
    const val TASK_STATUS = "TASK_STATUS"

    //ADD TASK
    const val ADDTASK_CLIENTIT = "ADDTASK_CLIENTIT"
    const val ADDTASK_CLIENT_NAME = "ADDTASK_CLIENT_NAME"
    const val ADDTASK_PICRESPONSE = "ADDTASK_PICRESPONSE"
    const val ADDTASK_PICDESCRIPTION = "ADDTASK_PICDESCRIPTION"
    const val ADDTASK_TIME = "ADDTASK_TIME"
    const val ADDTASK_TIME2 = "ADDTASK_TIME2"

    const val ADDTASK_TASKNAME = "ADDTASK_TASKNAME"
    const val ADDTASK_TASKDESC = "ADDTASK_TASKDESC"

    const val CLIENTIT_SEDN = "CLIENTIT_SEDN"
    const val CLIENT_NAME_SEND = "CLIENT_NAME_SEND"


    const val CLIENTIT_RESEDN = "CLIENTIT_RESEDN"
    const val CLIENT_NAME_RESEND = "CLIENT_NAME_RESEND"

    const val CHANGED_FRAGMENT_FIRST_TIME = "CHANGED_FRAGMENT_FIRST_TIME"

    const val ADDTASK_UPLOADDOSC_1 = "ADDTASK_UPLOADDOSC1"
    const val ADDTASK_UPLOADDOSC_2 = "ADDTASK_UPLOADDOSC2"

    const val FILENAME_DOCS_1 = "FILENAME_DOCS1"
    const val FILENAME_DOCS_2 = "FILENAME_DOCS2"


    const val SELECTED_MODEL_ITEM = "SELECTED_MODEL_ITEM"
    const val SELECTED_MODEL_ITEM_1 = "SELECTED_MODEL_ITEM_1"
    const val SELECTED_MODEL_ITEM_2 = "SELECTED_MODEL_ITEM_2"
    const val SELECTED_MODEL_ITEM_3 = "SELECTED_MODEL_ITEM_3"



    const val FRAGMENT_NO ="FRAGMENT_NO"
    const val BACKNO = "BACKNO"
    const val PICRESPONSE_GALLERY = "PICRESPONSE_GALLERY"
    const val PIC_DESC_NEW= "PIC_DESC_NEW"
    const val STARTDONEBACK = "STARTDONEBACK"

    const val FIRST_HOME_CLICK = "FIRST_HOME_CLICK"

    const val LAT_UPDATED = "LAT_UPDATED"
    const val LON_UPDATED = "LON_UPDATED"

    //COOKIES CONSTRAINT

    const val COOKIES_WEB = "COOKIES_WEB"
    const val TOKEN_ID = "TOKEN_ID"
    const val ENCRYPT_EMAIL_DATA = "ENCRYPT_EMAIL_DATA"

    const val LAT_CLIENT_ADD = "LAT_CLIENT_ADD"
    const val LON_CLIENT_ADD = "LON_CLIENT_ADD"

    const val CLIENT_PROFILE_URL = "CLIENT_PROFILE_URL"


    const val CATEGORY_CLIENT = "CATEGORY_CLIENT"

    //saving user data from login
    const val LOGIN_NAME_PERSON = "LOGIN_NAME_PERSON"
    const val LOGIN_ROLE_PERSON = "LOGIN_ROLE_PERSON"


    const val TASK_TIME_SCHEDULE = "TASK_TIME_SCHEDULE"
    const val TASK_TIME_SCHEDULE2 = "TASK_TIME_SCHEDULE2"

    const val  LATITUDE_CLIENT_NEW = "LATITUDE_CLIENT_NEW"
    const val LONGITUDE_CLIENT_NEW = "LONGITUDE_CLIENT_NEW"

    //MODE SELECTED FOR TRANSPORT
    const val LAST_MODE_SELECTED = "LAST_MODE_SELECTED"

    const val UPDATE_PROFILE_LAT = "UPDATE_PROFILE_LAT"
    const val UPDATE_PROFILE_LON = "UPDATE_PROFILE_LON"

    const val PROFILE_PIC_URL_USER = "PROFILE_PIC_URL_USER"
    const val CLIENT_EDIT_UPDATE_PROFILE = "CLIENT_EDIT_UPDATE_PROFILE"

    const val IS_CHECKEDIN = "IS_CHECKEDIN"
    const val DOC_URI = "DOC_URI"

    const val TASK_VOLUME = "TASK_VOLUME"
    const val TASK_VALUE = "TASK_VALUE"
    const val CURRENCY_SELECTION = "CURRENCY_SELECTION"

    const val TASKVALUE1 = "TASKVALUE1"
    const val TASKVOLUME1 = "TASKVOLUME1"
    const val CURRENCYSELECTION1 = "CURRENCYSELECTION1"

    const val DOCS_URI_1 = "DOCS_URI_1"
    const val DOCS_URI_2 = "DOCS_URI_2"
    const val COUNT = "COUNT"

    const val PICS_UPLOAD_MULTIPLE = "PICS_UPLOAD_MULTIPLE"
    const val PICS_URL_1 = "PICS_URL_1"
    const val PICS_URL_2 = "PICS_URL_2"
    const val PICS_URL_3 = "PICS_URL_3"
    const val PICS_URL_4 = "PICS_URL_4"

    const val PICS_DISC_1 = "PICS_DISC_1"
    const val PICS_DISC_2 = "PICS_DISC_2"
    const val PICS_DISC_3 = "PICS_DISC_3"
    const val PICS_DISC_4 = "PICS_DISC_4"


    const val NOTIFICATION_ALL_READ = "NOTIFICATION_ALL_READ"

    const val IS_GLOBAL_USER = "IS_GLOBAL_USER"
    const val ITEM_DATE = "ITEM_DATE"
    const val FRAGMENT_NAME = "FRAGMENT_NAME"
    const val MOBILE_NUMBER = "MOBILE_NUMBER"

    const val IS_PENDING_TASK = "IS_PENDING_TASK"
    const val IS_PENDING_TASK_RESCHEDULE = "IS_PENDING_TASK_RESCHEDULE"

    const val IS_CHECKED_OUT = "IS_CHECKED_OUT"
    const val UPDATE_CLIENT_BACK_MAP = "UPDATE_CLIENT_BACK_MAP"
}
