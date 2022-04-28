package org.litteraworlds;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private String hostName="ec2-52-48-159-67.eu-west-1.compute.amazonaws.com";
    private String dBName ="dfblnf1s7191i8";
    private String port="5432";
    private String user = "esukhkdkydaenr";
    private String password ="74c49179e980b54db7205c8b3379df4ab7e326a55f741bd8d6d81234295f609a";

    static public String authTable = "users";

    static Connection connectionDB;


    public  DatabaseConnection(){}


    public  void createConnect() throws SQLException {

        openConnectionDB();

    }



    public  void openConnectionDB()  {


        try{
            Class.forName("org.postgresql.Driver");
            connectionDB= DriverManager
                    .getConnection("jdbc:postgresql://"+hostName+":"+port+"/"+ dBName +"?user="+user+"&password="+password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("[DATABASE_CONNECTION] successful connection to "+ dBName);

    }







    public  void closeConnectionDB() throws SQLException {


        connectionDB.close();



    }



}
