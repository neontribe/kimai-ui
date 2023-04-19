package uk.co.neontribe.kimai.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;
import uk.co.neontribe.kimai.api.dto.TimeSheetDto;
import uk.co.neontribe.kimai.config.Settings;
import uk.co.neontribe.kimai.desktop.DuplicateEntryModal;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@AllArgsConstructor
public class TimeSheet extends Entity {

    static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    private String description;
    private Date begin;
    private Date end;
    private int project;
    private int activity;
    private int user;

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

    public static String postTimeSheet(Settings settings, TimeSheet timeSheet) throws IOException {
        URL url = new URL(settings.getKimaiUri() + "/api/timesheets");

        TimeSheet[] timesheets = getTimeSheets(settings, timeSheet.project, timeSheet.activity, timeSheet.begin, timeSheet.end);
        if (timesheets.length > 0) {
            DuplicateEntryModal duplicateEntryModal = new DuplicateEntryModal(settings);
            duplicateEntryModal.setVisible(true);
            if (!duplicateEntryModal.getShouldProceed()) {
                return null;
            }
        }

        TimeSheetDto timeSheetDto = new TimeSheetDto(timeSheet);
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting();
        builder.setDateFormat(DATE_FORMAT).create();
        Gson gson = builder.create();
        String postContent = gson.toJson(timeSheetDto);

        return postApi(url, settings, postContent);
    }

    public static TimeSheet[] getTimeSheets(Settings settings, int projectId, int activityId, Date begin, Date end) throws IOException {
        URL url = new URL(settings.getKimaiUri() + "/api/timesheets");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);

        List<Map.Entry<String, String>> parameters = new ArrayList<>();
        // TODO Fix this to work when project and activity get replaced with projects and activities
        parameters.add(new AbstractMap.SimpleEntry<>("project", String.valueOf(projectId)));
        parameters.add(new AbstractMap.SimpleEntry<>("activity", String.valueOf(activityId)));
        parameters.add(new AbstractMap.SimpleEntry<>("begin", simpleDateFormat.format(begin)));
        parameters.add(new AbstractMap.SimpleEntry<>("end", simpleDateFormat.format(end)));

        String content = Entity.getApi(url, settings, parameters);
        Gson gson = new Gson();
        TypeToken<List<TimeSheet>> projectType = new TypeToken<List<TimeSheet>>() {
        };
        List<TimeSheet> data = gson.fromJson(content, projectType);
        TimeSheet[] timesheets = new TimeSheet[data.size()];
        for (int i = 0; i < data.size(); i++) {
            timesheets[i] = data.get(i);
        }
        return timesheets;
    }
}