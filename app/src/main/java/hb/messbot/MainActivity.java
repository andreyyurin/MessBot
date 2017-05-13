package hb.messbot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.vk.sdk.*;
import com.vk.sdk.api.VKError;

public class MainActivity extends Activity {

    private ListView listFriends;
    private Button btn_login;
    private ImageView imgLogo;
    private String[] scope = new String[]{VKScope.MESSAGES, VKScope.FRIENDS, VKScope.WALL};
    private static final String VK_APP_ID = "5641146";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgLogo = (ImageView) findViewById(R.id.imglogo);
        imgLogo.setImageResource(R.drawable.bg);
        Display display = getWindowManager().getDefaultDisplay();


        btn_login = (Button) findViewById(R.id.log_btn);

        final Intent intent = new Intent(MainActivity.this, AllAct.class);

        if(VKSdk.isLoggedIn())
        {
            startActivity(new Intent(MainActivity.this, AllAct.class));
        }
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VKSdk.login(MainActivity.this, scope);
            }
        });
       // String[] fingerprints = VKUtil.getCertificateFingerprint(this, this.getPackageName());
      /*  for(int i = 0;i<fingerprints.length; i++) {
            Log.d("finger",  fingerprints[i]);
        }*/
        //Получаем токен

        //Инициализируем

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                // Пользователь успешно авторизовался
                //Toast.makeText(getApplicationContext(), "Good", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, AllAct.class));
                finish();
            }
            @Override
            public void onError(VKError error) {
                // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
               //Toast.makeText(getApplicationContext(), "Bad", Toast.LENGTH_LONG).show();
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(VKSdk.isLoggedIn())
        {
            finish();
        }
    }

}