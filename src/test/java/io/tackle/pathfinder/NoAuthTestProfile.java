package io.tackle.pathfinder;

public class NoAuthTestProfile extends PathfinderTestProfile {

    @Override
    public String getConfigProfile() {
        return "test,noauth";
    }

}
