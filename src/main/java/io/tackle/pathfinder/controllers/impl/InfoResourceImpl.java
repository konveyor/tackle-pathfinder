package io.tackle.pathfinder.controllers.impl;

import io.tackle.pathfinder.controllers.InfoResource;
import io.tackle.pathfinder.dto.InfoDto;

import javax.inject.Named;

import java.time.Instant;

public class InfoResourceImpl implements InfoResource {
    @Named("startup")
    Instant startup;

    @Override
    public io.tackle.pathfinder.dto.InfoDto getinfo() {
        return InfoDto.builder()
        .date(Instant.now())
        .uptime(Instant.now().toEpochMilli() - startup.toEpochMilli())
        .build();
    }

}
