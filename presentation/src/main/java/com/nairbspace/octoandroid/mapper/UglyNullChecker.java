package com.nairbspace.octoandroid.mapper;

import com.nairbspace.octoandroid.domain.model.Websocket;

@SuppressWarnings("ConstantConditions")
public class UglyNullChecker {

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

    public boolean isCompletionNotNull(Websocket websocket) {
        return isProgressNotNull(websocket) && websocket.current().progress().completion() != null;
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

    public boolean isStateFlagsNotNull(Websocket websocket) {
        return isStateNotNull(websocket) && websocket.current().state().flags() != null;
    }

    public boolean isTempsListNotNull(Websocket websocket) {
        return isCurrentNotNull(websocket) && websocket.current().temps() != null;
    }

    public boolean isTempsListNotEmpty(Websocket websocket) {
        return isTempsListNotNull(websocket) && websocket.current().temps().size() > 0;
    }

    public boolean isTempNotNull(Websocket websocket) {
        return isTempsListNotEmpty(websocket) && websocket.current().temps().get(0) != null;
    }

    public boolean isTempTimeNotNull(Websocket websocket) {
        return isTempNotNull(websocket) && websocket.current().temps().get(0).time() != null;
    }

    public boolean isTempBedNotNull(Websocket websocket) {
        return isTempNotNull(websocket) && websocket.current().temps().get(0).bed() != null;
    }

    public boolean isActualTempBedNotNull(Websocket websocket) {
        return isTempBedNotNull(websocket) && websocket.current().temps().get(0).bed().actual() != null;
    }

    public boolean isTargetTempBedNotNull(Websocket websocket) {
        return isTempBedNotNull(websocket) && websocket.current().temps().get(0).bed().target() != null;
    }

    public boolean isTempTool0NotNull(Websocket websocket) {
        return isTempNotNull(websocket) && websocket.current().temps().get(0).tool0() != null;
    }

    public boolean isActualTempTool0NotNull(Websocket websocket) {
        return isTempTool0NotNull(websocket) && websocket.current().temps().get(0).tool0().actual() != null;
    }

    public boolean isTargetTempTool0NotNull(Websocket websocket) {
        return isTempTool0NotNull(websocket) && websocket.current().temps().get(0).tool0().target() != null;
    }

    public boolean isTempTool1NotNull(Websocket websocket) {
        return isTempNotNull(websocket) && websocket.current().temps().get(0).tool1() != null;
    }

    public boolean isActualTempTool1NotNull(Websocket websocket) {
        return isTempTool1NotNull(websocket) && websocket.current().temps().get(0).tool1().actual() != null;
    }

    public boolean isTargetTempTool1NotNull(Websocket websocket) {
        return isTempTool1NotNull(websocket) && websocket.current().temps().get(0).tool1().target() != null;
    }
}
