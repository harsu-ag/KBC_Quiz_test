package helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.harsu.developer.kbcquiztest.QuestionSet;


/**
 * Created by harsu on 6/24/2015.
 */
public class TableManager {
    public static final String KEY_ID = "ID";

    public static final String KEY_QUESTION = "Question";
    public static final String KEY_OPTION_1 = "Option1";
    public static final String KEY_OPTION_2 = "Option2";
    public static final String KEY_OPTION_3 = "Option3";
    public static final String KEY_OPTION_4 = "Option4";
    public static final String KEY_ANSWER = "answer";       //we can have just 1, 2, 3 or 4 stored in this column
    public static final String KEY_ANSWERED = "answerByUser";       //we can have just 1, 2, 3 or 4 stored in this column
    public static final String KEY_SELECTED = "selected";       //1 represents already asked this question, 0 represents not asked this question

    public static final String TAG = "TableManager";

    private static final String DATABASE_TABLE = "FeedTable";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "FeedDatabase";
    private Context context;
    private DBHelper ourHelper;
    private SQLiteDatabase ourDatabase;

    public TableManager(Context c) {
        context = c;

    }

    public TableManager open() {
        ourHelper = new DBHelper(context);
        ourDatabase = ourHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        ourHelper.close();
        ourDatabase.close();
    }

    public long addEntry(
            String question,
            String option1,
            String option2,
            String option3,
            String option4, int answer) {
        long success = -1;

        ContentValues cv = new ContentValues();

        cv.put(KEY_QUESTION, question);
        cv.put(KEY_OPTION_1, option1);
        cv.put(KEY_OPTION_2, option2);
        cv.put(KEY_OPTION_3, option3);
        cv.put(KEY_OPTION_4, option4);
        cv.put(KEY_ANSWER,answer);

        int presence = checkData(cv);

        if (presence == -1) {
            open();
            success = ourDatabase.insert(DATABASE_TABLE, null, cv);
            close();
        }
        return success;
    }

    public int checkData(ContentValues cv) {
        open();
        int id = -1;
        Cursor cursor = ourDatabase.rawQuery("SELECT " + KEY_ID + " FROM " + DATABASE_TABLE +
                        " WHERE " + KEY_QUESTION + " = '" + cv.getAsString(KEY_QUESTION)+"'",
                null);
        if (cursor.moveToFirst())
            id = cursor.getInt(0);
        cursor.close();
        close();
        return id;
    }

    public void markAllUnSelected() {
        open();
        ContentValues cv=new ContentValues();
        cv.put(KEY_SELECTED,0);
        ourDatabase.update(DATABASE_TABLE, cv, null, null);
        close();
    }

    public boolean alreadySelected(int randomNum) {
        open();
        boolean result=false;
        Cursor cursor=ourDatabase.rawQuery("SELECT "+KEY_SELECTED+" FROM "+DATABASE_TABLE+" WHERE "+KEY_ID+"="+randomNum,null);
        if(cursor.moveToFirst()){
            result=cursor.getInt(0)==0?false:true;
        }
        cursor.close();
        close();
        return result;
    }

    public QuestionSet getQuestionAt(int id) {
        open();

        QuestionSet questionSet=null;
        Cursor cursor=ourDatabase.rawQuery("SELECT * FROM "+DATABASE_TABLE+" WHERE "+KEY_ID+"="+id,null);
        if(cursor.moveToFirst()){
            questionSet=new QuestionSet(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
        }
        cursor.close();
        close();
        return questionSet;

    }

    public void markAnswer(int markedAnswer,int id) {
        open();
        ContentValues cv=new ContentValues();
        cv.put(KEY_ANSWERED,markedAnswer);
        cv.put(KEY_SELECTED,1);
        ourDatabase.update(DATABASE_TABLE, cv, KEY_ID + "=" + id, null);
        close();
    }

    public int getAnswer(int i) {

        open();
        int answer=0;
        Cursor cursor=ourDatabase.rawQuery("SELECT "+ KEY_ANSWER+" FROM "+DATABASE_TABLE+" WHERE "+KEY_ID+"="+i,null);
        if(cursor.moveToFirst()){
            answer=cursor.getInt(0);
        }
        cursor.close();
        close();
        return answer;
    }

    public int getAnswered(int i) {

        open();
        int answered=0;
        Cursor cursor=ourDatabase.rawQuery("SELECT "+ KEY_ANSWERED+" FROM "+DATABASE_TABLE+" WHERE "+KEY_ID+"="+i,null);
        if(cursor.moveToFirst()){
            answered=cursor.getInt(0);
        }
        cursor.close();
        close();
        return answered;
    }

    private static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            String query = "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +       //this will auto increment from 1
                    KEY_QUESTION + " TEXT NOT NULL, " +
                    KEY_OPTION_1 + " TEXT NOT NULL, " +
                    KEY_OPTION_2 + " TEXT NOT NULL, " +
                    KEY_OPTION_3 + " TEXT NOT NULL, " +
                    KEY_OPTION_4 + " TEXT NOT NULL, " +
                    KEY_ANSWER + " INTEGER, " +         //I have not put it as null, as the values stored in these columns will initially be null ie. when just questions are added.
                    KEY_ANSWERED + " INTEGER, " +
                    KEY_SELECTED + " INTEGER);";


            db.execSQL(query);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }
    }


}