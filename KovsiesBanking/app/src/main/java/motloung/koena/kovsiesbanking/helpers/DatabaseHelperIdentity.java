package motloung.koena.kovsiesbanking.helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

//db for identity
public class DatabaseHelperIdentity extends SQLiteOpenHelper {
    private Context mContext;
    private String sDBName;
    private String sDBPath;
    private String sFilePath;
    private SQLiteDatabase mDatabase;

    public DatabaseHelperIdentity(Context _mContext, String sDBName, int iVersion) {
        super(_mContext, sDBName, null, iVersion);
        this.mContext = _mContext;
        this.sDBName = sDBName;

        this.sDBPath = this.mContext.getDatabasePath(sDBName).getPath();

        //this.sDBPath = "/data/data" + "motloung.koena.kovsiesbanking" + "databases";
    }
    public void openDatabase() {
        this.mDatabase = this.getWritableDatabase();
    }

     public void openDatabasee() {
        SQLiteDatabase.openDatabase(sFilePath, null, 0);}
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void copyDatabasee() throws IOException {
        InputStream input = mContext.getAssets().open("databases/KovsiesBankingIdentityDB.db");
        String outFileName = mContext.getDatabasePath(sDBName).getPath();

        OutputStream output = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = input.read(buffer)) > 0) {
            output.write(buffer, 0, length);
        }

        output.flush();
        output.close();
        input.close();
    }
    public boolean checkDatabase() {
        File dbFile = mContext.getDatabasePath(sDBName);
        return dbFile.exists();
    }
    public void checkDb() {
        SQLiteDatabase checkDB = null;
        sFilePath = sDBPath + sDBName;
        try {
            checkDB = SQLiteDatabase.openDatabase(sFilePath, null, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            Toast.makeText(mContext, "Database already exists.", Toast.LENGTH_SHORT).show();
        } else {
            copyDatabase();
        }

    }

    public void copyDatabase() {
        this.getReadableDatabase();

        try {
            InputStream inputStream = mContext.getAssets().open("databases/KovsiesBankingIdentityDB.db");
            OutputStream outputStream = new FileOutputStream(sDBPath + sDBName);
            byte[] buffer = new byte[1024];
            int iLength;

            while ((iLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, iLength);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public boolean checkUserCredentials(String username, String password) {
        String query = "SELECT * FROM AppUser WHERE StudentEmployeeNumber = ? AND Password = ?";
        Cursor cursor = mDatabase.rawQuery(query, new String[]{username, password});

        // Check if a record was found
        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;  // Credentials are valid
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;  // Credentials are invalid
    }
}
