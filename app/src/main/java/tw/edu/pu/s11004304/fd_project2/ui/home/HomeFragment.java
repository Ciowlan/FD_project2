package tw.edu.pu.s11004304.fd_project2.ui.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

import tw.edu.pu.s11004304.fd_project2.MainActivity;
import tw.edu.pu.s11004304.fd_project2.R;
import tw.edu.pu.s11004304.fd_project2.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    ViewPager vp_pager;
    int prePosition;
    LinearLayout ll_container;
    ArrayList<ImageView> al;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        vp_pager = root.findViewById(R.id.vp_pager);
        ll_container = root.findViewById(R.id.ll_container);
        initData();
        PollThread pThread = new PollThread();
        pThread.start();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    class PollThread extends Thread{
        @Override
        public void run() {
            boolean poll = true;
            while (poll){

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        vp_pager.setCurrentItem(vp_pager.getCurrentItem()+1);
                    }
                });

            }
        }
    }
Thread mUiThread;
    Handler mHandler = new Handler();
    public final void runOnUiThread(Runnable action) {
        if (Thread.currentThread() != mUiThread) {
            mHandler.post(action);
        } else {
            action.run();
        }
    }

    private void initData() {

        int[] i = new int[]{R.drawable.eight, R.drawable.mysta, R.drawable.nine, R.drawable.mysta};
        al = new ArrayList<ImageView>();
        for (int x = 0; x < i.length; x++) {
            ImageView iv = new ImageView(getContext());
            iv.setBackgroundResource(i[x]);
            al.add(iv);
            View v = new View(getContext());
            v.setBackgroundResource(R.drawable.point_normal);
            //有多少張圖就放置幾個點
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(15, 15);
            layoutParams.leftMargin = 30;
            ll_container.addView(v, layoutParams);
        }
        vp_pager.setAdapter(new Myadapter());
        vp_pager.setOnPageChangeListener(new MyPageListener());
        vp_pager.setCurrentItem(al.size() * 1000);  //這個是無線輪詢的關鍵
        ll_container.getChildAt(0).setBackgroundResource(R.drawable.point_select);
        prePosition = 0;
    }


    class Myadapter extends PagerAdapter {
        @Override
        public int getCount() {
            return Integer.MAX_VALUE; // 要無限輪播
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int position1 = position % al.size();
            ImageView imageView = al.get(position1);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    class MyPageListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            int newPosition = position % al.size();
            ll_container.getChildAt(newPosition).setBackgroundResource(R.drawable.point_select);
            ll_container.getChildAt(prePosition).setBackgroundResource(R.drawable.point_normal);
            prePosition=newPosition;
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}