package io.tackle.pathfinder.model.assessment;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Test extends PanacheEntity {
    String hola;
}
