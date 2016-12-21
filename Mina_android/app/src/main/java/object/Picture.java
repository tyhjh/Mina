package object;

/**
 * Created by Tyhj on 2016/10/17.
 */

public class Picture {
    String path_origin;
    String path_little;

    int checkable;



    public int isCheckable() {
        return checkable;
    }

    public void setCheckable(int checkable) {
        this.checkable = checkable;
    }

    public Picture(String path_origin, String path_little) {
        this.path_origin = path_origin;
        this.path_little = path_little;
    }

    public String getPath_origin() {
        return path_origin;
    }

    public void setPath_origin(String path_origin) {
        this.path_origin = path_origin;
    }

    public String getPath_little() {
        return path_little;
    }

    public void setPath_little(String path_little) {
        this.path_little = path_little;
    }

    public int getCheckable() {
        return checkable;
    }
}
