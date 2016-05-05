package com.nairbspace.octoandroid.net.rest.model;

import com.google.gson.annotations.SerializedName;

public class Connect {
    public static final String CONNECT = "connect";

    @SerializedName("command") private String command;
    @SerializedName("port") private String port;
    @SerializedName("baudrate") private Integer baudrate;
    @SerializedName("printerProfile") private String printerProfile;
    @SerializedName("save") private Boolean save;
    @SerializedName("autoconnect") private Boolean autoconnect;

    public Connect(String command, String port, Integer baudrate, String printerProfile, Boolean save, Boolean autoconnect) {
        this.command = command;
        this.port = port;
        this.baudrate = baudrate;
        this.printerProfile = printerProfile;
        this.save = save;
        this.autoconnect = autoconnect;
    }

    public Connect(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public Integer getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(Integer baudrate) {
        this.baudrate = baudrate;
    }

    public String getPrinterProfile() {
        return printerProfile;
    }

    public void setPrinterProfile(String printerProfile) {
        this.printerProfile = printerProfile;
    }

    public Boolean getSave() {
        return save;
    }

    public void setSave(Boolean save) {
        this.save = save;
    }

    public Boolean getAutoconnect() {
        return autoconnect;
    }

    public void setAutoconnect(Boolean autoconnect) {
        this.autoconnect = autoconnect;
    }

    @Override
    public String toString() {
        return "Connect{" +
                "command='" + command + '\'' +
                ", port='" + port + '\'' +
                ", baudrate=" + baudrate +
                ", printerProfile='" + printerProfile + '\'' +
                ", save=" + save +
                ", autoconnect=" + autoconnect +
                '}';
    }
}
