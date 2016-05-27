package com.nairbspace.octoandroid.domain.exception;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class DefaultErrorBundleTest {
    private DefaultErrorBundle mDefaultErrorBundle;

    @Mock private Exception mockException;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mDefaultErrorBundle = new DefaultErrorBundle(mockException);
    }

    @Test
    public void testGetErrorMessageInteraction() {
        mDefaultErrorBundle.getErrorMessage();

        Mockito.verify(mockException).getMessage();
    }
}
