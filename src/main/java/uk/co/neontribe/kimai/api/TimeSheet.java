package uk.co.neontribe.kimai.api;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import uk.co.neontribe.kimai.config.ConfigNotInitialisedException;
import uk.co.neontribe.kimai.config.Settings;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class TimeSheet extends Entity {

    Date begin;
    Date end;
    int project;
    int activity;
    int user;

    public TimeSheet(String description, Date begin, Date end, int project, int activity, int user) {
        this(description, -1, begin, end, project, activity, user);
    }
    public TimeSheet(String description, int id, Date begin, Date end, int project, int activity, int user) {
        super(description, id);
        this.begin = begin;
        this.end = end;
        this.project = project;
        this.activity = activity;
        this.user = user;
    }

    public static void postTimeSheet() {
        List<Map.Entry<String, String>> parameters = new ArrayList<>();
        parameters.add(new AbstractMap.SimpleEntry<>("begin", ""));
        parameters.add(new AbstractMap.SimpleEntry<>("end", ""));
        parameters.add(new AbstractMap.SimpleEntry<>("project", ""));
        parameters.add(new AbstractMap.SimpleEntry<>("activity", ""));
        parameters.add(new AbstractMap.SimpleEntry<>("user", ""));
        parameters.add(new AbstractMap.SimpleEntry<>("description", ""));
    }

    /*

￼Example Value
￼Model
{
  "begin": "2023-03-31T09:47:08",
  "end": "2023-03-31T09:47:08",
  "project": 0,
  "activity": 0,
  "description": "string",
  "fixedRate": 0,
  "hourlyRate": 0,
  "user": 0,
  "exported": true,
  "billable": true,
  "tags": "string"
}
     */

    public String getDescription() {
        return this.getName();
    }
    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public int getProject() {
        return project;
    }

    public int getActivity() {
        return activity;
    }

    public int getUser() {
        return user;
    }

    public static void postTimeSheet(TimeSheet timeSheet) {

    }

    public static TimeSheet[] getTimeSheets(int customerId, int projectId, int ActivityId, Object date, int duration) throws ConfigNotInitialisedException, IOException {
        Settings settings = Settings.getInstance();
        URL url = new URL(settings.getKimaiUri() + "/api/timesheets");
        String content = Entity.callApi(url);
        Gson gson = new Gson();
        TypeToken<List<TimeSheet>> projectType = new TypeToken<List<TimeSheet>>() {
        };
        List<TimeSheet> data = gson.fromJson(content, projectType);
        // data.removeIf(p -> p.getCustomer() != id);
        TimeSheet[] timesheets = new TimeSheet[data.size()];
        for (int i = 0; i < data.size(); i++) {
            timesheets[i] = data.get(i);
        }
        return timesheets;
    }
}
