package com.example.chatbot.interfaces;

import com.google.cloud.dialogflow.v2.DetectIntentResponse;

public interface BotReply {

    public void callback(DetectIntentResponse returnResponse);
}
