package com.example.contest.Utils.noUse;
// no use
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class DataBase extends SQLiteOpenHelper {
    public  static String CERATE_Location="create table location("
            +"id integer primary key autoincrement,"
            +"time integer,"
            +"longtitude real,"
            +"latitude real)";
    private Context mContext;
    public DataBase(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CERATE_Location);
       Log.e("db","Create succeeded");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists location");
        onCreate(db);

    }
}

