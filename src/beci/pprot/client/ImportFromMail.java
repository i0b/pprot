package beci.pprot.client;

import org.apache.tools.ant.listener.MailLogger;
import org.eclipse.jdt.internal.core.util.HandleFactory;

import beci.pprot.shared.MailData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class ImportFromMail {
	private static ImportFromMail instance = null;

	private VLayout mainLayout;
	private Label mailBody;

	public static ImportFromMail getInstance() {
		if (instance == null)
			instance = new ImportFromMail();
		return instance;
	}

	public VLayout getMainLayout() {
		return mainLayout;
	}

	public ImportFromMail() {
		mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		mailBody = new Label();
		mailBody.setWidth100();
		mailBody.setHeight100();

		SectionStack sectionStack = new SectionStack();
		sectionStack.setWidth100();
		sectionStack.setHeight100();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setAnimateSections(true);
		sectionStack.setOverflow(Overflow.HIDDEN);

		SectionStackSection subjectSection = new SectionStackSection();
		subjectSection.setTitle("Betreff");
		subjectSection.setExpanded(true);

		ListGrid mailDataGrid = new ListGrid();
		mailDataGrid.setWidth100();
		mailDataGrid.setHeight100();
		mailDataGrid.setShowAllRecords(true);

		mailDataGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent event) {
				int messageNumber = Integer.parseInt(event.getRecord()
						.getAttributeAsString("messageNumber"));
				ImportFromMailDS.setBodyToLabelByNumber(messageNumber, mailBody);
			}
		});

		ListGridField messageNumberFiled = new ListGridField("messageNumber",
				"Nummer");
		ListGridField composedField = new ListGridField("composed", "Datum");
		composedField.setType(ListGridFieldType.DATE);
		ListGridField senderField = new ListGridField("sender", "Absender");
		ListGridField subjectField = new ListGridField("subject", "Betreff");
		ListGridField attachmentField = new ListGridField("attachment",
				"Anhang");
		attachmentField.setType(ListGridFieldType.BOOLEAN);

		mailDataGrid.setFields(new ListGridField[] { messageNumberFiled,
				composedField, senderField, subjectField, attachmentField });
		mailDataGrid.setCanResizeFields(true);
		ImportFromMailDS.setRecords(mailDataGrid);
		subjectSection.addItem(mailDataGrid);

		SectionStackSection importSection = new SectionStackSection();
		importSection.setTitle("Importieren");
		importSection.setExpanded(true);

		TabSet importTabSet = new TabSet();
		importTabSet.setTabBarPosition(Side.TOP);
		importTabSet.setTabBarAlign(Side.RIGHT);
		importTabSet.setWidth100();
		importTabSet.setHeight100();

		Tab detailsTab = new Tab("Details"); // Icon?

		HLayout detailsLayout = new HLayout();
		detailsLayout.addMember(mailBody);
		// TODO add file list here
		detailsTab.setPane(detailsLayout);

		Tab importTab = new Tab("Import");

		HLayout importLayout = new HLayout();

		Label importLabel = new Label("To be filled, too");
		importLayout.addMember(importLabel);
		importTab.setPane(importLayout);

		importTabSet.addTab(detailsTab);
		importTabSet.addTab(importTab);

		importSection.setItems(importTabSet);

		sectionStack.setSections(subjectSection, importSection);

		mainLayout.addMember(sectionStack);
	}
}
