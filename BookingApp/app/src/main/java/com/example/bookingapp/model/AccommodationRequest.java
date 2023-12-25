package com.example.bookingapp.model;

import com.example.bookingapp.model.enums.AccommodationRequestStatus;

public class AccommodationRequest {
    public Long id;
    public Long unapprovedAccommodationId;   //za dobavljanje podataka o neodobrenom smestaju
    public AccommodationRequestStatus requestStatus;
    public Long originalAccommodationId;  //bice null ako je smestaj tek kreiran -->status tog smestaja u bazi je PENDING

    public AccommodationRequest(Long id, Long unapprovedAccommodationId, AccommodationRequestStatus requestStatus, Long originalAccommodationId) {
        this.id = id;
        this.unapprovedAccommodationId = unapprovedAccommodationId;
        this.requestStatus = requestStatus;
        this.originalAccommodationId = originalAccommodationId;
    }

    public AccommodationRequest(Long unapprovedAccommodationId, AccommodationRequestStatus requestStatus, Long originalAccommodationId) {
        this.unapprovedAccommodationId = unapprovedAccommodationId;
        this.requestStatus = requestStatus;
        this.originalAccommodationId = originalAccommodationId;
    }

    public AccommodationRequest() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUnapprovedAccommodationId() {
        return unapprovedAccommodationId;
    }

    public void setUnapprovedAccommodationId(Long unapprovedAccommodationId) {
        this.unapprovedAccommodationId = unapprovedAccommodationId;
    }

    public AccommodationRequestStatus getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(AccommodationRequestStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public Long getOriginalAccommodationId() {
        return originalAccommodationId;
    }

    public void setOriginalAccommodationId(Long originalAccommodationId) {
        this.originalAccommodationId = originalAccommodationId;
    }
}
