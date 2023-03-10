package com.aplicacion.incidencias.model;

public class Login {

    private String strUser;
    private String strPass;

    public Login(String strUser, String strPass) {
        this.strUser = strUser;
        this.strPass = strPass;
    }

    public String getStrUser() {
        return strUser;
    }

    public void setStrUser(String strUser) {
        this.strUser = strUser;
    }

    public String getStrPass() {
        return strPass;
    }

    public void setStrPass(String strPass) {
        this.strPass = strPass;
    }
}
