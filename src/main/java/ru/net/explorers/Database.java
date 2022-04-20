package ru.net.explorers;

import java.sql.*;

//+ fixed
// todo make these functions compatible with SQLITE and MSSQL, emulation of json

/**
 * Database connector class. Provides connection, statement and other from JDBC
 */
public class Database {
    static ConsoleWrapper cw = new ConsoleWrapper();
    // sql shit
    public Connection con = null;
    public Statement statement = null;
    public ResultSet resultset = null;
    public String databaseServerType = null;
    public String sql_url;
    public String sql_user;
    public String sql_password;

    /**
     * The type of databse.
     * can be MYSQL,MSSQL,POSTGRESQL,SQLITE.
     * now works with MYSQL only (which is default yet)
     * 
     */
    public String dbType;

    // constructor
    public Database(String url, String username, String password) {
        this.sql_url = url;
        this.sql_user = username;
        this.sql_password = password;
    }

    void connect() {
        // This function opens sql db for whole class
        try {
            // opening database connection to sql server
            this.con = DriverManager.getConnection(sql_url, sql_user, sql_password);

            // getting Statement object to execute query
            this.statement = con.createStatement();

        } catch (SQLException sqlEx) {
            sqlEx.printStackTrace();
            // alarm("Something wrong with database connection. Check stack trace, config
            // and other shit");
        }
    }

    /**
     * /unused/
     * Method that adds row to the end of table
     * 
     * @param table   name of table, you want to add row
     * @param columns columns, you want to fill in these row. Example:
     *                name,locationX,locationY,locationZ,location_world,egg_group
     * @param values  values, you want to add in matching column. Example:
     *                'jar of vodka',1917,100,-1991,'overworld','Communists memes'
     */
    void addRow(String table, String columns, String values) {
        String sql = "INSERT INTO " + table + "(" + columns + ")VALUES(" + values + ");";
        cw.verboseLog(sql);
        try {
            this.statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    void disconnect() {
        // This disconnects the
        try {
            con.close();
            statement.close();
        } catch (SQLException se) {
            // alarm("Database not closed, but should. Summon soul of communism to solve
            // this");
        }

        // this.resultset = null;
        // this.statement = null;
        this.con = null;

    }

}