package beci.pprot.client;

import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONException;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.util.SC;

public class ImportValues {
	private static ImportValues instance = null;
	private static JSONArray gridData = null;
	private static final String URL = GWT.getHostPageBaseURL()+"ds/examinations.json";

	private static Set<String> lecturer;
	private static Set<String> courses;

	public ImportValues getInstance() {
		if (instance == null)
			instance = new ImportValues();

		return instance;
	}

	public ImportValues() {
		// parse the response text into JSON
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL);
		try {
			builder.setCallback(new RequestCallback() {

				public void onError(Request request, Throwable exception) {
					SC.say(request.toString());
				}

				public void onResponseReceived(Request request,
						Response response) {

					if (response.getStatusCode() == 200)
						gridData = JSONParser.parseStrict(response.getText())
								.isArray();
					else
						SC.say(response.getStatusText());
					
					try {
						if (gridData == null)
							throw new JSONException();
					} catch (JSONException e) {
						//SC.say("Could not parse JSON");
						return;
					}

					JSONValue jsonValue;
					
					for (int i = 0; i < gridData.size(); i++) {
						JSONObject jsItem;
						JSONArray jsLecturer, jsCourses;

						if ((jsItem = gridData.get(i).isObject()) == null)
							continue;

						if ((jsonValue = jsItem.get("lecturer")) == null)
							continue;
						if ((jsLecturer = jsonValue.isArray()) == null)
							continue;
						else
							for (int j=0; j<jsLecturer.size(); j++)
								lecturer.add(jsLecturer.get(j).toString());

						if ((jsonValue = jsItem.get("courses")) == null)
							continue;
						if ((jsCourses = jsonValue.isArray()) == null)
							continue;
						else
							for (int j=0; j<jsCourses.size(); j++)
								courses.add(jsCourses.get(j).toString());
					}
				}
			});
			builder.send();
		} catch (Exception e) {
			SC.say(e.getMessage());
		}
	}

	public String[] getLecturer() {
		if (lecturer == null)
			return new String[]{};
		return (String[]) lecturer.toArray();
	}

	public String[] getCourses() {
		if (courses == null)
			return new String[]{};
		return (String[]) courses.toArray();
	}
}