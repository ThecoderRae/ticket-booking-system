package org.sibeworks.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sibeworks.entities.Train;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {

    private static final String TRAINS_PATH = "localDb/trains.json";  // Ensure correct path in classpath
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<Train> trainList = new ArrayList<>();

    public TrainService() {}

    public List<Train> loadTrainData() throws IOException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(TRAINS_PATH);

        if (inputStream == null) {
            throw new IOException("Train data file not found in the classpath.");
        }

        return objectMapper.readValue(inputStream, new TypeReference<List<Train>>() {});
    }


    public List<Train> getTrainList() {
        try {
            if (trainList.isEmpty()) {  // Check if train list is empty and load data if needed
                trainList = loadTrainData();
                System.out.println("Train data loaded successfully: " + trainList);
            }
        } catch (IOException e) {
            System.err.println("Error loading train data: " + e.getMessage());
        }
        return trainList;
    }


    public List<Train> searchTrains(String source, String destination) {
        if (source == null || destination == null || source.isEmpty() || destination.isEmpty()) {
            System.out.println("Source and destination cannot be empty.");
            return new ArrayList<>(); // Return an empty list if either source or destination is invalid
        }

        // Create new effectively final variables for use in lambda expressions
        final String normalizedSource = source.trim().toLowerCase();  // Ensure case-insensitive matching
        final String normalizedDestination = destination.trim().toLowerCase();  // Ensure case-insensitive matching

        return getTrainList().stream()
                .filter(train -> validTrain(train, normalizedSource, normalizedDestination))  // Use final variables
                .collect(Collectors.toList());
    }


    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();

        // Debugging output: Print available stations
        System.out.println("Available stations: " + stationOrder);

        // Convert stations to lowercase for case-insensitive matching
        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

        // Debugging output: Print source and destination index
        System.out.println("Source index: " + sourceIndex + ", Destination index: " + destinationIndex);

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }
}
