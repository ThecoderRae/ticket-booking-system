package org.sibeworks.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sibeworks.entities.Train;
import org.sibeworks.entities.User;
import org.sibeworks.util.UserServiceUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserBookingService {

    private static final String USERS_PATH = "app/src/main/java/org/sibeworks/localDb/users.json";
    private User user;
    private ObjectMapper objectMapper = new ObjectMapper();
    private List<User> userList;

    public UserBookingService (User user) throws IOException {

        this.user = user;
        File users = new File(USERS_PATH);
        userList = objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }


    public UserBookingService() throws IOException {
        loadUsers();
    }


    public List<User> loadUsers() throws IOException {
        File users = new File(USERS_PATH);
        return objectMapper.readValue(users, new TypeReference<List<User>>() {});
    }


    public Boolean loginUser() {
        Optional<User> foundUser = userList.stream().filter(user1 ->
            user1.getName().equals(user.getName()) &&
            UserServiceUtil.checkPassword(user.getPassword(), user1.getHashPassword())
            ).findFirst();
        return foundUser.isPresent();
    }


    public Boolean signUp(User user1) {
        try {
            userList.add(user1);
            saveUserListToFile();
            return Boolean.TRUE;

        } catch (IOException exception) {
            return Boolean.FALSE;
        }
    }


    private void saveUserListToFile() throws IOException {
        File usersFile = new File(USERS_PATH);
        objectMapper.writeValue(usersFile, userList);
    }


    public void fetchBooking() {
        user.printTickets();
    }

    public List<Train> getTrains(String source, String destination) {
        TrainService trainService = new TrainService();
        return trainService.searchTrains(source, destination);
    }
}
