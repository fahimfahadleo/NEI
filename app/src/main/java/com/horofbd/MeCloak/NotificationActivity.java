package com.horofbd.MeCloak;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.horofbd.MeCloak.MainActivity.notificationcounter;
import static com.horofbd.MeCloak.MainActivity.notificationcounterveiw;

public class NotificationActivity extends AppCompatActivity implements ServerResponse {
    static Context context;

    public static void closeActivtiy() {
        Functions.dismissDialogue();
        ((Activity) context).finish();
    }

    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<String> namelist;
    DatabaseHelper helper;

    static {
        System.loadLibrary("native-lib");
    }

    public static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native void StartActivity(Context context, String activity,String data);

    public static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode,Context context);

    public static native String getLoginInfo(String key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        namelist = new ArrayList<>();
        //ServerRequest.RetriveFriendRequest(this,1);
        context = this;
        helper = new DatabaseHelper(this);
        new Functions(this);


        MainActivity.notificationcount = 0;
        MainActivity.notificationRead = "true";
        helper.UpdateNotification("notificationcount", "0");
        helper.UpdateNotification("notificationread", "true");
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                notificationcounter.setText("0");
                notificationcounterveiw.setVisibility(View.INVISIBLE);
            }
        });




        final MyAdapter adapter = new MyAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {

        int maxLogSize = 1000;
        for(int i = 0; i <= response.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = Math.min(end, response.length());
            Log.e("data", response.substring(start, end));
        }

        JSONObject jsonObject = new JSONObject(response);
        JSONObject tempjson = jsonObject.getJSONObject("response");
        JSONArray array = tempjson.getJSONArray("data");
        for(int i = 0;i<array.length();i++){
            JSONObject js = array.getJSONObject(i);
            Log.e("notification",js.toString());
        }
        String s= tempjson.getString("next_page_url");
        Log.e("nextpageurl",s);

        CheckResponse(this, this, response, requestcode);
        Functions.dismissDialogue();
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(NotificationActivity.this, failresponse, Toast.LENGTH_SHORT).show();
                Functions.dismissDialogue();
            }
        });
    }


    public class MyAdapter extends FragmentPagerAdapter {

        private Context myContext;
        int totalTabs;

        public MyAdapter(Context context, FragmentManager fm, int totalTabs) {
            super(fm);
            myContext = context;
            this.totalTabs = totalTabs;
        }

        // this is for fragment tabs
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                    return new ContactRequest(NotificationActivity.this, NotificationActivity.this);
                case 0:

                default:
                    return new NotificationFragment(NotificationActivity.this, NotificationActivity.this);
            }
        }

        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
    }


}