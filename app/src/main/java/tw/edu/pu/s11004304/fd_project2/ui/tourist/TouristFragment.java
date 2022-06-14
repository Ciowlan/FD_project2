package tw.edu.pu.s11004304.fd_project2.ui.tourist;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import tw.edu.pu.s11004304.fd_project2.R;
import tw.edu.pu.s11004304.fd_project2.databinding.FragmentTouristBinding;


public class TouristFragment extends Fragment {

private FragmentTouristBinding binding;
    RecyclerView rv;
    ArrayList<SiteName> siteNames;
    NetAdapter adapter;
    Message msg;
    Bundle bundle;
    private ConstraintLayout mainLayout;
    Handler handler = new Handler(Looper.getMainLooper());
    LinearLayout layout;
    ProgressBar pBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        TouristViewModel touristViewModel =
                new ViewModelProvider(this).get(TouristViewModel.class);

    binding = FragmentTouristBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textTourist;
        touristViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        rv = root.findViewById(R.id.rv);
        pBar = root.findViewById(R.id.p_bar);
        pBar.setVisibility(View.VISIBLE);
        mainLayout = root.findViewById(R.id.touristLayout);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        // 設置格線
        rv.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        GetWebInfo dTask = new GetWebInfo();
        siteNames = new ArrayList<>();
        dTask.execute("https://www.twtainan.net/data/shops_zh-tw.json");
        return root;
    }

    class GetWebInfo extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            pBar.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            pBar.setForegroundGravity(Gravity.CENTER);
            pBar.setBackgroundColor(Color.GREEN);
            pBar.setAlpha(0.7f);

            //
            pBar.setVisibility(View.VISIBLE);
            //
            Toast.makeText(getContext(), "開始下載 ...", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(String... strings) {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(10);
                    pBar.setProgress(i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            String result = getData(strings[0]);
            return result;
        }

        @Override
        protected void onPostExecute(String msg) {
            pBar.setVisibility(View.GONE);
            mainLayout.removeView(pBar);
            super.onPostExecute(msg);
            try {
                JSONArray array = new JSONArray(msg);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject site = array.getJSONObject(i);
                    //2-2
                    String name = site.getString("name");
                    String address = site.getString("address");
                    String district = site.getString("district");
                    String tel = site.getString("tel");
                    String open_time = site.getString("open_time");
                    String introduction = site.getString("introduction");

                    SiteName siteName = new SiteName(name, address, district, tel, open_time, introduction);
                    //
                    siteNames.add(siteName);


                }
            } catch (JSONException e) {
                Log.v("tag", e.toString());
                e.printStackTrace();
            }
            if (siteNames.size() != 0) {
                adapter = new NetAdapter();
                adapter.setmData(siteNames);
                rv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pBar.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

    }


    public String getData(String strUrl){
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(strUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream in = conn.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            String str;
            while((str = br.readLine())!=null){
                sb.append(str);
            }

            JSONArray array = new JSONArray(sb.toString());

        } catch (Exception e) {
            Log.v("tag",e.toString());
            e.printStackTrace();
        }finally {
        }
        return sb.toString();
    }

}



class NetAdapter extends RecyclerView.Adapter <NetAdapter.VH>{

    private List<SiteName> mData;


    public  NetAdapter(){

    }

    public void setmData(List<SiteName> data){
        mData = data;
        Log.v("NetAdapter",mData.size()+"");
    }

    @NonNull
    @Override
    public NetAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.myitem2, parent, false);
        return new NetAdapter.VH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NetAdapter.VH holder, int position) {
        holder.tvName.setText(mData.get(position).getName());
        holder.tvAddress.setText(mData.get(position).getAddress());
        holder.tvTel.setText(mData.get(position).getTel());


    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class VH extends RecyclerView.ViewHolder{
        TextView tvName,tvAddress,tvDistrict,tvTel,tvOpen_time,tvIntroduction;
        public VH(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAddress = itemView.findViewById(R.id.tvAddress);
            tvTel = itemView.findViewById(R.id.tvTel);

        }
    }


}

class SiteName implements Serializable {
    public String name;
    public String address;
    public String district;
    public String tel;
    public String open_time;
    public String introduction;

    public SiteName(String name, String address, String district, String tel, String open_time, String introduction) {
        this.name = name;
        this.address = address;
        this.district = district;
        this.tel = tel;
        this.open_time = open_time;
        this.introduction = introduction;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getDistrict() {
        return district;
    }

    public String getTel() {
        return tel;
    }

    public String getOpen_time() {
        return open_time;
    }

    public String getIntroduction() {
        return introduction;
    }

    @Override
    public String toString() {
        return "SiteName{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", district='" + district + '\'' +
                ", tel='" + tel + '\'' +
                ", open_time='" + open_time + '\'' +
                ", introduction='" + introduction + '\'' +
                '}';
    }
}