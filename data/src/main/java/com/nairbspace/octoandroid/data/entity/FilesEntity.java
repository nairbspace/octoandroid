package com.nairbspace.octoandroid.data.entity;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class FilesEntity {
    @SerializedName("files") public abstract List<File> files();
    @SerializedName("free") public abstract Integer free();
    @SerializedName("total") public abstract Integer total();

    @AutoValue
    public abstract static class File {
        @SerializedName("name") public abstract String name();
        @SerializedName("origin") public abstract String origin();
        @SerializedName("refs") public abstract Refs refs();
        @SerializedName("type") public abstract String type();
        @Nullable @SerializedName("size") public abstract Integer size();
        @Nullable @SerializedName("date") public abstract Integer date();
        @Nullable @SerializedName("gcodeAnalysis") public abstract GcodeAnalysis gcodeAnalysis();
        @Nullable @SerializedName("print") public abstract Print print();

        @AutoValue
        public abstract static class Refs {
            @SerializedName("resource") public abstract String resource();
            @Nullable @SerializedName("download") public abstract String download();

        }

        @AutoValue
        public abstract static class GcodeAnalysis {
            @Nullable @SerializedName("estimatedPrintTime") public abstract Integer estimatedPrintTime();
            @Nullable @SerializedName("filament") public abstract Filament filament();

            @AutoValue
            public abstract static class Filament {
                @Nullable @SerializedName("length") public abstract Integer length();
                @Nullable @SerializedName("volume") public abstract Double volume();
            }
        }

        @AutoValue
        public abstract static class Print {
            @Nullable @SerializedName("failure") public abstract Integer failure();
            @Nullable @SerializedName("success") public abstract Integer success();
            @Nullable @SerializedName("last") public abstract Last last();

            @AutoValue
            public abstract static class Last {
                @Nullable @SerializedName("date") public abstract Integer date();
                @Nullable @SerializedName("success") public abstract Boolean success();

            }
        }
    }
}
