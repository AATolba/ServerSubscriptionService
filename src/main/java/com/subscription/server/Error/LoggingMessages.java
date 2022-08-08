package com.subscription.server.Error;

public class LoggingMessages {
    public String addUser(){
        return "User Added Successfully";
    }

    public String updateUser() {
        return "user updated successfully";
    }

    public String deleteUser() {
        return "user deleted successfully";

    }

    public String returnUser() {
        return "user found successfully";
    }

    public String retunedAllUsers() {
        return "user all server database was loaded and returned";
    }

    public String checkingForCreatingServers() {
        return "waiting until creating server finishes to subscribe to the rest of the remaining memory";
    }

    public String allocatingServer() {
        return "allocating a server to a user";

    }

    public String attemptingToSaveServer() {
        return "saving a new server to the dataBase ";

    }

    public String attemptingToSubscribeToServer() {
        return "a user is trying to subscribe to a server";

    }
}
