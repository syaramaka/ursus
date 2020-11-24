package com.doj.ursus.model;

import java.util.List;

public class ForceReason {

    private int forceReasonId;
    private String forceReason;
    private int officerId;

    public int getForceReasonId() {
        return forceReasonId;
    }

    public void setForceReasonId(int forceReasonId) {
        this.forceReasonId = forceReasonId;
    }

    public String getForceReason() {
        return forceReason;
    }

    public void setForceReason(String forceReason) {
        this.forceReason = forceReason;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    @Override
    public String toString() {
        return "ForceReason{" +
                "forceReasonId=" + forceReasonId +
                ", forceReason='" + forceReason + '\'' +
                ", officerId=" + officerId +
                '}';
    }
}
