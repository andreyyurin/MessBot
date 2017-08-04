package hb.messbot;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.BoolRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Switch;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import hb.messbot.fragments.DBHelper;

/**
 * Created by admin on 04.08.2017.
 */

public class MyService extends Service {

    private NotificationManager notificationManager;
    public static final int DEFAULT_NOTIFICATION_ID = 101;
    final String LOG_TAG = "myLogs";

    public static final String APP_PREFERENCES = "switchCHECK";
    public SharedPreferences switchcheck;
    public static final String app_switch = "switch";

    public static  ArrayList<Integer> flagsHave;
    private static RecyclerView listBase;
    public static ArrayList<String> arrayBase;
    public static ArrayList<String> arrayBase2;

    private ArrayList<Sentences> sentences = new ArrayList<Sentences>();
    private static final List<Sentences> sents = new ArrayList<Sentences>();
    private Switch switchOnOff;


    public static ArrayList<Integer> flags = new ArrayList<Integer>();

    private DBHelper mDatabaseHelper;
    MyTask mt;
    public static SQLiteDatabase mSqLiteDatabase;
    static boolean fr = false;
    private static TextView txtWelcome;
    private String txt2;
    private static ArrayList<Boolean> chats;
    public static String txt3;
    private static final int NOTIFY_ID = 101;




    public void onCreate() {
        super.onCreate();

        Log.d("xyi","started");
        switchcheck = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        arrayBase = new ArrayList<String>();
        arrayBase2 = new ArrayList<String>();
        flagsHave = new ArrayList<Integer>();
        chats = new ArrayList<Boolean>();

        mDatabaseHelper = new DBHelper(MyService.this, "showdb.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        SharedPreferences.Editor editor = switchcheck.edit();
        //Log.d(LOG_TAG, "onHandleIntent start " + label);
        Log.d("xyi", "SERVICE");
        MainProg();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Removing any notifications
//        notificationManager.cancel(DEFAULT_NOTIFICATION_ID);

        //Disabling service
        stopSelf();
    }

    void MainProg()
    {
        mt = new MyTask();
        mt.execute();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("xyi","started Service");
        MainProg();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
     //   Log.d("xyi","started Service");
        MainProg();
        return Service.START_STICKY;
    }

    int getMyId() {
        final VKAccessToken vkAccessToken = VKAccessToken.currentToken();
        return vkAccessToken != null ? Integer.parseInt(vkAccessToken.userId) : 0;
    }

    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                VKRequest request = VKApi.messages().getDialogs(VKParameters.from(VKApiConst.COUNT, 20,VKApiConst.UNREAD, true));
                request.executeWithListener(new VKRequest.VKRequestListener() {
                    @Override
                    public void onComplete(final VKResponse response) {
                        super.onComplete(response);
                        ArrayList<String> messes=new ArrayList<String>();
                        ArrayList<Integer> id = new ArrayList<Integer>();

                        VKApiGetDialogResponse vkApiGetMessagesResponse = (VKApiGetDialogResponse)response.parsedModel;
                        String kk = response.responseString;
                        VKList<VKApiDialog> list = vkApiGetMessagesResponse.items;

                        for(VKApiDialog msg : list){
                            messes.add(msg.message.body);
                            id.add(msg.message.user_id);
                        }
                        algthBase(messes, id);
                    }
                });
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mt = new MyTask();
            mt.execute();
        }
    }

    int checkSTR(final String str, int id, final String text)
    {
        if(algthSTR(str, text) == 1) {
            return 1;
        }
        return 0;
    }

    int algthSTR(String str1, String str2)
    {

        if(str1.toLowerCase().replaceAll(" ", "").equals(str2.toLowerCase().replaceAll(" ", "")) || str2.toLowerCase().replaceAll(" ", "").equals(str1.toLowerCase().replaceAll(" ", ""))) {
            return 1;
        }else{
            return 0;
        }
    }

    void algthBase(ArrayList<String> messes, ArrayList<Integer> id)
    {
        Cursor cursor = mSqLiteDatabase.query("data", new String[] {
                        DBHelper.TEXT_COLUMN, DBHelper.ANSWER_COLUMN},
                null, null,
                null, null, null) ;
        while (cursor.moveToNext()) {
            for (int i = 0; i < messes.size(); i++) {
                if (checkSTR(messes.get(i), id.get(i), cursor.getString(cursor.getColumnIndex(DBHelper.TEXT_COLUMN))) == 1 && switchcheck.getString(app_switch, "").equals("on") && Integer.parseInt(switchcheck.getString("user_id", "")) != id.get(i)) {

                    Log.d(getMyId() + "", id.get(i) + "");
                    VKRequest request = new VKRequest("messages.send", VKParameters.from(VKApiConst.USER_ID,
                            id.get(i), VKApiConst.MESSAGE, cursor.getString(cursor.getColumnIndex(DBHelper.ANSWER_COLUMN))));
                    request.executeWithListener(new VKRequest.VKRequestListener() {
                        @Override
                        public void onComplete(VKResponse response) {
                            super.onComplete(response);
                            Log.d("ANSWER", "УСПЕХ");
                        }
                    });
                }

            }
        }
    }

}
