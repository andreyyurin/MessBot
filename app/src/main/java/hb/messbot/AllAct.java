package hb.messbot;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.ObbInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKApiConst;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKList;
import com.vk.sdk.api.model.VKUsersArray;
import com.vk.sdk.util.VKUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import hb.messbot.fragments.DBHelper;

/**
 * Created by Andrey on 24.09.2016.
 */
public class AllAct extends ActionBarActivity{

    public static final String APP_PREFERENCES = "switchCHECK";
    public  SharedPreferences switchcheck;
    public static final String app_switch = "switch";




    private Button btnAdd;
    private ImageButton btnClear;
    private EditText etSentence, etAnswer;

    public static  ArrayList<Integer> flagsHave;
    private static RecyclerView listBase;
    public static ArrayList<String> arrayBase;
    public static ArrayList<String> arrayBase2;


    private ArrayList<Sentences> sentences = new ArrayList<Sentences>();
    private static final List<Sentences> sents = new ArrayList<Sentences>();
    private Switch switchOnOff;
    private TextView txtSwitch;
    private FloatingActionButton fab;
    public static View linear;
    private Toolbar toolbar;
    private static CustAdapter custAdapter;

   // private TextView txtWelcome;

    public static ArrayList<Integer> flags = new ArrayList<Integer>();

