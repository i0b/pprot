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
		properties = Configuration.getInstance().getProperties();
		
		if (properties.getProperty("grid.source.refresh.interval.min") != null)
			REFRESH_IN_MIN = Integer.valueOf(properties.getProperty("grid.source.refresh.interval.min"));
		
		if (properties.getProperty("grid.source.refresh.command") != null)
			COMMAND = properties.getProperty("grid.source.refresh.command");
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
