package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;
import com.nairbspace.octoandroid.domain.repository.PrinterRepository;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

public class GetPrinterDetailsTest {

    private GetPrinterDetails mGetPrinterDetails;

    @Mock private PrinterRepository mockPrinterRepository;
    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mGetPrinterDetails = new GetPrinterDetails(mockPrinterRepository,
                mockThreadExecutor, mockPostExecutionThread);
    }

    @Test
    public void testGetPrinterDetailsUseCaseObservable() {
        mGetPrinterDetails.buildUseCaseObservable();

        verify(mockPrinterRepository).printerDetails();
        verifyNoMoreInteractions(mockPrinterRepository);
        verifyNoMoreInteractions(mockThreadExecutor);
        verifyNoMoreInteractions(mockPostExecutionThread);
    }
}
