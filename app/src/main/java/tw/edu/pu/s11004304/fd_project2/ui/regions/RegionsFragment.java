package tw.edu.pu.s11004304.fd_project2.ui.regions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import tw.edu.pu.s11004304.fd_project2.databinding.FragmentRegionsBinding;

public class RegionsFragment extends Fragment {

private FragmentRegionsBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {
        RegionsViewModel regionsViewModel =
                new ViewModelProvider(this).get(RegionsViewModel.class);

    binding = FragmentRegionsBinding.inflate(inflater, container, false);
    View root = binding.getRoot();
        final TextView textView = binding.textRegions;
        regionsViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

@Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}