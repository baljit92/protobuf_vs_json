package com.baljit.performance.listener;

import com.baljit.performance.model.SubmissionOuterClass.Submissions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.protobuf.InvalidProtocolBufferException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class QueueListener {
    private static final String PROTO_QUEUE = "proto";
    private static final String JSON_QUEUE = "json";

    private final Logger logger = LoggerFactory.getLogger(QueueListener.class);

    @JmsListener(destination = PROTO_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessageProto(Message<byte[]> user) throws InvalidProtocolBufferException {
        Submissions.Builder submissions = Submissions.newBuilder();
        submissions.mergeFrom(user.getPayload());

        logger.info("Protobuf Received message: {}", submissions.getSubmissionCount());
//		logger.info("Protobuf Received message: {}", submissions.toString());
        logger.info("Protobuf Size message: {}", user.getPayload().length);
    }

    @JmsListener(destination = JSON_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void receiveMessageJson(Message<byte[]> user) throws IOException {

        Submissions.Builder submissions = Submissions.newBuilder();
        String request = new String(user.getPayload());
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(request);
        String prettyJsonString = gson.toJson(je);
        logger.info("Json Received message total: {}", je.getAsJsonObject().get("submissions").getAsJsonArray().size());
//		logger.info("Json Received message: {}", prettyJsonString);
        logger.info("Json Size message: {}", user.getPayload().length);

    }

}

