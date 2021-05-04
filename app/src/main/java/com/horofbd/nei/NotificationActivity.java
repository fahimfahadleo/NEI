package com.horofbd.nei;

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

public class NotificationActivity extends AppCompatActivity implements ServerResponse {


    TabLayout tabLayout;
    ViewPager viewPager;
    ArrayList<String> namelist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);
        namelist = new ArrayList<>();
        //ServerRequest.RetriveFriendRequest(this,1);





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


//        View root = tabLayout.getChildAt(0);
//        if (root instanceof LinearLayout) {
//            ((LinearLayout) root).setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
//            GradientDrawable drawable = new GradientDrawable();
//            drawable.setColor(getResources().getColor(R.color.black));
//            drawable.setSize(2, 5);
//            ((LinearLayout) root).setDividerPadding(10);
//            ((LinearLayout) root).setDividerDrawable(drawable);
//        }




    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        if (requestcode == 1) {
            Log.e("Notification", response);





        }
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

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
                    return new ContactRequest(NotificationActivity.this);
                case 0:

                default:
                    return new NotificationFragment(NotificationActivity.this);
            }
        }

        // this counts total number of tabs
        @Override
        public int getCount() {
            return totalTabs;
        }
    }




}