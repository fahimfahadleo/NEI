package com.horofbd.MeCloak;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SASLAuthentication;
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
import org.jivesoftware.smack.sasl.SASLMechanism;
import org.jivesoftware.smack.tcp.BundleAndDefer;
import org.jivesoftware.smack.tcp.BundleAndDeferCallback;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smack.util.ByteUtils;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptRequest;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.util.Random;

import javax.security.auth.callback.CallbackHandler;

public abstract class XmppConnection{
    static Context context;
    String touserjid;
    String userid;
    String pass;
    protected static ConnectionListener connectionListener;
    protected static AbstractXMPPConnection mConnection;

    public XmppConnection(Context context,String userid,String pass){
        XmppConnection.context = context;
        this.userid = userid;
        this.pass = pass;

        Connect connect = new Connect();
        connect.execute(userid,pass);
    }
    public XmppConnection(Context context,String jid){
        XmppConnection.context = context;
        this.touserjid = jid;
    }




   static class Connect extends AsyncTask<String, String, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            //TOdo debugger remove//
            XMPPTCPConnectionConfiguration config = null;
            XMPPTCPConnectionConfiguration.Builder builder;
            try {
                builder = XMPPTCPConnectionConfiguration.builder()
                        .setServiceName(JidCreate.domainBareFrom(Important.getXmppHost()))
                        .setHost(Important.getXmppHost())
                        // .setDebuggerEnabled(true)
                        .setPort(Integer.parseInt(Important.getXmppPort()))
                        .setUsernameAndPassword(strings[0], strings[1])
                        .setSecurityMode(ConnectionConfiguration.SecurityMode.required)
                        .setResource(Important.getXmppResource())
                        .setCustomX509TrustManager(Functions.getTrustFactory(context))
                        .setSendPresence(true)
                        .setCompressionEnabled(false);

                config = builder.build();

            } catch (IOException e) {
                e.printStackTrace();
            }
            XMPPTCPConnection.setUseStreamManagementResumptionDefault(true);

            XMPPTCPConnection.setUseStreamManagementDefault(true);
            mConnection = new XMPPTCPConnection(config);

            try {
                mConnection.addConnectionListener(connectionListener);
                mConnection.connect();
            } catch (SmackException | IOException | XMPPException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}

