package com.nairbspace.octoandroid.data.net;

import com.nairbspace.octoandroid.data.db.PrinterDbEntity;
import com.nairbspace.octoandroid.data.disk.PrefHelper;
import com.nairbspace.octoandroid.data.entity.ConnectEntity;
import com.nairbspace.octoandroid.data.entity.ConnectionEntity;
import com.nairbspace.octoandroid.data.entity.FileCommandEntity;
import com.nairbspace.octoandroid.data.entity.FilesEntity;
import com.nairbspace.octoandroid.data.entity.PrinterStateEntity;
import com.nairbspace.octoandroid.data.entity.VersionEntity;
import com.nairbspace.octoandroid.domain.model.TempCommand;
import com.nairbspace.octoandroid.domain.model.TempCommand.ToolBedOffsetTemp;

import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.MultipartBody;
import retrofit2.http.Body;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;
import rx.functions.Func1;

@Singleton
public class ApiManagerImpl implements ApiManager {
    private static final String TOOL_PATH = "tool";
    private static final String BED_PATH = "bed";

    private final OctoApi mOctoApi;
    private final PrefHelper mPrefHelper;

    @Inject
    public ApiManagerImpl(OctoApi octoApi, PrefHelper prefHelper) {
        mOctoApi = octoApi;
        mPrefHelper = prefHelper;
    }

    @Override
    public Observable<VersionEntity> getVersion() {
        return mOctoApi.getVersion();
    }

    @Override
    public Observable<ConnectionEntity> getConnection() {
        return mOctoApi.getConnection();
    }

    @Override
    public Observable<Object> postConnect(@Body ConnectEntity connectEntity) {
        return mOctoApi.postConnect(connectEntity);
    }

    @Override
    public Observable<PrinterStateEntity> getPrinter() {
        return mOctoApi.getPrinter();
    }

    @Override
    public Observable<FilesEntity> getAllFiles() {
        return mOctoApi.getAllFiles();
    }

    @Override
    public Observable<Object> sendJobCommand(@Body HashMap<String, String> command) {
        return mOctoApi.sendJobCommand(command);
    }

    @Override
    public Observable<Object> startFilePrint(@Url String url, @Body FileCommandEntity fileCommandEntity) {
        return mOctoApi.startFilePrint(url, fileCommandEntity);
    }

    @Override
    public Observable<Object> deleteFile(@Url String url) {
        return mOctoApi.deleteFile(url);
    }

    @Override
    public Observable<Object> uploadFile(@Path("location") String location, @Part MultipartBody.Part file) {
        return mOctoApi.uploadFile(location, file);
    }

    @Override
    public Observable<Object> sendToolOrBedTempCommand(@Path("toolOrBed") String toolOrBed, @Body Object object) {
        return mOctoApi.sendToolOrBedTempCommand(toolOrBed, object);
    }

    @Override
    public Observable<Object> sendPrintHeadCommand(@Body Object object) {
        return mOctoApi.sendPrintHeadCommand(object);
    }

    @Override
    public Observable<Object> selectTool(@Body Object object) {
        return mOctoApi.selectTool(object);
    }

    @Override
    public Observable<Object> sendArbitraryCommand(@Body Object object) {
        return mOctoApi.sendArbitraryCommand(object);
    }

    @Override
    public Func1<PrinterDbEntity, Observable<VersionEntity>> funcGetVersion() {
        return new Func1<PrinterDbEntity, Observable<VersionEntity>>() {
            @Override
            public Observable<VersionEntity> call(PrinterDbEntity printerDbEntity) {
                return getVersion();
            }
        };
    }

    @Override
    public Func1<ConnectionEntity, Observable<ConnectionEntity>> funcGetConnection() {
        return new Func1<ConnectionEntity, Observable<ConnectionEntity>>() {
            @Override
            public Observable<ConnectionEntity> call(ConnectionEntity connectionEntity) {
                return getConnection();
            }
        };
    }

    @Override
    public Func1<ConnectEntity, Observable<?>> connectToPrinter() {
        return new Func1<ConnectEntity, Observable<?>>() {
            @Override
            public Observable<?> call(ConnectEntity connectEntity) {
                return postConnect(connectEntity);
            }
        };
    }

    @Override
    public Func1<FileCommandEntity, Observable<?>> funcStartFilePrint(final String apiUrl) {
        return new Func1<FileCommandEntity, Observable<?>>() {
            @Override
            public Observable<?> call(FileCommandEntity fileCommandEntity) {return startFilePrint(apiUrl, fileCommandEntity);
            }
        };
    }

    @Override
    public Func1<MultipartBody.Part, Observable<?>> funcUploadFile() {
        final String location = mPrefHelper.getUploadLocation();
        return new Func1<MultipartBody.Part, Observable<?>>() {
            @Override
            public Observable call(MultipartBody.Part part) {
                return uploadFile(location, part);
            }
        };
    }

    @Override
    public Func1<Object, Observable<?>> funcSendToolOrBedCommand(final TempCommand tempCommand) {
        return new Func1<Object, Observable<?>>() {
            @Override
            public Observable<?> call(Object object) {
                if (tempCommand.toolBedOffsetTemp() == ToolBedOffsetTemp.OFFSET_BED ||
                        tempCommand.toolBedOffsetTemp() == ToolBedOffsetTemp.TARGET_BED) {
                    return sendToolOrBedTempCommand(BED_PATH, object);
                } else {
                    return sendToolOrBedTempCommand(TOOL_PATH, object);
                }
            }
        };
    }

    @Override
    public Func1<Object, Observable<?>> funcSendPrintHeadCommand() {
        return new Func1<Object, Observable<?>>() {
            @Override
            public Observable<?> call(Object o) {
                return sendPrintHeadCommand(o); // TODO-low should probably check if instance of PrintHeadCommandEntity
            }
        };
    }

    @Override
    public Func1<Object, Observable<?>> funcSendToolCommand() {
        return new Func1<Object, Observable<?>>() {
            @Override
            public Observable call(Object o) {
                return selectTool(o);
            }
        };
    }
}
