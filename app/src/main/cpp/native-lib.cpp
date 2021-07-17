#include <stdint.h>

#pragma clang diagnostic push
#pragma ide diagnostic ignored "cert-err58-cpp"

#include <jni.h>
#include <string>
#include <cstring>
#include <iostream>
#include <list>


#include <wchar.h>
#include <cwchar>

#include <locale>
#include <clocale>
#include "md5.h"
#include "sha1.h"
#include <android/log.h>

using namespace std;

static jclass Function;
static jclass Important;
static jclass JSONObject;
static jclass JSONArray;
static jmethodID SetSharedPreference;
static jmethodID GetSharedPreference;
static jmethodID ClearSharedPreference;
static jmethodID IsInternetAvailable;
static jmethodID Base64Encode;
static jmethodID Base64Decode;
static jmethodID CheckMD5;
static jmethodID CalculateMD5;
static jmethodID Request;
static jmethodID ShowToast;
static jmethodID IsJSONValid;
static jmethodID getCurrentTimeInMilliseconds;
/////////get token every 50 minutes////////
/////////Set Private Token///////////////
static jmethodID getString;
static jmethodID getJsonObject;
static jmethodID newJSONObject;
static jmethodID isNull;
static jmethodID has;
static jmethodID getIpaddress;
static jmethodID formattime;
static jmethodID logcat;
static jmethodID getJSONArray;
static jmethodID getInt;
static jmethodID jsonarraylength;
static jmethodID jsonarraygetjsonobject;
static jmethodID jsonPut;
static jmethodID showProgress;
static jmethodID dismissProgress;
static jmethodID UploadFile;
static jmethodID closeActivity;
static jmethodID isJSONArrayNull;


static jmethodID MethodID , MethodID40 , MethodID49 ,
        MethodID50 , MethodID33 , MethodID47 , MethodID48 ,
        MethodID46 , MethodID45 , MethodID42 , MethodID44 , MethodID43 ,
        MethodID41 , MethodID39 , MethodID38 , MethodID37 , MethodID36 ,
        MethodID34 , MethodID35 , MethodID32 , MethodID31 , MethodID30 ,
        MethodID29 , MethodID23 , MethodID28 , MethodID27 , MethodID26 ,
        MethodID25 , MethodID24 , MethodID22 , MethodID19 , MethodID20 ,
        MethodID21 , MethodID18 , MethodID1 , MethodID15 , MethodID17 , MethodID16 ,
        MethodID5 , MethodID14 , MethodID13 , MethodID3 , MethodID12 , MethodID11 ,
        MethodID7 , MethodID10 , MethodID9 , MethodID8 , MethodID4 , MethodID2 ,
        MethodID51 , MethodID52 , MethodID53 , MethodID54 , MethodID55 , MethodID6 ,
        MethodID56 , MethodID57 , MethodID58 , MethodID59 , MethodID60 , MethodID61 , MethodID62 , MethodID63 ,
        MethodID64 , MethodID65 , MethodID66 , MethodID67 , MethodID68 , MethodID69 ,
        MethodID70 , MethodID71 , MethodID72 , MethodID73 , MethodID74 , MethodID75;

string schema = "http";
string domain = "192.168.152.9";
string port = "8000";
string uri = "/api";
string baseUrl = schema + "://" + domain + ":" + port + uri;

string xmppPort = "5222";
string xmppHost = domain;
string xmppResource = "MeCloak";


//////////notification///////////


////////advertise/////////////
string advertise_links = baseUrl + "/advertise/links";
string advertise_watch = baseUrl + "/advertise/watch";
string advertise_end = baseUrl + "/advertise/end";
////////auth////////////
string profile_register = baseUrl + "/auth/register";
string page_register = baseUrl + "/auth/page-register";
string profile_login = baseUrl + "/auth/login";
string page_login = baseUrl + "/auth/page-login";
string page_delete = baseUrl + "/auth/page-delete";
string profile_logout = baseUrl + "/auth/logout";
string verify_email = baseUrl + "/auth/verify-email";
string phone_verification = baseUrl + "/auth/verify-phone";
string forgot_profile_password = baseUrl + "/auth/forget-password";
string forgot_page_password = baseUrl + "/auth/forget-page-password";
string validate_reset_password_otp = baseUrl + "/auth/validate-reset-password-otp";
string prepare_offline = baseUrl + "/auth/prepare-offline";
string upload_page_security_image = baseUrl + "/auth/upload-page-security-image";
string upload_page_security_question = baseUrl + "/auth/upload-page-security-question";
string page_security_image = baseUrl + "/auth/page-security-image";
string update_page_security_question = baseUrl + "/auth/update-page-security-question";
string create_page_recovery_certificate = baseUrl + "/auth/create-page-recovery-certificate";
string token_refresh = baseUrl + "/auth/token-refresh";
string privatekey = baseUrl + "/auth/private-key";
string publickey = baseUrl + "/auth/public-key";

string updateprofilepassword = baseUrl + "/auth/profile-update-password";
string createpagerecoverycode = baseUrl + "/auth/create-page-recovery-code";
string chillengepagerecoverycode = baseUrl + "/auth/challenge-page-recovery-code";
string uploadpagesecurityimage = baseUrl + "/auth/upload-page-security-image";
string deletepagesecurityimage = baseUrl + "/auth/delete-page-security-image";
string downloadpagesecurityimage = baseUrl + "/auth/download-page-security-image?challenge=0";
string chillengepagesecurityimage = baseUrl + "/auth/challenge-page-security-image";
string getpagesecurityquestions = baseUrl + "/auth/get-security-questions";
string answerpagesecurityquestion = baseUrl + "/auth/answer-security-questions";
string viewansweredsecurityquestions = baseUrl + "/auth/view-answered-security-questions";
string deleteansweredsecurityquestions = baseUrl + "/auth/delete-security-question-answers";
string getchillengerpagesecurityquestions = baseUrl + "/auth/get-challenge-security-questions";
string chillengerpagesecurityquestions = baseUrl + "/auth/challenge-security-questions";
string cratepageresetcertificate = baseUrl + "/auth/create-page-recovery-certificate";


//////////Friend//////////////////
string search_friend = baseUrl + "/friend/search";
string add_friend = baseUrl + "/friend/request";
string accept_friend = baseUrl + "/friend/accept";
string block_friend = baseUrl + "/friend/block";
string block_friend_list = baseUrl + "/friend/block-list";
string unfriend_friend = baseUrl + "/friend/unfriend";
string cancel_friend_request = baseUrl + "/friend/decline";
string delete_friend_request = baseUrl + "/friend/delete";
string ignore_friend = baseUrl + "/friend/ignore";
string friend_list = baseUrl + "/friend/list";
string retrive_friend_request = baseUrl + "/friend/request-list";
/////////////message//////////////
string send_message = baseUrl + "/message/send";
string get_message = baseUrl + "/message/get";
string delete_message = baseUrl + "/message/delete";
string upload_offline_message = baseUrl + "/message/upload-offline";
/////////////premium/////////////
string premium_features = baseUrl + "/premium/features";
string buy_premium = baseUrl + "/premium/buy";
string activate_previous_premium = baseUrl + "/premium/activate";
string buy_page = baseUrl + "/premium/buy-page";
///////////profile//////////////
string referrals = baseUrl + "/profile/referrals";
string chang_profile_name = baseUrl + "/profile/name-change";
string chang_profile_picture = baseUrl + "/profile/picture-change";
string chang_page_name = baseUrl + "/profile/page-name-change";
string chang_phone_number = baseUrl + "/profile/number-change";
string view_profile_picture = baseUrl + "/profile/picture";
/////////transection/////////////
string reference_balance_withdraw = baseUrl + "/transection/ref-withdraw";
string recharge = baseUrl + "/transaction/recharge";
///////////////tutorial/////////////////
string tutorial_list = baseUrl + "/tutorial/list";
string watch_tutorial = baseUrl + "/tutorial/watch";
///////////////////vault//////////////
string enter_vault = baseUrl + "/vault/login";
string open_vault = baseUrl + "/vault/open";
string upload_to_vault = baseUrl + "/vault/upload";
string create_vault = baseUrl + "/vault/create";
string getNotification = baseUrl + "/notification";

///////////////////Activity//////////////////////////
string MainActivity = "com/horofbd/MeCloak/MainActivity";
string AboutActivity = "com/horofbd/MeCloak/AboutActivity";
string AddFriend = "com/horofbd/MeCloak/AddFriend";
string AddOrEditPageSecurityImage = "com/horofbd/MeCloak/AddOrEditPageSecurityImageActivity";
string AutoBotActivity = "com/horofbd/MeCloak/AutoBotActivity";
string BLockListActivity = "com/horofbd/MeCloak/BlockListActivity";
string BuyActivity = "com/horofbd/MeCloak/BuyActivity";
string ChangeOrUpdateProfilePassword = "com/horofbd/MeCloak/ChangeOrUpdateProfilePassword";
string ContactUsActivity = "com/horofbd/MeCloak/ContactUsActivity";
string DownloadResetCode = "com/horofbd/MeCloak/DownloadResetCode";
string EditPageSecurityQuestionActivity = "com/horofbd/MeCloak/EditPageSecurityQuestionActivity";
string ForgotPagePassword = "com/horofbd/MeCloak/ForgotPagePassword";
string ForgotProfilePassword = "com/horofbd/MeCloak/ForgotProfilePassword";
string FriendListActivity = "com/horofbd/MeCloak/FriendListActivity";
string GetPremiumActivity = "com/horofbd/MeCloak/GetPremiumActivity";
string GetSecretCodeorCertificateActivity = "com/horofbd/MeCloak/GetSecretCodeorCertificateActivity";
string HelpActivity = "com/horofbd/MeCloak/HelpActivity";
string LoginActivity = "com/horofbd/MeCloak/LoginActivity";
string MyStuffActivity = "com/horofbd/MeCloak/MyStuffActivity";
string NotificationActivity = "com/horofbd/MeCloak/NotificationActivity";
string PhoneNumberVeriricationActivity = "com/horofbd/MeCloak/PhoneNumberVerificationActivity";
string PremiumFeaturesActivity = "com/horofbd/MeCloak/PremiumFeaturesActivity";
string ProfileActivity = "com/horofbd/MeCloak/ProfileActivity";
string RechargeActivity = "com/horofbd/MeCloak/RechargeActivity";
string ReferAFriendActivity = "com/horofbd/MeCloak/ReferAFriendActivity";
string ReferenceActivity = "com/horofbd/MeCloak/ReferenceActivity";
string ReferFriendActivity = "com/horofbd/MeCloak/ReferFriendActivity";
string ReferralActivity = "com/horofbd/MeCloak/ReferralActivity";
string RegisterNewUser = "com/horofbd/MeCloak/RegisterNewUser";
string RegisterVerifiedUser = "com/horofbd/MeCloak/RegisterVerifiedUser";
string ShowUserMessage = "com/horofbd/MeCloak/ShowUserMessage";
string SettingsActivity = "com/horofbd/MeCloak/SettingsActivity";
string UserVerificationActivity = "com/horofbd/MeCloak/UserVerificationActivity";
string VaultActivity = "com/horofbd/MeCloak/VaultActivity";
string WatchVideoActivity = "com/horofbd/MeCloak/WatchVideoActivity";
string PageresetimageActivity = "com/horofbd/MeCloak/PageResetImage";

string publickeyname;

