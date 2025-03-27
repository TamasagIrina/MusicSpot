package com.example.musicspot;

import java.sql.*;
import java.util.ArrayList;


public class DatabaseConection {
    public static Connection databaseLink;

    public static Connection getConnection(){
        String databaseName=System.getenv("databaseName");;
        String databaseUser=System.getenv("rootName");;
        String databasePassword=System.getenv("databasePassword");;
        String url ="jdbc:mysql://localhost/"+databaseName;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink= DriverManager.getConnection(url, databaseUser, databasePassword);
        }catch (Exception e){
            e.printStackTrace();
        }

        return databaseLink;

    }
    public boolean exitInDatabase(String name, String password){
        Connection connectDB= getConnection();


        String verifyLogin ="SELECT count(1) FROM useraccounts WHERE UserName = '"+name+"' AND Password = '"+password+"'";

        try {

            Statement statement= connectDB.createStatement();
            ResultSet queryResut = statement.executeQuery(verifyLogin);

            while (queryResut.next()){
                if(queryResut.getInt(1)==1){
                    return true;

                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean verifyIsAccountExistDataBase(String name, String database, String calom, int id){
        Connection connectDB= getConnection();

        String verify="SELECT count(1) FROM "+database+" WHERE "+calom+" = '"+name+"' AND  idUserAccounts="+id+";";

        try {

            Statement statement= connectDB.createStatement();
            ResultSet queryResut = statement.executeQuery(verify);

            while (queryResut.next()){
                if(queryResut.getInt(1)==1){

                    return true;

                }
            }

        }catch (Exception e){
            e.printStackTrace();

        }
        return false;

    }

    public void addInDatabase(String name_friend, int id, String database) throws SQLException {
        Connection connectDB= getConnection();

        String insert= "INSERT INTO "+database+" (idUserAccounts , friend_name)\n" +
                "VALUES ("+id+", \""+name_friend+"\");";

        Statement statement= connectDB.createStatement();
        statement.execute(insert);

    }
    public ArrayList<String> getFriendsFromDatabase(int id, String table){
        Connection connectDB= getConnection();
        ArrayList<String> name =new ArrayList<>();

        String segment="SELECT friend_name FROM "+table+" WHERE idUserAccounts="+id+";";
        try {

            Statement statement= connectDB.createStatement();
            ResultSet queryResut = statement.executeQuery(segment);


            while (queryResut.next()){
                name.add(queryResut.getString(1));
            }


        }catch (Exception e){
            e.printStackTrace();

        }
        return name;
    }

    public int getID(String name) throws SQLException {
        Connection connectDB= getConnection();

        String getId="SELECT idUserAccounts FROM useraccounts WHERE UserName='"+name+"';";

        Statement statement = connectDB.createStatement();
        //statement.execute(getId);
        ResultSet rs = statement.executeQuery(getId);
        int generatedKey = 0;
        if (rs.next()) {
            generatedKey = (int) rs.getLong(1);
        }

        return generatedKey;
    }

    public String getname(int id) throws SQLException {
        Connection connectDB= getConnection();

        String getId="SELECT UserName FROM useraccounts WHERE idUserAccounts="+id+";";
        Statement statement = connectDB.createStatement();
        ResultSet rs = statement.executeQuery(getId);
        String name = "";

        if (rs.next()) {
            name= rs.getString(1);
        }

        return name;
    }

    public void delete(String database,String name, int id) throws SQLException {
        DatabaseConection connectNow = new DatabaseConection();
        Connection connectDB= connectNow.getConnection();
        try {

            String delete="DELETE FROM "+database+" WHERE friend_name=\""+name+"\" AND idUserAccounts="+id+";";
            Statement statement= connectDB.createStatement();
            statement.execute(delete);

        }catch (Error error){
            System.out.println("Selectati");
        }
    }

    public ArrayList<String> getListOfSongs(ArrayList<String> file){
        Connection connector= getConnection();
        ArrayList<String> name =new ArrayList<>();
        try {
            for(int i=-0;i<file.size();i++){
                String segment="select music_name From musiclist WHERE  music_file=\""+file.get(i)+"\";";
                Statement statement = connector.createStatement();
                ResultSet queryResut = statement.executeQuery(segment);

                while (queryResut.next()){
                    name.add(queryResut.getString(1));
                }

            }
        }catch (Exception e){
            System.out.println("No no");
        }
        return name;
    }
    public String getSongFile(String song){
        Connection connection= getConnection();
        String segment="select music_file From musiclist WHERE  music_name=\""+song+"\";";
        try {
            Statement statement = connection.createStatement();
            ResultSet queryResut = statement.executeQuery(segment);

            while (queryResut.next()){
                return queryResut.getString(1);
            }

        }catch (Exception e){
            System.out.println("Not good");
        }
        return "nu a facut nimic";

    }

}
