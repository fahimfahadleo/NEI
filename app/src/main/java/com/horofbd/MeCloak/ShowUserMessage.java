package com.horofbd.MeCloak;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat2.Chat;
import org.jivesoftware.smack.chat2.ChatManager;
import org.jivesoftware.smack.chat2.IncomingChatMessageListener;
import org.jivesoftware.smack.chat2.OutgoingChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.MessageBuilder;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jivesoftware.smackx.filetransfer.FileTransfer;
import org.jivesoftware.smackx.filetransfer.FileTransferListener;
import org.jivesoftware.smackx.filetransfer.FileTransferManager;
import org.jivesoftware.smackx.filetransfer.FileTransferRequest;
import org.jivesoftware.smackx.filetransfer.IncomingFileTransfer;
import org.jivesoftware.smackx.filetransfer.OutgoingFileTransfer;
import org.jivesoftware.smackx.httpfileupload.HttpFileUploadManager;
import org.jivesoftware.smackx.httpfileupload.UploadProgressListener;
import org.jivesoftware.smackx.httpfileupload.UploadService;
import org.jivesoftware.smackx.httpfileupload.element.Slot;
import org.jivesoftware.smackx.httpfileupload.element.Slot_V0_2;
import org.jivesoftware.smackx.jingle.JingleManager;
import org.jivesoftware.smackx.jingle.JingleSession;
import org.jivesoftware.smackx.jingle.element.Jingle;
import org.jivesoftware.smackx.mam.MamManager;
import org.jivesoftware.smackx.omemo.OmemoManager;
import org.jivesoftware.smackx.receipts.DeliveryReceipt;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jivesoftware.smackx.xdata.FormField;
import org.jivesoftware.smackx.xevent.MessageEventManager;
import org.jivesoftware.smackx.xevent.MessageEventNotificationListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;
import org.jxmpp.util.XmppStringUtils;
import org.w3c.dom.Text;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.horofbd.MeCloak.MainActivity.connection;
import static java.lang.Thread.sleep;

public class ShowUserMessage extends AppCompatActivity implements ServerResponse, IncomingChatMessageListener, OutgoingChatMessageListener, ImageResponse {

    static {
        System.loadLibrary("native-lib");
    }

    CardView setpasswordview;
    TextView setPassword;
    String boundageposition;

    EditText typemessage;
    ImageView send, sendimage;
    List<Message> messages;
    ChatManager chatManager;
    RecyclerView messagerecyclerview;
    int me = 0;
    int him = 0;
    DrawerLayout drawerLayout;
    ChatMessageAdapter adapter;
    EditText typepassword;
    ImageView timer, enableboundage, file;
    CardView timerview, enableboundageview, boundageview, fileview;
    TextView boundagetext, boundagetip, passwordtip, timertip;
    DatabaseHelper helper;
    ImageView menubutton;
    static boolean isboundageitemavailable = false;
    static Context context;
    AlertDialog dialog;
    String passstr = "";
    String timerstr = "";
    String boundagestr = "";
    String userphonenumber;
    boolean isDatabaseAvailable = false;
    Bundle save;
    TextView titletext, timerside;
    CircleImageView profilepicture;
    ImageView showbelow;
    ScrollView belowview;
    boolean isbelowviewvisible = false;
    ImageView boundagebelow;
    CardView boundagebelowview;
    CardView passwordview, passwordbelowview, timerbelowview, timersideview;
    ImageView passwordbutton, passwordbelowbutton, timerbelow;


    static native void StartActivity(Context context, String activity, String data);

    static native void globalRequest(ServerResponse serverResponse, String requesttype, String link, JSONObject jsonObject, int requestcode, Context context);

    static native void InitLinks();

    static native void CheckResponse(ServerResponse serverResponse, Context context, String response, int requestcode);

    static native String getLoginInfo(String key);

    static native void ImageRequest(ImageResponse imageResponse, CircleImageView imageView, String requestType, String Link, JSONObject jsonObject, int requestcode);

    static native String EncrytpAndDecrypt(String message, String type, String password);

    static native void saveUserData(String key, String value);
    static native void sendFiletoUser(ServerResponse serverResponse, AbstractXMPPConnection connection,File file,int requestcode);

