package com.vidal.task.Model;

public class GenerateRequest {

    private String name;
    private String regNo;
    private String email;

    public GenerateRequest(String name, String regNo, String email) {
        this.name = name;
        this.regNo = regNo;
        this.email = email;
    }

    public GenerateRequest() {}

    public String getName() { return name; }
    public String getRegNo() { return regNo; }
    public String getEmail() { return email; }

    public void setName(String name) { this.name = name; }
    public void setRegNo(String regNo) { this.regNo = regNo; }
    public void setEmail(String email) { this.email = email; }
}
