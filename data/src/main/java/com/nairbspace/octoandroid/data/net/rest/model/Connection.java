package com.nairbspace.octoandroid.data.net.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Connection {

    @SerializedName("current") private Current current;
    @SerializedName("options") private Options options;

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public Options getOptions() {
        return options;
    }

    public void setOptions(Options options) {
        this.options = options;
    }


    public class Current {

        @SerializedName("baudrate") private Integer baudrate;
        @SerializedName("port") private String port;
        @SerializedName("printerProfile") private String printerProfile;
        @SerializedName("state") private String state;

        public Integer getBaudrate() {
            return baudrate;
        }

        public void setBaudrate(Integer baudrate) {
            this.baudrate = baudrate;
        }

        public String getPort() {
            return port;
        }

        public void setPort(String port) {
            this.port = port;
        }

        public String getPrinterProfile() {
            return printerProfile;
        }

        public void setPrinterProfile(String printerProfile) {
            this.printerProfile = printerProfile;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

    }

    public class Options {

        @SerializedName("baudratePreference") private Integer baudratePreference;
        @SerializedName("baudrates") private List<Integer> baudrates = new ArrayList<Integer>();
        @SerializedName("portPreference") private String portPreference;
        @SerializedName("ports") private List<String> ports = new ArrayList<String>();
        @SerializedName("printerProfilePreference") private String printerProfilePreference;
        @SerializedName("printerProfiles") private List<PrinterProfile> printerProfiles = new ArrayList<PrinterProfile>();

        public Integer getBaudratePreference() {
            return baudratePreference;
        }

        public void setBaudratePreference(Integer baudratePreference) {
            this.baudratePreference = baudratePreference;
        }

        public List<Integer> getBaudrates() {
            return baudrates;
        }

        public void setBaudrates(List<Integer> baudrates) {
            this.baudrates = baudrates;
        }

        public String getPortPreference() {
            return portPreference;
        }

        public void setPortPreference(String portPreference) {
            this.portPreference = portPreference;
        }

        public List<String> getPorts() {
            return ports;
        }

        public void setPorts(List<String> ports) {
            this.ports = ports;
        }

        public String getPrinterProfilePreference() {
            return printerProfilePreference;
        }

        public void setPrinterProfilePreference(String printerProfilePreference) {
            this.printerProfilePreference = printerProfilePreference;
        }

        public List<PrinterProfile> getPrinterProfiles() {
            return printerProfiles;
        }

        public void setPrinterProfiles(List<PrinterProfile> printerProfiles) {
            this.printerProfiles = printerProfiles;
        }
    }

    public class PrinterProfile {

        @SerializedName("id") private String id;
        @SerializedName("name") private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
