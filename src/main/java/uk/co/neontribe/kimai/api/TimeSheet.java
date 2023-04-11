package uk.co.neontribe.kimai.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.AllArgsConstructor;
import uk.co.neontribe.kimai.api.dto.TimeSheetDto;
import uk.co.neontribe.kimai.config.Settings;

import java.io.IOException;
import java.net.URL;
import java.util.Date;

@AllArgsConstructor
public class TimeSheet extends Entity {

    private String description;
    private int id;
    private Date begin;
    private Date end;
    private int project;
    private int activity;
    private int user;

    public TimeSheet(String description, Date begin, Date end, int project, int activity, int user) {
        this(description, -1, begin, end, project, activity, user);
    }

    public String getDescription() {
        return description;
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

    public static String postTimeSheet(TimeSheet timeSheet) throws IOException {
        Settings settings = Settings.getInstance();
        URL url = new URL(settings.getKimaiUri() + "/api/timesheets");

        TimeSheetDto timeSheetDto = new TimeSheetDto(timeSheet);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
        Gson gson = builder.create();
        String postContent = gson.toJson(timeSheetDto);

        return postApi(url, postContent);
    }

//    public static TimeSheet[] getTimeSheets(int customerId, int projectId, int ActivityId, Object date, int duration) throws ConfigNotInitialisedException, IOException {
//        Settings settings = Settings.getInstance();
//        URL url = new URL(settings.getKimaiUri() + "/api/timesheets");
//        String content = Entity.callApi(url);
//        Gson gson = new Gson();
//        TypeToken<List<TimeSheet>> projectType = new TypeToken<List<TimeSheet>>() {
//        };
//        List<TimeSheet> data = gson.fromJson(content, projectType);
//        // data.removeIf(p -> p.getCustomer() != id);
//        TimeSheet[] timesheets = new TimeSheet[data.size()];
//        for (int i = 0; i < data.size(); i++) {
//            timesheets[i] = data.get(i);
//        }
//        return timesheets;
//    }
}