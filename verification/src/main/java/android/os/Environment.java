package android.os;
import java.io.File;
/** JVM-only test adapter; not included in the Android app. */
public final class Environment {
    public static final String DIRECTORY_DOWNLOADS = "Downloads";
    public static File getExternalStoragePublicDirectory(String type) {
        return new File(System.getProperty("pcbcalc.test.downloads"));
    }
}
