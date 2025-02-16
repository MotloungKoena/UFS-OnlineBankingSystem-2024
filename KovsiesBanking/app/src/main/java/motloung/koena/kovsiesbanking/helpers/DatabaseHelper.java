package motloung.koena.kovsiesbanking.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper
{
    private Context mContext;
    private String sDBName;
    private String sDBPath;
    private String sFilePath;
    private SQLiteDatabase mDatabase;

    public DatabaseHelper(Context _mContext, String sDBName, int iVersion) {
        super(_mContext, sDBName, null, iVersion);
        this.mContext = _mContext;
        this.sDBName = sDBName;

        this.sDBPath = "/data/data" + "motloung.koena.kovsiesbanking" + "Databases";
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
    public void checkDb(){
        SQLiteDatabase checkDB =null;
        sFilePath = sDBPath +sDBName;
        try
        {
            checkDB = SQLiteDatabase.openDatabase(sFilePath,null,0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(checkDB !=null)
        {
            Toast.makeText(mContext, "Database already exists.",Toast.LENGTH_SHORT).show();
        }else
        {copyDatabase();}

    }

    public void copyDatabase(){
        this.getReadableDatabase();

        try
        {
            InputStream inputStream = mContext.getAssets().open("sDBName");
            OutputStream outputStream = new FileOutputStream(sDBPath+sDBName);
            byte[] buffer = new byte[1024];
            int iLength;

            while ((iLength = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, iLength);
            }
            outputStream.flush();
            inputStream.close();
            outputStream.close();
        }
        catch (Exception e){e.printStackTrace();}

    }
    public void openDatabase(){
        SQLiteDatabase.openDatabase(sFilePath,null,0);
    }

    public boolean depositMoney(String accountNumber, double amount) {
        // Check if the account exists
        Cursor cursor = mDatabase.rawQuery("SELECT balance FROM accounts WHERE account_number = ?", new String[]{accountNumber});

        if (cursor != null && cursor.moveToFirst()) {
            double currentBalance = cursor.getDouble(0);
            cursor.close();

            // Update the account's balance
            double newBalance = currentBalance + amount;
            ContentValues values = new ContentValues();
            values.put("balance", newBalance);
            mDatabase.update("accounts", values, "account_number = ?", new String[]{accountNumber});

            return true;  // Deposit successful
        }

        if (cursor != null) {
            cursor.close();
        }
        return false;  // Account not found
    }

    public boolean transferMoney(String fromAccount, String toAccount, double amount) {
        Cursor fromCursor = mDatabase.rawQuery("SELECT balance FROM accounts WHERE account_number = ?", new String[]{fromAccount});
        Cursor toCursor = mDatabase.rawQuery("SELECT balance FROM accounts WHERE account_number = ?", new String[]{toAccount});

        if (fromCursor != null && fromCursor.moveToFirst() && toCursor != null && toCursor.moveToFirst()) {
            double fromBalance = fromCursor.getDouble(0);
            double toBalance = toCursor.getDouble(0);

            if (fromBalance >= amount) {
                double newFromBalance = fromBalance - amount;
                double newToBalance = toBalance + amount;

                ContentValues fromValues = new ContentValues();
                fromValues.put("balance", newFromBalance);
                mDatabase.update("accounts", fromValues, "account_number = ?", new String[]{fromAccount});

                ContentValues toValues = new ContentValues();
                toValues.put("balance", newToBalance);
                mDatabase.update("accounts", toValues, "account_number = ?", new String[]{toAccount});

                return true;  // Transfer successful
            }
        }

        if (fromCursor != null) fromCursor.close();
        if (toCursor != null) toCursor.close();
        return false;  // Transfer failed (account not found or insufficient funds)
    }
}


