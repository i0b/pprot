package beci.pprot.client;

import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;

public class ImportFromMail {
	private static ImportFromMail instance = null;
	private VLayout mainLayout;

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

		ListGridField composedField = new ListGridField("composed", "Datum");
		composedField.setType(ListGridFieldType.DATE);
		ListGridField senderField = new ListGridField("sender", "Absender");
		ListGridField subjectField = new ListGridField("subject", "Betreff");
		ListGridField attachmentField = new ListGridField("attachment",
				"Anhang");
		attachmentField.setType(ListGridFieldType.BOOLEAN);

		mailDataGrid.setFields(new ListGridField[] { composedField,
				senderField, subjectField, attachmentField });
		mailDataGrid.setCanResizeFields(true);
		ImportFromMailDS.setRecords(mailDataGrid);
		subjectSection.addItem(mailDataGrid);

		SectionStackSection importSection = new SectionStackSection();
		importSection.setTitle("Importieren");
		importSection.setExpanded(true);
		// importSection.setItems(tabSet);

		sectionStack.setSections(subjectSection, importSection);

		mainLayout.addMember(sectionStack);
	}
}