void initlinks(JNIEnv *env) {
    MethodID = env->GetStaticMethodID(Important , "setBaseUrl" , "(Ljava/lang/String;)V");
    MethodID1 = env->GetStaticMethodID(Important , "setAdvertise_links" ,
                                       "(Ljava/lang/String;)V");
    MethodID2 = env->GetStaticMethodID(Important , "setAdvertise_watch" ,
                                       "(Ljava/lang/String;)V");
    MethodID3 = env->GetStaticMethodID(Important , "setAdvertise_end" ,
                                       "(Ljava/lang/String;)V");
    MethodID4 = env->GetStaticMethodID(Important , "setProfile_register" ,
                                       "(Ljava/lang/String;)V");
    MethodID5 = env->GetStaticMethodID(Important , "setPage_register" ,
                                       "(Ljava/lang/String;)V");
    MethodID6 = env->GetStaticMethodID(Important , "setProfile_login" ,
                                       "(Ljava/lang/String;)V");
    MethodID7 = env->GetStaticMethodID(Important , "setPage_login" , "(Ljava/lang/String;)V");
    MethodID8 = env->GetStaticMethodID(Important , "setPage_delete" , "(Ljava/lang/String;)V");
    MethodID9 = env->GetStaticMethodID(Important , "setProfile_logout" ,
                                       "(Ljava/lang/String;)V");
    MethodID10 = env->GetStaticMethodID(Important , "setVerify_email" ,
                                        "(Ljava/lang/String;)V");
    MethodID11 = env->GetStaticMethodID(Important , "setPhone_verification" ,
                                        "(Ljava/lang/String;)V");
    MethodID12 = env->GetStaticMethodID(Important , "setForgot_profile_password" ,
                                        "(Ljava/lang/String;)V");
    MethodID13 = env->GetStaticMethodID(Important , "setForgot_page_password" ,
                                        "(Ljava/lang/String;)V");
    MethodID14 = env->GetStaticMethodID(Important , "setValidate_reset_password_otp" ,
                                        "(Ljava/lang/String;)V");
    MethodID15 = env->GetStaticMethodID(Important , "setPrepare_offline" ,
                                        "(Ljava/lang/String;)V");
    MethodID16 = env->GetStaticMethodID(Important , "setUpload_page_security_image" ,
                                        "(Ljava/lang/String;)V");
    MethodID17 = env->GetStaticMethodID(Important , "setUpload_page_security_question" ,
                                        "(Ljava/lang/String;)V");
    MethodID18 = env->GetStaticMethodID(Important , "setPage_security_image" ,
                                        "(Ljava/lang/String;)V");
    MethodID19 = env->GetStaticMethodID(Important , "setUpdate_page_security_question" ,
                                        "(Ljava/lang/String;)V");
    MethodID20 = env->GetStaticMethodID(Important , "setCreate_page_recovery_certificate" ,
                                        "(Ljava/lang/String;)V");
    MethodID21 = env->GetStaticMethodID(Important , "setToken_refresh" ,
                                        "(Ljava/lang/String;)V");
    MethodID22 = env->GetStaticMethodID(Important , "setSearch_friend" ,
                                        "(Ljava/lang/String;)V");
    MethodID23 = env->GetStaticMethodID(Important , "setAdd_friend" , "(Ljava/lang/String;)V");
    MethodID24 = env->GetStaticMethodID(Important , "setBlock_friend" ,
                                        "(Ljava/lang/String;)V");
    MethodID25 = env->GetStaticMethodID(Important , "setBlock_friend_list" ,
                                        "(Ljava/lang/String;)V");
    MethodID26 = env->GetStaticMethodID(Important , "setUnfriend_friend" ,
                                        "(Ljava/lang/String;)V");
    MethodID27 = env->GetStaticMethodID(Important , "setCancel_friend_request" ,
                                        "(Ljava/lang/String;)V");
    MethodID28 = env->GetStaticMethodID(Important , "setDelete_friend_request" ,
                                        "(Ljava/lang/String;)V");
    MethodID29 = env->GetStaticMethodID(Important , "setIgnore_friend" ,
                                        "(Ljava/lang/String;)V");
    MethodID30 = env->GetStaticMethodID(Important , "setFriend_list" , "(Ljava/lang/String;)V");
    MethodID31 = env->GetStaticMethodID(Important , "setSend_message" ,
                                        "(Ljava/lang/String;)V");
    MethodID32 = env->GetStaticMethodID(Important , "setGet_message" , "(Ljava/lang/String;)V");
    MethodID33 = env->GetStaticMethodID(Important , "setDelete_message" ,
                                        "(Ljava/lang/String;)V");
    MethodID34 = env->GetStaticMethodID(Important , "setUpload_offline_message" ,
                                        "(Ljava/lang/String;)V");
    MethodID35 = env->GetStaticMethodID(Important , "setPremium_features" ,
                                        "(Ljava/lang/String;)V");
    MethodID36 = env->GetStaticMethodID(Important , "setBuy_premium" , "(Ljava/lang/String;)V");
    MethodID37 = env->GetStaticMethodID(Important , "setReferrals" , "(Ljava/lang/String;)V");
    MethodID38 = env->GetStaticMethodID(Important , "setReference_balance_withdraw" ,
                                        "(Ljava/lang/String;)V");
    MethodID39 = env->GetStaticMethodID(Important , "setRecharge" , "(Ljava/lang/String;)V");
    MethodID40 = env->GetStaticMethodID(Important , "setTutorial_list" ,
                                        "(Ljava/lang/String;)V");
    MethodID41 = env->GetStaticMethodID(Important , "setWatch_tutorial" ,
                                        "(Ljava/lang/String;)V");
    MethodID42 = env->GetStaticMethodID(Important , "setEnter_vault" , "(Ljava/lang/String;)V");
    MethodID43 = env->GetStaticMethodID(Important , "setOpen_vault" , "(Ljava/lang/String;)V");
    MethodID44 = env->GetStaticMethodID(Important , "setUpload_to_vault" ,
                                        "(Ljava/lang/String;)V");
    MethodID45 = env->GetStaticMethodID(Important , "setCreate_vault" ,
                                        "(Ljava/lang/String;)V");
    MethodID46 = env->GetStaticMethodID(Important , "setGet_notification" ,
                                        "(Ljava/lang/String;)V");
    MethodID47 = env->GetStaticMethodID(Important , "setActivate_previous_premium" ,
                                        "(Ljava/lang/String;)V");
    MethodID48 = env->GetStaticMethodID(Important , "setBuy_page" , "(Ljava/lang/String;)V");
    MethodID49 = env->GetStaticMethodID(Important , "setRetrive_request_list" ,
                                        "(Ljava/lang/String;)V");
    MethodID50 = env->GetStaticMethodID(Important , "setAccept_friend" , "(Ljava/lang/String;)V");

    MethodID51 = env->GetStaticMethodID(Important , "setChangeprofilename" ,
                                        "(Ljava/lang/String;)V");
    MethodID52 = env->GetStaticMethodID(Important , "setChangeprofilepicture" ,
                                        "(Ljava/lang/String;)V");
    MethodID53 = env->GetStaticMethodID(Important , "setChangepagename" , "(Ljava/lang/String;)V");
    MethodID54 = env->GetStaticMethodID(Important , "setChangephonenumber" ,
                                        "(Ljava/lang/String;)V");
    MethodID55 = env->GetStaticMethodID(Important , "setViewprofilepicture" ,
                                        "(Ljava/lang/String;)V");
    MethodID56 = env->GetStaticMethodID(Important , "setXmppHost" ,
                                        "(Ljava/lang/String;)V");
    MethodID57 = env->GetStaticMethodID(Important , "setXmppPort" ,
                                        "(Ljava/lang/String;)V");
    MethodID58 = env->GetStaticMethodID(Important , "setXmppResource" ,
                                        "(Ljava/lang/String;)V");
    MethodID59 = env->GetStaticMethodID(Important , "setPublickey" ,
                                        "(Ljava/lang/String;)V");
    MethodID60 = env->GetStaticMethodID(Important , "setPrivatekey" ,
                                        "(Ljava/lang/String;)V");

    MethodID61 = env->GetStaticMethodID(Important , "setGetNotification" , "(Ljava/lang/String;)V");

    MethodID62 = env->GetStaticMethodID(Important , "setUpdateprofilepassword" ,
                                        "(Ljava/lang/String;)V");
    MethodID63 = env->GetStaticMethodID(Important , "setCreatepagerecoverycode" ,
                                        "(Ljava/lang/String;)V");
    MethodID64 = env->GetStaticMethodID(Important , "setChillengepagerecoverycode" ,
                                        "(Ljava/lang/String;)V");
    MethodID65 = env->GetStaticMethodID(Important , "setUploadpagerecoveryimage" ,
                                        "(Ljava/lang/String;)V");
    MethodID66 = env->GetStaticMethodID(Important , "setDeletepagesecurityimage" ,
                                        "(Ljava/lang/String;)V");
    MethodID67 = env->GetStaticMethodID(Important , "setDownloadpagesecurityimage" ,
                                        "(Ljava/lang/String;)V");
    MethodID68 = env->GetStaticMethodID(Important , "setChillengepagesecurityimage" ,
                                        "(Ljava/lang/String;)V");
    MethodID69 = env->GetStaticMethodID(Important , "setGetpagesecurityquestions" ,
                                        "(Ljava/lang/String;)V");
    MethodID70 = env->GetStaticMethodID(Important , "setAnswerpagesecurityquestion" ,
                                        "(Ljava/lang/String;)V");
    MethodID71 = env->GetStaticMethodID(Important , "setViewansweredsecurityquestions" ,
                                        "(Ljava/lang/String;)V");
    MethodID72 = env->GetStaticMethodID(Important , "setDeleteansweredsecurityquestions" ,
                                        "(Ljava/lang/String;)V");
    MethodID73 = env->GetStaticMethodID(Important , "setGetchillengerpagesecurityquestions" ,
                                        "(Ljava/lang/String;)V");
    MethodID74 = env->GetStaticMethodID(Important , "setChillengerpagesecurityquestions" ,
                                        "(Ljava/lang/String;)V");
    MethodID75 = env->GetStaticMethodID(Important , "setCratepageresetcertificate" ,
                                        "(Ljava/lang/String;)V");

}


void CloseActivity(JNIEnv *env , jobject context) {
    env->CallStaticVoidMethod(Function , closeActivity , context);
}

