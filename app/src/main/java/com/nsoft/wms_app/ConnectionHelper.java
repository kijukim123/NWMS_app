package com.nsoft.wms_app;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionHelper {
    Connection con;
    String uname, pass, ip, port, database;
    @SuppressLint("NewApi")

    public Connection ConnectionClass()
    {
        ip = "192.168.0.202";
        database = "NSTORM_WMS";
        uname = "sa";
        pass = "edu5535@!";
        port = "";

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection connection = null;
        String ConnectionURL = null;

        try{
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            ConnectionURL = "jdbc:jtds:sqlserver://" + ip + ";" + "databasename=" + database + ";user=" + uname + ";password=" + pass + ";";
            connection = DriverManager.getConnection(ConnectionURL);

        }catch (Exception ex){
            Log.e("ERROR", ex.getMessage());
        }

        return connection;
    }

}
