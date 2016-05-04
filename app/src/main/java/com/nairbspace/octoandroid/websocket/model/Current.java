package com.nairbspace.octoandroid.websocket.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Current {

    @SerializedName("state") private State state;
    @SerializedName("job") private Job job;
    @SerializedName("progress") private Progress progress;
    @SerializedName("currentZ") private Float currentZ;
    @SerializedName("offsets") private Offsets offsets;
    @SerializedName("temps") private List<Temps> temps;
    @SerializedName("logs") private List<String> logs;
    @SerializedName("messages") private List<String> messages;
    @SerializedName("serverTime") private Long serverTime; // Pretty sure unix, not in doc
    @SerializedName("busyFiles") private BusyFiles busyFiles; // Not in docs

    public class State {
        @SerializedName("text") private String text;
        @SerializedName("flags") private Flags flags;

        public class Flags {
            @SerializedName("operational") private Boolean operational;
            @SerializedName("paused") private Boolean paused;
            @SerializedName("printing") private Boolean printing;
            @SerializedName("sdReady") private Boolean sdReady;
            @SerializedName("error") private Boolean error;
            @SerializedName("ready") private Boolean ready;
            @SerializedName("closedOrError") private Boolean closedOrError;
        }
    }

    public class Job {
        @SerializedName("file") private File file;
        @SerializedName("estimatedPrintTime") private Integer estimatedPrintTime;
        @SerializedName("lastPrintTime")
        private Integer lastPrintTime;
        @SerializedName("filament") private Filament filament;

        public class File { // TODO need to ask questions on this since doc doesn't match actual output
            @SerializedName("origin") private String origin;
            @SerializedName("date") private Long date; // Unix Timestamp (in seconds)
            @SerializedName("name") private String name;
            @SerializedName("size") private Long size;
        }

        public class Filament {
            @SerializedName("length") private Integer length;
            @SerializedName("volume") private Float volume;
        }
    }

    public class Progress {
        @SerializedName("completion") private Float completion;
        @SerializedName("filepos") private Integer filepos;
        @SerializedName("printTime") private Integer printTime;
        @SerializedName("printTimeLeft") private Integer printTimeLeft;
    }

    public class Offsets {
        @SerializedName("tool0") private Integer tool0;
        @SerializedName("tool1") private Integer tool1;
        @SerializedName("bed") private Integer bed;
    }

    public class Temps {
        @SerializedName("time") private Long time; // Unix Timestamp (in seconds)
        @SerializedName("tool0") private Tool0 tool0;
        @SerializedName("tool1") private Tool1 tool1;
        @SerializedName("bed") private Bed bed;

        private class Tool0 {
            @SerializedName("actual") private Integer actual;
            @SerializedName("target") private Integer target;
            @SerializedName("offset") private Integer offset;
        }

        private class Tool1 {
            @SerializedName("actual") private Integer actual;
            @SerializedName("target") private Integer target;
            @SerializedName("offset") private Integer offset;
        }

        private class Bed {
            @SerializedName("actual") private Integer actual;
            @SerializedName("target") private Integer target;
            @SerializedName("offset") private Integer offset;
        }
    }

    private class BusyFiles {
        @SerializedName("origin") private String origin;
        @SerializedName("name") private String name;
    }
}
