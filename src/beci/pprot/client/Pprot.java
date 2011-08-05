package beci.pprot.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.core.KeyIdentifier;
import com.smartgwt.client.util.KeyCallback;
import com.smartgwt.client.util.Page;
import com.smartgwt.client.util.SC;

public class Pprot implements EntryPoint {

	public void onModuleLoad() {
		KeyIdentifier debugKey = new KeyIdentifier();
		debugKey.setCtrlKey(true);
		debugKey.setKeyName("Y");

		Page.registerKey(debugKey, new KeyCallback() {
			public void execute(String keyName) {
				SC.showConsole();
			}
		});

		//UIExaminationGrid.getInstance().getMainLayout().draw();
		UIImportFromMail.getInstance().getMainLayout().draw();
	}
}