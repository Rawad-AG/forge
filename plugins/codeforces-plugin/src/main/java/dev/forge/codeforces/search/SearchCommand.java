package dev.forge.codeforces.search;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import dev.forge.codeforces.search.cache.ProblemCache;
import dev.forge.codeforces.search.models.Problem;
import dev.forge.codeforces.search.models.ProblemsetProblemsResponse;
import dev.forge.engine.cli.input.Prompter;
import dev.forge.engine.cli.output.Console;
import dev.forge.engine.config.ForgeConfigLoader;
import dev.forge.engine.core.ForgeEngine;
import dev.forge.engine.utils.MustacheUtils;
import dev.forge.engine.utils.SystemUtils;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "search", aliases = "s", mixinStandardHelpOptions = true)
public class SearchCommand implements Runnable {

    private final Console console = ForgeEngine.context().console();
    private final Prompter prompter = ForgeEngine.context().prompter();
    private final ForgeConfigLoader config = ForgeEngine.context().config();
    private final ProblemCache cache = new ProblemCache();

    @Option(names = { "--min", "-min" }, description = "Minimum rating")
    private Integer minRating;

    @Option(names = { "--max", "-max" }, description = "Maximum rating")
    private Integer maxRating;

    @Option(names = { "-i", "--invalidate-cache" }, description = "delete the locale cache", defaultValue = "false")
    private boolean invalidateCache;

    @Override
    public void run() {
        try {
            int min = minRating != null ? minRating : config.getInt("codeforces.min-rating", 800);
            int max = maxRating != null ? maxRating : config.getInt("codeforces.max-rating", 3500);

            console.info("Fetching problems (rating: " + min + " - " + max + ")...");

            ProblemsetProblemsResponse response = getProblems();

            if (response == null || !"OK".equals(response.getStatus())) {
                console.error("Failed to fetch problems from Codeforces API");
                return;
            }

            List<Problem> filteredProblems = filterByRating(response.getResult().getProblems(), min, max);

            if (filteredProblems.isEmpty()) {
                console.warn("No problems found with rating between " + min + " and " + max);
                return;
            }

            console.success("Found " + filteredProblems.size() + " problems");

            Problem selected = selectProblem(filteredProblems, 0);

            if (selected != null) {
                createProblemFile(selected);
                SystemUtils.openBrowser(selected.getProblemUrl());
            }

        } catch (Exception e) {
            console.error("Error during search", e);
        }
    }

    private ProblemsetProblemsResponse getProblems() throws IOException {
        ProblemsetProblemsResponse cached = null;

        if (!invalidateCache)
            cached = cache.getCachedProblems();

        if (cached != null) {
            console.info("Using cached problems");
            return cached;
        }

        console.info("Fetching problems from API...");
        ProblemsetProblemsResponse response = cache.fetchProblemsFromApi();
        if (response != null && "OK".equals(response.getStatus())) {
            cache.cacheProblems(response);
            console.success("Problems cached successfully");
        }
        return response;
    }

    private List<Problem> filterByRating(List<Problem> problems, int minRating, int maxRating) {
        return problems.stream()
                .filter(p -> p.getRating() != null && p.getRating() >= minRating && p.getRating() <= maxRating)
                .sorted((v1, v2) -> Integer.compare(v1.getRating(), v2.getRating()))
                .toList();
    }

    private Problem selectProblem(List<Problem> problems, int last) {
        int PAGE_SIZE = config.getInt("codeforces.page-size", 10);
        Problem randomProblem = problems.get(new Random().nextInt(problems.size()));

        return prompter.paginate("choose a problem [random choice: " + "]", problems)
                .size(PAGE_SIZE)
                .defaultOption(randomProblem)
                .run();
    }

    private void createProblemFile(Problem problem) throws IOException {
        String problemsDir = config.getPath("codeforces.problems-dir", ".");
        String template = "templates/Main.mustache";

        File dir = new File(problemsDir, problem.getFileName());
        if (!dir.exists())
            dir.mkdirs();

        MustacheUtils.create(getClass().getClassLoader().getResourceAsStream(template), new File(dir, "Main.java"));
    }
}
