package net.hockeyapp.android;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.test.ActivityInstrumentationTestCase2;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.FilenameFilter;

@RunWith(AndroidJUnit4.class)
public class ExceptionHandlerTest extends ActivityInstrumentationTestCase2<UpdateActivity> {

    private File filesDirectory;

    public ExceptionHandlerTest() {
        super(UpdateActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();

        injectInstrumentation(InstrumentationRegistry.getInstrumentation());

        if (Constants.FILES_PATH == null) {
            Constants.loadFromContext(getActivity());
        }

        filesDirectory = new File(Constants.FILES_PATH);
        File[] stacktraceFiles = filesDirectory.listFiles(new StacktraceFilenameFilter());
        for (File f : stacktraceFiles) {
            f.delete();
        }
    }

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @Test
    public void saveExceptionTest() {

        Throwable tr = new RuntimeException("Just a test exception");

        ExceptionHandler.saveException(tr, null, null);

        File[] files = filesDirectory.listFiles(new StacktraceFilenameFilter());
        assertEquals(1, files.length);
    }

    @SuppressWarnings("ThrowableInstanceNeverThrown")
    @Test
    public void saveExceptionCustomListenerTest() {

        Throwable tr = new RuntimeException("Just a test exception");

        ExceptionHandler.saveException(tr, null, new CrashManagerListener() {
        });

        File[] files = filesDirectory.listFiles(new StacktraceFilenameFilter());
        assertEquals(1, files.length);
    }

    static class StacktraceFilenameFilter implements FilenameFilter {

        @Override
        public boolean accept(File dir, String filename) {
            return filename.endsWith(".stacktrace");
        }
    }

}
