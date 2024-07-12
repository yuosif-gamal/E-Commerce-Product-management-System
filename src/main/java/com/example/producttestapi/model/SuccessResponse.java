package com.example.producttestapi.dto;

public class SuccessResponse {
    private String message;
    private boolean success;
    private Object data;
    private int status;

    public SuccessResponse() {}

    public SuccessResponse(String message, boolean success, Object data, int status) {
        this.message = message;
        this.success = success;
        this.data = data;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
