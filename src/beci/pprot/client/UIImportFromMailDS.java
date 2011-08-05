package beci.pprot.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import beci.pprot.shared.MailData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeNode;

public class UIImportFromMailDS extends DataSource {
	private static MailRecord[] records = null;
	private static HashMap<Integer,ArrayList<String>> attachmentList = null; //TODO maybe join it with the variable above
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

		mailBody.setContents("Lade Inhalt...");
		mailDataSvc.getMailBody(messageNumber, callback);
	}

	public static void getNewRecords(final ListGrid listGrid) {
		if (mailDataSvc == null)
			mailDataSvc = GWT.create(MailAccessService.class);

		AsyncCallback<MailData[]> callback = new AsyncCallback<MailData[]>() {
			public void onFailure(Throwable caught) {
				SC.say("Es konnten keine Daten aus dem E-Mail Postfach entnommen werden.");// TODO
																						   // useful
																						   // message
			}

			public void onSuccess(MailData[] result) {
				records = new MailRecord[result.length];
				attachmentList = new HashMap<Integer,ArrayList<String>>();

				for (int i = 0; i < result.length; i++) {
					records[i] = new MailRecord(result[i].getMessageNumber(),
							result[i].getComposed(), result[i].getSender(),
							result[i].getSubject(), !result[i].getAttachment().isEmpty());
					attachmentList.put(Integer.valueOf(result[i].getMessageNumber()), result[i].getAttachment());
				}

				listGrid.setData(records);
			}
		};

		mailDataSvc.getMailData(callback);
	}

	public static void setAttachmentListData(int messageNumber,
			final TreeGrid attachmentGrid) {
		ArrayList<String> attachments = attachmentList.get(messageNumber);
		
		FileTreeNode[] attachmentNodes = new FileTreeNode[attachments.size()+1];
		
		attachmentNodes[0] = new FileTreeNode("0","0","Anhänge");
		
		for (int i=0; i<attachments.size(); i++)
			attachmentNodes[i] = new FileTreeNode(String.valueOf(i+1), "0", attachments.get(i));
			
		attachmentGrid.setData(attachmentNodes); // TODO make this work!
	}
}


class MailRecord extends ListGridRecord {
	public MailRecord() {}

	public MailRecord(int messageNumber, Date composed, String sender,
			String subject, Boolean attachments) {
		setMessageNumber(messageNumber);
		setComposed(composed);
		setSender(sender);
		setSubject(subject);
		setAttachment(attachments);
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

	public void setAttachment(Boolean attachments) {
		setAttribute("attachment", attachments);
	}

	public Boolean getAttachment() {
		return getAttributeAsBoolean("attachment");
	}
}

class FileTreeNode extends TreeNode {  
	public FileTreeNode() { setId("0"); setBelongsTo("0"); setFilename(""); }
	
    public FileTreeNode(String id, String belongsTo, String filename) {  
    	setId(id);
        setBelongsTo(belongsTo);
        setFilename(filename);
    }

    public void setId(String value) {  
        setAttribute("id", value);
    }

    public void setBelongsTo(String value) {  
        setAttribute("belongsTo", value);
    }

    public void setFilename(String value) {  
        setAttribute("filename", value);
    }
}