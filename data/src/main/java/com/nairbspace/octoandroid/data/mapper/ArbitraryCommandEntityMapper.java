package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.ArbitraryCommandEntity;
import com.nairbspace.octoandroid.domain.model.ArbitraryCommand;

public class ArbitraryCommandEntityMapper {

    public static Object mapToArbitraryCommandEntity(ArbitraryCommand arbitraryCommand) {
        Object o = null;
        switch (arbitraryCommand.type()) {
            case MOTORS_OFF:
                o = ArbitraryCommandEntity.Single.motorsOff();
                break;
            case FAN_ON:
                o = ArbitraryCommandEntity.Single.fanOn();
                break;
            case FAN_OFF:
                o = ArbitraryCommandEntity.Single.fanOff();
                break;
            case SINGLE:
                o = ArbitraryCommandEntity.Single.create(arbitraryCommand.command());
                break;
            case MULTIPLE:
                o = ArbitraryCommandEntity.Multiple.create(arbitraryCommand.commands());
                break;
        }
        return o;
    }
}
