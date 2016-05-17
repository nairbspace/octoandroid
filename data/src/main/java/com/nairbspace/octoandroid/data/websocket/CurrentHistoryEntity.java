package com.nairbspace.octoandroid.data.websocket;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;
import com.nairbspace.octoandroid.domain.model.AutoGson;

import java.util.List;

@AutoValue
@AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity.class)
public abstract class CurrentHistoryEntity {
    @SerializedName("state") public abstract State state();
    @SerializedName("job") public abstract Job job();
    @SerializedName("progress") public abstract Progress progress();
    @SerializedName("currentZ") public abstract Float currentZ();
    @SerializedName("offsets") public abstract Offsets offsets();
    @SerializedName("temps") public abstract List<Temps> temps();
    @SerializedName("logs") public abstract List<String> logs();
    @SerializedName("messages") public abstract List<String> messages();
    @SerializedName("serverTime") public abstract Float serverTime(); // Pretty sure unix time, not in doc
    @SerializedName("busyFiles") public abstract List<BusyFiles> busyFiles(); // Not in docs

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_State.class)
    public abstract static class State {
        @SerializedName("text") public abstract String text();
        @SerializedName("flags") public abstract Flags flags();

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_State_Flags.class)
        public abstract static class Flags {
            @SerializedName("operational") public abstract Boolean operational();
            @SerializedName("paused") public abstract Boolean paused();
            @SerializedName("printing") public abstract Boolean printing();
            @SerializedName("sdReady") public abstract Boolean sdReady();
            @SerializedName("error") public abstract Boolean error();
            @SerializedName("ready") public abstract Boolean ready();
            @SerializedName("closedOrError") public abstract Boolean closedOrError();
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Job.class)
    public abstract static class Job {
        @SerializedName("file") public abstract File file();
        @SerializedName("estimatedPrintTime") public abstract Integer estimatedPrintTime();
        @SerializedName("lastPrintTime") public abstract Integer lastPrintTime();
        @SerializedName("filament") public abstract Filament filament();

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Job_File.class)
        public abstract static class File { // TODO need to ask questions on this since doc doesn't match actual output
            @SerializedName("origin") public abstract String origin();
            @SerializedName("date") public abstract Long date(); // Unix Timestamp (in seconds)
            @SerializedName("name") public abstract String name();
            @SerializedName("size") public abstract Long size();
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Job_Filament.class)
        public abstract static class Filament {
            @SerializedName("length") public abstract Integer length();
            @SerializedName("volume") public abstract Float volume();
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Progress.class)
    public abstract static class Progress {
        @SerializedName("completion") public abstract Float completion();
        @SerializedName("filepos") public abstract Integer filepos();
        @SerializedName("printTime") public abstract Integer printTime();
        @SerializedName("printTimeLeft") public abstract Integer printTimeLeft();
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Offsets.class)
    public abstract static class Offsets {
        @SerializedName("tool0") public abstract Integer tool0();
        @SerializedName("tool1") public abstract Integer tool1();
        @SerializedName("bed") public abstract Integer bed();
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Temps.class)
    public abstract static class Temps {
        @SerializedName("time") public abstract Long time(); // Unix Timestamp (in seconds)
        @SerializedName("tool0") public abstract Tool0 tool0();
        @SerializedName("tool1") public abstract Tool1 tool1();
        @SerializedName("bed") public abstract Bed bed();

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Temps_Tool0.class)
        public abstract static class Tool0 {
            @SerializedName("actual") public abstract Integer actual();
            @SerializedName("target") public abstract Integer target();
            @SerializedName("offset") public abstract Integer offset();
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Temps_Tool1.class)
        public abstract static class Tool1 {
            @SerializedName("actual") public abstract Integer actual();
            @SerializedName("target") public abstract Integer target();
            @SerializedName("offset") public abstract Integer offset();
        }

        @AutoValue
        @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_Temps_Bed.class)
        public abstract static class Bed {
            @SerializedName("actual") public abstract Integer actual();
            @SerializedName("target") public abstract Integer target();
            @SerializedName("offset") public abstract Integer offset();
        }
    }

    @AutoValue
    @AutoGson(autoValueClass = AutoValue_CurrentHistoryEntity_BusyFiles.class)
    public abstract static class BusyFiles {
        @SerializedName("origin") public abstract String origin();
        @SerializedName("name") public abstract String name();
    }
}