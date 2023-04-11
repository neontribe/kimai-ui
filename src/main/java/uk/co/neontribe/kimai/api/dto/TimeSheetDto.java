package uk.co.neontribe.kimai.api.dto;

import uk.co.neontribe.kimai.api.TimeSheet;

import java.util.Date;

/**
 * We need this to allow us to serialise the TimeSheet object without including the ID field
 */
public class TimeSheetDto {

    private String description;
    private Date begin;
    private Date end;
    private int project;
    private int activity;
    private int user;

    public TimeSheetDto(TimeSheet timeSheet) {
        this.description = timeSheet.getDescription();
        this.begin = timeSheet.getBegin();
        this.end = timeSheet.getEnd();
        this.project = timeSheet.getProject();
        this.activity = timeSheet.getActivity();
        this.user = timeSheet.getUser();
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public void setProject(int project) {
        this.project = project;
    }

    public void setActivity(int activity) {
        this.activity = activity;
    }

    public void setUser(int user) {
        this.user = user;
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
}