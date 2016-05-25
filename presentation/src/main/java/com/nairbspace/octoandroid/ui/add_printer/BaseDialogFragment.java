package com.nairbspace.octoandroid.ui.add_printer;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

import com.nairbspace.octoandroid.ui.templates.BaseActivity;
import com.nairbspace.octoandroid.ui.Navigator;

import butterknife.Unbinder;

public abstract class BaseDialogFragment<L> extends DialogFragment {

    private L mListener;
    private Unbinder mUnbinder;

    @NonNull
    protected abstract L setListener();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mListener = setListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mListener != null) {
            mListener = null;
        }
    }

    protected void setUnbinder(Unbinder unbinder) {
        mUnbinder = unbinder;
    }

    protected Navigator getNavigator() {
        try {
            return ((BaseActivity) getActivity()).getNavigator();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString() +
                    " should extend from BaseActivity");
        }
    }
}
