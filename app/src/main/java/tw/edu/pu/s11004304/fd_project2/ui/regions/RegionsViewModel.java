package tw.edu.pu.s11004304.fd_project2.ui.regions;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegionsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public RegionsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is regions fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}