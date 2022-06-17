package tw.edu.pu.s11004304.fd_project2.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("台南是最受歡迎的旅遊城市之一，除了可以尋訪老城獨有的魅力，海線風景也十分迷人。");
    }

    public LiveData<String> getText() {
        return mText;
    }
}