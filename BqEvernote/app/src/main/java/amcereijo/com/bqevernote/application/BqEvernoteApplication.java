package amcereijo.com.bqevernote.application;

import android.app.Application;

import roboguice.RoboGuice;


/**
 * Created by amcereijo on 07/12/14.
 */
public class BqEvernoteApplication extends Application {
    // to solve a bug with RoboGuice
    static {
        RoboGuice.setUseAnnotationDatabases(false);
    }
}
