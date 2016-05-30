package com.nairbspace.octoandroid.data.entity;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

import java.util.List;

@AutoValue
@AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity.class)
public abstract class CurrentHistoryEntity {
    @Nullable @SerializedName("state") public abstract State state();
    @Nullable @SerializedName("job") public abstract Job job();
    @Nullable @SerializedName("progress") public abstract Progress progress();
//    @Nullable @SerializedName("currentZ") public abstract Double currentZ();
//    @Nullable @SerializedName("offsets") public abstract Offsets offsets();
    @Nullable @SerializedName("temps") public abstract List<Temps> temps();
//    @Nullable @SerializedName("logs") public abstract List<String> logs();
//    @Nullable @SerializedName("messages") public abstract List<String> messages();
//    @Nullable @SerializedName("serverTime") public abstract Double serverTime(); // Pretty sure unix time, not in doc
//    @Nullable @SerializedName("busyFiles") public abstract List<BusyFiles> busyFiles(); // Not in docs

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_State.class)
    public abstract static class State {
        @Nullable @SerializedName("text") public abstract String text();
        @Nullable @SerializedName("flags") public abstract Flags flags();

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_State_Flags.class)
        public abstract static class Flags {
            @Nullable @SerializedName("operational") public abstract Boolean operational();
            @Nullable @SerializedName("paused") public abstract Boolean paused();
            @Nullable @SerializedName("printing") public abstract Boolean printing();
            @Nullable @SerializedName("sdReady") public abstract Boolean sdReady();
            @Nullable @SerializedName("error") public abstract Boolean error();
            @Nullable @SerializedName("ready") public abstract Boolean ready();
            @Nullable @SerializedName("closedOrError") public abstract Boolean closedOrError();
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Job.class)
    public abstract static class Job {
        @Nullable @SerializedName("file") public abstract File file();
        @Nullable @SerializedName("estimatedPrintTime") public abstract Double estimatedPrintTime();
        @Nullable @SerializedName("lastPrintTime") public abstract Double lastPrintTime();
//        @Nullable @SerializedName("filament") public abstract Filament filament();

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Job_File.class)
        public abstract static class File { // Test against this, because doesn't match with API docs
            @Nullable @SerializedName("origin") public abstract String origin();
            @Nullable @SerializedName("date") public abstract Long date(); // Unix Timestamp (in seconds)
            @Nullable @SerializedName("name") public abstract String name();
            @Nullable @SerializedName("size") public abstract Long size();
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Job_Filament.class)
        public abstract static class Filament {
            @Nullable @SerializedName("length") public abstract Double length();
            @Nullable @SerializedName("volume") public abstract Double volume();
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Progress.class)
    public abstract static class Progress {
        @Nullable @SerializedName("completion") public abstract Double completion();
        @Nullable @SerializedName("filepos") public abstract Long filepos();
        @Nullable @SerializedName("printTime") public abstract Long printTime();
        @Nullable @SerializedName("printTimeLeft") public abstract Long printTimeLeft();
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Offsets.class)
    public abstract static class Offsets {
        @Nullable @SerializedName("tool0") public abstract Integer tool0();
        @Nullable @SerializedName("tool1") public abstract Integer tool1();
        @Nullable @SerializedName("bed") public abstract Integer bed();
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Temps.class)
    public abstract static class Temps {
        @Nullable @SerializedName("time") public abstract Long time(); // Unix Timestamp (in seconds)
        @Nullable @SerializedName("tool0") public abstract Tool0 tool0();
        @Nullable @SerializedName("tool1") public abstract Tool1 tool1();
        @Nullable @SerializedName("bed") public abstract Bed bed();

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Temps_Tool0.class)
        public abstract static class Tool0 {
            @Nullable @SerializedName("actual") public abstract Double actual();
            @Nullable @SerializedName("target") public abstract Double target();
//            @Nullable @SerializedName("offset") public abstract Double offset();
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Temps_Tool1.class)
        public abstract static class Tool1 {
            @Nullable @SerializedName("actual") public abstract Double actual();
            @Nullable @SerializedName("target") public abstract Double target();
//            @Nullable @SerializedName("offset") public abstract Double offset();
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Temps_Bed.class)
        public abstract static class Bed {
            @Nullable @SerializedName("actual") public abstract Double actual();
            @Nullable @SerializedName("target") public abstract Double target();
//            @Nullable @SerializedName("offset") public abstract Double offset();
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_BusyFiles.class)
    public abstract static class BusyFiles {
        @Nullable @SerializedName("origin") public abstract String origin();
        @Nullable @SerializedName("name") public abstract String name();
    }
}