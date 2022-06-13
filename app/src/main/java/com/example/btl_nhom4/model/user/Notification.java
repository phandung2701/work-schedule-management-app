package com.example.btl_nhom4.model.user;

public class Notification {
    String idSender;
    String idReceiver;
    String message;
    int idMessage;
    int idWorkspace;
    String emailSender;
    int isAccept;
    int isRefuse;

    public Notification(String idSender, String idReceiver, String message, int idMessage, int idWorkspace,String emailSender, int isAccept,int isRefuse) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.message = message;
        this.idMessage = idMessage;
        this.idWorkspace = idWorkspace;
        this.emailSender = emailSender;
        this.isAccept = isAccept;
        this.isRefuse = isRefuse;
    }

    public  Notification(){

    }

    public int getIsAccept() {
        return isAccept;
    }

    public void setIsAccept(int isAccept) {
        this.isAccept = isAccept;
    }

    public int getIsRefuse() {
        return isRefuse;
    }

    public void setIsRefuse(int isRefuse) {
        this.isRefuse = isRefuse;
    }

    public String getEmailSender() {
        return emailSender;
    }

    public void setEmailSender(String emailSender) {
        this.emailSender = emailSender;
    }

    public String getIdSender() {
        return idSender;
    }

    public void setIdSender(String idSender) {
        this.idSender = idSender;
    }

    public String getIdReceiver() {
        return idReceiver;
    }

    public void setIdReceiver(String idReceiver) {
        this.idReceiver = idReceiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getIdMessage() {
        return idMessage;
    }

    public void setIdMessage(int idMessage) {
        this.idMessage = idMessage;
    }

    public int getIdWorkspace() {
        return idWorkspace;
    }

    public void setIdWorkspace(int idWorkspace) {
        this.idWorkspace = idWorkspace;
    }
}
