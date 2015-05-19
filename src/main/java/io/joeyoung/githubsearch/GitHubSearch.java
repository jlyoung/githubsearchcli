/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.joeyoung.githubsearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import static java.lang.Thread.sleep;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Git Hub command line search tool
 *
 * @author joeyoung
 */
public class GitHubSearch {

    private static final Logger LOGGER = Logger.getLogger(
            GitHubSearch.class.getName());

    /**
     * Searches Git Hub repositories for the supplied search term and displays
     * results.
     *
     * @param args
     */
    public static void main(String[] args) {
        configureLogHandler();

        Options options = buildOptions();

        // Parse the command line
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine line = parser.parse(options, args);
            StringBuilder requestUri = buildRequestUri(line);

            // Execute API call
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(requestUri.toString());
            HttpResponse response = client.execute(request);
            try {
                sleep(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }

            // Parse the response
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
            StringBuilder results = new StringBuilder();
            String resultLine;
            while ((resultLine = bufferedReader.readLine()) != null) {
                results.append(resultLine).append("\n");
            }
            JsonObject responseObject;
            try (JsonReader jsonReader = Json.createReader(new StringReader(results.toString()))) {
                // Parse the result entries
                responseObject = jsonReader.readObject();
                int totalCount = responseObject.getInt("total_count");
                JsonArray items = responseObject.getJsonArray("items");
                List<SearchResultEntry> searchResults = parseSearchResults(items);

                // Display the result entries
                displaySearchResults(searchResults, totalCount);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Unable to read Git Hub response json object.", e);
            }
        } catch (NumberFormatException nfe) {
            LOGGER.log(Level.SEVERE, "limit argument requires a number.", nfe);
        } catch (ParseException exp) {
            //LOGGER.log(Level.SEVERE, "Command line parsing failed.", exp);
            String header = "Search Git Hub from the command line\n\n";
            String footer = "\nAuthor: Joe Young http://joeyoung.io";
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar githubsearchcli-1.0.jar", header, options, footer, true);
        } catch (UnsupportedEncodingException ex) {
            LOGGER.log(Level.SEVERE, "Unable to encode argument.", ex);
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "GitHub API call failed.", ex);
        }
    }

    /**
     * Formats search results and displays them to standard output.
     *
     * @param searchResults
     * @param totalCount
     */
    private static void displaySearchResults(List<SearchResultEntry> searchResults, int totalCount) {
        System.out.format("Displaying %d of %d results:\n\n", searchResults.size(), totalCount);
        for (SearchResultEntry entry : searchResults) {
            System.out.format("%-54s %15s * %-6d\n", entry.getFullName(), entry.getLanguage(), entry.getStargazersCount());
            System.out.println(entry.getDescription());
            StringBuilder updatedBuilder = new StringBuilder("Updated on ");
            if (entry.getUpdatedAt() != null) {
                SimpleDateFormat updatedFormat = new SimpleDateFormat("yyyy/MM/dd");
                updatedBuilder.append(updatedFormat.format(entry.getUpdatedAt()));
            }
            System.out.println(updatedBuilder.toString());
            System.out.println("Repo: " + entry.getHtmlUrl());
            System.out.println("git clone " + entry.getCloneUrl());
            System.out.println(StringUtils.repeat("-", 80));
        }
    }

    /**
     * Returns a list of SearchResultEntry objects populated from the parsed
     * items JSON Array
     *
     * @param items
     * @return
     */
    private static List<SearchResultEntry> parseSearchResults(JsonArray items) {
        List<SearchResultEntry> searchResults = new ArrayList<>();
        for (JsonValue item : items) {
            JsonObject itemEntry;
            try (JsonReader itemReader = Json.createReader(new StringReader(item.toString()))) {
                itemEntry = itemReader.readObject();
                SearchResultEntry sre = new SearchResultEntry();
                sre.setName(itemEntry.isNull("name") ? "" : itemEntry.getString("name"));
                sre.setFullName(itemEntry.isNull("full_name") ? "" : itemEntry.getString("full_name"));
                sre.setHtmlUrl(itemEntry.isNull("html_url") ? "" : itemEntry.getString("html_url"));
                sre.setDescription(itemEntry.isNull("description") ? "" : itemEntry.getString("description"));
                if (!itemEntry.isNull("updated_at")) {
                    DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    sre.setUpdatedAt(sdf.parse(itemEntry.getString("updated_at")));
                }
                sre.setCloneUrl(itemEntry.isNull("clone_url") ? "" : itemEntry.getString("clone_url"));
                sre.setStargazersCount(itemEntry.getInt("stargazers_count"));
                sre.setLanguage(itemEntry.isNull("language") ? "" : itemEntry.getString("language"));
                sre.setForksCount(itemEntry.getInt("forks_count"));
                searchResults.add(sre);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Unable to read search result item json object.", e);
            }
        }
        return searchResults;
    }

    /**
     * Builds and returns the URL that will be used to query the Git Hub API
     *
     * @param line
     * @return
     * @throws NumberFormatException
     * @throws UnsupportedEncodingException
     */
    private static StringBuilder buildRequestUri(CommandLine line) throws NumberFormatException, UnsupportedEncodingException {
        StringBuilder requestUri = new StringBuilder();
        requestUri.append("https://api.github.com/search/repositories")
                .append("?q=")
                .append(URLEncoder.encode(
                                line.getOptionValue("search-term"), "UTF-8"));
        if (line.hasOption("fields")) {
            String[] fields = line.getOptionValues("fields");
            requestUri.append("+in:").append(StringUtils.join(fields, ","));
        }
        if (line.hasOption("limit")) {
            String limit = line.getOptionValue("limit");
            Integer.parseInt(limit);
            requestUri.append("&per_page=").append(limit).append("&page=1");
        }
        return requestUri;
    }

    /**
     * Configures and returns a commons-cli command line option object.
     *
     * @return
     */
    private static Options buildOptions() {
        // Configure the command line arguments
        Options options = new Options();
        options.addOption(Option.builder("h").longOpt("help")
                .required(false)
                .desc("This help message")
                .hasArg(false).build());
        options.addOption(Option.builder("s").longOpt("search-term")
                .required(true)
                .desc("Terms to search for")
                .hasArgs().argName("\"SEARCH TERMS\"").build());
        options.addOption(Option.builder("f").longOpt("fields")
                .required(false)
                .desc("Fields to be included in search\n"
                        + "\tValid search fields: name description readme\n")
                .hasArgs().argName("FIELD1 FIELD2 FIELD3").build());
        options.addOption(Option.builder("l").longOpt("limit")
                .required(false)
                .desc("Number of entries to limit search results to")
                .hasArg(true).argName("NUM").build());
        return options;
    }

    /**
     * Configures logging.
     *
     * @throws SecurityException
     */
    private static void configureLogHandler() throws SecurityException {
        // Setup logging
        Handler consoleHandler = new ConsoleHandler();
        LOGGER.addHandler(consoleHandler);
        consoleHandler.setLevel(Level.SEVERE);
    }
}
