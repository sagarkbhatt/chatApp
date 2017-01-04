package com.example.sagarbhatt.chatapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.FirebaseListAdapter;

import java.util.Random;

public class MainActivity extends AppCompatActivity  {

    private Firebase FireRef;
    private ListView chatLis;
   //private EditText UserName;
   private static boolean mIsInForegroundMode;
    private String clientName;
private String mUsername;
    FirebaseListAdapter<ChatMessage> mListAdapter ;
    Button login;

    public MainActivity() {
    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        mListAdapter.cleanup();
    }
@Override
protected void onStart()
{
    super.onStart();
    SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs",0);
    mIsInForegroundMode = true;
    EditText username = (EditText) findViewById(R.id.txtUser);
   // Log.d("Usernameoutside", username.getText().toString());
    String use = prefs.getString("username",null);
if(use!=null && use != "")
{
    username.setText(use);
    username.setFocusable(false);
}
}
    @Override
    protected void onResume() {
        super.onResume();

    }
    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        final PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),9999,intent,PendingIntent.FLAG_UPDATE_CURRENT);


        Firebase.setAndroidContext(this);

        chatLis = (ListView)this.findViewById(android.R.id.list);

        FireRef = new Firebase("https://fiery-heat-2572.firebaseio.com/chat");

        final ListView listView = (ListView) this.findViewById(android.R.id.list);

        mListAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,android.R.layout.two_line_list_item, FireRef) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                Notification notification= new Notification.Builder(getApplicationContext())
                        .setContentTitle("New message: " + model.getName())
                        .setContentText( model.getMessage())
                        .setSound(Uri.parse("android.resource://com.example.sagarbhatt.chatapp/raw/thugringa"))
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.messenger_bubble_small_blue)
                        .setAutoCancel(true)
                        .build();

                Random random = new Random();
                int m = random.nextInt(9999 - 1000) + 1000;
                CharSequence appName =  getApplicationContext().getPackageManager().getApplicationLabel(getApplicationContext().getApplicationInfo());
                NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify((String)appName,m,notification);
                Log.d("Inside Adapter","Hello");
                ((TextView) v.findViewById(android.R.id.text1)).setText(model.getName());
                ((TextView) v.findViewById(android.R.id.text2)).setText(model.getMessage());
            }
        };


        mListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(mListAdapter.getCount()-1);
            }
        });

        listView.setAdapter(mListAdapter);
        Button BtnSend=(Button)findViewById(R.id.btnSend);
        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                EditText inputText = (EditText) findViewById(R.id.txtEdit);
                String input = inputText.getText().toString();
                if (!input.equals("")) {

                    SharedPreferences prefs = getApplication().getSharedPreferences("ChatPrefs",0);

                    EditText username = (EditText) findViewById(R.id.txtUser);

                    String use = prefs.getString("username",null);
                    if ( use == "" ||use == null ) {


                        prefs.edit().putString("username", username.getText().toString()).apply();
                    }


                    ChatMessage chat = new ChatMessage(username.getText().toString(),input);
                    // Create a new, auto-generated child of that chat location, and save our chat data there
                    FireRef.push().setValue(chat);
                    inputText.setText("");
                }
            }
        });



    }


}
