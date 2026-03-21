package dev.forge.codeforces.search.models;

import java.util.List;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.Ansi.Color;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Problem {
    private Integer contestId;
    private String index;
    private String name;
    private String type;
    private Integer points;
    private Integer rating;
    private List<String> tags;

    public String getProblemUrl() {
        return "https://codeforces.com/contest/" + contestId + "/problem/" + index;
    }

    public String getFileName() {
        return contestId + index;
    }

    public String getHeader() {
        return contestId + index + " - " + name + " (" + Ansi
                .ansi()
                .fg(getColor())
                .a(rating)
                .reset()
                .toString() + ")";
    }

    public Color getColor() {
        if (rating == null)
            return Color.DEFAULT;

        if (rating <= 1000)
            return Color.GREEN;

        if (rating <= 1500)
            return Color.GREEN;

        if (rating <= 3000)
            return Color.RED;

        return Color.DEFAULT;
    }

    @Override
    public String toString() {
        return getHeader();
    }

}
