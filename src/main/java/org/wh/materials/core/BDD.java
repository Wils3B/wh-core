package org.wh.materials.core;

import java.io.Closeable;
import java.sql.*;

public class BDD implements Closeable, AutoCloseable {
  private Connection connection;
  private String url;
  private String username;
  private String password;
  private Statement statement;
  private boolean actif;

  public BDD(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
    connection = null;
    statement = null;
    actif = false;
  }

  public BDD(String url) {
    this(url, null, null);
  }

  public boolean connect(String className) {
    try {
      Class.forName(className);
      if (username == null)
        connection = DriverManager.getConnection(url);
      else
        connection = DriverManager.getConnection(url, username, password);
      statement = connection.createStatement();
    } catch (ClassNotFoundException e) {
      Log.writeError("Problème rencontré lors du chargement de la classe : " + e.getMessage());
    } catch (SQLException e) {
      Log.writeError("Problème rencontré lors de la creation de la connection à la base de données : " + e.getMessage());
    }
    actif = connection != null && statement != null;
    return actif;
  }

  @Override
  public void close() {
    actif = false;
    if (connection != null) try {
      connection.close();
    } catch (SQLException e) {
      Log.writeError("Erreur lors de la fermeture de la connection : " + e.getMessage());
    }
  }

  public ResultSet executeQuery(String query) {
    ResultSet resultSet = null;
    try {
      Log.writeLog("Process the query : " + query);
      resultSet = statement.executeQuery(query);
    } catch (SQLException e) {
      Log.writeError("Erreur lors de l'envoi du message " + query + " vers la BD : " + e.getMessage());
    }
    return resultSet;
  }

  public int executeUpdate(String query) {
    int n = 0;
    try {
      Log.writeLog("Process the query : " + query);
      n = statement.executeUpdate(query);
    } catch (SQLException e) {
      Log.writeError("Erreur lors de l'envoi du message " + query + " vers la BD : " + e.getMessage());
    }
    return n;
  }

  public boolean isActive() {
    return actif;
  }

  public void inserer(Valuable e, String table) {
    final int update = this.executeUpdate("INSERT INTO " + table + " VALUES " + e.valuesWithNull() + ";");
    Log.writeLog("Modification affectant " + update + " ligne(s)");
  }

    /*/----------------------------------------------------------------------------------------
    public ResultSet querySelect(String[] colonnes,String nomTable){
        String query = "SELECT "+colonnes[0];
        for(int i=1;i<colonnes.length;i++) query+=", "+colonnes[i];
        query+=" FROM "+nomTable+";";
        //Log.writeLog(query);
        return this.executeQuery(query);
    }

    public ResultSet querySelect(String nomTable){
        return this.executeQuery("SELECT * FROM "+nomTable+";");
    }

    public ResultSet querySelect(String condition,String nomTable){
        return this.executeQuery("SELECT * FROM "+nomTable+" WHERE "+condition+";");
    }

    public ResultSet querySelect(String condition, String[] colonnes, String nomTable){
        String query = "SELECT "+colonnes[0];
        for(int i=1;i<colonnes.length;i++) query+=", "+colonnes[i];
        query+=" FROM "+nomTable+" WHERE "+condition+";";
        //Log.writeLog(query);
        return this.executeQuery(query);
    }

    //------------------------------------------------------------------------------------------
    public int queryInsert(String[] contenu,String nomTable){
        String query = "INSERT INTO "+nomTable+"VALUES('"+contenu[0]+"'";
        for(int i=1;i<contenu.length;i++) query+=", '"+contenu[i]+"'";
        query+=");";
        //Log.writeLog(query);
        return this.executeUpdate(query);
    }

    public int queryInsert(Object[] contenu,String nomTable){
        String query = "INSERT INTO "+nomTable+"VALUES('"+contenu[0].toString()+"'";
        for(int i=1;i<contenu.length;i++) query+=", '"+contenu[i].toString()+"'";
        query+=");";
        //Log.writeLog(query);
        return this.executeUpdate(query);
    }

    public int queryInsert(String[] nomColonnes, String[] contenu, String nomTable){
        String query = "INSERT INTO "+nomTable+ " ("+nomColonnes[0];
        int i;
        for(i=1;i<nomColonnes.length;i++) query+=", "+nomColonnes[i];
        query+=") VALUES('"+contenu[0]+"'";
        for(i=1;i<contenu.length;i++) query+=", '"+contenu[i]+"'";
        query+=");";
        //Log.writeLog(query);
        return this.executeUpdate(query);
    }

    public int queryInsert(String[] nomColonnes, Object[] contenu, String nomTable){
        String query = "INSERT INTO "+nomTable+ " ("+nomColonnes[0];
        int i;
        for(i=1;i<nomColonnes.length;i++) query+=", "+nomColonnes[i];
        query+=") VALUES('"+contenu[0]+"'";
        for(i=0;i<contenu.length;i++) query+=", '"+contenu[i]+"'";
        query+=");";
        //Log.writeLog(query);
        return this.executeUpdate(query);
    }

    //-----------------------------------------------------------------------------------------
    public int queryDelete(String condition,String nomTable){
        return this.executeUpdate("DELETE FROM "+nomTable+" WHERE "+condition);
    }

    //-----------------------------------------------------------------------------------------
    public int queryUpdate(String condition, String[] colonnes, String[] contenu, String nomTable){
        String query = "UPDATE "+nomTable+" SET "+colonnes[0]+"='"+contenu[0]+"'";
        for(int i=1;i<colonnes.length;i++) query+=", "+colonnes[i]+"='"+contenu[i]+"'";
        query+=" WHERE "+condition+";";
        //Log.writeLog(query);
        return this.executeUpdate(query);
    }*/
}
