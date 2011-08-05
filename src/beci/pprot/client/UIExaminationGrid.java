package beci.pprot.client;

// TODO login? - see: -> http://www.smartclient.com/smartgwt/javadoc/com/smartgwt/client/docs/Relogin.html

import beci.pprot.shared.PrintData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.MultiSortCallback;
import com.smartgwt.client.data.MultiSortDialog;
import com.smartgwt.client.data.SortSpecifier;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.SortDirection;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Label;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class UIExaminationGrid {
	private static UIExaminationGrid instance = null;
	private PrintDataServiceAsync fileListSvc = GWT.create(PrintDataService.class);
	private VLayout mainLayout;

	public static UIExaminationGrid getInstance() {
		if (instance == null)
			instance = new UIExaminationGrid();
		return instance;
	}

	public VLayout getMainLayout() {
		return mainLayout;
	}

	public UIExaminationGrid() {
		mainLayout = new VLayout();
		final Label countSelected;

		mainLayout.setWidth100();
		mainLayout.setHeight100();

		final ListGrid examinationGrid = new ListGrid();
		examinationGrid.setWidth100();
		examinationGrid.setHeight("75%");
		examinationGrid.setShowFilterEditor(true);
		examinationGrid.setFilterOnKeypress(true);
		examinationGrid.setSortField(0);
		examinationGrid.setSortDirection(SortDirection.DESCENDING);
		examinationGrid.setSelectionType(SelectionStyle.SIMPLE);
		// examinationGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
		examinationGrid.setDataSource(UIExaminationGridDS.getInstance());
		examinationGrid.setAutoFetchData(true);
		examinationGrid.setShowEmptyMessage(true);
		examinationGrid
				.setEmptyMessage("<br>keine <b>Protokolle</b> verfügbar");

		final ToolStrip toolStrip = new ToolStrip();
		toolStrip.setWidth100();
		toolStrip.addFill();

		countSelected = new Label("0 Protokoll(e) ausgewählt");
		countSelected.setWidth100();
		countSelected.setAlign(Alignment.CENTER);
		toolStrip.addMember(countSelected);

		toolStrip.addSeparator();

		ToolStripButton multiSortButton = new ToolStripButton("Multilevel Sort");
		multiSortButton.setIcon("table_sort.png");
		toolStrip.addButton(multiSortButton);
		multiSortButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				MultiSortDialog.askForSort(examinationGrid, examinationGrid
						.getSort(), new MultiSortCallback() {
					public void execute(SortSpecifier[] sortLevels) {
						// if sortLevels is null, it means that the Cancel
						// button was
						// clicked in which case we simply want to dismiss the
						// dialog
						if (sortLevels != null)
							examinationGrid.setSort(sortLevels);
					}
				});
			}
		});

		ToolStripButton deselectButton = new ToolStripButton();
		deselectButton.setIcon("table_delete.png");
		deselectButton.setTitle("Auswahl aufheben");
		toolStrip.addButton(deselectButton);
		deselectButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				examinationGrid.deselectAllRecords();
			}
		});

		ToolStripButton printButton = new ToolStripButton();
		printButton.setIcon("printer.png");
		printButton.setTitle("Drucken");
		toolStrip.addButton(printButton);

		final ListGrid selectedExaminationGrid = new ListGrid();
		// selectedExaminationGrid.setWidth100();
		selectedExaminationGrid.setHeight("25%");
		selectedExaminationGrid.setShowAllRecords(true);
		selectedExaminationGrid.setDataSource(UIExaminationGridDS.getInstance());
		selectedExaminationGrid.setShowEmptyMessage(true);
		selectedExaminationGrid
				.setEmptyMessage("<br>zu druckende <b>Protokolle</b> auswählen");
		selectedExaminationGrid.clear();

		examinationGrid
				.addSelectionChangedHandler(new SelectionChangedHandler() {
					public void onSelectionChanged(SelectionEvent event) {
						selectedExaminationGrid.setData(examinationGrid
								.getSelectedRecords());

						int count = examinationGrid.getSelectedRecords().length;

						String pre = new String();
						String post = new String();

						if (count >= 20) {
							pre = "<b><font color='Red'>";
							post = "</font></b>";
						} else if (count >= 10) {
							pre = "<b>";
							post = "</b>";
						}

						countSelected.setContents(pre + String.valueOf(count)
								+ post + " Protokoll(e) ausgewählt");
					}
				});

		selectedExaminationGrid
				.addSelectionChangedHandler(new SelectionChangedHandler() {
					public void onSelectionChanged(SelectionEvent event) {
						examinationGrid.deselectRecord(event.getRecord());
					}
				});

		printButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = selectedExaminationGrid
						.getSelectedRecords();
				int numberSelected = selectedRecords.length;
				String[] filesToPrint = new String[numberSelected];

				for (int i = 0; i < selectedRecords.length; i++)
					filesToPrint[i] = selectedRecords[i].getAttribute("file");
				
				printFiles(new PrintData(filesToPrint));
				SC.say("printrequest sent");
			}
		});

		mainLayout.addMember(examinationGrid);
		mainLayout.addMember(toolStrip);
		mainLayout.addMember(selectedExaminationGrid);
	}

	private void printFiles(PrintData data) {
		// Initialize the service proxy.
		if (fileListSvc == null) {
			fileListSvc = GWT.create(PrintDataService.class);
		}

		// Set up the callback object.
		AsyncCallback<Boolean> callback = new AsyncCallback<Boolean>() {
			public void onFailure(Throwable caught) {
				SC.say("Fehler beim Übertragen der Daten.");
			}

			public void onSuccess(Boolean result) {
				if (!result)
					SC.say("Fehler beim Drucken.");
				else
					SC.say("Druck erfolgreich.");
			}
		};

		fileListSvc.printFiles(data, callback);
	}
}
