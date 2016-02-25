package com.example.sagarbhatt.chatapp;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.fasterxml.jackson.databind.deser.std.DateDeserializers;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseListAdapter;
import com.firebase.ui.auth.core.AuthProviderType;
import com.firebase.ui.auth.core.FirebaseLoginBaseActivity;
import com.firebase.ui.auth.core.FirebaseLoginError;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity  {

    private Firebase FireRef;
    private ListView chatLis;
   //private EditText UserName;
    private String clientName;
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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent =new Intent(getApplicationContext(),MainActivity.class);
        final PendingIntent pendingIntent=PendingIntent.getActivity(getApplicationContext(),1,intent,0);

        Firebase.setAndroidContext(this);

        chatLis = (ListView)this.findViewById(android.R.id.list);

        FireRef = new Firebase("https://fiery-heat-2572.firebaseio.com/chat");

        final ListView listView = (ListView) this.findViewById(android.R.id.list);
        mListAdapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,android.R.layout.two_line_list_item, FireRef) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                Notification notification= new Notification.Builder(getApplicationContext())
                        .setContentTitle("New message")
                        .setContentText( model.getName())
                        .setSound(Uri.parse("android.resource://com.example.sagarbhatt.chatapp/raw/thugringa"))
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.messenger_bubble_small_blue)
                        .build();

                NotificationManager notificationManager=(NotificationManager)getSystemService(NOTIFICATION_SERVICE);
                notificationManager.notify(1,notification);
                Log.d("Inside Adapter","Hello");
                ((TextView) v.findViewById(android.R.id.text1)).setText(model.getName());
                ((TextView) v.findViewById(android.R.id.text2)).setText(model.getMessage());
            }
        };
        listView.setAdapter(mListAdapter);

        final EditText UserName=(EditText)findViewById(R.id.txtUser);

        UserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {

                    UserName.setFocusable(false);
                    if (UserName.getText().toString().length() <= 0) {

                        UserName.setText("Annonymous");
                    }
                }
            }
        });

        final EditText msgSend = (EditText) findViewById(R.id.txtEdit);
        Button BtnSend=(Button)findViewById(R.id.btnSend);
        BtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = msgSend.getText().toString();
                clientName = UserName.getText().toString();
                ChatMessage message = new ChatMessage(clientName, text);
                FireRef.push().setValue(message);
                msgSend.setText("");
            }
        });



    }


}
