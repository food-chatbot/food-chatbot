package com.example.chatbot;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatbot.helpers.SendMessageInBg;
import com.example.chatbot.interfaces.BotReply;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.dialogflow.v2.DetectIntentResponse;
import com.google.cloud.dialogflow.v2.QueryInput;
import com.google.cloud.dialogflow.v2.SessionName;
import com.google.cloud.dialogflow.v2.SessionsClient;
import com.google.cloud.dialogflow.v2.SessionsSettings;
import com.google.cloud.dialogflow.v2.TextInput;
import com.google.common.collect.Lists;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;



public class ChatFragment extends Fragment implements BotReply {

    Message message;
    RecyclerView chatView;
    ChatAdapter chatAdapter;
    List<Message> messageList = new ArrayList<>();
    EditText editMessage;
    Button btnSend;
    View view;
    //dialogFlow
    private SessionsClient sessionsClient;
    private SessionName sessionName;
    private String uuid = UUID.randomUUID().toString();
    private String TAG = "ChatFragment";

    private DBHelper helper;
    private SQLiteDatabase db;
    StringBuffer out_buffer = new StringBuffer();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.chat, container, false);
        setHasOptionsMenu(true);

        chatView = view.findViewById(R.id.chatView);
        editMessage = view.findViewById(R.id.editMessage);
        btnSend = view.findViewById(R.id.btnSend);

        chatAdapter = new ChatAdapter(messageList, getActivity());
        chatView.setAdapter(chatAdapter);

        helper = new DBHelper(getActivity().getApplicationContext());
        db = helper.getWritableDatabase();

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                String message = editMessage.getText().toString();
                if (!message.isEmpty()) {
                    messageList.add(new Message(message, false));
                    editMessage.setText("");
                    sendMessageToBot(message);
                    Objects.requireNonNull(chatView.getAdapter()).notifyDataSetChanged();
                    Objects.requireNonNull(chatView.getLayoutManager())
                            .scrollToPosition(messageList.size() - 1);
                } else {
                    Toast.makeText(getActivity(), "Please enter text!", Toast.LENGTH_SHORT).show();
                }
            }
        });


        setUpBot();
        return view;
    }


    private void setUpBot() {
        try {
            InputStream stream = this.getResources().openRawResource(R.raw.credential);
            GoogleCredentials credentials = GoogleCredentials.fromStream(stream)
                    .createScoped(Lists.newArrayList("https://www.googleapis.com/auth/cloud-platform"));
            String projectId = ((ServiceAccountCredentials) credentials).getProjectId();

            SessionsSettings.Builder settingsBuilder = SessionsSettings.newBuilder();
            SessionsSettings sessionsSettings = settingsBuilder.setCredentialsProvider(
                    FixedCredentialsProvider.create(credentials)).build();
            sessionsClient = SessionsClient.create(sessionsSettings);
            sessionName = SessionName.of(projectId, uuid);

            Log.d(TAG, "projectId : " + projectId);
        } catch (Exception e) {
            Log.d(TAG, "setUpBot: " + e.getMessage());
        }
    }

    private void sendMessageToBot(String message) {
        QueryInput input = QueryInput.newBuilder()
                .setText(TextInput.newBuilder().setText(message).setLanguageCode("ko-KR")).build();
        new SendMessageInBg(this, sessionName, sessionsClient, input).execute();

    }

    private void getResponse(String msg){

        String message = msg.toLowerCase();
        messageList.add(new Message(message, true));
        chatAdapter.notifyDataSetChanged();

        if(message.contains("재료")){
            int idx = message.indexOf("재료");
            String ingredient = message.substring(idx + 1);
            URL url = null;
            try {
                url = new URL("http://openapi.foodsafetykorea.go.kr/api/f11ac32b2116463f9f13/COOKRCP01/xml/0/1000/RCP_PARTS_DTLS=" + ingredient);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


        }
        else if(message.contains("음식")){
            int idx = message.indexOf("음식");
            String food = message.substring(idx + 1);
            URL url = null;
            try {
                url = new URL("http://openapi.foodsafetykorea.go.kr/api/f11ac32b2116463f9f13/COOKRCP01/xml/0/1000/RCP_NM=" + food);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void callback(DetectIntentResponse returnResponse) {
        if(returnResponse!=null) {
            String botReply = returnResponse.getQueryResult().getFulfillmentText();
            if(!botReply.isEmpty()){
                messageList.add(new Message(botReply, true));
                chatAdapter.notifyDataSetChanged();
                Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
                if(botReply.contains("보여드리겠습니다")) {
                    getRecipe();
                    String sql = "delete from recipe";
                    db.execSQL(sql);
                }
            }else {
                Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "failed to connect!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getRecipe(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                getXmlData();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String sql = "select id, rcp_nm, rcp_parts_dtls, manual from recipe";

                        int recordCount = -1;
                        Cursor cursor = null;
                        try{
                            cursor = db.rawQuery(sql,null);
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        if(cursor!=null){
                            recordCount = cursor.getCount();
                            for(int i=0;i<recordCount;i++){
                                cursor.moveToNext();

                                String rsp_nm = cursor.getString(1);
                                String rsp_parts_dtls = cursor.getString(2);
                                String manual = cursor.getString(3);

                                out_buffer.append("요리명 : " + rsp_nm + "\n\n").append("재료 : " + rsp_parts_dtls + "\n\n").append("조리법" + "\n" + manual);
                                messageList.add(new Message(out_buffer.toString(), false));
                                chatAdapter.notifyDataSetChanged();
                                Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
                                out_buffer.delete(0, out_buffer.length());
                            }

                        }
                    }
                });

            }
        }).start();
    }



    void getXmlData() {
        StringBuffer buffer = new StringBuffer();
        String rsp = "당근";
        String location = URLEncoder.encode(rsp);

        String queryUrl = "http://openapi.foodsafetykorea.go.kr/api/61022da3f7f74985a123/COOKRCP01/xml/1/3/RCP_PARTS_DTLS=" + location;

        try {
            URL url = new URL(queryUrl);
            InputStream is = url.openStream();

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new InputStreamReader(is, "UTF-8"));

            String tag;
            xpp.next();
            int eventType = xpp.getEventType();

            ContentValues cv = new ContentValues();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {

                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.END_TAG:
                        tag = xpp.getName();
                        if (tag.contains("row")) {
                            cv.put("manual", buffer.toString());
                            db.insert("recipe", null, cv);
                        }
                        break;

                    case XmlPullParser.START_TAG:

                        tag = xpp.getName();

                        if (tag.equals("RCP_NM")) {

                            xpp.next();
                            cv.put("rcp_nm", xpp.getText());


                        } else if (tag.contains("MANUAL") && !tag.contains("_")) {
                            xpp.next();

                            if (xpp.getText() != null) {
                                buffer.append(xpp.getText());
                                buffer.append("\n");
                            }

                        } else if (tag.equals("RCP_PARTS_DTLS")) {

                            xpp.next();
                            cv.put("rcp_parts_dtls", xpp.getText());

                        }
                        break;

                }
                eventType = xpp.next();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}