package np.info.roshan.benionlinecomnp.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class SQLiteHandler extends SQLiteOpenHelper {

    public static final int version =8;
    public static final String name = "news_table";

    public SQLiteHandler(Context context) {
        super(context, name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE all_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE national_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE myagdi_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE parbat_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE baglung_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE mustang_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE foreign_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE sport_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE eco_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE misc_news(id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
        db.execSQL("CREATE TABLE fav_news(news_id INT, title TEXT, date TEXT, image TEXT, content TEXT, category TEXT, author TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        db.execSQL("DROP TABLE IF EXISTS fav_news");
        onCreate(db);

    }
}
