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
import java.sql.Blob;
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

    int different_recipe; // 더보기를 입력한 횟수

    String param; // url 추가 파라미터값 (재료명인지 음식명인지)
    String name; // url 추가 파라미터값 (이 재료or음식의 이름이 뭔지)

    String[] ingredient_array; // 재료들 들어갈 배열

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
                    if(message.contains("재료") || message.contains("음식")){
                        getResponse(message);
                        String sql = "delete from recipe";
                        db.execSQL(sql);
                        different_recipe = 1;
                    }
                    if(message.contains("더보기")){
                        different_recipe++;
                    }
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

        ingredient_array = msg.split(" ");
        name = ingredient_array[1];

        if(msg.contains("재료")){
            param =  "RCP_PARTS_DTLS";
        } else {
            param ="RCP_NM";
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
                if(botReply.contains("보여드리겠습니다")) { // 재료 당근, 음식 카레, 더보기   << 이런 말들을 했을 때 봇의 대답에 공통적으로 포함된 문자열이 '보여드리겠습니다'임.
                    getRecipe();
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

                        String sql = "select rcp_nm, rcp_parts_dtls, manual , eng, car, pro, fat, na, img from recipe";
                        if(ingredient_array.length > 2){

                            sql += " where (";

                            String ingredient;
                            for(int i=1;i<ingredient_array.length;i++){
                                ingredient=ingredient_array[i];
                                sql += "rcp_parts_dtls like '%" + ingredient + "%'";
                                if(i< ingredient_array.length - 1){
                                    sql += " and ";
                                }
                            }
                            sql += ")";
                        }

                        Cursor cursor = null;
                        try{
                            cursor = db.rawQuery(sql,null);
                        } catch (Exception e){
                            e.printStackTrace();
                        }

                        if(cursor!=null){
                            for(int i=0;i<different_recipe;i++) {
                                cursor.moveToNext();
                            }

                            String rsp_nm = cursor.getString(0);
                            String rsp_parts_dtls = cursor.getString(1);
                            String manual = cursor.getString(2);
                            String eng = cursor.getString(3);
                            String car  = cursor.getString(4);
                            String pro = cursor.getString(5);
                            String fat = cursor.getString(6);
                            String na = cursor.getString(7);
                            byte[] img = cursor.getBlob(8);


                            out_buffer.append("요리명 : " + rsp_nm + "\n\n")
                                    .append("재료 : " + rsp_parts_dtls + "\n\n")
                                    .append("조리법" + "\n" + manual + "\n\n")
                                    .append("열량 : " + eng +"\n\n")
                                    .append("탄수화물 : " + car +"\n\n")
                                    .append("단백질 : " + pro +"\n\n")
                                    .append("지방 : " + fat + "\n\n")
                                    .append("나트륨 : " + na +"\n\n")
                                    .append("다른 레시피를 원하시면 '더보기'를 입력해주세요.");
                            messageList.add(new Message(out_buffer.toString(), false));
                            chatAdapter.notifyDataSetChanged();
                            Objects.requireNonNull(chatView.getLayoutManager()).scrollToPosition(messageList.size() - 1);
                            out_buffer.delete(0, out_buffer.length());
                        }
                    }
                });

            }
        }).start();
    }



    void getXmlData() {
        StringBuffer buffer = new StringBuffer();
        String location = URLEncoder.encode(name);

        String queryUrl = "http://openapi.foodsafetykorea.go.kr/api/61022da3f7f74985a123/COOKRCP01/xml/" + different_recipe + "/1000/" + param + "=" + location;

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
                            buffer.delete(0, buffer.length());
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

                        }else if(tag.equals("INFO_ENG")){
                            xpp.next();
                            cv.put("eng", xpp.getText());
                        }else if(tag.equals("INFO_CAR")){
                            xpp.next();
                            cv.put("car", xpp.getText());
                        }else if(tag.equals("INFO_PRO")){
                            xpp.next();
                            cv.put("pro", xpp.getText());
                        }else if(tag.equals("INFO_FAT")){
                            xpp.next();
                            cv.put("fat", xpp.getText());
                        }else if(tag.equals("INFO_NA")){
                            xpp.next();
                            cv.put("na", xpp.getText());
                        }else if(tag.equals("ATT_FILE_NO_MAI")){
                            xpp.next();
                            cv.put("img", xpp.getText());
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