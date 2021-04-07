#include <iostream>
#include <string>
#include <cstring>
#include <wchar.h>
#include <cwchar>

#include <locale>
#include <clocale>
#include "md5.h"
#include "sha1.h"
std::wstring  textHash (const std::wstring& text, const std::string& password, const std::string& action = "encode") {
    //hashes
    std::wstring password_md5;
    std::wstring password_sha1;

    std::string smd5(md5(password));
    std::string ssha1(sha1(password));
    password_md5.assign(smd5.begin(), smd5.end());
    password_sha1.assign(ssha1.begin(), ssha1.end());

    // declaring character array
    wchar_t text_array[text.length() + 1];
    wchar_t password_md5_array[password_md5.length()+1];
    wchar_t password_sha1_array[password_sha1.length()+1];

    // declaring int array
    int text_int_array[text.length() + 1];
    int password_int_md5_array[password_md5.length()+1];
    int password_int_sha1_array[password_sha1.length()+1];

    //copy to array
    wcscpy (text_array, text.c_str());
    wcscpy (password_md5_array, password_md5.c_str());
    wcscpy (password_sha1_array, password_sha1.c_str());

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
    wchar_t result_array[text.length()+1];

    if (action == "encode") {
        for (int i4 = 0; i4 < text.length(); i4++) {
            result_array[i4] = text_int_array[i4] + password_int_md5_array[i4%password_md5.length()] + password_int_sha1_array[i4%password_sha1.length()];
        }
    } else {
        for (int i4 = 0; i4 < text.length(); i4++) {
            result_array[i4] = (text_int_array[i4] - password_int_md5_array[i4%password_md5.length()] - password_int_sha1_array[i4%password_sha1.length()]);
        }
    }

    //convert array to string
    int i;
    std::wstring  Return;
    for (i = 0; i < text.length(); i++) {
        Return += result_array[i];
    }
    return Return;

}


int main() {

    std::wstring s = L"Read more at: https://bangla.asianetnews.com/";
    std::wstring  hash = textHash(s, "z");
    std::wstring  deHash = textHash(hash, "z", "decode");
    std::cout  << deHash.length()<< ", " << hash.length() << std::endl;
}