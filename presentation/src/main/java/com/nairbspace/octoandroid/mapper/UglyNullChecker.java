package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.model.Websocket;

import javax.inject.Inject;

@SuppressWarnings("ConstantConditions")
public class UglyNullChecker {

    @Inject
    public UglyNullChecker() {
        // TODO not sure if should implement null checker this way
    }

    public boolean isWebSocketNotNull(Websocket websocket) {
        return websocket != null;
    }

    public boolean isCurrentNotNull(Websocket websocket) {
        return isWebSocketNotNull(websocket) && websocket.current() != null;
    }

    public boolean isStateNotNull(Websocket websocket) {
        return isCurrentNotNull(websocket) && websocket.current().state() != null;
    }

    public boolean isJobNotNull(Websocket websocket) {
        return isCurrentNotNull(websocket) && websocket.current().job() != null;
    }

    public boolean ifStateTextNotNull(Websocket websocket) {
        return isStateNotNull(websocket) && websocket.current().state().text() != null;
    }

    public boolean isFileNotNull(Websocket websocket) {
        return isJobNotNull(websocket) && websocket.current().job().file() != null;
    }

    public boolean isFileNameNotNull(Websocket websocket) {
        return isFileNotNull(websocket) && websocket.current().job().file().name() != null;
    }

    public boolean isApproxTotalPrintTimeIsNotNull(Websocket websocket) {
        return isJobNotNull(websocket) && websocket.current().job().estimatedPrintTime() != null;
    }

    public boolean isProgressNotNull(Websocket websocket) {
        return isCurrentNotNull(websocket) && websocket.current().progress() != null;
    }

    public boolean isPrintTimeIsNotNull(Websocket websocket) {
        return isProgressNotNull(websocket) && websocket.current().progress().printTime() != null;
    }

    public boolean isPrintTimeLeftIsNotNull(Websocket websocket) {
        return isProgressNotNull(websocket) && websocket.current().progress().printTimeLeft() != null;
    }

    public boolean isPrintedBytesIsNotNull(Websocket websocket) {
        return isProgressNotNull(websocket) && websocket.current().progress().filepos() != null;
    }

    public boolean isPrintedFileSizeIsNotNull(Websocket websocket) {
        return isFileNotNull(websocket) && websocket.current().job().file().size() != null;
    }
}
