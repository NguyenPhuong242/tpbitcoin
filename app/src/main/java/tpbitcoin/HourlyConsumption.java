package tpbitcoin;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class HourlyConsumption {
    private final long value; //in Mwh
    private final LocalDateTime startTime; // ISO_LOCAL format
    private final LocalDateTime endTime;

    public HourlyConsumption(long value, LocalDateTime startTime, LocalDateTime endTime) {
        this.value = value;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    //return consumption in kwh
    public long getConsumption(){
        return value * 1000;
    }


    // TODO
    public static long getFinnishConsumptionLast24h(String apiKey){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime yesterday = now.minus(1, ChronoUnit.DAYS);
        return getFinnishConsumption(yesterday, now, apiKey).stream().mapToLong(HourlyConsumption::getConsumption).sum();
    }

    // TODO
    public static List<HourlyConsumption> getFinnishConsumption(LocalDateTime startTime, LocalDateTime endTime, String apiKey){
        String json = queryFinnishAPI(startTime, endTime, apiKey);
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Type listType = new TypeToken<ArrayList<HourlyConsumption>>(){}.getType();
        return gson.fromJson(json, listType);
    }

    // TODO
    private static String queryFinnishAPI(LocalDateTime startTime, LocalDateTime endTime, String apiKey){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
        String start = startTime.format(formatter);
        String end = endTime.format(formatter);
        HttpClient client = HttpClient.newHttpClient();
        // https://data.fingrid.fi/api/datasets/{datasetId}/data[?startTime][&endTime][&format][&oneRowPerTimePeriod][&page][&pageSize][&locale][&sortBy][&sortOrder]
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://data.fingrid.fi/api/datasets/124/data?" + start + "&" + end))
                .header("x-api-key", apiKey)
                .build();
        HttpResponse<String> response = null;
        try {
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        return response.body();
    }

}
