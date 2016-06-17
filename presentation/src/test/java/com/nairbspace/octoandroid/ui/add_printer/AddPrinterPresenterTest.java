package com.nairbspace.octoandroid.ui.add_printer;

import android.content.Context;

import com.nairbspace.octoandroid.domain.interactor.AddPrinterDetails;
import com.nairbspace.octoandroid.domain.interactor.VerifyPrinterDetails;
import com.nairbspace.octoandroid.mapper.AddPrinterModelMapper;
import com.nairbspace.octoandroid.model.AddPrinterModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Subscriber;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AddPrinterPresenterTest {

    private AddPrinterPresenter mAddPrinterPresenter;

    @Mock VerifyPrinterDetails mVerifyPrinterDetails;
    @Mock AddPrinterModelMapper mAddPrinterModelMapper;
    @Mock AddPrinterDetails mAddPrinterDetails;
    @Mock AddPrinterScreen mScreen;
    @Mock Context mContext;

    private AddPrinterModel mAddPrinterModel = AddPrinterModel.builder()
            .accountName("")
            .apiKey("")
            .ipAddress("")
            .isSslChecked(false)
            .port("")
            .build();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mAddPrinterPresenter = new AddPrinterPresenter(
                mVerifyPrinterDetails, mAddPrinterModelMapper, mAddPrinterDetails);
        mAddPrinterPresenter.onInitialize(mScreen);
    }

    @Test
    public void testOnAddPrinterClicked() {
        mAddPrinterPresenter.onAddPrinterClicked(mAddPrinterModel);
        verify(mAddPrinterModelMapper).execute(any(Subscriber.class), any(AddPrinterModel.class));
        noInteractions();
    }

    @Test
    public void testContext() {
        given(mScreen.context()).willReturn(mContext);
    }

    private void noInteractions() {
        verifyNoMoreInteractions(mAddPrinterModelMapper);
        verifyNoMoreInteractions(mVerifyPrinterDetails);
        verifyNoMoreInteractions(mAddPrinterDetails);
        verifyNoMoreInteractions(mScreen);
    }
}
