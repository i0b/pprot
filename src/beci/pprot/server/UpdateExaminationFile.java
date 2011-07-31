package beci.pprot.server;

import static java.util.concurrent.TimeUnit.MINUTES;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

// TODO: Logging in files, creating the script :)

/**
 * This class will update the examination data.
 * It's done by a command which is called repeatedly.
 * Both the interval and the command are specified in a configuration file.
 *
 */
public class UpdateExaminationFile {
	private static int REFRESH_IN_MIN = 10;
	private static String COMMAND = new String();
	
	private Properties properties;
	// This lies in PROJECT/war/configuration.xml
	final static String CONFIGFILE = "configuration.xml";

	
	private final ScheduledExecutorService scheduler = Executors
			.newScheduledThreadPool(1);

	public UpdateExaminationFile() {
		try {
			loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.timedRefresh();
	}

	private void loadProperties() throws IOException {
		properties = new Properties();
		FileInputStream fis;
		fis = new FileInputStream(CONFIGFILE);
		properties.loadFromXML(fis);
		
		if (properties.getProperty("refreshInMin") != null)
			REFRESH_IN_MIN = Integer.valueOf(properties.getProperty("refreshInMin"));
		
		if (properties.getProperty("refreshCommand") != null)
			COMMAND = properties.getProperty("refreshCommand");
	}
	
	private void saveProperties() throws IOException {
		FileOutputStream fos;
		fos = new FileOutputStream(CONFIGFILE);
		properties.storeToXML(fos, "ExamnData Configuration", "UTF-8");
	}

	public void refreshExaminationFile() {
		System.out.print(
			getDateTime() + " update [" + COMMAND + "] - " );
		/*try {
			Process process = Runtime.getRuntime().exec(COMMAND);
			int exitValue = process.waitFor();
			System.out.println("returned: " + exitValue);
		} catch (IOException e) {
			e.printStackTrace(); TODO print error code
		}*/
	}

	private void timedRefresh() {
		final Runnable refresher = new Runnable() {
			public void run() {
				refreshExaminationFile();
			}
		};
		// final ScheduledFuture<?> refreshHandle =
		scheduler.scheduleAtFixedRate(refresher, 0, REFRESH_IN_MIN, MINUTES);
		/*
		 * Single timed task: scheduler.schedule(new Runnable() { public void
		 * run() { refreshHandle.cancel(true); } }, 60 * 60, SECONDS);
		 */
	}

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
