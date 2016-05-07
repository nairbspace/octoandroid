package com.nairbspace.octoandroid.data.net.rest.model;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class Files {
    @SerializedName("files") public abstract List<File> files();
    @SerializedName("free") public abstract Integer free();
    @SerializedName("total") public abstract Integer total();

    @AutoValue
    public abstract static class File {
        @SerializedName("name") public abstract String name();
        @SerializedName("size") public abstract Integer size();
        @SerializedName("date") public abstract Integer date();
        @SerializedName("origin") public abstract String origin();
        @SerializedName("refs") public abstract Refs refs();
        @SerializedName("gcodeAnalysis") public abstract GcodeAnalysis gcodeAnalysis();
        @SerializedName("print") public abstract Print print();

        @AutoValue
        public abstract static class Refs {
            @SerializedName("resource") public abstract String resource();
            @SerializedName("download") public abstract String download();

        }

        @AutoValue
        public abstract static class GcodeAnalysis {
            @SerializedName("estimatedPrintTime") public abstract Integer estimatedPrintTime();
            @SerializedName("filament") public abstract Filament filament();

            @AutoValue
            public abstract static class Filament {
                @SerializedName("length") public abstract Integer length();
                @SerializedName("volume") public abstract Double volume();
            }
        }

        @AutoValue
        public abstract static class Print {
            @SerializedName("failure") public abstract Integer failure();
            @SerializedName("success") public abstract Integer success();
            @SerializedName("last") public abstract Last last();

            @AutoValue
            public abstract static class Last {
                @SerializedName("date") public abstract Integer date();
                @SerializedName("success") public abstract Boolean success();

            }
        }
    }
}
