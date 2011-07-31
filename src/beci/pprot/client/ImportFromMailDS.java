package beci.pprot.client;

import java.util.Date;

import beci.pprot.shared.MailData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ImportFromMailDS extends DataSource {
	private static MailRecord[] records = null;
	private static MailAccessServiceAsync mailDataSvc = GWT.create(MailAccessService.class);
	
	
	public static void setRecords(ListGrid listGrid) {  
        if (records == null)
            getNewRecords(listGrid);
        else
        	listGrid.setData(records);
    }  
  
    public static void getNewRecords(final ListGrid listGrid) {
    	if (mailDataSvc == null)
    		mailDataSvc = GWT.create(MailAccessService.class);
    	
    	AsyncCallback<MailData[]> callback = new AsyncCallback<MailData[]>() {
			public void onFailure(Throwable caught) {
				SC.say("Es konnten keine Daten aus dem E-Mail Postfach entnommen werden.");//TODO useful message
			}
			
			public void onSuccess(MailData[] result) {
				records = new MailRecord[result.length];
				
				for (int i = 0; i < result.length; i++) {
					records[i] = new MailRecord(result[i].getComposed(),result[i].getSender(),
							result[i].getSubject(),result[i].getAttachment());
				}
				
				listGrid.setData(records);
			}
    	};
    	
		mailDataSvc.getMailData(callback);
	}
}

class MailRecord extends ListGridRecord {

	public MailRecord() {
	}

	public MailRecord(Date composed, String sender, String subject, String attachment) {
		setComposed(composed);
		setSender(sender);
		setSubject(subject);
		setAttachment(attachment);
	}

	public void setComposed(Date date) {
		setAttribute("composed", date);
	}

	public String getComposed() {
		return getAttributeAsString("composed");
	}

	public void setSender(String sender) {
		setAttribute("sender", sender);
	}

	public String getSender() {
		return getAttributeAsString("sender");
	}
	
	public void setSubject(String subject) {
		setAttribute("subject", subject);
	}

	public String getSubject() {
		return getAttributeAsString("subject");
	}

	public void setAttachment(String attachment) {
		setAttribute("attachment", !attachment.isEmpty());
	}

	public String getAttachment() {
		return getAttributeAsString("attachment");
	}
}