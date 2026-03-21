package dev.forge.codeforces.search.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ProblemStatistics {
    private Integer contestId;
    private String index;
    private Integer solvedCount;

}
