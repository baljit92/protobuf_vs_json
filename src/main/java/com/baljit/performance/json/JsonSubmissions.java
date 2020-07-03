package com.baljit.performance.json;

import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class JsonSubmissions {
        private List<JsonSubmission> submissions = Collections.emptyList();
}
