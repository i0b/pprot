package beci.pprot.client;

/*The plan:
 * The gui should be layouted like this:
 * -----------------------------------------------
 * '  - PDF1 -  '             PDF1               '
 * '(attachment '           Data Data            '
 * ' of a mail) '                                '
 * '-------------                                '
 * '    PDF2    '                                '
 * '            '                                '
 * '-------------                                '
 * '  .         '                                '
 * '  .         '                                '
 * '  .         '                                '
 * '-------------                                '
 * '            '                                '
 * '----------------------------------------------
 * '              Import this attachment         '
 * '                                             '
 * ' | Date: ___\/ | Lecturer: ____\/ | ... |    '
 * '                                             '
 * -----------------------------------------------
 * 
 * To make this work some kind of pdf renderer (like pdf.js or acrobat)
 * is needed. Which both don't work as good as needed to use it efficiently.
 * 
 * */


import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.Overflow;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.types.VisibilityMode;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.events.CellClickEvent;
import com.smartgwt.client.widgets.grid.events.CellClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.SectionStack;
import com.smartgwt.client.widgets.layout.SectionStackSection;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;

public class UIImportFromMail {
	private static UIImportFromMail instance = null;

	private VLayout mainLayout;
	private Label mailBody;
	private TreeGrid attachmentGrid;

	public static UIImportFromMail getInstance() {
		if (instance == null)
			instance = new UIImportFromMail();
		return instance;
	}

	public VLayout getMainLayout() {
		return mainLayout;
	}

	public UIImportFromMail() {
		mainLayout = new VLayout();
		mainLayout.setWidth100();
		mainLayout.setHeight100();

		mailBody = new Label();
		mailBody.setAlign(Alignment.LEFT);
		mailBody.setValign(VerticalAlignment.TOP);
		mailBody.setScrollbarSize(5);
		mailBody.setWidth100();
		mailBody.setHeight("60%"); // FIX Value?
		
		ImportValues importValues = new ImportValues().getInstance(); // TODO should be without 'new...'!

		SectionStack sectionStack = new SectionStack();
		sectionStack.setWidth100();
		sectionStack.setHeight100();
		sectionStack.setVisibilityMode(VisibilityMode.MULTIPLE);
		sectionStack.setAnimateSections(true);
		sectionStack.setOverflow(Overflow.HIDDEN);

		// ----------------------- Subject Section -----------------------------
		
		SectionStackSection subjectSection = new SectionStackSection();
		subjectSection.setTitle("Betreff");
		//subjectSection.setShowHeader(false);
		//subjectSection.setCanCollapse(false);
		subjectSection.setExpanded(true);

		ListGrid mailDataGrid = new ListGrid();
		mailDataGrid.setWidth100();
		mailDataGrid.setHeight100();
		mailDataGrid.setShowAllRecords(true);

		mailDataGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent event) {
				int messageNumber = Integer.parseInt(event.getRecord()
						.getAttributeAsString("messageNumber"));
				UIImportFromMailDS.setBodyToLabelByNumber(messageNumber,
						mailBody);
			}
		});
		mailDataGrid.addCellClickHandler(new CellClickHandler() {
			// TODO better: anonymous function which is used in the method above
			// and here - but lambda is yet to come in java
			public void onCellClick(CellClickEvent event) {
				int messageNumber = Integer.parseInt(event.getRecord()
						.getAttributeAsString("messageNumber"));
				
				UIImportFromMailDS.setAttachmentListData(messageNumber, attachmentGrid);
				
				UIImportFromMailDS.setBodyToLabelByNumber(messageNumber,
						mailBody);
				UIImportFromMailDS.setAttachmentListData(messageNumber, attachmentGrid);
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
		UIImportFromMailDS.setRecords(mailDataGrid);
		subjectSection.addItem(mailDataGrid);

		// ------------------------ Details Section -----------------------------
		
		SectionStackSection detailsSection = new SectionStackSection();
		detailsSection.setTitle("Details");
		detailsSection.setExpanded(false);

		HLayout detailsLayout = new HLayout();
		detailsLayout.setWidth100();
		detailsLayout.setHeight("60%");
		
		detailsLayout.addMember(mailBody);
		
		// Attachment list right to mail body layout
		attachmentGrid = new TreeGrid();  
		attachmentGrid.setLoadDataOnDemand(false);  
		attachmentGrid.setWidth(400);  
		attachmentGrid.setHeight100();
		FileTreeNode[] emptyFileTree = {new FileTreeNode()};
		attachmentGrid.setData(emptyFileTree);  
		attachmentGrid.setCanEdit(false);
		attachmentGrid.setAutoFetchData(false);           
  
        TreeGridField nameField = new TreeGridField("filename", "Dateiname");  
        nameField.setFrozen(true);  
  
        attachmentGrid.setFields(nameField);
        
        detailsLayout.addMember(attachmentGrid);

		detailsSection.addItem(detailsLayout);

		
		SectionStackSection importSection = new SectionStackSection();
		importSection.setTitle("Importieren");
		importSection.setExpanded(false);

		DynamicForm importForm = new DynamicForm();
		importForm.setNumCols(4);
		importForm.setWidth100();
		importForm.setHeight(150);
		
		DateItem dateField = new DateItem("Datum");
		ComboBoxItem typeField = new ComboBoxItem("Typ");
		String[] values = {"Schriftlich","Muendlich"};
		typeField.setValueMap(values);
		typeField.setDefaultValue(values[0]);
				
		ComboBoxItem lecturerField = new ComboBoxItem("Dozent");
		lecturerField.setValueMap(importValues.getLecturer()); //TODO this should be ImportValues.getInstance.getLecturers() !
		
		ComboBoxItem courseField = new ComboBoxItem("Vorlesung");
		courseField.setValueMap(importValues.getCourses());
		
		importForm.setFields(dateField,typeField,lecturerField,courseField);
		
		importSection.addItem(importForm);
		
		sectionStack.setSections(subjectSection, detailsSection, importSection);

		mainLayout.addMember(sectionStack);
	}
}
