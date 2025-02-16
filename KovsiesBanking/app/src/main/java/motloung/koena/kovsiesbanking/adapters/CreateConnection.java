package motloung.koena.kovsiesbanking.adapters;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.StrictMode;

import androidx.core.app.ActivityCompat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CreateConnection
{
    private Activity context;
    private static final String
            ip_address = "",
            port = "", Classes = "",
            database = "", username = "",
             password = "",
            url = "" + database;
    private static Connection connection = null;
    public CreateConnection(Activity _context){
        context = _context;
        ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.INTERNET,
                        Manifest.permission.ACCESS_NETWORK_STATE},
                PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public Connection getConnection()
    {
        try {
            Class.forName(Classes);
            connection = DriverManager.getConnection(url, username, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public ResultSet getData(String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSet;
    }
    public boolean insertData(String insertQuery)
    {
        try
        {
            Connection conn = getConnection();
            if (conn != null)
            {
                PreparedStatement preparedStatement = conn.prepareStatement(insertQuery);
                int rowsAffected = preparedStatement.executeUpdate();
                conn.close();
                return rowsAffected > 0;
            }
        } catch (SQLException e)
        {
            throw new RuntimeException(e);
        }
        return false;
    }
}
