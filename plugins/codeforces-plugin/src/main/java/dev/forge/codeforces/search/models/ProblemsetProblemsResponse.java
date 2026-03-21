package dev.forge.codeforces.search.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProblemsetProblemsResponse {
    private String status;
    private ProblemsetProblemsResult result;

}
