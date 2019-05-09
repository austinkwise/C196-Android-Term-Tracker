package com.example.termtracker_c196.Model;

public class Notes {
    private Integer id;
    private String courseId;
    private String message;

    public Notes(Integer id, String message, String courseId) {
        this.id = id;
        this.courseId = courseId;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
