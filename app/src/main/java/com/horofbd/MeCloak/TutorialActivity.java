package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class TutorialActivity extends AppCompatActivity implements ServerResponse {
    static EditText features;
    static ImageView dropdown;
    static PlayerView playerView;
    static TextView name;
    static Context context;
    static LayoutInflater inflater;
    static Activity activity;
    static ServerResponse serverResponse;
    static ImageView enterfullscreene;
    static SimpleExoPlayer player;
    static CardView container;
    static TextView copyrite;
    static ScrollView textcontainer;
    static TextView text;
    static boolean isFullscreen = false;

    private final String STATE_RESUME_WINDOW = "resumeWindow";
    private final String STATE_RESUME_POSITION = "resumePosition";
    private final String STATE_PLAYER_FULLSCREEN = "playerFullscreen";
    private final String STATE_CHECKSUM = "CHECKSUM";
    private final String STATE_LIST = "ARRAYLIST";
    private static Dialog mFullScreenDialog;
    private static int mResumeWindow;
    private static long mResumePosition;
    static FrameLayout frameLayout;

    static {
        System.loadLibrary("native-lib");
    }


    static native void StartActivity(Context context, String activity, String data);

    static native void InitLinks();

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode);

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);


    static ArrayList<MyObject> dogsList;
    static PopupWindow popupWindow, popupWindow2;


    static ArrayList<MyObject> purseJSONOBJECT(ArrayList<JSONObject> list) {
        ArrayList<MyObject> temp = new ArrayList<>();
        for (JSONObject j : list) {
            try {
                MyObject object = new MyObject(j.getString("purpose"),
                        j.getString("type"), j.getString("checksum"),
                        j.getString("id"), j.getString("deleted_at"),
                        j.getString("created_at"), j.getString("updated_at"));
                temp.add(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return temp;
    }

    public static void setUpData(ArrayList<JSONObject> list) {
        dogsList = purseJSONOBJECT(list);


        Log.e("jsonobject", dogsList.toString());
        for (JSONObject jsonObject : list) {
            Log.e("list", jsonObject.toString());
        }
        ((Activity) context).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                init();
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);
        features = findViewById(R.id.features);
        playerView = findViewById(R.id.playerview);
        dropdown = findViewById(R.id.dropdown);
        enterfullscreene = findViewById(R.id.fullscreene);
        frameLayout = ((FrameLayout) findViewById(R.id.main_media_frame));
        container = findViewById(R.id.container);
        copyrite = findViewById(R.id.copyrite);
        text = findViewById(R.id.text);
        textcontainer = findViewById(R.id.textcontainer);


        name = findViewById(R.id.name);
        serverResponse = this;

        name.setText("Boundage");
        InitLinks();
        context = this;
        inflater = getLayoutInflater();
        activity = this;


        features.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                features.setCursorVisible(true);
            }
        });

        enterfullscreene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openFullscreenDialog();

            }
        });


        mFullScreenDialog = new Dialog(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen) {
            public void onBackPressed() {
                if (isFullscreen)
                    closeFullscreenDialog();
                super.onBackPressed();
            }
        };

        if (savedInstanceState != null) {
            mResumeWindow = savedInstanceState.getInt(STATE_RESUME_WINDOW);
            mResumePosition = savedInstanceState.getLong(STATE_RESUME_POSITION);
            isFullscreen = savedInstanceState.getBoolean(STATE_PLAYER_FULLSCREEN);
            dogsList = savedInstanceState.getParcelableArrayList(STATE_LIST);
            url = savedInstanceState.getString(STATE_CHECKSUM);


        } else {
            globalRequest(this, "GET", Important.getTutorial_list(), new JSONObject(), 15);

        }


    }


    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putInt(STATE_RESUME_WINDOW, mResumeWindow);
        outState.putLong(STATE_RESUME_POSITION, mResumePosition);
        outState.putBoolean(STATE_PLAYER_FULLSCREEN, isFullscreen);
        outState.putParcelableArrayList(STATE_LIST, dogsList);
        outState.putString(STATE_CHECKSUM, url);

        super.onSaveInstanceState(outState);
    }


    private static void openFullscreenDialog() {
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        ((ViewGroup) playerView.getParent()).removeView(playerView);
        mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        // mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
        isFullscreen = true;
        mFullScreenDialog.show();
    }


    private static void closeFullscreenDialog() {
        ((Activity) context).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ((ViewGroup) playerView.getParent()).removeView(playerView);

        frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        frameLayout.addView(playerView);
        isFullscreen = false;
        mFullScreenDialog.dismiss();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                init();
            }
        }, 1000);

        Log.e("doglist", dogsList.toString());


        // mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_expand));

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (playerView != null && player != null) {
            mResumeWindow = player.getCurrentWindowIndex();
            mResumePosition = Math.max(0, player.getContentPosition());
            player.release();
        }

        if (mFullScreenDialog != null)
            mFullScreenDialog.dismiss();
    }

    @Override
    protected void onResume() {

        super.onResume();

        if (playerView == null) {
            playerView = findViewById(R.id.playerview);
        }

        Set_Player(url);

        if (isFullscreen) {
            ((ViewGroup) playerView.getParent()).removeView(playerView);
            mFullScreenDialog.addContentView(playerView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            //  mFullScreenIcon.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.ic_fullscreen_skrink));
            mFullScreenDialog.show();
        }
    }


    public static void init() {

        popupWindow = new PopupWindow(dropdown, 1000, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        popupWindow2 = new PopupWindow(dropdown, 1000, ViewGroup.LayoutParams.WRAP_CONTENT, false);


        dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.e("dropdownclicked", "clicked");

                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                } else {
                    View vi = inflater.inflate(R.layout.tutorialinfo, null, false);
                    RecyclerView tutorials = vi.findViewById(R.id.tutorials);
                    tutorials.setLayoutManager(new LinearLayoutManager(context));

                    ListViewAdapter adapter;
                    if (dogsList.size() != 0) {
                        adapter = new ListViewAdapter(dogsList, context);
                    } else {
                        adapter = null;
                    }
                    tutorials.setAdapter(adapter);
                    popupWindow.setContentView(vi);
                    popupWindow.showAsDropDown(dropdown);


                }

            }

        });

        features.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                Log.e("textwatcher", "clicked");


                String str = features.getText().toString();
                ArrayList<MyObject> list = new ArrayList<>();
                for (int p = 0; p < dogsList.size(); p++) {
                    String s = null;

                    s = dogsList.get(p).purpose;


                    if (s.toLowerCase().contains(str.toLowerCase())) {
                        list.add(dogsList.get(p));
                    }
                }
                if (popupWindow2.isShowing()) {
                    popupWindow2.dismiss();
                }

                View vi = inflater.inflate(R.layout.tutorialinfo, null, false);
                RecyclerView tutorials = vi.findViewById(R.id.tutorials);
                tutorials.setLayoutManager(new LinearLayoutManager(context));
                ListViewAdapter adapter;
                if (list.size() != 0) {
                    adapter = new ListViewAdapter(list, context);
                } else {
                    adapter = null;
                }
                tutorials.setAdapter(adapter);
                popupWindow2.setContentView(vi);
                if (list.size() != 0) {
                    popupWindow2.showAsDropDown(dropdown);
                }

                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public static void setTextData(String response) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(response);
            }
        });
    }

    static String url;

    static class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> {
        protected ArrayList<MyObject> listdata;
        Context context;

        public ListViewAdapter(ArrayList<MyObject> listdata, Context context) {
            this.listdata = listdata;
            this.context = context;
        }

        @Override
        public ListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem = layoutInflater.inflate(R.layout.listviewlist, parent, false);
            return new ListViewAdapter.ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(ListViewAdapter.ViewHolder holder, int position) {
            final MyObject myListData = listdata.get(position);

            holder.textView.setText(myListData.purpose);
            holder.textView1.setText(myListData.type);

            holder.textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (myListData.type.equals("video")) {
                        container.setVisibility(View.VISIBLE);
                        copyrite.setVisibility(View.VISIBLE);
                        textcontainer.setVisibility(View.GONE);
                        features.setText(myListData.purpose);
                        name.setText(myListData.purpose);
                        popupWindow2.dismiss();
                        features.setCursorVisible(false);
                        Set_Player(myListData.checksum);
                        mResumePosition = 0L;
                        url = myListData.checksum;
                    } else {
                        features.setText(myListData.purpose);
                        name.setText(myListData.purpose);
                        popupWindow2.dismiss();
                        features.setCursorVisible(false);
                        textcontainer.setVisibility(View.VISIBLE);
                        container.setVisibility(View.GONE);
                        copyrite.setVisibility(View.GONE);


                        globalRequest(serverResponse, "GET", Important.getWatch_tutorial() + "?checksum=" + myListData.checksum, new JSONObject(), 16);


                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return listdata.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView textView;
            public TextView textView1;

            public ViewHolder(View itemView) {
                super(itemView);
                this.textView = (TextView) itemView.findViewById(R.id.title);
                this.textView1 = (TextView) itemView.findViewById(R.id.option);
            }
        }
    }

    static String tempchecksum = url;

    public static void Set_Player(String checksum) {


        DefaultTrackSelector trackSelector = new DefaultTrackSelector();
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector);


        try {
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(context,
                    Util.getUserAgent(context, "MeCloak"));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(Important.getWatch_tutorial() + "?checksum=" + checksum));
            player.prepare(videoSource);
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

        player.setPlayWhenReady(true);

        boolean haveResumePosition = mResumeWindow != C.INDEX_UNSET;

        if (haveResumePosition) {
            if (tempchecksum != null && tempchecksum.equals(checksum)) {
                Log.e("DEBUG", " haveResumePosition ");
                Log.e("position", String.valueOf(mResumePosition));
                player.seekTo(mResumeWindow, mResumePosition);
            }

            tempchecksum = url;


        }
//        String contentUrl = getString(url);

        playerView.setPlayer(player);


    }


    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        Log.e("tutorialresponse", response);
        CheckResponse(this, this, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }


    static class MyObject implements Parcelable {
        String purpose;
        String type;
        String checksum;
        String id;
        String deleted_at;
        String created_at;
        String updated_at;

        MyObject(String purpose, String type, String checksum, String id, String deleted_at, String created_at, String updated_at) {
            this.purpose = purpose;
            this.type = type;
            this.checksum = checksum;
            this.id = id;
            this.deleted_at = deleted_at;
            this.created_at = created_at;
            this.updated_at = updated_at;
        }

        private MyObject(Parcel in) {
            purpose = in.readString();
            type = in.readString();
            checksum = in.readString();
            id = in.readString();
            deleted_at = in.readString();
            created_at = in.readString();
            updated_at = in.readString();
        }

        public int describeContents() {
            return 0;
        }

        @Override
        public String toString() {
            return purpose + ": " + type + ": " + checksum;
        }

        public void writeToParcel(Parcel out, int flags) {
            out.writeString(purpose);
            out.writeString(type);
            out.writeString(checksum);
            out.writeString(id);
            out.writeString(deleted_at);
            out.writeString(created_at);
            out.writeString(updated_at);
        }

        public final Parcelable.Creator<MyObject> CREATOR = new Parcelable.Creator<MyObject>() {
            public MyObject createFromParcel(Parcel in) {
                return new MyObject(in);
            }

            public MyObject[] newArray(int size) {
                return new MyObject[size];
            }
        };
    }


}