    private DBHelper mDatabaseHelper;
    MyTask mt;
    public static SQLiteDatabase mSqLiteDatabase;
    static boolean fr = false;
    private static TextView txtWelcome;
    private String txt2;
    public static ArrayList<Boolean> chats;
    public static String txt3;
    private static final int NOTIFY_ID = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_base);

        //MainProg();

        switchcheck = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);


        btnAdd = (Button) findViewById(R.id.btnAdd);

        initViews();

        txt2 = getResources().getString(R.string.instruction);
        txt3 = txt2;
        arrayBase = new ArrayList<String>();
        arrayBase2 = new ArrayList<String>();
        flagsHave = new ArrayList<Integer>();
        chats = new ArrayList<Boolean>();

        switchOnOff = (Switch) findViewById(R.id.btnSwitch);
        txtSwitch = (TextView) findViewById(R.id.switchtxt);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        linear = (View) findViewById(R.id.linear4);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        etSentence = (EditText) findViewById(R.id.etSentence);
        etAnswer = (EditText) findViewById(R.id.etAnswer);

        txtWelcome = (TextView) findViewById(R.id.txtWelcome);

        mDatabaseHelper = new DBHelper(AllAct.this, "showdb.db", null, 1);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        linear.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
        linear.setVisibility(View.INVISIBLE);

        txtWelcome.setMovementMethod(new ScrollingMovementMethod());

        setSupportActionBar(toolbar);


        SharedPreferences.Editor editor = switchcheck.edit();
        editor.putString("user_id", String.valueOf(getMyId()));
        editor.apply();

        


        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.toolbar_orange)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(linear.getVisibility()==View.INVISIBLE) {

                    linear.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.WRAP_CONTENT));
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        final Animator[] animator = {null};
                        linear.post(new Runnable() {
                            @Override
                            public void run() {
                                int cx = (linear.getLeft() + linear.getRight()) / 2;
                                int cy = (linear.getTop());

                                int dx = Math.max(cx, linear.getWidth() - cx);
                                int dy = Math.max(cy, linear.getHeight() - cy);

                                float finalRadius = (float) Math.hypot(dx, dy);

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    animator[0] = ViewAnimationUtils.createCircularReveal(linear, cx, cy, 0, finalRadius);
                                }

                                animator[0].addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {
                                            super.onAnimationStart(animation);
                                            linear.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    animator[0].setInterpolator(new AccelerateDecelerateInterpolator());
                                    animator[0].setDuration(500);
                                    animator[0].start();


                            }
                        });
                    }else {
                    linear.setVisibility(View.VISIBLE);
                }
                }else{
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        final Animator[] animator = {null};
                        linear.post(new Runnable() {
                            @Override
                            public void run() {
                                int cx = (linear.getLeft() + linear.getRight()) / 2;
                                int cy = (linear.getTop());

                                int dx = Math.max(cx, linear.getWidth() - cx);
                                int dy = Math.max(cy, linear.getHeight() - cy);

                                float finalRadius = (float) Math.hypot(dx, dy);
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                                    animator[0] = ViewAnimationUtils.createCircularReveal(linear, cx, cy, finalRadius, 0);
                                }

                                    animator[0].setInterpolator(new AccelerateDecelerateInterpolator());
                                    animator[0].setDuration(500);
                                    animator[0].start();
                                    animator[0].addListener(new AnimatorListenerAdapter() {
                                        @Override
                                        public void onAnimationEnd(Animator animation) {
                                            super.onAnimationEnd(animation);
                                            linear.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                                            linear.setVisibility(View.INVISIBLE);
                                        }
                                    });


                            }
                        });
                    }else{
                    linear.setLayoutParams(new RelativeLayout.LayoutParams(0, 0));
                    linear.setVisibility(View.INVISIBLE);
                }
                }
            }
        });

        if(switchcheck.getString(app_switch, "").equals("on"))
        {
            switchOnOff.setChecked(true);
            txtSwitch.setText("ON");

            editor = switchcheck.edit();
            editor.putString(app_switch, "on");
            editor.apply();
        }else{
            switchOnOff.setChecked(false);
            txtSwitch.setText("OFF");

            editor = switchcheck.edit();
            editor.putString(app_switch, "off");
            editor.apply();
        }

        MainProg();

        switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {


                    txtSwitch.setText("ON");
                    SharedPreferences.Editor editor = switchcheck.edit();
                    editor.putString(app_switch, "on");
                    editor.apply();

                }else{
                    txtSwitch.setText("OFF");
                    SharedPreferences.Editor editor = switchcheck.edit();
                    editor.putString(app_switch, "off");
                    editor.apply();
                }
            }
        });

        show_base(AllAct.this);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etSentence.getText().toString().isEmpty() && !etAnswer.getText().toString().isEmpty())
                {
                    ContentValues values = new ContentValues();
                    values.put(DBHelper.TEXT_COLUMN, etSentence.getText().toString());
                    values.put(DBHelper.ANSWER_COLUMN, etAnswer.getText().toString());
                    etSentence.setText("");
                    etAnswer.setText("");
                    mSqLiteDatabase.insert("data", null, values);
                    show_base(AllAct.this);
                }else if(etSentence.getText().toString().isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllAct.this);
                    builder.setTitle("Ошибка!")
                            .setMessage("Напишите сообщение!")
                            .setCancelable(false)
                            .setNegativeButton("Ок",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }else if(etAnswer.getText().toString().isEmpty())
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(AllAct.this);
                    builder.setTitle("Ошибка!")
                            .setMessage("Напишите ответ!")
                            .setCancelable(false)
                            .setNegativeButton("Ок",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
     //   Log.d("xyi","started Main conver");
        startService(new Intent(this,MyService.class));
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
        while (cursor.moveToNext())
        {
            for(int i = 0; i<messes.size(); i++) {
                if(checkSTR(messes.get(i), id.get(i), cursor.getString(cursor.getColumnIndex(DBHelper.TEXT_COLUMN)))==1 && switchcheck.getString(app_switch,"").equals("on")&& Integer.parseInt(switchcheck.getString("user_id","")) != id.get(i))
                {

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

        cursor.close();
    }


    private void initViews() {
        listBase = (RecyclerView)
                findViewById(R.id.recycler_view);
        listBase.setHasFixedSize(true);
        listBase
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));// Here 2 is no. of columns to be displayed
    }



    class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                //Log.d("xyi", "getDialogs");
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

        static void show_base(Context ctx)
        {
            listBase.destroyDrawingCache();
            arrayBase.clear();
            arrayBase2.clear();
            flagsHave.clear();
            flags.clear();
            chats.clear();

            Cursor cursor = mSqLiteDatabase.query("data", new String[] {
                            DBHelper.TEXT_COLUMN, DBHelper.ANSWER_COLUMN},
                    null, null,
                    null, null, null) ;


            while (cursor.moveToNext())
            {
                flagsHave.add(0);
                flags.add(0);
                arrayBase.add(cursor.getString(cursor.getColumnIndex(DBHelper.TEXT_COLUMN)));
                arrayBase2.add(cursor.getString(cursor.getColumnIndex(DBHelper.ANSWER_COLUMN)));
                //sentences.add(new Sentences(cursor.getString(cursor.getColumnIndex(DBHelper.TEXT_COLUMN)), cursor.getString(cursor.getColumnIndex(DBHelper.ANSWER_COLUMN))));
            }

            cursor.close();
            //listBase.setAdapter(adapter);
           /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                    R.layout.item_db, arrayBase);*/

            if(arrayBase.size()>0)
            {
                txtWelcome.setVisibility(View.INVISIBLE);
                txtWelcome.setText("");
            }else{
                txtWelcome.setVisibility(View.VISIBLE);
                txtWelcome.setText(ctx.getResources().getString(R.string.instruction));
            }
            fr = true;
            custAdapter = new CustAdapter(ctx, arrayBase, arrayBase2);
            custAdapter.notifyDataSetChanged();

            boolean fall = false;
            if(linear.getVisibility()==View.VISIBLE) {
                fall = true;
            }
            linear.setLayoutParams(new RelativeLayout.LayoutParams(0,0));
            linear.setVisibility(View.INVISIBLE);

            listBase.setAdapter(custAdapter);

            if(fall) {
                linear.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linear.setVisibility(View.VISIBLE);
            }

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    int getMyId() {
        final VKAccessToken vkAccessToken = VKAccessToken.currentToken();
        return vkAccessToken != null ? Integer.parseInt(vkAccessToken.userId) : 0;
    }

    public static void deleteElement(int i)
    {
       // mSqLiteDatabase.delete("data", "text = '"+arrayBase.get(i)+"'", null);
        mSqLiteDatabase.delete("data", "text = '" + arrayBase.get(i) + "' and answer = '"+ arrayBase2.get(i)+"'", null);
        mSqLiteDatabase.delete("data", "answer = '" + arrayBase2.get(i) + "' and text = '"+ arrayBase.get(i)+"'",null);
       /* flags.remove(i);
        arrayBase2.remove(i);
        arrayBase.remove(i);
        flagsHave.remove(i);*/
        // custAdapter.notifyItemChanged(i);
        //UpdateBD(ctx);
    }

    public static void UpdateBD(Context ctx)
    {
        //listBase.setAdapter(new CustAdapter(ctx ,arrayBase, arrayBase2));
     //   (ctx);
       // custAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                mSqLiteDatabase.delete("data", null, null);
                show_base(AllAct.this);
                break;
            case R.id.action_delsel:
                for(int i = 0; i<flags.size(); i++)
                {
                    if(flags.get(i)==1)
                    {
                        mSqLiteDatabase.delete("data", "text = '"+arrayBase.get(i)+"'", null);
                        flags.set(i, -1);

                    }
                }

                for(int i = 0; i<flags.size(); i++) {
                    if(flags.get(i)==-1)
                    {
                        flags.remove(i);
                    }
                }
                show_base(AllAct.this);
                break;
            case R.id.action_exitacc:
                VKSdk.logout();
                startActivity(new Intent(AllAct.this, MainActivity.class));
                finish();
                break;
            case R.id.action_goGroupVk:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://vk.com/ltc_omsk")));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    void MainProg()
    {
        mt = new MyTask();
        mt.execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();


    }

    @Override
    protected void onStop() {
        super.onStop();

    }


}
