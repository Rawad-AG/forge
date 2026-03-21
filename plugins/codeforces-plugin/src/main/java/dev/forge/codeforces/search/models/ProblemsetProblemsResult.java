package dev.forge.codeforces.search.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProblemsetProblemsResult {
    private List<Problem> problems;
    private List<ProblemStatistics> problemStatistics;

}
