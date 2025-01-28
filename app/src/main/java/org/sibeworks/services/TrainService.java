package org.sibeworks.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sibeworks.entities.Train;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TrainService {

    private List<Train> trainList;

    public TrainService(List<Train> trainList) {
        this.trainList = trainList;
    }

    public TrainService() {
        this.trainList = new ArrayList<>();
        try {
            loadTrainData();
        } catch (IOException e) {
            System.err.println("Error loading train data: " + e.getMessage());
        }
    }

    private void loadTrainData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File trainsFile = new File("path/to/trains.json"); // Update the path
        if (trainsFile.exists()) {
            trainList = objectMapper.readValue(trainsFile, new TypeReference<List<Train>>() {});
        } else {
            System.err.println("trains.json file not found at: " + trainsFile.getAbsolutePath());
        }
    }

    public List<Train> getTrainList() {
        return trainList;
    }

    public void setTrainList(List<Train> trainList) {
        this.trainList = trainList;
    }

    public List<Train> searchTrains(String source, String destination) {
        return trainList.stream().filter(train -> validTrain(train, source, destination)).collect(Collectors.toList());
    }

    private boolean validTrain(Train train, String source, String destination) {
        List<String> stationOrder = train.getStations();

        int sourceIndex = stationOrder.indexOf(source.toLowerCase());
        int destinationIndex = stationOrder.indexOf(destination.toLowerCase());

        return sourceIndex != -1 && destinationIndex != -1 && sourceIndex < destinationIndex;
    }
}
