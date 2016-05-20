package com.nairbspace.octoandroid.model;

import android.os.Parcelable;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class FilesModel implements Parcelable {
    public abstract List<FileModel> fileModels();
    public abstract String free();
    public abstract String total();

    public static Builder builder() {
        return new AutoValue_FilesModel.Builder();
    }

    @AutoValue.Builder
    public static abstract class Builder {
        public abstract Builder fileModels(List<FileModel> fileModels);
        public abstract Builder free(String free);
        public abstract Builder total(String total);
        public abstract FilesModel build();
    }

    @AutoValue
    public static abstract class FileModel implements Parcelable {
        public abstract String name();
        public abstract String size(); // Need to format to human readable
        public abstract String date();
        public abstract String time();
        public abstract String origin();
        public abstract String apiUrl();
        public abstract String downloadUrl();
        public abstract String estimatedPrintTime(); // Should format this
        public abstract String type(); // if its a folder then have to ignore
        public static Builder builder() {
            return new AutoValue_FilesModel_FileModel.Builder();
        }

        @AutoValue.Builder
        public static abstract class Builder {
            public abstract Builder name(String name);
            public abstract Builder size(String size);
            public abstract Builder date(String date);
            public abstract Builder time(String time);
            public abstract Builder origin(String origin);
            public abstract Builder apiUrl(String apiUrl);
            public abstract Builder downloadUrl(String downloadUrl);
            public abstract Builder estimatedPrintTime(String estimatedPrintTime);
            public abstract Builder type(String type);
            public abstract FileModel build();
        }
    }
}
