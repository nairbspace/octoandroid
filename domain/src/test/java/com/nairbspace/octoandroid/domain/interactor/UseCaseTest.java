package com.nairbspace.octoandroid.domain.interactor;

import com.nairbspace.octoandroid.domain.executor.PostExecutionThread;
import com.nairbspace.octoandroid.domain.executor.ThreadExecutor;

import org.hamcrest.MatcherAssert;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import rx.Observable;
import rx.Subscriber;
import rx.observers.TestSubscriber;
import rx.schedulers.TestScheduler;

public class UseCaseTest {

    private UseCaseTestClass mUseCase;

    @Mock private ThreadExecutor mockThreadExecutor;
    @Mock private PostExecutionThread mockPostExecutionThread;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        mUseCase = new UseCaseTestClass(mockThreadExecutor, mockPostExecutionThread);
    }

    @Test
    public void testBuildUseCaseObservableReturnCorrectResult() {
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();
        TestScheduler testScheduler = new TestScheduler();
        BDDMockito.given(mockPostExecutionThread.getScheduler()).willReturn(testScheduler);

        mUseCase.execute(testSubscriber);

        MatcherAssert.assertThat(testSubscriber.getOnNextEvents().size(), Is.is(0));
    }

    @Test
    public void testSubscriptionWhenExecutingUseCase() {
        TestSubscriber<Integer> testSubscriber = new TestSubscriber<>();

        mUseCase.execute(testSubscriber);
        mUseCase.unsubscribe();

        MatcherAssert.assertThat(testSubscriber.isUnsubscribed(), Is.is(true));
    }

    private static class UseCaseTestClass extends UseCase {

        public UseCaseTestClass(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
            super(threadExecutor, postExecutionThread);
        }

        @Override
        protected Observable buildUseCaseObservable() {
            return Observable.empty();
        }

        @Override
        public void execute(Subscriber useCaseSubscriber) {
            super.execute(useCaseSubscriber);
        }
    }
}
