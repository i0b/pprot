package beci.pprot.client;

import java.util.Date;

import beci.pprot.shared.MailData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class ImportFromMailDS extends DataSource {
	private static MailRecord[] records = null;
	private static MailAccessServiceAsync mailDataSvc = GWT
			.create(MailAccessService.class);

	public static void setRecords(ListGrid listGrid) {
		if (records == null)
			getNewRecords(listGrid);
		else
			listGrid.setData(records);
	}
	
	public static void setBodyToLabelByNumber(int messageNumber,
			final Label mailBody) {
		if (mailDataSvc == null)
			mailDataSvc = GWT.create(MailAccessService.class);

		AsyncCallback<String> callback = new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				SC.say("Der Nachrichtentext konnte nicht angezeigt werden.");
			}

			public void onSuccess(String result) {
				mailBody.setContents(result);
			}
		};

		mailDataSvc.getMailBody(messageNumber, callback);
	}

	public static void getNewRecords(final ListGrid listGrid) {
		if (mailDataSvc == null)
			mailDataSvc = GWT.create(MailAccessService.class);

		AsyncCallback<MailData[]> callback = new AsyncCallback<MailData[]>() {
			public void onFailure(Throwable caught) {
				SC
						.say("Es konnten keine Daten aus dem E-Mail Postfach entnommen werden.");// TODO
																									// useful
																									// message
			}

			public void onSuccess(MailData[] result) {
				records = new MailRecord[result.length];

				for (int i = 0; i < result.length; i++) {
					records[i] = new MailRecord(result[i].getMessageNumber(),
							result[i].getComposed(), result[i].getSender(),
							result[i].getSubject(), result[i].getAttachment().size());
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

	public MailRecord(int messageNumber, Date composed, String sender,
			String subject, int numAttachments) {
		setMessageNumber(messageNumber);
		setComposed(composed);
		setSender(sender);
		setSubject(subject);
		setAttachment(numAttachments);
	}

	public void setMessageNumber(int messageNumber) {
		setAttribute("messageNumber", messageNumber);
	}

	public String getMessageNumber() {
		return getAttributeAsString("messageNumber");
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

	public void setAttachment(int numAttachments) {
		setAttribute("attachment", numAttachments > 0);
	}

	public String getAttachment() {
		return getAttributeAsString("attachment");
	}
}