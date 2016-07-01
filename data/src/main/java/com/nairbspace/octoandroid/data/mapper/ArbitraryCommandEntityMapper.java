package com.nairbspace.octoandroid.data.mapper;

import com.nairbspace.octoandroid.data.entity.ArbitraryCommandEntity;
import com.nairbspace.octoandroid.domain.model.ArbitraryCommand;

public class ArbitraryCommandEntityMapper {

    public static ArbitraryCommandEntity mapToArbitraryCommandEntity(ArbitraryCommand arbitraryCommand) {
        ArbitraryCommandEntity entity = null;
        switch (arbitraryCommand.type()) {
            case MOTORS_OFF:
                entity = ArbitraryCommandEntity.motorsOff();
                break;
            case FAN_ON:
                entity = ArbitraryCommandEntity.fanOn();
                break;
            case FAN_OFF:
                entity = ArbitraryCommandEntity.fanOff();
                break;
            case SINGLE:
                entity = ArbitraryCommandEntity.createSingle(arbitraryCommand.command());
                break;
            case MULTIPLE:
                entity = ArbitraryCommandEntity.create(arbitraryCommand.commands());
                break;
        }
        return entity;
    }
}
