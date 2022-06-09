package tw.edu.pu.s11004304.fd_project2.ui.tourist;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import tw.edu.pu.s11004304.fd_project2.R;
import tw.edu.pu.s11004304.fd_project2.MainActivity;

public class TouristActivity extends MainActivity implements OnClickListener {
    // (1) 宣告屬性
    private ConstraintLayout mainLayout;
    private Button btnStartStop;
    private ListView lvSiteInfo;
    //
    ArrayList<String> mList = new ArrayList<>();
    ArrayList<LatLng> posList = new ArrayList<>();

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_tourist);

        // (2) 產生關聯
        mainLayout = (ConstraintLayout)findViewById(R.id.touristLayout);
        btnStartStop = (Button) findViewById(R.id.btnStartStop);
        // TODO 建立關聯 ListView

        // (3) 註冊事件處理
        btnStartStop.setOnClickListener(this);
        // TODO 設定 ListView 項目點擊事件的傾聽者(處理程式)

    }

    @Override
    public void onClick(View v) {
        // DEMO 展示參考用： 產生非同步工作  AsyncTask
        DemoAsyncTask dTask = new DemoAsyncTask();
        // OpenData 網址
        // http://eclass.hust.edu.tw/filedownload/166024/fdd76dd427c73c2039606c2b6d9b1b3a/台中市景點資料.json
        // ...
        dTask.execute("http://eclass.hust.edu.tw/filedownload/166024/fdd76dd427c73c2039606c2b6d9b1b3a/台中市景點資料.json");
    }

    // ******************************************************************************
    //  DEMO 這是範例：非同步工作 DemoAsyncTask ，展示用
    // ******************************************************************************
    class DemoAsyncTask extends AsyncTask<String, Void, String> {
        ProgressBar pBar;
        //
        @Override
        protected void onPreExecute() {
            // 顯示進度條
            pBar = new ProgressBar(TouristActivity.this, null,
                    android.R.attr.progressBarStyleLarge);
            pBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            pBar.setForegroundGravity(Gravity.CENTER);
            pBar.setBackgroundColor(Color.GREEN);
            pBar.setAlpha( 0.7f);
            //
            mainLayout.addView(pBar);
            pBar.setVisibility(View.VISIBLE);
            //
            Toast.makeText(TouristActivity.this, "開始下載 ...", Toast.LENGTH_SHORT).show();
        }
        @Override
        protected String doInBackground(String... urls) {
            // 背景處理程序 : ex:下載網址
            Log.d("XXXXXX", "doInBackground() 準備下載資料網址：\n" + urls[0] );

            // -------------------------------------------------------------------
            // 模擬網路連線 ; 以下是測試用的程式碼 : 延遲 6 秒
            for (int i=0; i<3; i++) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            //
            // return "demo only!";
            // -------------------------------------------------------------------
            // ?? 你要寫的程式碼?
            String result = "";
            result = downloadOpenData(urls[0]);
            return result;
        }
        //
        ArrayList<HashMap<String, String>> siteNames = new ArrayList<>();
        @Override
        protected void onPostExecute(String result) {
            // 隱藏進度條
            pBar.setVisibility(View.GONE);
            mainLayout.removeView(pBar);
            //
            Log.d("XXXXXX", "onPostExecution() 收到回傳結果：\n"+result);
            //
            // TODO １ - JSON 解析
            try {
                JSONArray allSites = new JSONArray(result);
                //2-1
                //ArrayList<String> siteNames = new ArrayList<String>();
                // siteNames 提升為屬性
                //
                for (int i=0; i<allSites.length(); i++) {
                    JSONObject site = allSites.getJSONObject(i);
                    Log.i("XXXXXX", site.getString("名稱"));
                    //2-2
                    HashMap<String,String> siteInfo = new HashMap<>();
                    siteInfo.put("name",site.getString("名稱"));
                    siteInfo.put("address",site.getString("鄉鎮市區")+site.getString("地址"));
                    siteInfo.put("long",site.getString("東經"));
                    siteInfo.put("lat",site.getString("北緯"));
                    //
                    //siteNames.add(site.getString("名稱"));
                    siteNames.add(siteInfo);
                }
                // TODO ２－ 包裝為 ListView 可用的資料
                ListView lv = findViewById(R.id.lvSiteInfo);
//                ArrayAdapter<String> adapter = new ArrayAdapter<>(
//                        MainActivity.this,
//                        //android.R.layout.simple_list_item_1,
//                        R.layout.myitem,
//                        siteNames
//                );
                SimpleAdapter adapter = new SimpleAdapter(
                        TouristActivity.this,
                        siteNames,
                        R.layout.myitem2,
                        new String[]{"name","address"},
                        new int[]{R.id.tvName,R.id.tvAddress}
                );
                //
                lv.setAdapter(adapter);
                //event Handing
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        HashMap<String, String> siteInfo = siteNames.get(position);
                        Log.i("xxxxxx",siteInfo.get("name"));
                        Toast.makeText(TouristActivity.this, "你選了 : "+siteInfo.get("name"), Toast.LENGTH_SHORT).show();
                    }
                });
                //

            } catch (JSONException e) {
                e.printStackTrace();
            }

            Toast.makeText(TouristActivity.this, "完成下載 ...", Toast.LENGTH_SHORT).show();
            //
        }
    }


    // ******************************************************************************
    // METHOD 這是你可以直接呼叫的方法：downloadOpenData( String strUrl)
    // ******************************************************************************
    // 方法：downloadOpenData( String strUrl)  輸入網址，可以下載該網址所回傳的任何"文字資料"
    // 回傳值：String, 非 null 就是正常回傳的遠端資料; 若為 null 就表示網址或網路連線有問題.
    // 限制：(1) 要放在獨立的執行緒內, 或非同步工作的 doInBackground(...) 內部
    //      (2) 在 AndroidManifest.xml 必須宣告 INTERNET 權限
    // ******************************************************************************
    private String downloadOpenData(String strUrl) {
        try {
            // [1]. 建立 HTTP/GET 連線
            URL url = new URL(strUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            // connection.setRequestProperty("authentication", MainActivity.Authentication);
            connection.setDoInput(true);
            // [2]. 取得輸入串流、HTTP狀態碼
            InputStream inputStream = connection.getInputStream();
            int status = connection.getResponseCode();
            Log.d("XXXXXX", "downloadOpenData():  status Code=" +String.valueOf(status));
            // [3]. 利用串流物件來讀取所有的回傳資料
            String result = "";
            if (inputStream != null) {
                InputStreamReader reader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader in = new BufferedReader(reader);
                // [3.1]. 利用文字緩衝讀取器來持續讀取所有的資料列 (一次讀取一列...直到沒有資料為止)
                String line = "";
                while ((line = in.readLine()) != null) {
                    result += (line + "\n");
                }
            } else {
                Log.d("XXXXXX", "downloadOpenData(): 無法下載資料?!");
                result = null;
            }

            // [4]. 關閉HTTP連線
            connection.disconnect();
            // [5]. 回傳結果
            return result;
            //
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 若過程有任何問題，就回傳 null
        return null;
    }
    // ******************************************************************************
}
