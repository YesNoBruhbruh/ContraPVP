package me.maanraj514.contrapvp.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TableBuilder {

    private final StringBuilder builder;
    private boolean primaryKey = false;

    public TableBuilder(String name) {
        builder = new StringBuilder();
        builder.append("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
    }

    public TableBuilder addField(String name, DataType dataType, int length) {
        builder.append(name).append(" ").append(dataType.name()).append("(").append(length).append("),");
        return this;
    }

    public TableBuilder setPrimaryKey(String name) {
        builder.append("PRIMARY KEY (").append(name).append("))");
        primaryKey = true;
        return this;
    }

    public void execute(Connection connection){
        try (PreparedStatement stmt = connection.prepareStatement(getCommand())) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getCommand() {
        if(!primaryKey) {
            throw new NullPointerException("No Primary key specified");
        }
        return builder.toString();
    }

    public enum DataType {

        TINYINT,
        SMALLINT,
        MEDIUMINT,
        INT,
        BIGINT,

        DECIMAL,
        FLOAT,
        DOUBLE,
        REAL,

        BIT,
        BOOLEAN,
        SERIAL,

        DATE,
        DATETIME,
        TIMESTAMP,
        TIME,
        YEAR,

        CHAR,
        VARCHAR,

        TINYTEXT,
        TEXT,
        MEDIUMTEXT,
        LONGTEXT,

        BINARY,
        VARBINARY,

        TINYBLOB,
        BLOB,
        MEDIUMBLOB,
        LONGBLOB,

        ENUM,
        SET,

        GEOMETRY,
        POINT,
        LINESTRING,
        POLYGON,
        MULTIPOINT,
        MULTILINESTRING,
        MULTIPOLYGON,
        GEOMETRYCOLLECTION,

        JSON

    }
}