void initver(JNIEnv *env) {

    jclass temp1 = env->FindClass("com/horofbd/MeCloak/Functions");
    jclass temp2 = env->FindClass("com/horofbd/MeCloak/Important");
    jclass temp3 = env->FindClass("org/json/JSONObject");
    jclass temp4 = env->FindClass("org/json/JSONArray");

    Function = (jclass) env->NewGlobalRef(temp1);
    Important = (jclass) env->NewGlobalRef(temp2);
    JSONObject = (jclass) env->NewGlobalRef(temp3);
    JSONArray = (jclass) env->NewGlobalRef(temp4);

    isJSONArrayNull = env->GetStaticMethodID(Function,"isJSONArrayNull", "(Lorg/json/JSONArray;)Z");
    closeActivity = env->GetStaticMethodID(Function , "CloseActivity" ,
                                           "(Landroid/content/Context;)V");

    UploadFile = env->GetStaticMethodID(Function , "UploadFile" ,
                                        "(Lcom/horofbd/MeCloak/ServerResponse;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/io/File;Lorg/json/JSONObject;I)V");

    showProgress = env->GetStaticMethodID(Function , "showProgress" ,
                                          "(Landroid/content/Context;)V");
    dismissProgress = env->GetStaticMethodID(Function , "dismissDialogue" , "()V");

    jsonPut = env->GetMethodID(JSONObject , "put" ,
                               "(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;");
    jsonarraygetjsonobject = env->GetMethodID(JSONArray , "getJSONObject" ,
                                              "(I)Lorg/json/JSONObject;");
    jsonarraylength = env->GetMethodID(JSONArray , "length" , "()I");
    getInt = env->GetMethodID(JSONObject , "getInt" , "(Ljava/lang/String;)I");

    getJSONArray = env->GetMethodID(JSONObject , "getJSONArray" ,
                                    "(Ljava/lang/String;)Lorg/json/JSONArray;");

    formattime = env->GetStaticMethodID(Function , "formattime" ,
                                        "(Ljava/lang/String;)Ljava/lang/String;");
    getIpaddress = env->GetStaticMethodID(Function , "getIpAddress" ,
                                          "(Landroid/content/Context;)Ljava/lang/String;");
    newJSONObject = env->GetMethodID(JSONObject , "<init>" , "(Ljava/lang/String;)V");
    isNull = env->GetMethodID(JSONObject , "isNull" , "(Ljava/lang/String;)Z");
    has = env->GetMethodID(JSONObject , "has" , "(Ljava/lang/String;)Z");
    logcat = env->GetStaticMethodID(Function , "Logcat" ,
                                    "(Ljava/lang/String;Ljava/lang/String;)V");
    getJsonObject = env->GetMethodID(JSONObject , "getJSONObject" ,
                                     "(Ljava/lang/String;)Lorg/json/JSONObject;");
    getString = env->GetMethodID(JSONObject , "getString" ,
                                 "(Ljava/lang/String;)Ljava/lang/String;");
    SetSharedPreference = env->GetStaticMethodID(Function , "setSharedPreference" ,
                                                 "(Ljava/lang/String;Ljava/lang/String;)V");
    GetSharedPreference = env->GetStaticMethodID(Function , "getSharedPreference" ,
                                                 "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    ClearSharedPreference = env->GetStaticMethodID(Function , "ClearSharedPreference" , "()V");
    IsInternetAvailable = env->GetStaticMethodID(Function , "isInternetAvailable" , "()Z");
    Base64Decode = env->GetStaticMethodID(Function , "Base64decode" ,
                                          "(Ljava/lang/String;)Ljava/lang/String;");
    Base64Encode = env->GetStaticMethodID(Function , "Base64encode" ,
                                          "(Ljava/lang/String;)Ljava/lang/String;");
    CheckMD5 = env->GetStaticMethodID(Function , "checkMD5" ,
                                      "(Ljava/lang/String;Ljava/io/File;)Z");
    CalculateMD5 = env->GetStaticMethodID(Function , "calculateMD5" ,
                                          "(Ljava/io/File;)Ljava/lang/String;");
    ShowToast = env->GetStaticMethodID(Function , "ShowToast" ,
                                       "(Landroid/content/Context;Ljava/lang/String;)V");
    IsJSONValid = env->GetStaticMethodID(Function , "isJSONValid" , "(Ljava/lang/String;)Z");
    getCurrentTimeInMilliseconds = env->GetStaticMethodID(Function ,
                                                          "getCurrentTimeInMilliseconds" ,
                                                          "()Ljava/lang/String;");
    Request = env->GetStaticMethodID(Function , "Request" ,
                                     "(Lcom/horofbd/MeCloak/ServerResponse;Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;I)V");

    initlinks(env);

}


void showProgressBar(JNIEnv *env , jobject context) {
    env->CallStaticVoidMethod(Function , showProgress , context);
}

void dismissProgressBar(JNIEnv *env) {
    env->CallStaticVoidMethod(Function , dismissProgress);
}


std::wstring
textHash(const std::wstring &text , const std::string &password , const std::string &action) {
    //hashes
    std::wstring password_md5;
    std::wstring password_sha1;

    std::string smd5(md5(password));
    std::string ssha1(sha1(password));
    password_md5.assign(smd5.begin() , smd5.end());
    password_sha1.assign(ssha1.begin() , ssha1.end());

    // declaring character array
    wchar_t text_array[text.length() + 1];
    wchar_t password_md5_array[password_md5.length() + 1];
    wchar_t password_sha1_array[password_sha1.length() + 1];

    // declaring int array
    int text_int_array[text.length() + 1];
    int password_int_md5_array[password_md5.length() + 1];
    int password_int_sha1_array[password_sha1.length() + 1];

    //copy to array
    wcscpy(text_array , text.c_str());
    wcscpy(password_md5_array , password_md5.c_str());
    wcscpy(password_sha1_array , password_sha1.c_str());

    //create ASCII array
    int m = 0;
    for (int i = 0; i < password_md5.length(); i++) {
        password_int_md5_array[i] = password_md5_array[i];


    }
    for (int i2 = 0; i2 < password_sha1.length(); i2++) {
        password_int_sha1_array[i2] = password_sha1_array[i2];

    }
    for (int i3 = 0; i3 < text.length(); i3++) {
        text_int_array[i3] = text_array[i3];

    }

    wchar_t result_array[text.length() + 1];
    if (action == "encode") {
        for (int i4 = 0; i4 < text.length(); i4++) {
            result_array[i4] =
                    text_int_array[i4] + password_int_md5_array[i4 % password_md5.length()] +
                    password_int_sha1_array[i4 % password_sha1.length()];
        }
    } else {
        for (int i4 = 0; i4 < text.length(); i4++) {
            result_array[i4] = (text_int_array[i4] -
                                password_int_md5_array[i4 % password_md5.length()] -
                                password_int_sha1_array[i4 % password_sha1.length()]);
        }
    }
    int i;
    std::wstring Return;
    for (i = 0; i < text.length(); i++) {
        Return += result_array[i];
    }
    return Return;
}

std::string jstring2string(JNIEnv *env , jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass , "getBytes" ,
                                                "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr , getBytes ,
                                                                       env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte *pBytes = env->GetByteArrayElements(stringJbytes , NULL);

    std::string ret = std::string((char *) pBytes , length);
    env->ReleaseByteArrayElements(stringJbytes , pBytes , JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

jstring getJstringFromWstring(JNIEnv *env , std::wstring somestring) {

    wchar_t *input = const_cast<wchar_t *>(somestring.c_str());
    jobject bb = env->NewDirectByteBuffer((void *) input , wcslen(input) * sizeof(wchar_t));

    jclass cls_Charset = env->FindClass("java/nio/charset/Charset");
    jmethodID mid_Charset_forName = env->GetStaticMethodID(cls_Charset , "forName" ,
                                                           "(Ljava/lang/String;)Ljava/nio/charset/Charset;");
    jobject charset = env->CallStaticObjectMethod(cls_Charset , mid_Charset_forName ,
                                                  env->NewStringUTF("UTF-32LE"));

    jmethodID mid_Charset_decode = env->GetMethodID(cls_Charset , "decode" ,
                                                    "(Ljava/nio/ByteBuffer;)Ljava/nio/CharBuffer;");
    jobject cb = env->CallObjectMethod(charset , mid_Charset_decode , bb);

    jclass cls_CharBuffer = env->FindClass("java/nio/CharBuffer");
    jmethodID mid_CharBuffer_toString = env->GetMethodID(cls_CharBuffer , "toString" ,
                                                         "()Ljava/lang/String;");
    jstring ret = static_cast<jstring>(env->CallObjectMethod(cb , mid_CharBuffer_toString));

    return ret;


}

std::wstring jstring2wstring(JNIEnv *env , jstring string) {
    std::wstring wStr;
    if (string == NULL) {
        return wStr; // empty string
    }

    try {
        const jchar *raw = env->GetStringChars(string , NULL);
        if (raw != NULL) {
            jsize len = env->GetStringLength(string);
            wStr.assign(raw , raw + len);
            env->ReleaseStringChars(string , raw);
        }
    }
    catch (const std::exception ex) {
        std::cout << "EXCEPTION in jstr2wsz translating string input " << string << std::endl;
        std::cout << "exception: " << ex.what() << std::endl;
    }
    return wStr;
}

jstring wstring2jstring(JNIEnv *env , std::wstring cstr) {
    jstring result = nullptr;
    try {
        int len = cstr.size();
        jchar *raw = new jchar[len];
        memcpy(raw , cstr.c_str() , len * sizeof(wchar_t));
        result = env->NewString(raw , len);
        delete[] raw;
        return result;
    }
    catch (const std::exception ex) {
        std::wcout << L"EXCEPTION in wsz2jstr translating string input " << cstr << std::endl;
        std::cout << "exception: " << ex.what() << std::endl;
    }

    return result;
}

void SetUpLinks(JNIEnv *env) {
    Important = env->FindClass("com/horofbd/MeCloak/Important");

    env->CallStaticVoidMethod(Important , MethodID , env->NewStringUTF(baseUrl.c_str()));
    env->CallStaticVoidMethod(Important , MethodID1 , env->NewStringUTF(advertise_links.c_str()));
    env->CallStaticVoidMethod(Important , MethodID2 , env->NewStringUTF(advertise_watch.c_str()));
    env->CallStaticVoidMethod(Important , MethodID3 , env->NewStringUTF(advertise_end.c_str()));
    env->CallStaticVoidMethod(Important , MethodID4 , env->NewStringUTF(profile_register.c_str()));
    env->CallStaticVoidMethod(Important , MethodID5 , env->NewStringUTF(page_register.c_str()));
    env->CallStaticVoidMethod(Important , MethodID6 , env->NewStringUTF(profile_login.c_str()));
    env->CallStaticVoidMethod(Important , MethodID7 , env->NewStringUTF(page_login.c_str()));
    env->CallStaticVoidMethod(Important , MethodID8 , env->NewStringUTF(page_delete.c_str()));
    env->CallStaticVoidMethod(Important , MethodID9 , env->NewStringUTF(profile_logout.c_str()));
    env->CallStaticVoidMethod(Important , MethodID10 , env->NewStringUTF(verify_email.c_str()));
    env->CallStaticVoidMethod(Important , MethodID11 ,
                              env->NewStringUTF(phone_verification.c_str()));
    env->CallStaticVoidMethod(Important , MethodID12 ,
                              env->NewStringUTF(forgot_profile_password.c_str()));
    env->CallStaticVoidMethod(Important , MethodID13 ,
                              env->NewStringUTF(forgot_page_password.c_str()));
    env->CallStaticVoidMethod(Important , MethodID14 ,
                              env->NewStringUTF(validate_reset_password_otp.c_str()));
    env->CallStaticVoidMethod(Important , MethodID15 , env->NewStringUTF(prepare_offline.c_str()));
    env->CallStaticVoidMethod(Important , MethodID16 ,
                              env->NewStringUTF(upload_page_security_image.c_str()));
    env->CallStaticVoidMethod(Important , MethodID17 ,
                              env->NewStringUTF(upload_page_security_question.c_str()));
    env->CallStaticVoidMethod(Important , MethodID18 ,
                              env->NewStringUTF(page_security_image.c_str()));
    env->CallStaticVoidMethod(Important , MethodID19 ,
                              env->NewStringUTF(update_page_security_question.c_str()));
    env->CallStaticVoidMethod(Important , MethodID20 ,
                              env->NewStringUTF(create_page_recovery_certificate.c_str()));
    env->CallStaticVoidMethod(Important , MethodID21 , env->NewStringUTF(token_refresh.c_str()));
    env->CallStaticVoidMethod(Important , MethodID22 , env->NewStringUTF(search_friend.c_str()));
    env->CallStaticVoidMethod(Important , MethodID23 , env->NewStringUTF(add_friend.c_str()));
    env->CallStaticVoidMethod(Important , MethodID24 , env->NewStringUTF(block_friend.c_str()));
    env->CallStaticVoidMethod(Important , MethodID25 ,
                              env->NewStringUTF(block_friend_list.c_str()));
    env->CallStaticVoidMethod(Important , MethodID26 , env->NewStringUTF(unfriend_friend.c_str()));
    env->CallStaticVoidMethod(Important , MethodID27 ,
                              env->NewStringUTF(cancel_friend_request.c_str()));
    env->CallStaticVoidMethod(Important , MethodID28 ,
                              env->NewStringUTF(delete_friend_request.c_str()));
    env->CallStaticVoidMethod(Important , MethodID29 , env->NewStringUTF(ignore_friend.c_str()));
    env->CallStaticVoidMethod(Important , MethodID30 , env->NewStringUTF(friend_list.c_str()));
    env->CallStaticVoidMethod(Important , MethodID31 , env->NewStringUTF(send_message.c_str()));
    env->CallStaticVoidMethod(Important , MethodID32 , env->NewStringUTF(get_message.c_str()));
    env->CallStaticVoidMethod(Important , MethodID33 , env->NewStringUTF(delete_message.c_str()));
    env->CallStaticVoidMethod(Important , MethodID34 ,
                              env->NewStringUTF(upload_offline_message.c_str()));
    env->CallStaticVoidMethod(Important , MethodID35 , env->NewStringUTF(premium_features.c_str()));
    env->CallStaticVoidMethod(Important , MethodID36 , env->NewStringUTF(buy_premium.c_str()));
    env->CallStaticVoidMethod(Important , MethodID37 , env->NewStringUTF(referrals.c_str()));
    env->CallStaticVoidMethod(Important , MethodID38 ,
                              env->NewStringUTF(reference_balance_withdraw.c_str()));
    env->CallStaticVoidMethod(Important , MethodID39 , env->NewStringUTF(recharge.c_str()));
    env->CallStaticVoidMethod(Important , MethodID40 , env->NewStringUTF(tutorial_list.c_str()));
    env->CallStaticVoidMethod(Important , MethodID41 , env->NewStringUTF(watch_tutorial.c_str()));
    env->CallStaticVoidMethod(Important , MethodID42 , env->NewStringUTF(enter_vault.c_str()));
    env->CallStaticVoidMethod(Important , MethodID43 , env->NewStringUTF(open_vault.c_str()));
    env->CallStaticVoidMethod(Important , MethodID44 , env->NewStringUTF(upload_to_vault.c_str()));
    env->CallStaticVoidMethod(Important , MethodID45 , env->NewStringUTF(create_vault.c_str()));
    env->CallStaticVoidMethod(Important , MethodID46 , env->NewStringUTF(getNotification.c_str()));
    env->CallStaticVoidMethod(Important , MethodID47 ,
                              env->NewStringUTF(activate_previous_premium.c_str()));
    env->CallStaticVoidMethod(Important , MethodID48 , env->NewStringUTF(buy_page.c_str()));
    env->CallStaticVoidMethod(Important , MethodID49 ,
                              env->NewStringUTF(retrive_friend_request.c_str()));
    env->CallStaticVoidMethod(Important , MethodID50 ,
                              env->NewStringUTF(accept_friend.c_str()));

    env->CallStaticVoidMethod(Important , MethodID51 ,
                              env->NewStringUTF(chang_profile_name.c_str()));
    env->CallStaticVoidMethod(Important , MethodID52 ,
                              env->NewStringUTF(chang_profile_picture.c_str()));
    env->CallStaticVoidMethod(Important , MethodID53 ,
                              env->NewStringUTF(chang_page_name.c_str()));
    env->CallStaticVoidMethod(Important , MethodID54 ,
                              env->NewStringUTF(chang_phone_number.c_str()));
    env->CallStaticVoidMethod(Important , MethodID55 ,
                              env->NewStringUTF(view_profile_picture.c_str()));
    env->CallStaticVoidMethod(Important , MethodID56 ,
                              env->NewStringUTF(xmppHost.c_str()));
    env->CallStaticVoidMethod(Important , MethodID57 ,
                              env->NewStringUTF(xmppPort.c_str()));
    env->CallStaticVoidMethod(Important , MethodID58 ,
                              env->NewStringUTF(xmppResource.c_str()));
    env->CallStaticVoidMethod(Important , MethodID59 ,

                              env->NewStringUTF(publickey.c_str()));
    env->CallStaticVoidMethod(Important , MethodID60 ,
                              env->NewStringUTF(privatekey.c_str()));

    env->CallStaticVoidMethod(Important , MethodID61 ,
                              env->NewStringUTF(getNotification.c_str()));


    env->CallStaticVoidMethod(Important , MethodID62 ,
                              env->NewStringUTF(updateprofilepassword.c_str()));
    env->CallStaticVoidMethod(Important , MethodID63 ,
                              env->NewStringUTF(createpagerecoverycode.c_str()));
    env->CallStaticVoidMethod(Important , MethodID64 ,
                              env->NewStringUTF(chillengepagerecoverycode.c_str()));
    env->CallStaticVoidMethod(Important , MethodID65 ,
                              env->NewStringUTF(uploadpagesecurityimage.c_str()));
    env->CallStaticVoidMethod(Important , MethodID66 ,
                              env->NewStringUTF(deletepagesecurityimage.c_str()));
    env->CallStaticVoidMethod(Important , MethodID67 ,
                              env->NewStringUTF(downloadpagesecurityimage.c_str()));
    env->CallStaticVoidMethod(Important , MethodID68 ,
                              env->NewStringUTF(chillengepagesecurityimage.c_str()));
    env->CallStaticVoidMethod(Important , MethodID69 ,
                              env->NewStringUTF(getpagesecurityquestions.c_str()));
    env->CallStaticVoidMethod(Important , MethodID70 ,
                              env->NewStringUTF(answerpagesecurityquestion.c_str()));
    env->CallStaticVoidMethod(Important , MethodID71 ,
                              env->NewStringUTF(viewansweredsecurityquestions.c_str()));
    env->CallStaticVoidMethod(Important , MethodID72 ,
                              env->NewStringUTF(deleteansweredsecurityquestions.c_str()));
    env->CallStaticVoidMethod(Important , MethodID73 ,
                              env->NewStringUTF(getchillengerpagesecurityquestions.c_str()));
    env->CallStaticVoidMethod(Important , MethodID74 ,
                              env->NewStringUTF(chillengerpagesecurityquestions.c_str()));
    env->CallStaticVoidMethod(Important , MethodID75 ,
                              env->NewStringUTF(cratepageresetcertificate.c_str()));


}

static void setSharedPreference(JNIEnv *env , jstring key , jstring value) {
    env->CallStaticVoidMethod(Function , SetSharedPreference , key , value);
}

static void finishActivity(JNIEnv *env , jobject context) {
    CloseActivity(env,context);
}

static jstring getSharedPreference(JNIEnv *env , jstring key) {
    return (jstring) env->CallStaticObjectMethod(Function , GetSharedPreference , key ,
                                                 env->NewStringUTF("0"));
}

static void clearSharedPreference(JNIEnv *env) {
    env->CallStaticVoidMethod(Function , ClearSharedPreference);
}

static bool isInternetAvailable(JNIEnv *env) {
    return env->CallStaticBooleanMethod(Function , IsInternetAvailable);
}

static jstring base64encode(JNIEnv *env , jstring string) {
    return (jstring) env->CallStaticObjectMethod(Function , Base64Encode , string);
}

static jstring base64decode(JNIEnv *env , jstring string) {
    return (jstring) env->CallStaticObjectMethod(Function , Base64Decode , string);
}

static bool checkMD5(JNIEnv *env , jstring string , jobject jobject1) {
    return env->CallStaticBooleanMethod(Function , CheckMD5 , string , jobject1);
}

static jstring calculateMD5(JNIEnv *env , jobject string) {
    return (jstring) env->CallStaticObjectMethod(Function , CalculateMD5 , string);
}

static void request(JNIEnv *env , jobject serverResponse , jstring requestType , jstring Link ,
                    jobject jsonObject ,
                    jint requestcode , jobject context) {

    showProgressBar(env , context);
    env->CallStaticVoidMethod(Function , Request , serverResponse , requestType , Link ,
                              jsonObject , requestcode);
}


static void request(JNIEnv *env , jobject serverResponse , jstring requestType , jstring Link ,
                   jstring filetype, jobject file , jobject jsonObject ,
                    jint requestcode , jobject context) {

    showProgressBar(env , context);


    env->CallStaticVoidMethod(Function , UploadFile , serverResponse , requestType , Link ,
                              filetype,file , jsonObject , requestcode);
}

void printlogcat(JNIEnv *env , string tag , string message) {
    env->CallStaticVoidMethod(Function , logcat , env->NewStringUTF(tag.c_str()) ,
                              env->NewStringUTF(message.c_str()));
}

void startActivity(JNIEnv *env , jobject context , const string &ckey , const string &data) {

    string acivity;
    if (ckey == "Register") {
        acivity = RegisterNewUser;
    } else if (ckey == "ForgotProfilePassword") {
        acivity = ForgotProfilePassword;
    } else if (ckey == "Login") {
        acivity = LoginActivity;
    } else if (ckey == "Main") {
        acivity = MainActivity;
    } else if (ckey == "About") {
        acivity = AboutActivity;
    } else if (ckey == "AddFriend") {
        acivity = AddFriend;
    } else if (ckey == "AddOrEditPageSecurityImage") {
        acivity = AddOrEditPageSecurityImage;
    } else if (ckey == "Bot") {
        acivity = AutoBotActivity;
    } else if (ckey == "BlockList") {
        acivity = BLockListActivity;
    } else if (ckey == "Buy") {
        acivity = BuyActivity;
    } else if (ckey == "ChangeOrUpdateProfilePassword") {
        acivity = ChangeOrUpdateProfilePassword;
    } else if (ckey == "ContactUs") {
        acivity = ContactUsActivity;
    } else if (ckey == "DownloadResetCode") {
        acivity = DownloadResetCode;
    } else if (ckey == "EditPageSecurityQuestion") {
        acivity = EditPageSecurityQuestionActivity;
    } else if (ckey == "ForgotPagePassword") {
        acivity = ForgotPagePassword;
    } else if (ckey == "FriendList") {
        acivity = FriendListActivity;
    } else if (ckey == "GetPremium") {
        acivity = GetPremiumActivity;
    } else if (ckey == "GetSecretCodeOrActivity") {
        acivity = GetSecretCodeorCertificateActivity;
    } else if (ckey == "Help") {
        acivity = HelpActivity;
    } else if (ckey == "MyStuff") {
        acivity = MyStuffActivity;
    } else if (ckey == "Notification") {
        acivity = NotificationActivity;
    } else if (ckey == "PhoneNumberVerification") {
        acivity = PhoneNumberVeriricationActivity;
    } else if (ckey == "PremiumFeature") {
        acivity = PremiumFeaturesActivity;
    } else if (ckey == "Profile") {
        acivity = ProfileActivity;
    } else if (ckey == "RechargeActivity") {
        acivity = RechargeActivity;
    } else if (ckey == "ReferAFriend") {
        acivity = ReferAFriendActivity;
    } else if (ckey == "Reference") {
        acivity = ReferenceActivity;
    } else if (ckey == "ReferFriend") {
        acivity = ReferFriendActivity;
    } else if (ckey == "Referral") {
        acivity = ReferralActivity;
    } else if (ckey == "RegisterVerifiedUser") {
        acivity = RegisterVerifiedUser;
    } else if (ckey == "Settings") {
        acivity = SettingsActivity;
    } else if (ckey == "Inbox") {
        acivity = ShowUserMessage;
    } else if (ckey == "UserVerification") {
        acivity = UserVerificationActivity;
    } else if (ckey == "Vault") {
        acivity = VaultActivity;
    } else if (ckey == "WatchVideo") {
        acivity = WatchVideoActivity;
    }else if(ckey == "pageresetimage"){
        acivity = PageresetimageActivity;
    } else if(ckey == "pagerecoveryquestion"){
        acivity = EditPageSecurityQuestionActivity;
    }

    jclass native_context = env->GetObjectClass(context);
    jclass intentClass = env->FindClass("android/content/Intent");
    jclass actionString = env->FindClass(acivity.c_str());
    jmethodID newIntent = env->GetMethodID(intentClass , "<init>" ,
                                           "(Landroid/content/Context;Ljava/lang/Class;)V");
    jobject intent = env->NewObject(intentClass , newIntent , context , actionString);

    jmethodID methodFlag = env->GetMethodID(intentClass , "setFlags" ,
                                            "(I)Landroid/content/Intent;");
    //jmethodID putExtra = env->GetMethodID(intentClass,"putExtra", "(Ljava/lang/String;[Ljava/lang/String;)Landroid/content/Intent;");

    jmethodID putextra = env->GetMethodID(intentClass , "putExtra" ,
                                          "(Ljava/lang/String;Ljava/lang/CharSequence;)Landroid/content/Intent;");
    jobject intentActivity = env->CallObjectMethod(intent , methodFlag , 268435456);

    jmethodID startActivityMethodId = env->GetMethodID(native_context , "startActivity" ,
                                                       "(Landroid/content/Intent;)V");


    if (ckey == "Inbox") {
        intentActivity = (jobject) env->CallObjectMethod(intentActivity , putextra ,
                                                         env->NewStringUTF("data") ,
                                                         env->NewStringUTF(data.c_str()));
    }


    env->CallVoidMethod(context , startActivityMethodId , intentActivity);


}

static void showToast(JNIEnv *env , jobject context , jstring string) {
    env->CallStaticVoidMethod(Function , ShowToast , context , string);
}

static bool isJsonValid(JNIEnv *env , jstring string) {
    return env->CallStaticBooleanMethod(Function , IsJSONValid , string);
}

static jstring currentTimeInMills(JNIEnv *env) {
    return (jstring) env->CallStaticObjectMethod(Function , getCurrentTimeInMilliseconds);
}

jstring getIpAddress(JNIEnv *env , jobject context) {
    return (jstring) env->CallStaticObjectMethod(Function , getIpaddress , context);
}

jstring FormatTime(JNIEnv *env , jstring jstring1) {
    return (jstring) env->CallStaticObjectMethod(Function , formattime , jstring1);
}

jstring GetDeviceName(JNIEnv *env) {
    // Fetch the device model as name for now
    jclass build_class = env->FindClass("android/os/Build");
    jfieldID model_id = env->GetStaticFieldID(build_class , "MODEL" , "Ljava/lang/String;");
    jstring model_obj = (jstring) env->GetStaticObjectField(build_class , model_id);
    return model_obj;
}

void LoginRequest(JNIEnv *env , jobject context , jobject serverresponse , jstring phone ,
                  jstring password , jint requestcode) {

    showProgressBar(env , context);

    setSharedPreference(env , env->NewStringUTF("sec") , password);

    jmethodID jmethodId = env->GetStaticMethodID(Function , "LoginRegisteredUser" ,
                                                 "(Lcom/horofbd/MeCloak/ServerResponse;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V");
    env->CallStaticVoidMethod(Function , jmethodId , serverresponse , phone , password ,
                              env->NewStringUTF(profile_login.c_str()) ,
                              env->NewStringUTF("android") , GetDeviceName(env) ,
                              getIpAddress(env , context) ,
                              FormatTime(env , currentTimeInMills(env)) , requestcode);
}

void saveLoginData(JNIEnv *env , jobject context , jstring response) {
    initver(env);
    if (env->CallStaticBooleanMethod(Function , IsJSONValid , response)) {
        jobject temp = env->NewObject(JSONObject , newJSONObject , response);

        if (env->CallBooleanMethod(temp , has , env->NewStringUTF("response"))) {
            printlogcat(env , "sometag" , "responseavailable");

            jobject object = env->CallObjectMethod(temp , getJsonObject ,
                                                   env->NewStringUTF("response"));

            if (!env->CallBooleanMethod(object , isNull , env->NewStringUTF("user"))) {
                printlogcat(env , "sometag" , "usernotnull");
                jobject jobject1 = env->CallObjectMethod(object , getJsonObject ,
                                                         env->NewStringUTF("user"));
                jstring access_token = (jstring) env->CallObjectMethod(object , getString ,
                                                                       env->NewStringUTF(
                                                                               "access_token"));
                string accesstokenvalidator = jstring2string(env , access_token);
                if (!accesstokenvalidator.empty()) {
                    printlogcat(env , "sometag" , "accesstokenavailable");
                    jstring token_type = (jstring) env->CallObjectMethod(object , getString ,
                                                                         env->NewStringUTF(
                                                                                 "token_type"));
                    jstring token_expiry = (jstring) env->CallObjectMethod(object , getString ,
                                                                           env->NewStringUTF(
                                                                                   "expires_in"));

                    jstring user_id = (jstring) env->CallObjectMethod(jobject1 , getString ,
                                                                      env->NewStringUTF("id"));
                    jstring user_name = (jstring) env->CallObjectMethod(jobject1 , getString ,
                                                                        env->NewStringUTF("name"));
                    jstring user_ref = (jstring) env->CallObjectMethod(jobject1 , getString ,
                                                                       env->NewStringUTF("ref"));
                    jstring phone_no = (jstring) env->CallObjectMethod(jobject1 , getString ,
                                                                       env->NewStringUTF(
                                                                               "phone_no"));
                    jstring login_token_expiry = (jstring) env->CallObjectMethod(jobject1 ,
                                                                                 getString ,
                                                                                 env->NewStringUTF(
                                                                                         "login_token_expiry"));
                    jstring main_balance = (jstring) env->CallObjectMethod(jobject1 , getString ,
                                                                           env->NewStringUTF(
                                                                                   "main_balance"));
                    jstring referral_balance = (jstring) env->CallObjectMethod(jobject1 ,
                                                                               getString ,
                                                                               env->NewStringUTF(
                                                                                       "referral_balance"));
                    jstring recharge_balance = (jstring) env->CallObjectMethod(jobject1 ,
                                                                               getString ,
                                                                               env->NewStringUTF(
                                                                                       "recharge_balance"));
                    jstring view_ad_point_balance = (jstring) env->CallObjectMethod(jobject1 ,
                                                                                    getString ,
                                                                                    env->NewStringUTF(
                                                                                            "view_ad_point_balance"));
                    jstring created_at = (jstring) env->CallObjectMethod(jobject1 , getString ,
                                                                         env->NewStringUTF(
                                                                                 "created_at"));
                    jstring updated_at = (jstring) env->CallObjectMethod(jobject1 , getString ,
                                                                         env->NewStringUTF(
                                                                                 "updated_at"));
                    jstring premium;
                    if (env->CallBooleanMethod(object , isNull , env->NewStringUTF("premium"))) {
                        premium = env->NewStringUTF("Null");
                    } else {
                        premium = (jstring) env->CallObjectMethod(object , getString ,
                                                                  env->NewStringUTF("premium"));
                    }


                    printlogcat(env , "tag1" , jstring2string(env , user_id));
                    printlogcat(env , "tag2" , jstring2string(env , user_name));
                    printlogcat(env , "tag3" , jstring2string(env , user_ref));
                    printlogcat(env , "tag4" , jstring2string(env , phone_no));
                    printlogcat(env , "tag5" , jstring2string(env , login_token_expiry));
                    printlogcat(env , "tag6" , jstring2string(env , main_balance));
                    printlogcat(env , "tag7" , jstring2string(env , referral_balance));
                    printlogcat(env , "tag8" , jstring2string(env , recharge_balance));
                    printlogcat(env , "tag9" , jstring2string(env , view_ad_point_balance));
                    printlogcat(env , "tag10" , jstring2string(env , created_at));
                    printlogcat(env , "tag11" , jstring2string(env , updated_at));
                    printlogcat(env , "tag12" , jstring2string(env , premium));

                    setSharedPreference(env , env->NewStringUTF("access_token") , access_token);

                    setSharedPreference(env , env->NewStringUTF("login_token_expiry") ,
                                        login_token_expiry);
                    setSharedPreference(env , env->NewStringUTF("referral_balance") ,
                                        referral_balance);
                    setSharedPreference(env , env->NewStringUTF("recharge_balance") ,
                                        recharge_balance);
                    setSharedPreference(env , env->NewStringUTF("view_ad_point_balance") ,
                                        view_ad_point_balance);
                    setSharedPreference(env , env->NewStringUTF("created_at") , created_at);
                    setSharedPreference(env , env->NewStringUTF("updated_at") , updated_at);
                    setSharedPreference(env , env->NewStringUTF("premium") , premium);
                    setSharedPreference(env , env->NewStringUTF("main_balance") , main_balance);

                    setSharedPreference(env , env->NewStringUTF("token_type") , token_type);
                    setSharedPreference(env , env->NewStringUTF("expires_in") , token_expiry);
                    setSharedPreference(env , env->NewStringUTF("login_time") ,
                                        currentTimeInMills(env));
                    setSharedPreference(env , env->NewStringUTF("user_id") , user_id);
                    setSharedPreference(env , env->NewStringUTF("user_name") , user_name);
                    setSharedPreference(env , env->NewStringUTF("user_ref") , user_ref);
                    setSharedPreference(env , env->NewStringUTF("phone_no") , phone_no);
                    setSharedPreference(env , env->NewStringUTF("login_status") ,
                                        env->NewStringUTF("true"));

                    if (env->CallBooleanMethod(object , has , env->NewStringUTF("error"))) {

                        printlogcat(env , "sometag" , "haserror");


                        setSharedPreference(env , env->NewStringUTF("code") ,
                                            (jstring) env->CallObjectMethod(object , getString ,
                                                                            env->NewStringUTF(
                                                                                    "code")));
                        startActivity(env , context , "PhoneNumberVerification" , "");

                        CloseActivity(env , context);

                    } else {
                        startActivity(env , context , "UserVerification" , "");
                        CloseActivity(env , context);

                    }

                } else {
                    showToast(env , context , env->NewStringUTF("Error Logging In!"));
                }
            } else {
                showToast(env , context , env->NewStringUTF("Error Logging In!"));
            }
        } else {
            showToast(env , context , env->NewStringUTF("Error Logging In!"));
        }
    } else {
        showToast(env , context , env->NewStringUTF("Username Or Password Didn't Matched!"));
    }
}
///////////////////////////////////////////////////////todo:this function should be deleted before release////////////////////////





void CheckResponse(JNIEnv *env , jobject ServerResponse , jobject context , jstring response ,
                   jint requestcode) {

    dismissProgressBar(env);

    printlogcat(env , "response" , jstring2string(env , response));
    if (isJsonValid(env , response)) {
        jobject jsonobject = env->NewObject(JSONObject , newJSONObject , response);
        switch (requestcode) {
            case 0: {

                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    showToast(env , context , env->NewStringUTF("Account Created Successfully!"));
                    LoginRequest(env , context , ServerResponse ,
                                 getSharedPreference(env , env->NewStringUTF("phone_no")) ,
                                 getSharedPreference(env , env->NewStringUTF("password")) ,
                                 1);

                    CloseActivity(env , context);
                } else {
                    showToast(env , context , env->NewStringUTF("Phone Number Already Taken!"));
                }
                break;
            }
            case 1: {
                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    printlogcat(env , "sometag" , "response");
                    jobject jsonobject1 = env->CallObjectMethod(jsonobject , getJsonObject ,
                                                                env->NewStringUTF("response"));

                    if (env->CallBooleanMethod(jsonobject1 , has ,
                                               env->NewStringUTF("access_token"))) {
                        printlogcat(env , "sometag" , "response2");
                        if (!env->CallBooleanMethod(jsonobject1 , isNull ,
                                                    env->NewStringUTF("user"))) {
                            printlogcat(env , "sometag" , "response3");
                            saveLoginData(env , context , response);
                        }
                    }
                } else {
                    showToast(env , context , env->NewStringUTF("Username Or Password Error!"));
                }

                break;
            }
            case 2: {
                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    clearSharedPreference(env);
                    startActivity(env , context , "Login" , "");
                    CloseActivity(env , context);
                }
                break;
            }
            case 3: {
                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    startActivity(env , context , "UserVerification" , "");
                    CloseActivity(env , context);
                } else {
                    showToast(env , context , response);
                }
                break;
            }
            case 4: {

                //user verification

                if (!env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("error"))) {
                    setSharedPreference(env , env->NewStringUTF("page_no") ,
                                        (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                        env->NewStringUTF(
                                                                                "page_no")));
                    if (env->CallBooleanMethod(jsonobject , isNull ,
                                               env->NewStringUTF("security_image"))) {
                        setSharedPreference(env , env->NewStringUTF("security_image") ,
                                            env->NewStringUTF("null"));
                    } else {
                        setSharedPreference(env , env->NewStringUTF("security_image") ,
                                            (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                            env->NewStringUTF(
                                                                                    "security_image")));
                    }
                    setSharedPreference(env , env->NewStringUTF("page_name") ,
                                        (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                        env->NewStringUTF("name")));
                    setSharedPreference(env , env->NewStringUTF("page_id") ,
                                        (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                        env->NewStringUTF("id")));
                    setSharedPreference(env , env->NewStringUTF("user_id") ,
                                        (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                        env->NewStringUTF(
                                                                                "user_id")));
                    setSharedPreference(env , env->NewStringUTF("created_at") ,
                                        (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                        env->NewStringUTF(
                                                                                "created_at")));
                    setSharedPreference(env , env->NewStringUTF("updated_at") ,
                                        (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                        env->NewStringUTF(
                                                                                "updated_at")));

                    startActivity(env , context , "Main" , "");
                    CloseActivity(env , context);
                } else {
                    showToast(env , context , response);
                }
                break;
            }
            case 5: {


                //search friend
                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("error"))) {
                    showToast(env , context , env->NewStringUTF("Number Not Found!"));
                } else {
                    jobject responseobject = env->CallObjectMethod(jsonobject , getJsonObject ,
                                                                   env->NewStringUTF("response"));
                    jstring id = (jstring) env->CallObjectMethod(responseobject , getString ,
                                                                 env->NewStringUTF("id"));
                    jstring name = (jstring) env->CallObjectMethod(responseobject , getString ,
                                                                   env->NewStringUTF("name"));
                    jstring phone = (jstring) env->CallObjectMethod(responseobject , getString ,
                                                                    env->NewStringUTF("phone_no"));
                    jclass jclass1 = env->FindClass("com/horofbd/MeCloak/AddFriend");
                    jmethodID showProfileDialogue = env->GetStaticMethodID(jclass1 ,
                                                                           "showProfileDialogue" ,
                                                                           "(Lcom/horofbd/MeCloak/ServerResponse;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V");
                    env->CallStaticVoidMethod(jclass1 , showProfileDialogue , ServerResponse ,
                                              name , phone , id ,
                                              getSharedPreference(env ,
                                                                  env->NewStringUTF("page_id")));

                }
                break;
            }
            case 6: {

                //send friend request

                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    showToast(env , context , env->NewStringUTF("Request Sent!"));
                } else {
                    showToast(env , context , env->NewStringUTF("Failed!"));
                }
                break;
            }
            case 7: {

                //block friend

                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    showToast(env , context , env->NewStringUTF("Blocked!"));
                } else {
                    showToast(env , context , env->NewStringUTF("Failed!"));
                }
                break;
            }
            case 8: {

                //logout

                clearSharedPreference(env);
                startActivity(env , context , "Login" , "");
                env->CallStaticVoidMethod(Function , closeActivity , context);


                break;
            }
            case 9: {


                //contact request notification
                jobject tempobject = env->CallObjectMethod(jsonobject , getJsonObject ,
                                                           env->NewStringUTF("response"));
                jint currentpage = (jint) env->CallIntMethod(tempobject , getInt ,
                                                             env->NewStringUTF("current_page"));
                jint total = (jint) env->CallIntMethod(tempobject , getInt ,
                                                       env->NewStringUTF("total"));
                if (total == 0) {
                    showToast(env , context , env->NewStringUTF("No Friend Request Available!"));
                } else {
                    jobject jsonArray = env->CallObjectMethod(tempobject , getJSONArray ,
                                                              env->NewStringUTF("data"));
                    jint length = (jint) env->CallIntMethod(jsonArray , jsonarraylength);

                    jclass arrayListClass = env->FindClass("java/util/ArrayList");
                    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass , "<init>" ,
                                                                      "()V");
                    jmethodID addMethod = env->GetMethodID(arrayListClass , "add" ,
                                                           "(Ljava/lang/Object;)Z");

                    // The list we're going to return:
                    jobject list = env->NewObject(arrayListClass , arrayListConstructor);

                    jstring nextpageurl;
                    if (!env->CallBooleanMethod(tempobject , isNull ,
                                                env->NewStringUTF("next_page_url"))) {
                        nextpageurl = (jstring) env->CallObjectMethod(tempobject , getString ,
                                                                      env->NewStringUTF(
                                                                              "next_page_url"));
                    } else {
                        nextpageurl = env->NewStringUTF("");
                    }

                    jmethodID nextpage = env->GetStaticMethodID(
                            env->FindClass("com/horofbd/MeCloak/ContactRequest") ,
                            "setNextpageurl" , "(Ljava/lang/String;)V");
                    env->CallStaticVoidMethod(env->FindClass("com/horofbd/MeCloak/ContactRequest") ,
                                              nextpage , nextpageurl);

                    // Add it to the list
                    for (int i = 0; i < length; i++) {
                        jobject tempjsonobject = env->CallObjectMethod(jsonArray ,
                                                                       jsonarraygetjsonobject , i);
                        jstring friendtableid = (jstring) env->CallObjectMethod(tempjsonobject ,
                                                                                getString ,
                                                                                env->NewStringUTF(
                                                                                        "id"));
                        jobject frienduser = env->CallObjectMethod(tempjsonobject , getJsonObject ,
                                                                   env->NewStringUTF("from_user"));
                        jmethodID map = env->GetStaticMethodID(Function , "map" ,
                                                               "(Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;)Lorg/json/JSONObject;");
                        env->CallBooleanMethod(list , addMethod ,
                                               env->CallStaticObjectMethod(Function , map ,
                                                                           env->NewStringUTF(
                                                                                   "table_id") ,
                                                                           friendtableid ,
                                                                           frienduser));

                        if (i == length - 1) {
                            jclass ContactRequest = env->FindClass(
                                    "com/horofbd/MeCloak/ContactRequest");
                            jmethodID SetUpData = env->GetStaticMethodID(ContactRequest ,
                                                                         "setUpData" ,
                                                                         "(Ljava/util/ArrayList;)V");
                            env->CallStaticVoidMethod(ContactRequest , SetUpData , list);
                        }
                    }
                }
                break;
            }
            case 10: {

                //accept friend request

                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    showToast(env , context , env->NewStringUTF("Accepted!"));
                } else {
                    showToast(env , context ,
                              env->NewStringUTF("There was an error accepting this request!"));
                }
                break;
            }
            case 11: {

                // Decline friend Request

                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    showToast(env , context , env->NewStringUTF("Declined!"));
                } else {
                    showToast(env , context , env->NewStringUTF("Error occured!"));
                }
                break;
            }
            case 12: {

                //get Chat List

                jint currentpage = (jint) env->CallIntMethod(jsonobject , getInt ,
                                                             env->NewStringUTF("current_page"));
                jint total = (jint) env->CallIntMethod(jsonobject , getInt ,
                                                       env->NewStringUTF("total"));


                if (total == 0) {
                    showToast(env , context , env->NewStringUTF("Friend List Empty!"));
                } else {

                    jstring nextlink;
                    jstring nextpageurl;
                    if (!env->CallBooleanMethod(jsonobject , isNull ,
                                                env->NewStringUTF("next_page_url"))) {
                        nextpageurl = (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                      env->NewStringUTF(
                                                                              "next_page_url"));
                    } else {
                        nextpageurl = env->NewStringUTF("");
                    }


                    jmethodID nextpage = env->GetStaticMethodID(
                            env->FindClass("com/horofbd/MeCloak/MainActivity") , "setNextpageurl" ,
                            "(Ljava/lang/String;)V");
                    env->CallStaticVoidMethod(env->FindClass("com/horofbd/MeCloak/MainActivity") ,
                                              nextpage , nextpageurl);


                    jobject jsonArray = env->CallObjectMethod(jsonobject , getJSONArray ,
                                                              env->NewStringUTF("data"));
                    jint length = (jint) env->CallIntMethod(jsonArray , jsonarraylength);

                    jclass arrayListClass = env->FindClass("java/util/ArrayList");
                    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass , "<init>" ,
                                                                      "()V");
                    jmethodID addMethod = env->GetMethodID(arrayListClass , "add" ,
                                                           "(Ljava/lang/Object;)Z");

                    // The list we're going to return:
                    jobject list = env->NewObject(arrayListClass , arrayListConstructor);


                    for (int i = 0; i < length; i++) {

                        jobject tempjsonobject = env->CallObjectMethod(jsonArray ,
                                                                       jsonarraygetjsonobject , i);

                        if (!env->CallBooleanMethod(tempjsonobject , isNull ,
                                                    env->NewStringUTF("friend"))) {
                            jobject friendobject = env->CallObjectMethod(tempjsonobject ,
                                                                         getJsonObject ,
                                                                         env->NewStringUTF(
                                                                                 "friend"));

                            jint friendtableid = (jint) env->CallIntMethod(friendobject ,
                                                                           getInt ,
                                                                           env->NewStringUTF(
                                                                                   "id"));

                            jint pagefriendtableid = (jint) env->CallIntMethod(tempjsonobject ,
                                                                               getInt ,
                                                                               env->NewStringUTF(
                                                                                       "id"));


                            jobject fromuser = env->CallObjectMethod(friendobject , getJsonObject ,
                                                                     env->NewStringUTF(
                                                                             "from_user"));

                            jobject touser = env->CallObjectMethod(friendobject , getJsonObject ,
                                                                   env->NewStringUTF("to_user"));


                            jstring to_status = (jstring) env->CallObjectMethod(tempjsonobject ,
                                                                                getString ,
                                                                                env->NewStringUTF(
                                                                                        "to_status"));
                            jstring from_status = (jstring) env->CallObjectMethod(tempjsonobject ,
                                                                                  getString ,
                                                                                  env->NewStringUTF(
                                                                                          "from_status"));


                            printlogcat(env , "tostatus" , jstring2string(env , to_status));
                            printlogcat(env , "fromstatus" , jstring2string(env , from_status));

                            jmethodID map = env->GetStaticMethodID(Function , "map" ,
                                                                   "(Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;)Lorg/json/JSONObject;");

                            if (jstring2string(env , (jstring) env->CallObjectMethod(fromuser ,
                                                                                     getString ,
                                                                                     env->NewStringUTF(
                                                                                             "phone_no"))) ==
                                jstring2string(env , (jstring) getSharedPreference(env ,
                                                                                   env->NewStringUTF(
                                                                                           "phone_no")))) {

                                jobject listadd = env->CallStaticObjectMethod(Function , map ,
                                                                              env->NewStringUTF(
                                                                                      "table_id") ,
                                                                              env->NewStringUTF(
                                                                                      to_string(
                                                                                              friendtableid).c_str()) ,
                                                                              touser);
                                jobject listadd1 = env->CallStaticObjectMethod(Function , map ,
                                                                               env->NewStringUTF(
                                                                                       "page_friend_id") ,
                                                                               env->NewStringUTF(
                                                                                       to_string(
                                                                                               pagefriendtableid).c_str()) ,
                                                                               listadd);


                                jobject listdd2 = env->CallStaticObjectMethod(Function , map ,
                                                                              env->NewStringUTF(
                                                                                      "status") ,
                                                                              from_status ,
                                                                              listadd1);


                                env->CallBooleanMethod(list , addMethod , listdd2);


                            } else {

                                jobject listadd = env->CallStaticObjectMethod(Function , map ,
                                                                              env->NewStringUTF(
                                                                                      "table_id") ,
                                                                              env->NewStringUTF(
                                                                                      to_string(
                                                                                              friendtableid).c_str()) ,
                                                                              fromuser);
                                jobject listadd1 = env->CallStaticObjectMethod(Function , map ,
                                                                               env->NewStringUTF(
                                                                                       "page_friend_id") ,
                                                                               env->NewStringUTF(
                                                                                       to_string(
                                                                                               pagefriendtableid).c_str()) ,
                                                                               listadd);

                                jobject listdd2 = env->CallStaticObjectMethod(Function , map ,
                                                                              env->NewStringUTF(
                                                                                      "status") ,
                                                                              to_status , listadd1);

                                env->CallBooleanMethod(list , addMethod , listdd2);
                            }

                            if (i == length - 1) {
                                printlogcat(env , "setupdata" , "setupdata");
                                jclass MainActivity = env->FindClass(
                                        "com/horofbd/MeCloak/MainActivity");
                                jmethodID SetUpData = env->GetStaticMethodID(MainActivity ,
                                                                             "setUpData" ,
                                                                             "(Ljava/util/ArrayList;)V");
                                env->CallStaticVoidMethod(MainActivity , SetUpData , list);
                            }
                        }
                    }

                }
                break;

            }
            case 13: {
                //get Friend List
                jint currentpage = (jint) env->CallIntMethod(jsonobject , getInt ,
                                                             env->NewStringUTF("current_page"));
                jint total = (jint) env->CallIntMethod(jsonobject , getInt ,
                                                       env->NewStringUTF("total"));


                jstring nextpageurl;
                if (!env->CallBooleanMethod(jsonobject , isNull ,
                                            env->NewStringUTF("next_page_url"))) {
                    nextpageurl = (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                  env->NewStringUTF(
                                                                          "next_page_url"));
                } else {
                    nextpageurl = env->NewStringUTF("");
                }

                if (total == 0) {
                    showToast(env , context , env->NewStringUTF("Friend List Empty!"));
                } else {
                    jobject jsonArray = env->CallObjectMethod(jsonobject , getJSONArray ,
                                                              env->NewStringUTF("data"));
                    jint length = (jint) env->CallIntMethod(jsonArray , jsonarraylength);

                    jclass arrayListClass = env->FindClass("java/util/ArrayList");
                    jmethodID arrayListConstructor = env->GetMethodID(arrayListClass , "<init>" ,
                                                                      "()V");
                    jmethodID addMethod = env->GetMethodID(arrayListClass , "add" ,
                                                           "(Ljava/lang/Object;)Z");

                    // The list we're going to return:
                    jobject list = env->NewObject(arrayListClass , arrayListConstructor);

                    jmethodID nextpage = env->GetStaticMethodID(
                            env->FindClass("com/horofbd/MeCloak/FriendListActivity") ,
                            "setNextpageurl" , "(Ljava/lang/String;)V");
                    env->CallStaticVoidMethod(
                            env->FindClass("com/horofbd/MeCloak/FriendListActivity") , nextpage ,
                            nextpageurl);


                    for (int i = 0; i < length; i++) {

                        jobject tempjsonobject = env->CallObjectMethod(jsonArray ,
                                                                       jsonarraygetjsonobject , i);

                        jobject friendobject = env->CallObjectMethod(tempjsonobject ,
                                                                     getJsonObject ,
                                                                     env->NewStringUTF("friend"));


                        if (!env->CallBooleanMethod(tempjsonobject , isNull ,
                                                    env->NewStringUTF("friend"))) {

                            jint friendtableid = (jint) env->CallIntMethod(friendobject ,
                                                                           getInt ,
                                                                           env->NewStringUTF(
                                                                                   "id"));

                            jint pagefriendtableid = (jint) env->CallIntMethod(tempjsonobject ,
                                                                               getInt ,
                                                                               env->NewStringUTF(
                                                                                       "id"));


                            jobject fromuser = env->CallObjectMethod(friendobject , getJsonObject ,
                                                                     env->NewStringUTF(
                                                                             "from_user"));

                            jobject touser = env->CallObjectMethod(friendobject , getJsonObject ,
                                                                   env->NewStringUTF("to_user"));

                            jmethodID map = env->GetStaticMethodID(Function , "map" ,
                                                                   "(Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;)Lorg/json/JSONObject;");

                            if (jstring2string(env ,
                                               (jstring) env->CallObjectMethod(fromuser ,
                                                                               getString ,
                                                                               env->NewStringUTF(
                                                                                       "phone_no"))) ==
                                jstring2string(env , (jstring) getSharedPreference(env ,
                                                                                   env->NewStringUTF(
                                                                                           "phone_no")))) {


                                jobject listadd = env->CallStaticObjectMethod(Function , map ,
                                                                              env->NewStringUTF(
                                                                                      "table_id") ,
                                                                              env->NewStringUTF(
                                                                                      to_string(
                                                                                              friendtableid).c_str()) ,
                                                                              touser);
                                jobject listadd1 = env->CallStaticObjectMethod(Function , map ,
                                                                               env->NewStringUTF(
                                                                                       "page_friend_id") ,
                                                                               env->NewStringUTF(
                                                                                       to_string(
                                                                                               pagefriendtableid).c_str()) ,
                                                                               listadd);


                                env->CallBooleanMethod(list , addMethod , listadd1);

                            } else {


                                jobject listadd = env->CallStaticObjectMethod(Function , map ,
                                                                              env->NewStringUTF(
                                                                                      "table_id") ,
                                                                              env->NewStringUTF(
                                                                                      to_string(
                                                                                              friendtableid).c_str()) ,
                                                                              fromuser);
                                jobject listadd1 = env->CallStaticObjectMethod(Function , map ,
                                                                               env->NewStringUTF(
                                                                                       "page_friend_id") ,
                                                                               env->NewStringUTF(
                                                                                       to_string(
                                                                                               pagefriendtableid).c_str()) ,
                                                                               listadd);


                                env->CallBooleanMethod(list , addMethod , listadd1);


                            }

                            if (i == length - 1) {
                                jclass MainActivity = env->FindClass(
                                        "com/horofbd/MeCloak/FriendListActivity");
                                jmethodID SetUpData = env->GetStaticMethodID(MainActivity ,
                                                                             "setUpData" ,
                                                                             "(Ljava/util/ArrayList;)V");
                                env->CallStaticVoidMethod(MainActivity , SetUpData , list);
                            }
                        }
                    }
                }
                break;
            }
            case 14: {
                //unfriend
                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    showToast(env , context , env->NewStringUTF("Unfriend Successfull!"));
                } else {
                    showToast(env , context , env->NewStringUTF("Error occured!"));
                }
                break;
            }
            case 15: {
                jobject tempobject = env->CallObjectMethod(jsonobject , getJSONArray ,
                                                           env->NewStringUTF("response"));
                jint length = (jint) env->CallIntMethod(tempobject , jsonarraylength);

                jclass arrayListClass = env->FindClass("java/util/ArrayList");
                jmethodID arrayListConstructor = env->GetMethodID(arrayListClass , "<init>" ,
                                                                  "()V");
                jmethodID addMethod = env->GetMethodID(arrayListClass , "add" ,
                                                       "(Ljava/lang/Object;)Z");

                // The list we're going to return:
                jobject list = env->NewObject(arrayListClass , arrayListConstructor);


                for (int i = 0; i < length; i++) {
                    jobject object = env->CallObjectMethod(tempobject , jsonarraygetjsonobject , i);
                    env->CallBooleanMethod(list , addMethod , object);
                }

                jclass TutorialActivity = env->FindClass("com/horofbd/MeCloak/TutorialActivity");
                jmethodID setUpdata = env->GetStaticMethodID(TutorialActivity , "setUpData" ,
                                                             "(Ljava/util/ArrayList;)V");

                env->CallStaticVoidMethod(TutorialActivity , setUpdata , list);


                break;
            }
            case 17: {
                jstring result = (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                 env->NewStringUTF("response"));
                if (jstring2string(env , result) == "Successfully changed") {
                    jclass UserVerificationactivity = env->FindClass(
                            "com/horofbd/MeCloak/UserVerificationActivity");
                    jmethodID setText = env->GetStaticMethodID(UserVerificationactivity ,
                                                               "setNewName" , "()V");
                    env->CallStaticVoidMethod(UserVerificationactivity , setText);
                } else {
                    showToast(env , context , env->NewStringUTF("Name Change Failed!"));
                }

                break;
            }
            case 18: {
                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    jstring result = (jstring) env->CallObjectMethod(jsonobject , getString ,
                                                                     env->NewStringUTF("response"));
                    if (jstring2string(env , result) == "Successfully Updated") {
                        jclass UserVerificationactivity = env->FindClass(
                                "com/horofbd/MeCloak/UserVerificationActivity");
                        jmethodID setText = env->GetStaticMethodID(UserVerificationactivity ,
                                                                   "setProfilePicture" , "()V");
                        env->CallStaticVoidMethod(UserVerificationactivity , setText);
                    } else {
                        showToast(env , context ,
                                  env->NewStringUTF("Iamge Upload or Change Failed!"));
                    }
                } else {
                    showToast(env , context , env->NewStringUTF("Iamge Upload or Change Failed!"));
                }

                break;
            }
            case 19: {
                if (env->CallBooleanMethod(jsonobject , has , env->NewStringUTF("response"))) {
                    showToast(env , context , env->NewStringUTF("Seal Successfull!"));
                } else {
                    showToast(env , context , env->NewStringUTF("Error occured!"));
                }
                break;
            }
            case 22: {


                jobject responseobject = env->CallObjectMethod(jsonobject , getJsonObject ,
                                                               env->NewStringUTF("response"));
                jint currentpage = (jint) env->CallIntMethod(responseobject , getInt ,
                                                             env->NewStringUTF("current_page"));
//                jint total = (jint) env->CallIntMethod(jsonobject , getInt ,
//                                                       env->NewStringUTF("total"));

                jobject dataArray = env->CallObjectMethod(responseobject , getJSONArray ,
                                                          env->NewStringUTF("data"));
                jint length = (jint) env->CallIntMethod(dataArray , jsonarraylength);

                jclass arrayListClass = env->FindClass("java/util/ArrayList");
                jmethodID arrayListConstructor = env->GetMethodID(arrayListClass , "<init>" ,
                                                                  "()V");
                jmethodID addMethod = env->GetMethodID(arrayListClass , "add" ,
                                                       "(Ljava/lang/Object;)Z");

                jobject list = env->NewObject(arrayListClass , arrayListConstructor);
                jstring nextpageurl;
                if (!env->CallBooleanMethod(responseobject , isNull ,
                                            env->NewStringUTF("next_page_url"))) {
                    nextpageurl = (jstring) env->CallObjectMethod(responseobject , getString ,
                                                                  env->NewStringUTF(
                                                                          "next_page_url"));
                } else {
                    nextpageurl = env->NewStringUTF("");
                }

                jmethodID nextpage = env->GetStaticMethodID(
                        env->FindClass("com/horofbd/MeCloak/NotificationFragment") ,
                        "setNextpageurl" , "(Ljava/lang/String;)V");
                env->CallStaticVoidMethod(
                        env->FindClass("com/horofbd/MeCloak/NotificationFragment") , nextpage ,
                        nextpageurl);

                for (int i = 0; i < length; i++) {
                    jobject tempnotificationobject = env->CallObjectMethod(dataArray ,
                                                                           jsonarraygetjsonobject ,
                                                                           i);

                    jstring type = (jstring) env->CallObjectMethod(tempnotificationobject ,
                                                                   getString ,
                                                                   env->NewStringUTF("type"));
                    jstring message = (jstring) env->CallObjectMethod(tempnotificationobject ,
                                                                      getString ,
                                                                      env->NewStringUTF("message"));
                    jstring created_at = (jstring) env->CallObjectMethod(tempnotificationobject ,
                                                                         getString ,
                                                                         env->NewStringUTF(
                                                                                 "created_at"));


                    jmethodID newJSon = env->GetStaticMethodID(Function , "getInstanse" ,
                                                               "()Lorg/json/JSONObject;");

                    jobject newjsonobject = env->CallStaticObjectMethod(Function , newJSon);

                    jmethodID map = env->GetStaticMethodID(Function , "map" ,
                                                           "(Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;)Lorg/json/JSONObject;");


                    jobject object = env->CallStaticObjectMethod(Function , map ,
                                                                 env->NewStringUTF(
                                                                         "type") ,
                                                                 type ,
                                                                 newjsonobject);

                    jobject object1 = env->CallStaticObjectMethod(Function , map ,
                                                                  env->NewStringUTF(
                                                                          "message") ,
                                                                  message ,
                                                                  object);
                    jobject object2 = env->CallStaticObjectMethod(Function , map ,
                                                                  env->NewStringUTF(
                                                                          "created_at") ,
                                                                  created_at ,
                                                                  object1);

                    printlogcat(env , "data1nat" , jstring2string(env , nextpageurl));

                    env->CallBooleanMethod(list , addMethod , object2);


                    if (i == length - 1) {
                        jclass NotificationRequest = env->FindClass(
                                "com/horofbd/MeCloak/NotificationFragment");
                        jmethodID SetUpData = env->GetStaticMethodID(NotificationRequest ,
                                                                     "setUpData" ,
                                                                     "(Ljava/util/ArrayList;)V");
                        env->CallStaticVoidMethod(NotificationRequest , SetUpData , list);
                        dismissProgressBar(env);
                    }

                }

                break;
            }
            case 23: {
                //Update Profile Password

                break;
            }
            case 24: {
                //Create page reset code
                break;
            }
            case 25:{
                //forgot profile password
                jobject jobject1 = env->CallObjectMethod(jsonobject,getJsonObject,env->NewStringUTF("response"));
                jobject tempobject = env->CallObjectMethod(jobject1,getJsonObject,env->NewStringUTF("forgot-id"));


                break;
            }
            case 26:{
                //upload page reset picture
                break;
            }
            case 27:{
                //delete page security image

                break;
            }case 28:{

                //parse security questions and answer
                if(env->CallBooleanMethod(jsonobject,has,env->NewStringUTF("response"))){
                    jobject jsonarray = env->CallObjectMethod(jsonobject,getJSONArray,env->NewStringUTF("response"));
                    if(env->CallStaticBooleanMethod(Function,isJSONArrayNull,jsonarray)){
                        printlogcat(env,"tag","null");
                        jmethodID newjsonobject = env->GetStaticMethodID(Function,"getInstanse", "()Lorg/json/JSONObject;");
                        jobject jobject1 = env->CallStaticObjectMethod(Function,newjsonobject);
                        request(env,ServerResponse,env->NewStringUTF("GET"),env->NewStringUTF(getpagesecurityquestions.c_str()),jobject1,29,context);
                    } else{
                        printlogcat(env,"tag","not null");
                    }
                } else{
                    showToast(env,context,env->NewStringUTF("Unauthorised!"));
                    finishActivity(env,context);
                }

                break;
            }case 29:{
                //purse security question
                jobject jsonarray = env->CallObjectMethod(jsonobject,getJSONArray,env->NewStringUTF("response"));

                jint length = (jint) env->CallIntMethod(jsonarray , jsonarraylength);

                jclass arrayListClass = env->FindClass("java/util/ArrayList");
                jmethodID arrayListConstructor = env->GetMethodID(arrayListClass , "<init>" ,
                                                                  "()V");
                jmethodID addMethod = env->GetMethodID(arrayListClass , "add" ,
                                                       "(Ljava/lang/Object;)Z");

                jobject list = env->NewObject(arrayListClass , arrayListConstructor);

                for(int i=0;i<length;i++){
                    jobject tempobject = env->CallObjectMethod(jsonarray,jsonarraygetjsonobject,i);
                    env->CallBooleanMethod(list , addMethod , tempobject);
                    if(i == length-1){
                        jclass jclass1 = env->FindClass("com/horofbd/MeCloak/EditPageSecurityQuestionActivity");
                        jmethodID setdata = env->GetStaticMethodID(jclass1,"setUpQuestionParse", "(Ljava/util/ArrayList;)V");
                        env->CallStaticVoidMethod(jclass1,setdata,list);
                    }
                }
                break;
            }
        }
    } else if (requestcode == 16) {
        jclass Tutorial = env->FindClass("com/horofbd/MeCloak/TutorialActivity");
        jmethodID setUpText = env->GetStaticMethodID(Tutorial , "setTextData" ,
                                                     "(Ljava/lang/String;)V");
        env->CallStaticVoidMethod(Tutorial , setUpText , response);
    } else if (requestcode == 20) {
        jmethodID writeToFile = env->GetStaticMethodID(Function , "writeToFile" ,
                                                       "(Ljava/lang/String;Landroid/content/Context;Ljava/lang/String;)V");
        env->CallStaticVoidMethod(Function , writeToFile , response , context ,
                                  env->NewStringUTF("private.key"));
        dismissProgressBar(env);
    } else if (requestcode == 21) {
        jmethodID writeToFile = env->GetStaticMethodID(Function , "writeToFile" ,
                                                       "(Ljava/lang/String;Landroid/content/Context;Ljava/lang/String;)V");
        string name = publickeyname + ".key";
        printlogcat(env , "name" , name);
        env->CallStaticVoidMethod(Function , writeToFile , response , context ,
                                  env->NewStringUTF(name.c_str()));
        dismissProgressBar(env);
    }

}


