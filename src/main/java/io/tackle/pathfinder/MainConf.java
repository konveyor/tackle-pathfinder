package io.tackle.pathfinder;

import javax.enterprise.inject.Produces;
import javax.inject.Named;
import javax.inject.Singleton;

import java.time.Instant;

public class MainConf {

    @Produces
    @Singleton
    @Named("startup")
    Instant startTime() {
        return Instant.now();
    }
}
