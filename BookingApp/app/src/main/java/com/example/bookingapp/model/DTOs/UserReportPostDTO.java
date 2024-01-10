package com.example.bookingapp.model.DTOs;

public class UserReportPostDTO {
    public String userThatReportsId;


    public String userThatIsReportedId;

    public String reason;

    public UserReportPostDTO(String userThatReports, String userThatIsReported, String reason) {
        this.userThatReportsId = userThatReports;
        this.userThatIsReportedId = userThatIsReported;
        this.reason = reason;
    }

    public String getUserThatReports() {
        return userThatReportsId;
    }

    public void setUserThatReports(String userThatReports) {
        this.userThatReportsId = userThatReports;
    }

    public String getUserThatIsReported() {
        return userThatIsReportedId;
    }

    public void setUserThatIsReported(String userThatIsReported) {
        this.userThatIsReportedId = userThatIsReported;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
