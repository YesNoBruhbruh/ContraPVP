package me.maanraj514.contrapvp.database;

import java.sql.Connection;

public interface IConnectedCallback {

    void onConnected(Connection connection);
    void onDisconnect();
}
