package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.model.AddPrinter;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class AddPrinterDetailsTest {

    private AddPrinterDetails mAddPrinterDetails;
    private AddPrinter mAddPrinter = AddPrinter.builder()
            .accountName("")
            .ipAddress("")
            .port("")
            .apiKey("")
            .isSslChecked(true)
            .build();

    @Mock private PrinterRepository mockPrinterRepository;
    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mAddPrinterDetails = new AddPrinterDetails(mockThreadExecutor,
                mockPostExecutionThread, mockPrinterRepository);
    }

    @Test
    public void testAddPrinterDetailsUseCaseInputObservable() {
        mAddPrinterDetails.buildUseCaseObservableInput(mAddPrinter);

        verify(mockPrinterRepository).addPrinterDetails(mAddPrinter);
        verifyNoMoreInteractions(mockPrinterRepository);
        verifyNoMoreInteractions(mockThreadExecutor);
        verifyNoMoreInteractions(mockPostExecutionThread);
    }
}
