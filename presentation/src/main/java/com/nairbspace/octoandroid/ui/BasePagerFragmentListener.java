package com.nairbspace.octoandroid.ui;

public abstract class BasePagerFragmentListener<T, L> extends BaseFragmentListener<T, L> {

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getView() != null) {
            if (isVisibleToUser) {
                setPresenter().isVisibleToUser();
            } else {
                setPresenter().isNotVisibleToUser();
            }
        }
    }

    /**
     * {@link #getUserVisibleHint()} might be set to true and view will be null. This will
     * do another check once we know view has been created.
     *
     * Also, since fragment does not update userVisibleHint when onPause is called, we will
     * just call the {@link #setUserVisibleHint(boolean)} with the same hint
     * to trigger the presenter methods already established.
     */
    @Override
    public void onResume() {
        super.onResume();
        if (getUserVisibleHint()) {
            setUserVisibleHint(getUserVisibleHint());
        }
    }

    /**
     * Fragment does not {@link #setUserVisibleHint(boolean)} to false when onPause is called. Must
     * call {@link Presenter#isNotVisibleToUser()} manually on the fragment that is currently
     * visible.
     */
    @Override
    public void onPause() {
        super.onPause();
        if (getUserVisibleHint()) {
            setPresenter().isNotVisibleToUser();
        }
    }
}
