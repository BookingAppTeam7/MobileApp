package com.example.bookingapp.model;

public class UserReport {
    public Long id;

    public String userThatReports;


    public String userThatIsReported;

    public String reason;

    public Boolean done;


    public UserReport(Long id, String userThatReports, String userThatIsReported, String reason,Boolean done) {
        this.id = id;
        this.userThatReports = userThatReports;
        this.userThatIsReported = userThatIsReported;
        this.reason = reason;
        this.done=done;
    }
    public UserReport( String userThatReports, String userThatIsReported, String reason,Boolean done) {

        this.userThatReports = userThatReports;
        this.userThatIsReported = userThatIsReported;
        this.reason = reason;
        this.done=done;
    }

    public UserReport() {

    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserThatReports() {
        return userThatReports;
    }

    public void setUserThatReports(String userThatReports) {
        this.userThatReports = userThatReports;
    }

    public String getUserThatIsReported() {
        return userThatIsReported;
    }

    public void setUserThatIsReported(String userThatIsReported) {
        this.userThatIsReported = userThatIsReported;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
