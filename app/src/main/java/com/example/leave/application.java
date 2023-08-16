package com.example.leave;

public class application {
    private String start, end, reason, type, status;
    private boolean pending;
    private int score;

    public application(String start, String end, String reason, String type, String status, boolean pending, int score) {
        this.start = start;
        this.end = end;
        this.reason = reason;
        this.type = type;
        this.status = status;
        this.pending = pending;
        this.score = score;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean getPending() {
        return pending;
    }

    public void setPending(boolean pending) {
        this.pending = pending;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public application() {
    }
}
