package np.info.roshan.benionlinecomnp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by roshan on 12/22/15.
 */

public class SQLiteHandler extends SQLiteOpenHelper {

    public static final int version =5;
    public static final String name = "news_table";

    public SQLiteHandler(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE all_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE national_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE myagdi_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE parbat_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE baglung_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE mustang_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE foreign_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE sport_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE eco_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE misc_news(title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {




    }
}
