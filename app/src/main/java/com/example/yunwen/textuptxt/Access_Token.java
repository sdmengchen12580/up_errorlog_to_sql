package com.example.yunwen.textuptxt;




public class Access_Token {
    private String message;
    private int status;
    private String access_token;

    @Override
    public String toString() {
        return "Access_Token{" +
                "message='" + message + '\'' +
                ", status=" + status +
                ", access_token='" + access_token + '\'' +
                '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }


}
