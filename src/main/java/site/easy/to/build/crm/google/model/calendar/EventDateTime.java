package site.easy.to.build.crm.google.model.calendar;

import com.google.api.client.util.DateTime;

public class EventDateTime {
//    private String date;
    private String dateTime;

    private String timeZone;
    private String date;
    // Getters and setters

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public void checkDateTime(){
        if (getDateTime()==null){
            DateTime t = new DateTime(getDate()+"T00:00:00+03:00");
            setDateTime(t.toString());
        }
    }
}