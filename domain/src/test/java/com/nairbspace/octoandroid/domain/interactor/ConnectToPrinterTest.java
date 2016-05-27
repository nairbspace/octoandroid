package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.Connect;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class ConnectToPrinterTest {

    private ConnectToPrinter mConnectToPrinter;
    private Connect mConnect = Connect.builder()
            .isNotConnected(true)
            .ports(new ArrayList<String>())
            .baudrates(new ArrayList<Integer>())
            .printerProfiles(new HashMap<String, String>())
            .selectedPortId(0)
            .selectedBaudrateId(0)
            .selectedPrinterProfileId(0)
            .isSaveConnectionChecked(true)
            .isAutoConnectChecked(true)
            .build();

    @Mock private PrinterRepository mockPrinterRepository;
    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mConnectToPrinter = new ConnectToPrinter(mockThreadExecutor,
                mockPostExecutionThread, mockPrinterRepository);
    }

    @Test
    public void testAddPrinterDetailsUseCaseInputObservable() {
        mConnectToPrinter.buildUseCaseObservableInput(mConnect);

        verify(mockPrinterRepository).connectToPrinter(mConnect);
        verifyNoMoreInteractions(mockPrinterRepository);
        verifyNoMoreInteractions(mockThreadExecutor);
        verifyNoMoreInteractions(mockPostExecutionThread);
    }
}