    static native String DefaultED(String message, String type);
    UploadProgressListener listener;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }

            Uri imageUri = data.getData();

            try {
//               Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                uploadedImage.setImageBitmap(bitmap);
//                uploadimage.setVisibility(View.GONE);
//                uploadedImage.buildDrawingCache();
//                Bitmap temp = Bitmap.createBitmap(uploadedImage.getDrawingCache(), 0, 0, uploadedImage.getWidth(), uploadedImage.getHeight());
//                mutableBitmap = temp.copy(Bitmap.Config.ARGB_8888, true);
//                Uri tempUri = getImageUri(getApplicationContext(), mutableBitmap);
//                Log.e("path",getRealPathFromURI(tempUri));
//                // CALL THIS METHOD TO GET THE ACTUAL PATH
//                file = new File(getRealPathFromURI(tempUri));
//                Log.e("mimetype",getMimeType(getRealPathFromURI(tempUri)));


                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                Uri tempUri = getImageUri(getApplicationContext(), bitmap);
                Log.e("path", getRealPathFromURI(tempUri));
                // CALL THIS METHOD TO GET THE ACTUAL PATH
                File file = new File(getRealPathFromURI(tempUri));
                //Log.e("mimetype",getMimeType(getRealPathFromURI(tempUri)));

                FileInputStream importdb = new FileInputStream(getContentResolver().openFileDescriptor(imageUri, "r").getFileDescriptor());
                DataInputStream di = new DataInputStream(importdb);

                byte[] b = new byte[10];
                new DataInputStream(new FileInputStream(di.readUTF())).readFully(b);

                Log.e("data", Arrays.toString(b));


            } catch (IOException e) {
                e.printStackTrace();
            }
        }else if(requestCode == 123 && resultCode == RESULT_OK){


            Log.e("data",data.toString());


            final Uri uri = data.getData();

            // Get the File path from the Uri
            String path = FileUtils.getPath(this, uri);
            Log.e("path",path);

            // Alternatively, use FileUtils.getFile(Context, Uri)
            if (path != null && FileUtils.isLocal(path)) {
                File file = new File(path);

                sendFiletoUser(this,connection,file,123);
            }

        }
    }


