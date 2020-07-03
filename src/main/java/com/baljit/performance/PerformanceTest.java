package com.baljit.performance;

import com.baljit.performance.json.JsonSubmission;
import com.baljit.performance.json.JsonSubmissions;
import com.baljit.performance.protobuf.SubmissionOuterClass.Submission;
import com.baljit.performance.protobuf.SubmissionOuterClass.Submissions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.util.JsonFormat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class PerformanceTest {
	private static final Integer DATA_PER_REQUEST = 50;
    private static Submissions submissions;
    private static String jsonSubmissionsString;

    @Bean
    ProtobufHttpMessageConverter protobufHttpMessageConverter() {
        return new ProtobufHttpMessageConverter();
    }

    public static void main(String[] args) throws IOException {
        final List<Submission> submissionList = new ArrayList<>();
        final List<JsonSubmission> jsonSubmissionsList = new ArrayList<>();
        for (int i = 0; i < DATA_PER_REQUEST; i++) {
            submissionList.add(generateSubmission(i));
            jsonSubmissionsList.add(generateJsonSubmission(i));

        }

        submissions = Submissions.newBuilder()
                .addAllSubmission(submissionList)
                .build();
        JsonSubmissions jsonSubmissions = new JsonSubmissions();
        jsonSubmissions.setSubmissions(jsonSubmissionsList);
        ObjectMapper Obj = new ObjectMapper();
        jsonSubmissionsString = Obj.writeValueAsString(jsonSubmissions);

        SpringApplication.run(PerformanceTest.class, args);
    }

    public static Submissions getSubmissions() {
        return submissions;
    }

    public static String getJsonSubmissions() {
        return jsonSubmissionsString;
    }

    private static Submission generateSubmission(int i) {
        return Submission.newBuilder()
                .setTpzUserName("dummy"+i+"@gmail.com")
                .setTpzPassword("DummyDummyDummy_"+i)
                .setCourseId("15619")
                .setProjectId("dummy-wiki-analysis")
                .setTpzSubmissionId(UUID.randomUUID().toString())
                .setTpzKey("2hlkjhkj2jhlkni")
                .setSubmissionPath("/path/submission/dummy"+i+"@gmail.com")
                .setCodeUrl("https://www.amigoingtosubmitthishomeworkornot.com/" +
                        "ifisubmititlatewillitbegradedwithpenalty?"+i)
                .setSemester("f20")
                .setDuration(600)
                .setRequestParameters("idon'tknowwhattoputhere")
                .setSubmissionFile("somethingfilewoah!"+i)
                .build();
    }

    private static JsonSubmission generateJsonSubmission(int i) {
        JsonSubmission jsonSubmission = new JsonSubmission();
        jsonSubmission.setTpzUserName("dummy"+i+"@gmail.com");
        jsonSubmission.setTpzPassword("DummyDummyDummy_"+i);
        jsonSubmission.setCourseId("15619");
        jsonSubmission.setProjectId("dummy-wiki-analysis");
        jsonSubmission.setTpzSubmissionId(UUID.randomUUID().toString());
        jsonSubmission.setTpzKey("2hlkjhkj2jhlkni");
        jsonSubmission.setSubmissionPath("/path/submission/dummy"+i+"@gmail.com");
        jsonSubmission.setCodeUrl("https://www.amigoingtosubmitthishomeworkornot.com/" +
                "ifisubmititlatewillitbegradedwithpenalty?"+i);
        jsonSubmission.setSemester("f20");
        jsonSubmission.setDuration(600);
        jsonSubmission.setRequestParameters("idon'tknowwhattoputhere");
        jsonSubmission.setSubmissionFile("somethingfilewoah!"+i);
        return jsonSubmission;
    }
}