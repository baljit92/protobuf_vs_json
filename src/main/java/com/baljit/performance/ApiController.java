package com.baljit.performance;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.util.EncodingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Date;

@RestController
public class ApiController {

    private static final String PROTO_QUEUE = "proto";
    private static final String JSON_QUEUE = "json";

    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

    @Autowired
    private JmsTemplate jmsTemplate;

    @RequestMapping(path = "/proto", method = RequestMethod.GET, produces = "application/json")
    public String protoMessage() {
        logger.info("Sending message");
        jmsTemplate.convertAndSend(PROTO_QUEUE, PerformanceTest.getSubmissions().toByteArray());
        return "Ok";
    }

    @RequestMapping(path = "/json", method = RequestMethod.GET, produces = "application/json")
    public String jsonMessage() throws IOException {
        logger.info("Sending message");
        jmsTemplate.convertAndSend(JSON_QUEUE, EncodingUtils.getAsciiBytes(PerformanceTest.getJsonSubmissions()));
        return "Ok";
    }

    @RequestMapping(path = "/both", method = RequestMethod.POST, produces = "application/json")
    public String both(@RequestParam Integer increment) throws IOException, UnirestException, InterruptedException {
        logger.info("Running both: " + increment);
        long protobufTimes = 0;
        long jsonTimes = 0;
//        increment requests to the number of requests to be put on the queue per protocol
        for (int i = 0; i < increment; i++) {
            protobufTimes += this.getProtobuf();
            jsonTimes += this.getJson();
        }
//        calculate average time
        long totalProtobuf = protobufTimes / increment;
        long totalJson = jsonTimes / increment;
        Thread.sleep(100L);
        logger.info("It took an average of " + totalProtobuf + "ms to load with protobuf.");
        logger.info("It took an average of " + totalJson + "ms to load with json.");
        logger.info("Both is done!");
        return "Ok";
    }

    private long getProtobuf() throws UnirestException, IOException {
        long start = (new Date()).getTime();
        this.protoMessage();
        long time = (new Date()).getTime() - start;
        System.out.println(
                "protobuf took " + time + "ms to load " + PerformanceTest.getSubmissions().getSubmissionCount() + " submissions.");
        return time;
    }

    private long getJson() throws UnirestException, IOException {
        long start = (new Date()).getTime();
        this.jsonMessage();
        long time = (new Date()).getTime() - start;
        System.out
                .println("json took " + time + "ms to load " + PerformanceTest.getSubmissions().getSubmissionCount() + " submissions.");
        return time;
    }
}