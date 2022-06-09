package tw.edu.pu.s11004304.fd_project2.ui.tourist;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TouristViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TouristViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is tourist fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}