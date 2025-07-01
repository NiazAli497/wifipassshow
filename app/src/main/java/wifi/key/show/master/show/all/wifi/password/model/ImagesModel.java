package wifi.key.show.master.show.all.wifi.password.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ImagesModel {
    String filePath,fileName;
    boolean selected;

    public ImagesModel(String filePath, String fileName, boolean selected) {
        this.filePath = filePath;
        this.fileName = fileName;
        this.selected = selected;
    }

    public boolean getSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
