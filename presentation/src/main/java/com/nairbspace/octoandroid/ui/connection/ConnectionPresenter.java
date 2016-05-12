package com.nairbspace.octoandroid.ui.connection;

import com.fernandocejas.frodo.annotation.RxLogSubscriber;
import com.nairbspace.octoandroid.domain.interactor.DefaultSubscriber;
import com.nairbspace.octoandroid.domain.interactor.GetConnectionDetails;
import com.nairbspace.octoandroid.domain.model.Connection;
import com.nairbspace.octoandroid.model.ConnectionModel;
import com.nairbspace.octoandroid.model.ConnectionModel.Current;
import com.nairbspace.octoandroid.model.ConnectionModel.Options;
import com.nairbspace.octoandroid.model.mapper.ModelMapper;
import com.nairbspace.octoandroid.ui.UseCasePresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.functions.Action1;

import static com.nairbspace.octoandroid.model.ConnectionModel.PrinterProfile;

public class ConnectionPresenter extends UseCasePresenter<ConnectionScreen> {

    private ConnectionScreen mScreen;
    private final GetConnectionDetails mGetConnectionDetails;
    private final ModelMapper mModelMapper;

    private boolean mIsFirstTime = true;

    @Inject
    public ConnectionPresenter(GetConnectionDetails getConnectionDetails, ModelMapper modelMapper) {
        super(getConnectionDetails);
        mGetConnectionDetails = getConnectionDetails;
        mModelMapper = modelMapper;
    }

    @Override
    protected void onInitialize(ConnectionScreen connectionScreen) {
        mScreen = connectionScreen;
        mGetConnectionDetails.execute(new GetConnectionSubscriber());
    }

    private void renderScreen(ConnectionModel connection) {

        Current current = connection.current();
        String state = current.state();
        boolean isNotConnected = state.equals("Closed");

        Options options = connection.options();
        List<String> ports = options.ports();

        List<Integer> baudrates = options.baudrates();

        List<PrinterProfile> printerProfiles = options.printerProfiles();
        List<String> printerProfileNames = new ArrayList<>();
        for (PrinterProfile printerProfile : printerProfiles) {
            printerProfileNames.add(printerProfile.name());
        }
        mScreen.updateUI(ports, baudrates, printerProfileNames, isNotConnected);

        if (mIsFirstTime) { // TODO need better logic so it doesn't check everytime
            int defaultPortId = 0;
            for (int i = 0; i < ports.size(); i++) {
                if (ports.get(i).equals(options.portPreference())) {
                    defaultPortId = i;
                }
            }

            int defaultBaudrateId = 0;
            for (int i = 0; i < baudrates.size(); i++) {
                if (baudrates.get(i).equals(options.baudratePreference())) {
                    defaultBaudrateId = i;
                }
            }

            int defaultPrinterNameId = 0;
            for (int i = 0; i < printerProfileNames.size(); i++) {
                if (printerProfileNames.get(i).equals(options.printerProfilePreference())) {
                    defaultPrinterNameId = i;
                }
            }

            mScreen.updateUiWithDefaults(defaultPortId, defaultBaudrateId, defaultPrinterNameId);
            mIsFirstTime = true;
        }
    }

    public void connectButtonClicked(String connectButtonText, int portPosition,
                                     int baudratePosition, int printerProfileNamePosition,
                                     boolean isSaveConnectionSettingsChecked,
                                     boolean isAutoConnectChecked) {

    }

    @RxLogSubscriber
    private final class GetConnectionSubscriber extends DefaultSubscriber<Connection> {

        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
        }

        @Override
        public void onNext(Connection connection) {
            mModelMapper.transformObs(connection).subscribe(mRenderScreen);
        }

        private Action1<ConnectionModel> mRenderScreen = new Action1<ConnectionModel>() {
            @Override
            public void call(ConnectionModel connectionModel) {
                renderScreen(connectionModel);
            }
        };
    }
}
