package tw.edu.pu.s11004304.fd_project2.ui.tourist;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import tw.edu.pu.s11004304.fd_project2.databinding.FragmentTouristBinding;



public class TouristFragment extends Fragment {

private FragmentTouristBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        TouristViewModel touristViewModel =
                new ViewModelProvider(this).get(TouristViewModel.class);

    binding = FragmentTouristBinding.inflate(inflater, container, false);
    View root = binding.getRoot();

        final TextView textView = binding.textTourist;
        touristViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}