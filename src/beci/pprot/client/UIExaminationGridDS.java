package beci.pprot.client;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.DSDataFormat;
import com.smartgwt.client.types.FieldType;

class UIExaminationGridDS extends DataSource {
	private static UIExaminationGridDS instance = null;

	public static UIExaminationGridDS getInstance() {
		if (instance == null) {
			instance = new UIExaminationGridDS("examinationDS_JSON");
		}
		return instance;
	}

	public UIExaminationGridDS(String id) {
		setID(id);
		setDataFormat(DSDataFormat.JSON);
		DataSourceField dateField = new DataSourceField("date",
				FieldType.DATE, "Datum");
		DataSourceField typeField = new DataSourceField("type",
				FieldType.TEXT, "Typ");
		typeField.setValueMap("Muendlich", "Klausur"); // TODO make this be automated
		DataSourceField lecturerField = new DataSourceField("lecturer",
				FieldType.ANY, "Dozent(en)");
		DataSourceField coursesField = new DataSourceField("courses",
				FieldType.ANY, "Vorlesung(en)");
		DataSourceField solutionField = new DataSourceField("solution",
				FieldType.BOOLEAN, "Lösung");
		//DataSourceField fileField = new DataSourceField("file",
		//		FieldType.TEXT, "Dateiname");
		setFields(dateField, typeField, lecturerField, coursesField,
				solutionField);
		setDataURL("ds/examinations.json"); // TODO save this in XML properties unfortunately 'properties' cannot be used in GWT
											// so find an other way to make this work!
	}
}