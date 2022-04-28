package org.litteraworlds;

import java.sql.SQLException;
import java.util.ArrayList;

public class ServerController {

    public static ArrayList<String> loginHistory = new ArrayList<>();

    public static void main(String[] args) throws SQLException {

        DatabaseConnection databaseConnection = new DatabaseConnection();
        databaseConnection.createConnect();

        ServerListener listener = new ServerListener();
        listener.startServerListener();

    }




}