extern "C" JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_MainActivity_stringFromJNI(JNIEnv *env , jobject  /* this */) {
    std::wstring s = L"Read more at: https://bangla.asianetnews.com/";
    //std::wstring s = L"Read more at: https://bangla.asianetnews.com/";
    std::wstring hash = textHash(s , "z" , "encode");
    std::wstring deHash = textHash(hash , "z" , "decode");
    std::cout << deHash.length() << ", " << hash.length() << std::endl;
    // someFunction();
    jstring string = getJstringFromWstring(env , deHash);
    return string;
}

extern "C" JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_MainActivity_tryencode(JNIEnv *env , jobject , jstring message ,
                                                jstring type ,
                                                jstring password) {
    std::wstring wstr = jstring2wstring(env , message);
    //  __android_log_print(ANDROID_LOG_ERROR, "sometag", "%s",
    //         jstring2string(env, getJstringFromWstring(env, wstr)).c_str());
    std::string pass = jstring2string(env , password);
    std::string typ = jstring2string(env , type);
    jstring encoded = getJstringFromWstring(env , textHash(wstr , pass , typ));
    return encoded;

}

extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_MainActivity_globalRequest(JNIEnv *env , jclass clazz ,
                                                    jobject serverresponse ,
                                                    jstring requesttype ,
                                                    jstring link , jobject jsonobject ,
                                                    jint requestcode , jobject context) {
    request(env , serverresponse , requesttype , link , jsonobject , requestcode , context);
}



extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_LoginActivity_SaveLogindata(JNIEnv *env , jclass clazz , jstring response ,
                                                     jobject context) {
    saveLoginData(env , context , response);
}



extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_RegisterNewUser_CheckRegisterData(JNIEnv *env , jclass clazz ,
                                                           jobject ServerResponse ,
                                                           jobject Context ,
                                                           jstring response , jint Requestcode) {
    initver(env);

    if (env->CallStaticBooleanMethod(Function , IsJSONValid , response)) {
        if (Requestcode == 0) {
            jobject object = env->NewObject(JSONObject , newJSONObject , response);
            if (env->CallBooleanMethod(object , isNull , env->NewStringUTF("user"))) {
                env->CallStaticVoidMethod(Function , ShowToast , Context ,
                                          env->NewStringUTF("Registration Successfull."));
                CheckResponse(env , ServerResponse , Context , response , Requestcode);
            } else {
                env->CallStaticVoidMethod(Function , ShowToast , Context , response);
            }
        } else if (Requestcode == 1) {
            CheckResponse(env , ServerResponse , Context , response , Requestcode);
        }
    } else {
        env->CallStaticVoidMethod(Function , ShowToast , Context , response);
    }

}

extern "C" JNIEXPORT __unused  void JNICALL
Java_com_horofbd_MeCloak_LoginActivity_LoginRequest(JNIEnv *env , jclass clazz , jobject context ,
                                                    jobject serverresponse ,
                                                    jstring phone , jstring password ,
                                                    jint requestcode) {
    initver(env);
    clearSharedPreference(env);
    LoginRequest(env , context , serverresponse , phone , password , requestcode);
}

extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_RegisterNewUser_RegisterRequest(JNIEnv *env , jclass clazz ,
                                                         jobject serverresponse ,
                                                         jstring phone , jstring name ,
                                                         jstring password ,
                                                         jstring confirmpassword ,
                                                         jstring reference ,
                                                         jstring termsandconditions ,
                                                         jstring policy ,
                                                         jint requestcode , jobject context) {
    initver(env);


    showProgressBar(env , context);
    jmethodID jmethodId = env->GetStaticMethodID(Function , "RequestForProfileRegistration" ,
                                                 "(Lcom/horofbd/MeCloak/ServerResponse;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;I)V");

    setSharedPreference(env , env->NewStringUTF("phone_no") , phone);
    setSharedPreference(env , env->NewStringUTF("password") , password);

    env->CallStaticVoidMethod(Function , jmethodId , serverresponse , phone , name , password ,
                              env->NewStringUTF(profile_register.c_str()) , confirmpassword ,
                              termsandconditions , policy , reference , requestcode);

}

extern "C" JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_LoginActivity_GetLoginInfo(JNIEnv *env , jclass clazz , jstring key) {
    initver(env);
    jstring result = env->NewStringUTF("0");
    string ckey = jstring2string(env , key);
    if (ckey == "PublicToken") {
        result = getSharedPreference(env , env->NewStringUTF("access_token"));
    } else if (ckey == "RegisterStatus") {
        result = getSharedPreference(env , env->NewStringUTF("login_status"));
    } else if (ckey == "TokenType") {
        result = getSharedPreference(env , env->NewStringUTF("token_type"));
    } else if (ckey == "TokenExpiry") {
        result = getSharedPreference(env , env->NewStringUTF("expires_in"));
    } else if (ckey == "UserID") {
        result = getSharedPreference(env , env->NewStringUTF("user_id"));
    } else if (ckey == "UserReference") {
        result = getSharedPreference(env , env->NewStringUTF("user_ref"));
    } else if (ckey == "UserName") {
        result = getSharedPreference(env , env->NewStringUTF("user_name"));
    } else if (ckey == "UserPhoneNumber") {
        result = getSharedPreference(env , env->NewStringUTF("phone_no"));
    } else if (ckey == "LoginTime") {
        result = getSharedPreference(env , env->NewStringUTF("login_time"));
    }
    return result;

}

extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_LoginActivity_StartActivity(JNIEnv *env , jclass clazz , jobject Context ,
                                                     jstring Class) {
    startActivity(env , Context , jstring2string(env , Class) , "");
}
extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_RegisterNewUser_StartActivity(JNIEnv *env , jclass clazz ,
                                                       jobject Context ,
                                                       jstring Class) {
    startActivity(env , Context , jstring2string(env , Class) , "");
}
extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_MainActivity_StartActivity(JNIEnv *env , jclass clazz , jobject Context ,
                                                    jstring Class , jstring data) {

    startActivity(env , Context , jstring2string(env , Class) , jstring2string(env , data));


}

extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_UserVerificationActivity_StartActivity(JNIEnv *env , jclass clazz ,
                                                                jobject Context ,
                                                                jstring Class) {
    startActivity(env , Context , jstring2string(env , Class) , "");
}

extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_PhoneNumberVerificationActivity_StartActivity(JNIEnv *env , jclass clazz ,
                                                                       jobject Context ,
                                                                       jstring Class) {
    startActivity(env , Context , jstring2string(env , Class) , "");
}





extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_PhoneNumberVerificationActivity_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_PhoneNumberVerificationActivity_CheckResponse(JNIEnv *env , jclass clazz ,
                                                                       jobject server_response ,
                                                                       jobject context ,
                                                                       jstring response ,
                                                                       jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);

}
extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_PhoneNumberVerificationActivity_RequestPhoneverification(JNIEnv *env ,
                                                                                  jclass clazz ,
                                                                                  jobject serverresponse ,
                                                                                  jstring requesttype ,
                                                                                  jstring link ,
                                                                                  jobject jsonobject ,
                                                                                  jint requestcode ,
                                                                                  jobject context) {
    request(env , serverresponse , requesttype , link , jsonobject , requestcode , context);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_RegisterVerifiedUser_StartActivity(JNIEnv *env , jclass clazz ,
                                                            jobject context , jstring activity) {

    startActivity(env , context , jstring2string(env , activity) , "");

}


extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_RegisterVerifiedUser_globalRequest(JNIEnv *env , jclass clazz ,
                                                            jobject server_response ,
                                                            jstring requesttype , jstring link ,
                                                            jobject json_object ,
                                                            jint requestcode , jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);

}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_RegisterVerifiedUser_CheckResponse(JNIEnv *env , jclass clazz ,
                                                            jobject server_response ,
                                                            jobject context ,
                                                            jstring response , jint requestcode) {

    CheckResponse(env , server_response , context , response , requestcode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_RegisterVerifiedUser_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_UserVerificationActivity_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_UserVerificationActivity_globalRequest(JNIEnv *env , jclass clazz ,
                                                                jobject server_response ,
                                                                jstring requesttype , jstring link ,
                                                                jobject json_object ,
                                                                jint requestcode ,
                                                                jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_UserVerificationActivity_CheckResponse(JNIEnv *env , jclass clazz ,
                                                                jobject server_response ,
                                                                jobject context , jstring response ,
                                                                jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_AddFriend_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_AddFriend_CheckResponse(JNIEnv *env , jclass clazz ,
                                                 jobject server_response ,
                                                 jobject context , jstring response ,
                                                 jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_AddFriend_StartActivity(JNIEnv *env , jclass clazz , jobject context ,
                                                 jstring activity , jstring data) {
    startActivity(env , context , jstring2string(env , activity) , jstring2string(env , data));
}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_AddFriend_globalRequest(JNIEnv *env , jclass clazz ,
                                                 jobject server_response ,
                                                 jstring requesttype , jstring link ,
                                                 jobject json_object , jint requestcode ,
                                                 jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_Animation_StartActivity(JNIEnv *env , jclass clazz , jobject context ,
                                                 jstring activity) {
    startActivity(env , context , jstring2string(env , activity) , "");
    jclass ANimation = env->FindClass("com/horofbd/MeCloak/Animation");
    jmethodID finish = env->GetStaticMethodID(ANimation , "closeActivtiy" , "()V");
    env->CallStaticVoidMethod(ANimation , finish);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_NotificationActivity_CheckResponse(JNIEnv *env , jclass clazz ,
                                                            jobject server_response ,
                                                            jobject context , jstring response ,
                                                            jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_NotificationActivity_StartActivity(JNIEnv *env , jclass clazz ,
                                                            jobject context , jstring activity ,
                                                            jstring data) {
    startActivity(env , context , jstring2string(env , activity) , jstring2string(env , data));
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_NotificationActivity_globalRequest(JNIEnv *env , jclass clazz ,
                                                            jobject server_response ,
                                                            jstring requesttype , jstring link ,
                                                            jobject json_object ,
                                                            jint requestcode , jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_NotificationActivity_getLoginInfo(JNIEnv *env , jclass clazz ,
                                                           jstring key) {
    return getSharedPreference(env , key);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_MainActivity_InitLinks(JNIEnv *env , jclass clazz , jobject context) {
    jmethodID initcerts = env->GetStaticMethodID(Function , "INITCRTS" ,
                                                 "(Landroid/content/Context;)V");
    env->CallStaticVoidMethod(Function , initcerts , context);

    initver(env);
    SetUpLinks(env);
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_MainActivity_getLoginInfo(JNIEnv *env , jclass clazz , jstring key) {
    return getSharedPreference(env , key);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_MainActivity_CheckResponse(JNIEnv *env , jclass clazz ,
                                                    jobject server_response , jobject context ,
                                                    jstring response , jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ProfileActivity_StartActivity(JNIEnv *env , jclass clazz ,
                                                       jobject context , jstring activity ,
                                                       jstring data) {
    startActivity(env , context , jstring2string(env , activity) , jstring2string(env , data));
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ProfileActivity_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ProfileActivity_globalRequest(JNIEnv *env , jclass clazz ,
                                                       jobject server_response ,
                                                       jstring requesttype , jstring link ,
                                                       jobject json_object , jint requestcode ,
                                                       jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ProfileActivity_CheckResponse(JNIEnv *env , jclass clazz ,
                                                       jobject server_response , jobject context ,
                                                       jstring response , jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_ProfileActivity_getLoginInfo(JNIEnv *env , jclass clazz , jstring key) {
    return getSharedPreference(env , key);

}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_FriendListActivity_StartActivity(JNIEnv *env , jclass clazz ,
                                                          jobject context , jstring activity ,
                                                          jstring data) {
    startActivity(env , context , jstring2string(env , activity) , jstring2string(env , data));
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_FriendListActivity_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_FriendListActivity_globalRequest(JNIEnv *env , jclass clazz ,
                                                          jobject server_response ,
                                                          jstring requesttype , jstring link ,
                                                          jobject json_object , jint requestcode ,
                                                          jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_FriendListActivity_CheckResponse(JNIEnv *env , jclass clazz ,
                                                          jobject server_response ,
                                                          jobject context , jstring response ,
                                                          jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_FriendListActivity_getLoginInfo(JNIEnv *env , jclass clazz , jstring key) {
    return getSharedPreference(env , key);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_TutorialActivity_StartActivity(JNIEnv *env , jclass clazz ,
                                                        jobject context , jstring activity ,
                                                        jstring data) {
    startActivity(env , context , jstring2string(env , activity) , jstring2string(env , data));
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_TutorialActivity_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_TutorialActivity_globalRequest(JNIEnv *env , jclass clazz ,
                                                        jobject server_response ,
                                                        jstring requesttype , jstring link ,
                                                        jobject json_object , jint requestcode ,
                                                        jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_TutorialActivity_CheckResponse(JNIEnv *env , jclass clazz ,
                                                        jobject server_response , jobject context ,
                                                        jstring response , jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_TutorialActivity_getLoginInfo(JNIEnv *env , jclass clazz , jstring key) {
    return getSharedPreference(env , key);
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_UserVerificationActivity_getLoginInfo(JNIEnv *env , jclass clazz ,
                                                               jstring key) {
    return getSharedPreference(env , key);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_UserVerificationActivity_UploadFile(JNIEnv *env , jclass clazz ,
                                                             jobject server_response ,
                                                             jstring requesttype , jstring link ,jstring filetype,
                                                             jobject file , jobject json_object ,
                                                             jint requestcode , jobject context) {

    request(env , server_response , requesttype , link ,filetype, file , json_object , requestcode ,
            context);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_UserVerificationActivity_ImageRequest(JNIEnv *env , jclass clazz ,
                                                               jobject image_response ,
                                                               jobject imageview ,
                                                               jstring request_type , jstring link ,
                                                               jobject json_object ,
                                                               jint requestcode) {
    jmethodID ImageRequest = env->GetStaticMethodID(Function , "ImageRequest" ,
                                                    "(Lcom/horofbd/MeCloak/ImageResponse;Lde/hdodenhof/circleimageview/CircleImageView;Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;I)V");
    env->CallStaticVoidMethod(Function , ImageRequest , image_response , imageview , request_type ,
                              link , json_object , requestcode);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_MainActivity_ImageRequest(JNIEnv *env , jclass clazz ,
                                                   jobject image_response , jobject imageview ,
                                                   jstring request_type ,
                                                   jstring link , jobject json_object ,
                                                   jint requestcode) {
    jmethodID ImageRequest = env->GetStaticMethodID(Function , "ImageRequest" ,
                                                    "(Lcom/horofbd/MeCloak/ImageResponse;Lde/hdodenhof/circleimageview/CircleImageView;Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;I)V");
    env->CallStaticVoidMethod(Function , ImageRequest , image_response , imageview , request_type ,
                              link , json_object , requestcode);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_StartActivity(JNIEnv *env , jclass clazz ,
                                                       jobject context , jstring activity ,
                                                       jstring data) {
    startActivity(env , context , jstring2string(env , activity) , jstring2string(env , data));
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_globalRequest(JNIEnv *env , jclass clazz ,
                                                       jobject server_response ,
                                                       jstring requesttype , jstring link ,
                                                       jobject json_object , jint requestcode ,
                                                       jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);

}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_CheckResponse(JNIEnv *env , jclass clazz ,
                                                       jobject server_response , jobject context ,
                                                       jstring response , jint requestcode) {

    CheckResponse(env , server_response , context , response , requestcode);

}extern "C"
JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_getLoginInfo(JNIEnv *env , jclass clazz , jstring key) {

    return getSharedPreference(env , key);

}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_ImageRequest(JNIEnv *env , jclass clazz ,
                                                      jobject image_response , jobject image_view ,
                                                      jstring request_type , jstring link ,
                                                      jobject json_object , jint requestcode) {
    jmethodID ImageRequest = env->GetStaticMethodID(Function , "ImageRequest" ,
                                                    "(Lcom/horofbd/MeCloak/ImageResponse;Lde/hdodenhof/circleimageview/CircleImageView;Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;I)V");
    env->CallStaticVoidMethod(Function , ImageRequest , image_response , image_view , request_type ,
                              link , json_object , requestcode);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_publicKeyRequest(JNIEnv *env , jclass clazz ,
                                                          jobject server_response , jstring name ,
                                                          jstring requesttype , jstring link ,
                                                          jobject json_object , jint requestcode ,
                                                          jobject context) {
    publickeyname = jstring2string(env , name);
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_EncrytpAndDecrypt(JNIEnv *env , jclass clazz ,
                                                           jstring message , jstring type ,
                                                           jstring password) {

    std::wstring wstr = jstring2wstring(env , message);


    std::string pass = jstring2string(env , password);
    std::string typ = jstring2string(env , type);
    jstring encoded = getJstringFromWstring(env , textHash(wstr , pass , typ));
    return encoded;
}extern "C"
JNIEXPORT jstring JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_DefaultED(JNIEnv *env , jclass clazz , jstring message ,
                                                   jstring type) {

    std::wstring wstr = jstring2wstring(env , message);

    std::string pass = jstring2string(env , env->NewStringUTF("password"));
    std::string typ = jstring2string(env , type);
    jstring encoded = getJstringFromWstring(env , textHash(wstr , pass , typ));

    return encoded;
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ShowUserMessage_saveUserData(JNIEnv *env , jclass clazz , jstring key ,
                                                      jstring value) {
    setSharedPreference(env , key , value);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_SettingsActivity_StartActivity(JNIEnv *env , jclass clazz ,
                                                        jobject context , jstring activity ,
                                                        jstring data) {
    startActivity(env , context , jstring2string(env , activity) , jstring2string(env , data));
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_SettingsActivity_globalRequest(JNIEnv *env , jclass clazz ,
                                                        jobject server_response ,
                                                        jstring requesttype , jstring link ,
                                                        jobject json_object , jint requestcode ,
                                                        jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_SettingsActivity_CheckResponse(JNIEnv *env , jclass clazz ,
                                                        jobject server_response , jobject context ,
                                                        jstring response , jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_SettingsActivity_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_PageResetImage_globalRequest(JNIEnv *env , jclass clazz ,
                                                      jobject server_response ,
                                                      jstring requesttype , jstring link ,
                                                      jobject json_object , jint requestcode ,
                                                      jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_PageResetImage_InitLinks(JNIEnv *env , jclass clazz , jobject context) {
    initver(env);
    SetUpLinks(env);
}

extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_PageResetImage_CheckResponse(JNIEnv *env , jclass clazz ,
                                                      jobject server_response , jobject context ,
                                                      jstring response , jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);

}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_PageResetImage_ImageRequest(JNIEnv *env , jclass clazz ,
                                                     jobject image_response , jobject image_view ,
                                                     jstring request_type , jstring link ,
                                                     jobject json_object , jint requestcode) {
    jmethodID ImageRequest = env->GetStaticMethodID(Function , "ImageRequest" ,
                                                    "(Lcom/horofbd/MeCloak/ImageResponse;Lde/hdodenhof/circleimageview/CircleImageView;Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;I)V");
    env->CallStaticVoidMethod(Function , ImageRequest , image_response , image_view , request_type ,
                              link , json_object , requestcode);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ForgotProfilePassword_globalRequest(JNIEnv *env , jclass clazz ,
                                                             jobject server_response ,
                                                             jstring requesttype , jstring link ,
                                                             jobject json_object ,
                                                             jint requestcode , jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}
extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ForgotProfilePassword_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_ForgotProfilePassword_CheckResponse(JNIEnv *env , jclass clazz ,
                                                             jobject server_response ,
                                                             jobject context , jstring response ,
                                                             jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_PageResetImage_UploadFile(JNIEnv *env , jclass clazz ,
                                                   jobject server_response , jstring requesttype ,
                                                   jstring link , jstring filetype , jobject file ,
                                                   jobject json_object , jint requestcode ,
                                                   jobject context) {
    request(env , server_response , requesttype , link ,filetype, file , json_object , requestcode ,
            context);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_EditPageSecurityQuestionActivity_globalRequest(JNIEnv *env , jclass clazz ,
                                                                        jobject server_response ,
                                                                        jstring requesttype ,
                                                                        jstring link ,
                                                                        jobject json_object ,
                                                                        jint requestcode ,
                                                                        jobject context) {
    request(env , server_response , requesttype , link , json_object , requestcode , context);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_EditPageSecurityQuestionActivity_CheckResponse(JNIEnv *env , jclass clazz ,
                                                                        jobject server_response ,
                                                                        jobject context ,
                                                                        jstring response ,
                                                                        jint requestcode) {
    CheckResponse(env , server_response , context , response , requestcode);
}extern "C"
JNIEXPORT void JNICALL
Java_com_horofbd_MeCloak_EditPageSecurityQuestionActivity_InitLinks(JNIEnv *env , jclass clazz) {
    initver(env);
    SetUpLinks(env);
}