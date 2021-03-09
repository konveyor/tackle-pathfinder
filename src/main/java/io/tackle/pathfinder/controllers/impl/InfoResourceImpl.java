package io.tackle.pathfinder.controllers.impl;

import io.tackle.pathfinder.controllers.InfoResource;
import io.tackle.pathfinder.dto.Info;

import java.time.LocalDate;

public class InfoResourceImpl implements InfoResource {

    @Override
    public io.tackle.pathfinder.dto.Info getinfo() {
        return Info.builder().date(LocalDate.now());
    }
    
}
