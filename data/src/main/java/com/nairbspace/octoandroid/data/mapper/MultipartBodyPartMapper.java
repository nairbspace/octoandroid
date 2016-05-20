package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.exception.EntityMapperException;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;

import javax.inject.Inject;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.Observable;
import rx.Subscriber;

public class MultipartBodyPartMapper {

    private static final String MULTIPART_CONTENT = "multipart/form-data";
    private static final String FILE = "file";

    @Inject
    public MultipartBodyPartMapper() {
    }

    public Observable.OnSubscribe<MultipartBody.Part> mapToMultiPartBodyPart(final String uriString) {
        return new Observable.OnSubscribe<MultipartBody.Part>() {
            @Override
            public void call(Subscriber<? super MultipartBody.Part> subscriber) {
                try {
                    URI uri = new URI(uriString);
                    File file = new File(uri.getPath());
                    RequestBody requestFile = RequestBody.create(MediaType.parse(MULTIPART_CONTENT), file);

                    MultipartBody.Part body =
                            MultipartBody.Part.createFormData(FILE, file.getName(), requestFile);

                    subscriber.onNext(body);
                    subscriber.onCompleted();
                } catch (URISyntaxException e) {
                    subscriber.onError(new EntityMapperException(e));
                }
            }
        };
    }
}