byte[] dataToSend,dataReceived;

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, String.valueOf(System.currentTimeMillis()), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        boundageposition = getIntent().getStringExtra("boundage");
        if (boundageposition.equals("1")) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        }
        save = savedInstanceState;
        setContentView(R.layout.activity_show_user_message);
        messagerecyclerview = findViewById(R.id.messagerecyclerview);
        typemessage = findViewById(R.id.typemessage);
        send = findViewById(R.id.sendbutton);
        InitLinks();
        timer = findViewById(R.id.timer);
        timerview = findViewById(R.id.timerview);
        enableboundage = findViewById(R.id.enableboundage);
        enableboundageview = findViewById(R.id.enableboundageview);
        helper = new DatabaseHelper(this);
        menubutton = findViewById(R.id.menubutton);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        titletext = findViewById(R.id.titletext);
        profilepicture = findViewById(R.id.profile_image);
        showbelow = findViewById(R.id.showbelow);
        belowview = findViewById(R.id.belowbar);
        drawerLayout = findViewById(R.id.drawer);
        typepassword = findViewById(R.id.typepassword);
        setPassword = findViewById(R.id.setpasswordtext);
        setpasswordview = findViewById(R.id.setpasswordview);
        boundageview = findViewById(R.id.boundageview);
        boundagetext = findViewById(R.id.boundagetext);
        boundagetip = findViewById(R.id.boundagetips);
        boundagebelow = findViewById(R.id.enablebelowboundage);
        boundagebelowview = findViewById(R.id.enablebelowboundageview);
        passwordview = findViewById(R.id.security).findViewById(R.id.passwordview);
        passwordbutton = findViewById(R.id.security).findViewById(R.id.password);
        sendimage = findViewById(R.id.image);
        passwordbelowbutton = findViewById(R.id.belowpassword);
        passwordbelowview = findViewById(R.id.belowpasswordview);
        timerbelow = findViewById(R.id.belowtimer);
        timerbelowview = findViewById(R.id.belowtimerview);
        timerside = findViewById(R.id.tiemrtext);
        timersideview = findViewById(R.id.tiemrview);
        passwordtip = findViewById(R.id.encryptionpasstips);
        timertip = findViewById(R.id.expirytips);
        file = findViewById(R.id.file);
        fileview = findViewById(R.id.fileview);



        String premiumstatus = getLoginInfo("premium");
        userphonenumber = getIntent().getStringExtra("phone_no");

        Cursor c = helper.getData(userphonenumber, getLoginInfo("page_no"));
        for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
            String data2 = c.getString(c.getColumnIndex("phonenumber"));
            passstr = c.getString(c.getColumnIndex("textpass"));
            boundagestr = c.getString(c.getColumnIndex("boundage"));
            timerstr = c.getString(c.getColumnIndex("timer"));
            isDatabaseAvailable = true;
        }
        c.close();

        if (!isDatabaseAvailable) {
            helper.setFriendInformation(userphonenumber, "", "0", "0", getLoginInfo("page_no"));
            passstr = "";
            timerstr = "0";
            boundagestr = "0";
        }

        context = this;
        titletext.setText(getIntent().getStringExtra("name"));


        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("friend", getIntent().getStringExtra("id"));
            ImageRequest(this, profilepicture, "GET", Important.getViewprofilepicture(), jsonObject, 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        belowview.setVisibility(View.GONE);

        timerbelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });
        timerbelowview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });

        timerside.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });

        timersideview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });


        boundagebelowview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();
            }
        });

        boundagebelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();
            }
        });

        boundageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();
            }
        });
        boundagetext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();
            }
        });

        boundagetip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTips(getResources().getString(R.string.boundagetip));
            }
        });
        timertip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTips(getString(R.string.timertip));
            }
        });

        passwordtip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTips(getString(R.string.passwordtip));
            }
        });


        enableboundageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();

            }
        });
        enableboundage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setBoundageIcon();
            }
        });


        showbelow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isbelowviewvisible) {
                    isbelowviewvisible = false;
                    belowview.setVisibility(View.GONE);
                    showbelow.setImageDrawable(getResources().getDrawable(R.drawable.arrowdown));
                } else {
                    isbelowviewvisible = true;
                    belowview.setVisibility(View.VISIBLE);
                    showbelow.setImageDrawable(getResources().getDrawable(R.drawable.arrowupicon));
                }
            }
        });

        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // takeScreenshot();
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });

        file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceName = android.os.Build.MODEL;
                String deviceMan = android.os.Build.MANUFACTURER;
                Log.e("devicename", deviceName);
                Log.e("deviceman", deviceMan);
                Intent intent;
                if (deviceMan.equals("Samsung")) {
                    intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                    intent.putExtra("CONTENT_TYPE", "*/*");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                }
                startActivityForResult(intent, 123);
            }
        });
        fileview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceName = android.os.Build.MODEL;
                String deviceMan = android.os.Build.MANUFACTURER;
                Log.e("devicename", deviceName);
                Log.e("deviceman", deviceMan);
                Intent intent;
                if (deviceMan.equals("Samsung")) {
                    intent = new Intent("com.sec.android.app.myfiles.PICK_DATA");
                    intent.putExtra("CONTENT_TYPE", "*/*");
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                } else {
                    intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    intent.setType("*/*");
                }
                startActivityForResult(intent, 123);
            }
        });

        sendimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                getIntent.setType("image/*");
                Intent pickIntent = new Intent(Intent.ACTION_PICK);
                pickIntent.setDataAndType(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});
                startActivityForResult(chooserIntent, 1);
            }
        });


        Log.e("pass", passstr);
        Log.e("pass", "passstr");

        if (!timerstr.equals("0")) {
            timerview.setCardBackgroundColor(getResources().getColor(R.color.green));
            timersideview.setCardBackgroundColor(getResources().getColor(R.color.green));
            timerbelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
        }

        if (!boundagestr.equals("0")) {
            enableboundageview.setCardBackgroundColor(getResources().getColor(R.color.green));
            boundagebelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
            boundageview.setCardBackgroundColor(getResources().getColor(R.color.green));
        }

        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });
        timerview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimerDialog();
            }
        });


        try {
            InitConnection();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setStackFromEnd(true);
        messagerecyclerview.setLayoutManager(manager);


        setpasswordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });
        setPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });

        passwordview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });

        passwordbelowview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });
        passwordbelowbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });
        passwordbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPassword();
            }
        });


