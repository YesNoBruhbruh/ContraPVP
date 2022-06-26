package me.maanraj514.contrapvp.database;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    @Getter
    private Connection connection;
    private final IConnectedCallback callback;

    public DatabaseConnection(String host, int port, String database, String username, String password, boolean useSSL, IConnectedCallback callback) {
        this.callback = callback;

        try {
            this.connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?useSSL=" + useSSL, username, password);

            if(isConnected()){
                callback.onConnected(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void disconnect(){
        if(isConnected()){
            try {
                this.connection.close();
                this.connection = null;
                this.callback.onDisconnect();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public boolean isConnected(){
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}
