#pragma clang diagnostic push
#pragma ide diagnostic ignored "cert-err58-cpp"
#include <jni.h>
#include <string>
#include <iostream>

#include <cstring>
#include <wchar.h>
#include <cwchar>

#include <locale>
#include <clocale>
#include "md5.h"
#include "sha1.h"
#include <android/log.h>


using namespace std;

std::wstring
textHash(const std::wstring &text, const std::string &password, const std::string &action) {
    //hashes
    std::wstring password_md5;
    std::wstring password_sha1;

    std::string smd5(md5(password));
    std::string ssha1(sha1(password));
    password_md5.assign(smd5.begin(), smd5.end());
    password_sha1.assign(ssha1.begin(), ssha1.end());

    // declaring character array
    wchar_t text_array[text.length() + 1];
    wchar_t password_md5_array[password_md5.length() + 1];
    wchar_t password_sha1_array[password_sha1.length() + 1];

    // declaring int array
    int text_int_array[text.length() + 1];
    int password_int_md5_array[password_md5.length() + 1];
    int password_int_sha1_array[password_sha1.length() + 1];

    //copy to array
    wcscpy(text_array, text.c_str());
    wcscpy(password_md5_array, password_md5.c_str());
    wcscpy(password_sha1_array, password_sha1.c_str());

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

    //calculation
    wchar_t result_array[text.length() + 1];

    __android_log_print(ANDROID_LOG_ERROR, "password_md5", "%s", md5(password).c_str());
    __android_log_print(ANDROID_LOG_ERROR, "password_sha1", "%s", sha1(password).c_str());

    if (action == "encode") {
        for (int i4 = 0; i4 < text.length(); i4++) {
            __android_log_print(ANDROID_LOG_ERROR, "matrix ", "%d %d %d \n", text_int_array[i4],
                                password_int_md5_array[i4], password_int_sha1_array[i4]);


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

    //convert array to string
    int i;
    std::wstring Return;
    for (i = 0; i < text.length(); i++) {
        Return += result_array[i];
    }
    return Return;

}

std::string jstring2string(JNIEnv *env, jstring jStr) {
    if (!jStr)
        return "";

    const jclass stringClass = env->GetObjectClass(jStr);
    const jmethodID getBytes = env->GetMethodID(stringClass, "getBytes", "(Ljava/lang/String;)[B");
    const jbyteArray stringJbytes = (jbyteArray) env->CallObjectMethod(jStr, getBytes,
                                                                       env->NewStringUTF("UTF-8"));

    size_t length = (size_t) env->GetArrayLength(stringJbytes);
    jbyte *pBytes = env->GetByteArrayElements(stringJbytes, NULL);

    std::string ret = std::string((char *) pBytes, length);
    env->ReleaseByteArrayElements(stringJbytes, pBytes, JNI_ABORT);

    env->DeleteLocalRef(stringJbytes);
    env->DeleteLocalRef(stringClass);
    return ret;
}

jstring getJstringFromWstring(JNIEnv *env, std::wstring somestring) {

    wchar_t *input = const_cast<wchar_t *>(somestring.c_str());
    jobject bb = env->NewDirectByteBuffer((void *) input, wcslen(input) * sizeof(wchar_t));

    jclass cls_Charset = env->FindClass("java/nio/charset/Charset");
    jmethodID mid_Charset_forName = env->GetStaticMethodID(cls_Charset, "forName",
                                                           "(Ljava/lang/String;)Ljava/nio/charset/Charset;");
    jobject charset = env->CallStaticObjectMethod(cls_Charset, mid_Charset_forName,
                                                  env->NewStringUTF("UTF-32LE"));

    jmethodID mid_Charset_decode = env->GetMethodID(cls_Charset, "decode",
                                                    "(Ljava/nio/ByteBuffer;)Ljava/nio/CharBuffer;");
    jobject cb = env->CallObjectMethod(charset, mid_Charset_decode, bb);

    jclass cls_CharBuffer = env->FindClass("java/nio/CharBuffer");
    jmethodID mid_CharBuffer_toString = env->GetMethodID(cls_CharBuffer, "toString",
                                                         "()Ljava/lang/String;");
    jstring ret = static_cast<jstring>(env->CallObjectMethod(cb, mid_CharBuffer_toString));

    return ret;


}

std::wstring jstring2wstring(JNIEnv *env, jstring string) {
    std::wstring wStr;
    if (string == NULL) {
        return wStr; // empty string
    }

    try {
        const jchar *raw = env->GetStringChars(string, NULL);
        if (raw != NULL) {
            jsize len = env->GetStringLength(string);
            wStr.assign(raw, raw + len);
            env->ReleaseStringChars(string, raw);
        }
    }
    catch (const std::exception ex) {
        std::cout << "EXCEPTION in jstr2wsz translating string input " << string << std::endl;
        std::cout << "exception: " << ex.what() << std::endl;
    }
    return wStr;
}

// std::wstring to jstring
jstring wstring2jstring(JNIEnv *env, std::wstring cstr) {
    jstring result = nullptr;
    try {
        int len = cstr.size();
        jchar *raw = new jchar[len];
        memcpy(raw, cstr.c_str(), len * sizeof(wchar_t));
        result = env->NewString(raw, len);
        delete[] raw;
        return result;
    }
    catch (const std::exception ex) {
        std::wcout << L"EXCEPTION in wsz2jstr translating string input " << cstr << std::endl;
        std::cout << "exception: " << ex.what() << std::endl;
    }

    return result;
}


extern "C" JNIEXPORT jstring JNICALL
Java_com_horofbd_nei_MainActivity_stringFromJNI(JNIEnv *env, jobject  /* this */) {
    std::wstring s = L"Read more at: https://bangla.asianetnews.com/";
    //std::wstring s = L"Read more at: https://bangla.asianetnews.com/";
    std::wstring hash = textHash(s, "z", "encode");
    std::wstring deHash = textHash(hash, "z", "decode");
    std::cout << deHash.length() << ", " << hash.length() << std::endl;
    // someFunction();
    jstring string = getJstringFromWstring(env, deHash);
    return string;
}


string host = "http://10.0.2.2:8000/api";
////////advertise/////////////
string advertise_links = host+"/advertise/links";
string advertise_watch = host+"/advertise/watch";
string advertise_end = host+"/advertise/end";
////////auth////////////
string profile_register = host+"/auth/register";
string page_register = host+"/auth/page-register";
string profile_login = host+"/auth/login";
string page_login = host+"/auth/page-login";
string page_delete = host+"/auth/page-delete";
string profile_logout = host+"/auth/logout";
string verify_email = host+"/auth/verify-email";
string phone_verification = host+"/auth/verify-phone";
string forgot_profile_password = host+"/auth/forget-profile-password";
string forgot_page_password = host+"/auth/forget-page-password";
string validate_reset_password_otp = host+"/auth/validate-reset-password-otp";
string prepare_offline = host+"/auth/prepare-offline";
string upload_page_security_image = host+"/auth/upload-page-security-image";
string upload_page_security_question = host+"/auth/upload-page-security-question";
string page_security_image = host+"/auth/page-security-image";
string update_page_security_question = host+"/auth/update-page-security-question";
string create_page_recovery_certificate = host+"/auth/create-page-recovery-certificate";
string token_refresh = host+"/auth/token-refresh";
//////////Friend//////////////////
string search_friend = host+"/friend/search";
string add_friend = host+"/friend/request";
string block_friend = host+"/friend/block";
string block_friend_list = host+"/friend/block-list";
string unfriend_friend = host+"/friend/unfriend";
string cancel_friend_request = host+"/friend/cancel";
string delete_friend_request = host+"/friend/delete";
string ignore_friend = host+"/friend/ignore";
string friend_list = host+"/friend/list";
/////////////message//////////////
string send_message = host+"/message/send";
string get_message = host+"/message/get";
string delete_message = host+"/message/delete";
string upload_offline_message = host+"/message/upload-offline";
/////////////premium/////////////
string premium_features = host+"/premium/features";
string buy_premium = host+"/premium/buy";
string activate_previous_premium = host+"/premium/activate";
string buy_page = host+"/premium/buy-page";
///////////profile//////////////
string referrals = host+"/profile/referrals";
/////////transection/////////////
string reference_balance_withdraw = host+"/transection/ref-withdraw";
string recharge = host+"/transaction/recharge";
///////////////tutorial/////////////////
string tutorial_list = host+"/tutorial/list";
string watch_tutorial = host+"/tutorial/watch";
///////////////////vault//////////////
string enter_vault = host+"/vault/login";
string open_vault = host+"/vault/open";
string upload_to_vault = host+"/vault/upload";
string create_vault = host+"/vault/create";
string getNotification = host+"/notification";

extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_nei_MainActivity_setUpLinks(JNIEnv *env, jobject  /* this */) {
    jclass clazz = env->FindClass("com/horofbd/nei/Important");
    jmethodID MethodID = env->GetStaticMethodID(clazz, "setHost", "(Ljava/lang/String;)V");
    jmethodID MethodID1 = env->GetStaticMethodID(clazz, "setAdvertise_links", "(Ljava/lang/String;)V");
    jmethodID MethodID2 = env->GetStaticMethodID(clazz, "setAdvertise_watch", "(Ljava/lang/String;)V");
    jmethodID MethodID3 = env->GetStaticMethodID(clazz, "setAdvertise_end", "(Ljava/lang/String;)V");
    jmethodID MethodID4 = env->GetStaticMethodID(clazz, "setProfile_register", "(Ljava/lang/String;)V");
    jmethodID MethodID5 = env->GetStaticMethodID(clazz, "setPage_register", "(Ljava/lang/String;)V");
    jmethodID MethodID6 = env->GetStaticMethodID(clazz, "setProfile_login", "(Ljava/lang/String;)V");
    jmethodID MethodID7 = env->GetStaticMethodID(clazz, "setPage_login", "(Ljava/lang/String;)V");
    jmethodID MethodID8 = env->GetStaticMethodID(clazz, "setPage_delete", "(Ljava/lang/String;)V");
    jmethodID MethodID9 = env->GetStaticMethodID(clazz, "setProfile_logout", "(Ljava/lang/String;)V");
    jmethodID MethodID10 = env->GetStaticMethodID(clazz, "setVerify_email", "(Ljava/lang/String;)V");
    jmethodID MethodID11 = env->GetStaticMethodID(clazz, "setPhone_verification", "(Ljava/lang/String;)V");
    jmethodID MethodID12 = env->GetStaticMethodID(clazz, "setForgot_profile_password", "(Ljava/lang/String;)V");
    jmethodID MethodID13 = env->GetStaticMethodID(clazz, "setForgot_page_password", "(Ljava/lang/String;)V");
    jmethodID MethodID14 = env->GetStaticMethodID(clazz, "setValidate_reset_password_otp", "(Ljava/lang/String;)V");
    jmethodID MethodID15 = env->GetStaticMethodID(clazz, "setPrepare_offline", "(Ljava/lang/String;)V");
    jmethodID MethodID16 = env->GetStaticMethodID(clazz, "setUpload_page_security_image", "(Ljava/lang/String;)V");
    jmethodID MethodID17 = env->GetStaticMethodID(clazz, "setUpload_page_security_question", "(Ljava/lang/String;)V");
    jmethodID MethodID18 = env->GetStaticMethodID(clazz, "setPage_security_image", "(Ljava/lang/String;)V");
    jmethodID MethodID19 = env->GetStaticMethodID(clazz, "setUpdate_page_security_question", "(Ljava/lang/String;)V");
    jmethodID MethodID20 = env->GetStaticMethodID(clazz, "setCreate_page_recovery_certificate", "(Ljava/lang/String;)V");
    jmethodID MethodID21 = env->GetStaticMethodID(clazz, "setToken_refresh", "(Ljava/lang/String;)V");
    jmethodID MethodID22 = env->GetStaticMethodID(clazz, "setSearch_friend", "(Ljava/lang/String;)V");
    jmethodID MethodID23 = env->GetStaticMethodID(clazz, "setAdd_friend", "(Ljava/lang/String;)V");
    jmethodID MethodID24 = env->GetStaticMethodID(clazz, "setBlock_friend", "(Ljava/lang/String;)V");
    jmethodID MethodID25 = env->GetStaticMethodID(clazz, "setBlock_friend_list", "(Ljava/lang/String;)V");
    jmethodID MethodID26 = env->GetStaticMethodID(clazz, "setUnfriend_friend", "(Ljava/lang/String;)V");
    jmethodID MethodID27 = env->GetStaticMethodID(clazz, "setCancel_friend_request", "(Ljava/lang/String;)V");
    jmethodID MethodID28 = env->GetStaticMethodID(clazz, "setDelete_friend_request", "(Ljava/lang/String;)V");
    jmethodID MethodID29 = env->GetStaticMethodID(clazz, "setIgnore_friend", "(Ljava/lang/String;)V");
    jmethodID MethodID30 = env->GetStaticMethodID(clazz, "setFriend_list", "(Ljava/lang/String;)V");
    jmethodID MethodID31 = env->GetStaticMethodID(clazz, "setSend_message", "(Ljava/lang/String;)V");
    jmethodID MethodID32 = env->GetStaticMethodID(clazz, "setGet_message", "(Ljava/lang/String;)V");
    jmethodID MethodID33 = env->GetStaticMethodID(clazz, "setDelete_message", "(Ljava/lang/String;)V");
    jmethodID MethodID34 = env->GetStaticMethodID(clazz, "setUpload_offline_message", "(Ljava/lang/String;)V");
    jmethodID MethodID35 = env->GetStaticMethodID(clazz, "setPremium_features", "(Ljava/lang/String;)V");
    jmethodID MethodID36 = env->GetStaticMethodID(clazz, "setBuy_premium", "(Ljava/lang/String;)V");
    jmethodID MethodID37 = env->GetStaticMethodID(clazz, "setReferrals", "(Ljava/lang/String;)V");
    jmethodID MethodID38 = env->GetStaticMethodID(clazz, "setReference_balance_withdraw", "(Ljava/lang/String;)V");
    jmethodID MethodID39 = env->GetStaticMethodID(clazz, "setRecharge", "(Ljava/lang/String;)V");
    jmethodID MethodID40 = env->GetStaticMethodID(clazz, "setTutorial_list", "(Ljava/lang/String;)V");
    jmethodID MethodID41 = env->GetStaticMethodID(clazz, "setWatch_tutorial", "(Ljava/lang/String;)V");
    jmethodID MethodID42 = env->GetStaticMethodID(clazz, "setEnter_vault", "(Ljava/lang/String;)V");
    jmethodID MethodID43 = env->GetStaticMethodID(clazz, "setOpen_vault", "(Ljava/lang/String;)V");
    jmethodID MethodID44 = env->GetStaticMethodID(clazz, "setUpload_to_vault", "(Ljava/lang/String;)V");
    jmethodID MethodID45 = env->GetStaticMethodID(clazz, "setCreate_vault", "(Ljava/lang/String;)V");
    jmethodID MethodID46 = env->GetStaticMethodID(clazz, "setGet_notification", "(Ljava/lang/String;)V");
    jmethodID MethodID47 = env->GetStaticMethodID(clazz,"setActivate_previous_premium", "(Ljava/lang/String;)V");
    jmethodID MethodID48 = env->GetStaticMethodID(clazz,"setBuy_page", "(Ljava/lang/String;)V");


    env->CallStaticVoidMethod(clazz, MethodID, env->NewStringUTF(host.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID1, env->NewStringUTF(advertise_links.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID2, env->NewStringUTF(advertise_watch.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID3, env->NewStringUTF(advertise_end.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID4, env->NewStringUTF(profile_register.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID5, env->NewStringUTF(page_register.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID6, env->NewStringUTF(profile_login.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID7, env->NewStringUTF(page_login.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID8, env->NewStringUTF(page_delete.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID9, env->NewStringUTF(profile_logout.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID10, env->NewStringUTF(verify_email.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID11, env->NewStringUTF(phone_verification.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID12, env->NewStringUTF(forgot_profile_password.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID13, env->NewStringUTF(forgot_page_password.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID14, env->NewStringUTF(validate_reset_password_otp.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID15, env->NewStringUTF(prepare_offline.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID16, env->NewStringUTF(upload_page_security_image.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID17, env->NewStringUTF(upload_page_security_question.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID18, env->NewStringUTF(page_security_image.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID19, env->NewStringUTF(update_page_security_question.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID20, env->NewStringUTF(create_page_recovery_certificate.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID21, env->NewStringUTF(token_refresh.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID22, env->NewStringUTF(search_friend.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID23, env->NewStringUTF(add_friend.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID24, env->NewStringUTF(block_friend.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID25, env->NewStringUTF(block_friend_list.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID26, env->NewStringUTF(unfriend_friend.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID27, env->NewStringUTF(cancel_friend_request.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID28, env->NewStringUTF(delete_friend_request.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID29, env->NewStringUTF(ignore_friend.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID30, env->NewStringUTF(friend_list.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID31, env->NewStringUTF(send_message.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID32, env->NewStringUTF(get_message.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID33, env->NewStringUTF(delete_message.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID34, env->NewStringUTF(upload_offline_message.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID35, env->NewStringUTF(premium_features.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID36, env->NewStringUTF(buy_premium.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID37, env->NewStringUTF(referrals.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID38, env->NewStringUTF(reference_balance_withdraw.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID39, env->NewStringUTF(recharge.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID40, env->NewStringUTF(tutorial_list.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID41, env->NewStringUTF(watch_tutorial.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID42, env->NewStringUTF(enter_vault.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID43, env->NewStringUTF(open_vault.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID44, env->NewStringUTF(upload_to_vault.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID45, env->NewStringUTF(create_vault.c_str()));
    env->CallStaticVoidMethod(clazz, MethodID46, env->NewStringUTF(getNotification.c_str()));
    env->CallStaticVoidMethod(clazz,MethodID47,env->NewStringUTF(activate_previous_premium.c_str()));
    env->CallStaticVoidMethod(clazz,MethodID48,env->NewStringUTF(buy_page.c_str()));

}



extern "C" JNIEXPORT jstring JNICALL
Java_com_horofbd_nei_MainActivity_tryencode(JNIEnv *env, jobject, jstring message, jstring type,
                                            jstring password) {
    std::wstring wstr = jstring2wstring(env, message);
    __android_log_print(ANDROID_LOG_ERROR, "sometag", "%s",
                        jstring2string(env, getJstringFromWstring(env, wstr)).c_str());
    std::string pass = jstring2string(env, password);
    std::string typ = jstring2string(env, type);
    jstring encoded = getJstringFromWstring(env, textHash(wstr, pass, typ));
    return encoded;

}

extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_nei_MainActivity_globalRequest(JNIEnv *env, jobject, jobject serverresponse, jstring requesttype,
                                            jstring link,jobject jsonobject,jint requestcode) {
    jclass fun = env->FindClass("com/horofbd/nei/Functions");
    jmethodID jmethodId = env->GetStaticMethodID(fun,"Request", "(Lcom/horofbd/nei/ServerResponse;Ljava/lang/String;Ljava/lang/String;Lorg/json/JSONObject;I)V");
    env->CallStaticVoidMethod(fun,jmethodId,serverresponse,requesttype,link,jsonobject,requestcode);

}

extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_nei_LoginActivity_SaveLogindata(JNIEnv *env, jobject,jobject object,jobject jobject1) {


    jclass cls = env->FindClass("org/json/JSONObject");
    jmethodID method2 = env->GetMethodID(cls, "getString", "(Ljava/lang/String;)Ljava/lang/String;");
    jstring access_token = (jstring) env->CallObjectMethod(object,method2,env->NewStringUTF("access_token"));
    jstring token_type = (jstring) env->CallObjectMethod(object,method2,env->NewStringUTF("token_type"));
    jstring token_expiry = (jstring) env->CallObjectMethod(object,method2,env->NewStringUTF("expires_in"));
    jstring user_id = (jstring) env->CallObjectMethod(object,method2,env->NewStringUTF("id"));
    jstring user_name = (jstring) env->CallObjectMethod(jobject1,method2,env->NewStringUTF("name"));
    jstring user_ref =(jstring) env->CallObjectMethod(jobject1,method2,env->NewStringUTF("ref"));
   // jstring login_status = (jstring) env->CallObjectMethod(jobject1,method2,env->NewStringUTF(""));
    jstring phone_no = (jstring) env->CallObjectMethod(jobject1,method2,env->NewStringUTF("phone_no"));

    jclass fun = env->FindClass("com/horofbd/nei/Functions");
    jmethodID jmethodId = env->GetStaticMethodID(fun,"setSharedPreference", "(Ljava/lang/String;Ljava/lang/String;)V");
    env->CallStaticVoidMethod(fun,jmethodId,env->NewStringUTF("access_token"),access_token);
    env->CallStaticVoidMethod(fun,jmethodId,env->NewStringUTF("token_type"),token_type);
    env->CallStaticVoidMethod(fun,jmethodId,env->NewStringUTF("expires_in"),token_expiry);
    env->CallStaticVoidMethod(fun,jmethodId,env->NewStringUTF("user_id"),user_id);
    env->CallStaticVoidMethod(fun,jmethodId,env->NewStringUTF("user_name"),user_name);
    env->CallStaticVoidMethod(fun,jmethodId,env->NewStringUTF("user_ref"),user_ref);
    env->CallStaticVoidMethod(fun,jmethodId,env->NewStringUTF("phone_no"),phone_no);
    env->CallStaticVoidMethod(fun,jmethodId,env->NewStringUTF("login_status"),env->NewStringUTF("true"));

}

extern "C" JNIEXPORT void JNICALL
Java_com_horofbd_nei_LoginActivity_LoginRequest(JNIEnv *env, jobject, jobject serverresponse, jstring phone,jstring password,jint requestcode) {
    jclass fun = env->FindClass("com/horofbd/nei/Functions");
    jmethodID jmethodId = env->GetStaticMethodID(fun,"LoginRegisteredUser", "(Lcom/horofbd/nei/ServerResponse;Ljava/lang/String;Ljava/lang/String;I)V");
    env->CallStaticVoidMethod(fun,jmethodId,serverresponse,phone,password,requestcode);

}



extern "C" JNIEXPORT jstring JNICALL
Java_com_horofbd_nei_LoginActivity_GetLoginInfo(JNIEnv *env, jobject,jstring key) {

    jstring result;
    jclass fun = env->FindClass("com/horofbd/nei/Functions");
    jmethodID jmethodId = env->GetStaticMethodID(fun,"getSharedPreference", "(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
    string ckey = jstring2string(env,key);
    if(ckey == "LoginToken"){
      result = (jstring)env->CallStaticObjectMethod(fun,jmethodId,key,env->NewStringUTF("0"));
    }


}

