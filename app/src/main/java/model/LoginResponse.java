package model;

public class LoginResponse {
    private int status;
    private String message;
    private String full_name;
    private String usertype;
    private String userstatus;

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getFull_name() {
        return full_name;
    }

    public String getUsertype() {
        return usertype;
    }

    public String getUserstatus() {
        return userstatus;
    }

    public LoginResponse(int status, String message, String full_name, String usertype, String userstatus) {
        this.status = status;
        this.message = message;
        this.full_name = full_name;
        this.usertype = usertype;
        this.userstatus = userstatus;
    }
}