// ContentView is the root view of the layout of this activity/fragment
        drawerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        Rect r = new Rect();
                        drawerLayout.getWindowVisibleDisplayFrame(r);
                        int screenHeight = drawerLayout.getRootView().getHeight();

                        // r.bottom is the position above soft keypad or device button.
                        // if keypad is shown, the r.bottom is smaller than that before.
                        int keypadHeight = screenHeight - r.bottom;
                        if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                            // keyboard is opened
                            if (!isKeyboardShowing) {
                                isKeyboardShowing = true;
                                onKeyboardVisibilityChanged();
                            }
                        } else {
                            // keyboard is closed
                            if (isKeyboardShowing) {
                                isKeyboardShowing = false;
                                onKeyboardVisibilityChanged();
                            }
                        }
                    }
                });


        typemessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String mess = charSequence.toString();
                send.setEnabled(mess != null);
                Chat chat = null;
                try {
                    chat = chatManager.chatWith(JidCreate.entityBareFrom(userphonenumber + "@" + Important.getXmppHost()));
                } catch (XmppStringprepException e) {
                    e.printStackTrace();
                }

                try {
                    ChatStateManager.getInstance(connection).setCurrentState(ChatState.composing, chat);
                } catch (SmackException.NotConnectedException | InterruptedException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = typemessage.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    typemessage.setText(null);
                    sendMessage(message, userphonenumber);
                }

            }
        });







        setScrollPosition();

    }


    void setBoundageIcon() {
        try {
            if (!boundagestr.equals("0")) {
                updateUserInfo(3, "0");
                enableboundageview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                boundageview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                boundagebelowview.setCardBackgroundColor(getResources().getColor(R.color.red_500));

            } else {
                updateUserInfo(3, "1");
                enableboundageview.setCardBackgroundColor(getResources().getColor(R.color.green));
                boundageview.setCardBackgroundColor(getResources().getColor(R.color.green));
                boundagebelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    AlertDialog tipdialog;

    void showTips(String type) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View vi = getLayoutInflater().inflate(R.layout.tipbanner, null, false);
        TextView tipmessage = vi.findViewById(R.id.tipmessage);
        tipmessage.setText(type);
        builder.setView(vi);
        tipdialog = builder.create();
        tipdialog.show();
    }

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    void setScrollPosition() {
        if (messagerecyclerview.getAdapter() != null && messagerecyclerview.getAdapter().getItemCount() != 0) {
            if (getIntent().hasExtra("recyclerview")) {
                typemessage.setText(getIntent().getStringExtra("message"));
                messagerecyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messagerecyclerview.scrollToPosition(getIntent().getIntExtra("recyclerview", 0));
                    }
                }, 10);

            } else {

                messagerecyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                    }
                }, 10);

            }
        }
    }

    void InitConnection() throws Settings.SettingNotFoundException {
        if (connection != null) {
            Log.e("connection", "notnull");
            chatManager = ChatManager.getInstanceFor(connection);
            chatManager.addIncomingListener(this);
            chatManager.addOutgoingListener(this);


            ChatStateManager chatmanager = ChatStateManager.getInstance(connection);
            chatmanager.addChatStateListener(new ChatStateListener() {
                @Override
                public void stateChanged(Chat chat, ChatState state, Message message) {
                    Log.e("chatstate", String.valueOf(state));
                }
            });


            MessageEventNotificationListener listener = new MessageEventNotificationListener() {
                @Override
                public void deliveredNotification(Jid from, String packetID) {
                    Log.e("messaereceived", "from: " + from.toString() + " id: " + packetID);
                }

                @Override
                public void displayedNotification(Jid from, String packetID) {
                    Log.e("notification", "from: " + from.toString() + " id: " + packetID);
                }

                @Override
                public void composingNotification(Jid from, String packetID) {
                    Log.e("typing", "from: " + from.toString() + " id: " + packetID);
                }

                @Override
                public void offlineNotification(Jid from, String packetID) {
                    Log.e("offline", "from: " + from.toString() + " id: " + packetID);
                }

                @Override
                public void cancelledNotification(Jid from, String packetID) {
                    Log.e("cancelled", "from: " + from.toString() + " id: " + packetID);
                }
            };


            Log.e("data", getIntent().getStringExtra("phone_no"));
            MessageEventManager messageEventManager = MessageEventManager.getInstanceFor(connection);
            messageEventManager.addMessageEventNotificationListener(listener);
            DeliveryReceiptManager dm = DeliveryReceiptManager.getInstanceFor(connection);
            dm.setAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
            dm.autoAddDeliveryReceiptRequests();
            try {
                if (dm.isSupported(JidCreate.bareFrom(getIntent().getStringExtra("phone_no") + "@" + Important.getXmppHost()))) {
                    Log.e("Deliveryreceipt", "supported");
                } else {
                    Log.e("Deliveryreceipt", "notsupported");
                }
                dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                    @Override
                    public void onReceiptReceived(Jid fromJid, Jid toJid,
                                                  final String receiptId, Stanza receipt) {
                        Log.e("messagestatus", "received");
                    }
                });
            } catch (SmackException | XmppStringprepException | InterruptedException | XMPPException e) {
                e.printStackTrace();
            }


            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            FormField formField = null;
                            try {
                                formField = FormField.builder("with").setValue(JidCreate.bareFrom(getIntent().getStringExtra("phone_no") + "@" + Important.getXmppHost())).build();
                            } catch (XmppStringprepException e) {
                                e.printStackTrace();
                            }

                            MamManager manager = MamManager.getInstanceFor(connection);
                            MamManager.MamQueryArgs mamQueryArgs = MamManager.MamQueryArgs.builder()
                                    .limitResultsSince(yesterday())
                                    .queryLastPage().withAdditionalFormField(formField)
                                    .build();
                            try {
                                MamManager.MamQuery mamQuery = manager.queryArchive(mamQueryArgs);
                                messages = new LinkedList<Message>(mamQuery.getMessages());
                                // Log.e("list", messages.toString());

                                adapter = new ChatMessageAdapter(ShowUserMessage.this, messages);
                                messagerecyclerview.setAdapter(adapter);
                                Log.e("message size", String.valueOf(messages.size()));

                                messagerecyclerview.scrollToPosition(adapter.getItemCount() - 1);
                                messagerecyclerview.setItemViewCacheSize(30);
                                messagerecyclerview.setDrawingCacheEnabled(true);
                                messagerecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
                                messagerecyclerview.setItemAnimator(new DefaultItemAnimator());


                                messagerecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                        super.onScrollStateChanged(recyclerView, newState);
                                        if (RecyclerView.SCROLL_STATE_IDLE == newState) {
                                            // fragProductLl.setVisibility(View.VISIBLE);
                                            Log.e("scrollstate", "idel");

                                            LinearLayoutManager layoutManager = ((LinearLayoutManager) messagerecyclerview.getLayoutManager());
                                            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                                            int lastvisiblePosition = layoutManager.findLastVisibleItemPosition();

                                            Log.e("first", String.valueOf(firstVisiblePosition));
                                            Log.e("last", String.valueOf(lastvisiblePosition));

                                            boundageposition = getIntent().getStringExtra("boundage");


                                            ChatMessageAdapter adapter = (ChatMessageAdapter) messagerecyclerview.getAdapter();
                                            for (int i = firstVisiblePosition; i <= lastvisiblePosition; i++) {
                                                String s = DefaultED(adapter.getItem(i).getBody(), "decode");


                                                try {
                                                    JSONObject jsonObject = new JSONObject(s);

                                                    if (jsonObject.getString("boundage").equals("1")) {
                                                        if (!boundageposition.equals("1")) {
                                                            RestartActivity("type1");
                                                        }
                                                        isboundageitemavailable = true;
                                                        break;
                                                    } else if (i == lastvisiblePosition) {
                                                        if (!boundageposition.equals("0")) {
                                                            RestartActivity("type0");
                                                            isboundageitemavailable = false;
                                                        }
                                                    }

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        }
                                    }
                                });


                            } catch (SmackException.NoResponseException | XMPPException.XMPPErrorException | SmackException.NotConnectedException | SmackException.NotLoggedInException | InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });


        }
    }


    void setUpData() {
        Log.e("setUpData", "called");
        messagerecyclerview.setAdapter(null);
        adapter = new ChatMessageAdapter(this, messages);
        messagerecyclerview.setAdapter(adapter);
        Log.e("message size", String.valueOf(messages.size()));


        messagerecyclerview.scrollToPosition(adapter.getItemCount() - 1);
        messagerecyclerview.setItemViewCacheSize(30);
        messagerecyclerview.setDrawingCacheEnabled(true);
        messagerecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        messagerecyclerview.setItemAnimator(new DefaultItemAnimator());
        messagerecyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messagerecyclerview.getAdapter() != null)
                    messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
            }
        }, 10);

    }

    void setPassword() {
        Dialog dialog = new Dialog(ShowUserMessage.this);
        View vi = getLayoutInflater().inflate(R.layout.setencryptionpassword, null, false);
        EditText setpassword = vi.findViewById(R.id.typepassword);
        TextView cancel = vi.findViewById(R.id.cancel);
        TextView save = vi.findViewById(R.id.save);
        setpassword.setText(passstr);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = setpassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    password = "";
                    try {
                        updateUserInfo(1, password);
                        setpasswordview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        passwordview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        passwordbelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        setUpData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        updateUserInfo(1, password);
                        setpasswordview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        passwordview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        passwordbelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        setUpData();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(vi);
        dialog.show();
    }

    void updateUserInfo(int field, String value) throws JSONException {
        switch (field) {
            case 1: {
                //update password
                helper.updateFriendInformation(userphonenumber, "textpass", value, getLoginInfo("page_no"));
                passstr = value;
                break;
            }
            case 2: {
                //update timer
                helper.updateFriendInformation(userphonenumber, "timer", value, getLoginInfo("page_no"));
                timerstr = value;
                break;
            }
            case 3: {
                //update boundage
                helper.updateFriendInformation(userphonenumber, "boundage", value, getLoginInfo("page_no"));
                boundagestr = value;
                break;
            }
        }
    }

    void showTimerDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view1 = getLayoutInflater().inflate(R.layout.numberpicker, null, false);
        EditText setTimer = view1.findViewById(R.id.timer);
        TextView save = view1.findViewById(R.id.save);
        setTimer.setText(timerstr);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String time = setTimer.getText().toString();
                if (TextUtils.isEmpty(time)) {
                    setTimer.setError("Field can not be emtpty!");
                    setTimer.requestFocus();
                } else {
                    try {
                        updateUserInfo(2, time);
                        if (time.equals("0")) {
                            timerview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                            timerbelowview.setCardBackgroundColor(getResources().getColor(R.color.red_500));
                            timersideview.setCardBackgroundColor(getResources().getColor(R.color.red_500));

                        } else {
                            timerview.setCardBackgroundColor(getResources().getColor(R.color.green));
                            timerbelowview.setCardBackgroundColor(getResources().getColor(R.color.green));
                            timersideview.setCardBackgroundColor(getResources().getColor(R.color.green));
                        }
                        dialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        builder.setView(view1);
        dialog = builder.create();
        dialog.show();
    }

    void setImage(ImageView imageView, Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) {

        InputStream inputStream = response.body().byteStream();
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        ((Activity) context).runOnUiThread(() -> {
            if (bitmap != null) {
                imageView.setImageBitmap(bitmap);

            } else {
                Log.e("bitmap", "null");
            }
        });


    }

    @Override
    public void onImageFailure(String failresponse) {

    }


    boolean isKeyboardShowing = false;

    void onKeyboardVisibilityChanged() {
        messagerecyclerview.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (messagerecyclerview.getAdapter() != null)
                    messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
            }
        }, 10);

    }

    @Override
    public void onResponse(String response, int code, int requestcode) throws JSONException {
        CheckResponse(this, this, response, requestcode);
    }

    @Override
    public void onFailure(String failresponse) throws JSONException {

    }


    @Override
    public void newIncomingMessage(EntityBareJid from, Message message, Chat chat) {
        Log.e("messageincomming", message.getBody());
        MessageEventManager manager = MessageEventManager.getInstanceFor(connection);
        try {
            manager.sendDeliveredNotification(from, message.getStanzaId());
        } catch (SmackException.NotConnectedException | InterruptedException e) {
            e.printStackTrace();
        }


        try {
            String s = DefaultED(message.getBody(), "decode");
            JSONObject jsonObject = null;

            jsonObject = new JSONObject(s);

            String encodec;

            encodec = EncrytpAndDecrypt(jsonObject.getString("body"), "decode", passstr);

            Log.e("encodeasdlfkdsaflkdsa", encodec);

            Log.e("odvudmessage", Base64.encodeToString(encodec.getBytes(), Base64.DEFAULT));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        addNewMessage(message);

    }

    @Override
    public void newOutgoingMessage(EntityBareJid to, MessageBuilder messageBuilder, Chat chat) {
        Message message = messageBuilder.build();
        Log.e("messageoutgoing", message.getBody());
        try {
            message.setFrom(JidCreate.entityBareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
        } catch (XmppStringprepException e) {
            e.printStackTrace();
        }
        addNewMessage(message);

    }


    public void sendMessage(String body, String toJid) {

        try {
            EntityBareJid jid = JidCreate.entityBareFrom(toJid + "@" + Important.getXmppHost());
            char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
            StringBuilder sb = new StringBuilder(20);
            Random random = new Random();
            for (int i = 0; i < 20; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }

            JSONObject jsonObject = new JSONObject();


            String encodec;

            encodec = EncrytpAndDecrypt(body, "encode", passstr);

            String userpassword = passstr;
            String userboundage = boundagestr;
            String userexpiry = timerstr;


            jsonObject.put("body", encodec);
            jsonObject.put("boundage", userboundage);
            jsonObject.put("hash1", Functions.md5(userpassword));
            jsonObject.put("hash2", Functions.Sha1(userpassword));
            jsonObject.put("timestamp", getDate("0"));
            jsonObject.put("expiry", getDate(userexpiry));


            Log.e("message", body);

            String finalbody = jsonObject.toString();

            Message message = new Message();
            message.setBody(DefaultED(finalbody, "encode"));

            message.setType(Message.Type.chat);
            message.setStanzaId(sb.toString());
            message.setFrom(JidCreate.entityBareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
            message.setTo(jid);


            Chat chat = chatManager.chatWith(jid);
            chat.send(message);
            MessageEventManager.addNotificationsRequests(message, false, true, false, false);
            DeliveryReceiptManager.setDefaultAutoReceiptMode(DeliveryReceiptManager.AutoReceiptMode.always);
            ProviderManager.addExtensionProvider(DeliveryReceipt.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceipt.Provider());
            ProviderManager.addExtensionProvider(DeliveryReceiptRequest.ELEMENT, DeliveryReceipt.NAMESPACE, new DeliveryReceiptRequest.Provider());
            DeliveryReceiptRequest.addTo(message);
            //mConnection.sendStanza();
        } catch (SmackException.NotConnectedException e) {
            Log.e("exception1", e.getMessage());
        } catch (InterruptedException e) {
            Log.e("exception2", e.getMessage());
        } catch (XmppStringprepException e) {
            Log.e("exception3", e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    void addNewMessage(Message message) {
        messages.add(message);
        Log.e("size ", String.valueOf(messages.size()));

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                adapter.notifyDataSetChanged();
                messagerecyclerview.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                    }
                }, 10);
            }
        });


    }


    public class ChatMessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements ImageResponse {
        Context context;
        List<Message> list;

        public static final int MESSAGE_TYPE_IN = 1;


        public ChatMessageAdapter(Context context, List<Message> mlist) { // you can pass other parameters in constructor
            this.context = context;
            list = mlist;
        }

        @Override
        public void onImageResponse(Response response, int code, int requestcode, CircleImageView imageView) throws JSONException {
            InputStream inputStream = response.body().byteStream();
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            ((Activity) context).runOnUiThread(() -> {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);

                } else {
                    Log.e("bitmap", "null");
                }
            });
        }

        @Override
        public void onImageFailure(String failresponse) throws JSONException {

        }

        private class MessageInViewHolder extends RecyclerView.ViewHolder {


            private final TextView message, time;
            private final CircleImageView profilepicture;

            MessageInViewHolder(final View itemView) {
                super(itemView);

                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);
                profilepicture = itemView.findViewById(R.id.profile_image);
            }

            void bind(int position) {
                him++;
                me = 0;
                if (him > 1) {
                    profilepicture.setVisibility(View.INVISIBLE);
                } else {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("friend", getIntent().getStringExtra("id"));
                        ImageRequest(ChatMessageAdapter.this, profilepicture, "GET", Important.getViewprofilepicture(), jsonObject, 1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    Message messageModel = list.get(position);

                    String s = DefaultED(messageModel.getBody(), "decode");
                    JSONObject jsonObject = new JSONObject(s);


                    String encodec;

                    encodec = EncrytpAndDecrypt(jsonObject.getString("body"), "decode", passstr);
                    Log.e("messageinaaaaaa", encodec);

                    String timestamp = jsonObject.getString("timestamp");
                    String expirytime = jsonObject.getString("expiry");
                    String boundagetime = jsonObject.getString("boundage");
                    if (!boundagetime.equals("0")) {
                        if (list.size() - 1 == position) {
                            if (!boundageposition.equals("1")) {
                                Toast.makeText(context, "Boundage content found Refreshing layout!", Toast.LENGTH_SHORT).show();
                                RestartActivity("type1");
                            }
                            if (!isboundageitemavailable) {
                                RestartActivity("type0");
                            }
                        }
                    }


                    message.setText(encodec);
                    time.setText(jsonObject.getString("timestamp"));

                    if (!expirytime.equals(timestamp)) {
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //
                        try {
                            Date expiredate = format.parse(expirytime);
                            Date currentime = format.parse(getDate("0"));
                            Date timestampdate = format.parse(timestamp);


                            if (currentime.compareTo(expiredate) > 0) {
                                message.setText("Message Expired!");
                            } else {
                                long difference = dateDifference(timestampdate, expiredate);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                message.setText("Message Expired!");
                                                messagerecyclerview.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                                                    }
                                                }, 10);
                                            }
                                        });

                                    }
                                }, difference);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        private class MessageOutViewHolder extends RecyclerView.ViewHolder {

            private final TextView message;
            private final TextView time;
            private final CircleImageView profilepicture;


            MessageOutViewHolder(final View itemView) {
                super(itemView);
                message = itemView.findViewById(R.id.sendertext);
                time = itemView.findViewById(R.id.timetext);
                profilepicture = itemView.findViewById(R.id.profile_image);
            }

            void bind(int position) {
                him = 0;
                profilepicture.setVisibility(View.INVISIBLE);
                Message messageModel = list.get(position);

                String mode = DefaultED(messageModel.getBody(), "deocode");


                try {
                    JSONObject jsonObject = new JSONObject(mode);


                    String encodec;
                    encodec = EncrytpAndDecrypt(jsonObject.getString("body"), "decode", passstr);
                    Log.e("messageoutaaaa", encodec);
                    String timestamp = jsonObject.getString("timestamp");
                    String expirytime = jsonObject.getString("expiry");
                    String boundagetime = jsonObject.getString("boundage");
                    message.setText(encodec);
                    time.setText(jsonObject.getString("timestamp"));

                    if (!boundagetime.equals("0")) {
                        if (list.size() - 1 == position) {
                            if (!boundageposition.equals("1")) {
                                Toast.makeText(context, "Boundage content found Refreshing layout!", Toast.LENGTH_SHORT).show();
                                RestartActivity("type1");
                                isboundageitemavailable = true;
                            } else {
                                if (!isboundageitemavailable) {
                                    RestartActivity("type0");
                                }
                            }
                        }
                    }


                    if (!expirytime.equals(timestamp)) {
                        SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //
                        try {
                            Date expiredate = format.parse(expirytime);
                            Date currentime = format.parse(getDate("0"));
                            Date timestampdate = format.parse(timestamp);
                            if (currentime.compareTo(expiredate) > 0) {
                                message.setText("Message Expired!");
                            } else {
                                long difference = dateDifference(timestampdate, expiredate);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                message.setText("Message Expired!");
                                                messagerecyclerview.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        messagerecyclerview.smoothScrollToPosition(messagerecyclerview.getAdapter().getItemCount());
                                                    }
                                                }, 10);
                                            }
                                        });

                                    }
                                }, difference);
                            }

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == MESSAGE_TYPE_IN) {
                return new MessageInViewHolder(LayoutInflater.from(context).inflate(R.layout.bubbleend, parent, false));
            } else {
                return new MessageOutViewHolder(LayoutInflater.from(context).inflate(R.layout.bubblestart, parent, false));
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Message message = list.get(position);
            Jid jid = null;
            try {
                jid = JidCreate.bareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost());
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }

            if (jid.toString().equals(message.getFrom().toString().split("/")[0])) {
                ((MessageOutViewHolder) holder).bind(position);

            } else if (jid.toString().equals(message.getTo().toString().split("/")[0])) {
                ((MessageInViewHolder) holder).bind(position);

            }
        }

        public Message getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        @Override
        public int getItemViewType(int position) {
            Message message = list.get(position);
            Jid jid = null;
            try {
                jid = JidCreate.bareFrom(JidCreate.bareFrom(getLoginInfo("phone_no") + "@" + Important.getXmppHost()));
            } catch (XmppStringprepException e) {
                e.printStackTrace();
            }


            if (message.getFrom().toString().split("/")[0].equals(jid.toString())) {
                return 0;
            } else if (message.getTo().toString().split("/")[0] == jid.toString()) {
                return 1;
            } else {
                return 1;
            }

        }
    }


    void RestartActivity(String type) {
        Intent i = getIntent();
        if (type.equals("type1")) {
            i.putExtra("boundage", "1");
        } else {
            i.putExtra("boundage", "0");
        }

        LinearLayoutManager layoutManager = ((LinearLayoutManager) messagerecyclerview.getLayoutManager());
        overridePendingTransition(0, 0);
        i.putExtra("recyclerview", layoutManager.findFirstVisibleItemPosition());

        if (typemessage.getText().toString() != null) {
            i.putExtra("message", typemessage.getText().toString());
        }
        startActivity(i);

        overridePendingTransition(0, 0);
        finish();

    }


    //1 day = 3600 x 24 = 86400
    public long dateDifference(Date startDate, Date endDate) {
        //milliseconds
        return endDate.getTime() - startDate.getTime();


    }

    private String getDate(String expiry) {
        String ourDate;
        try {

            SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss Z"); //this format changeable


            Calendar date = Calendar.getInstance();
            long t = date.getTimeInMillis();
            Date afterAddingTenMins = new Date(t + (Integer.parseInt(expiry) * 60000));

            ourDate = dateFormatter.format(afterAddingTenMins);

            //Log.d("ourDate", ourDate);
        } catch (Exception e) {
            ourDate = "00-00-0000 00:00";
        }
        return ourDate;
    }


    @Override
    protected void onDestroy() {
        try {
            updateUserInfo(1, "");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}