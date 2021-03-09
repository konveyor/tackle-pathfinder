package io.tackle.pathfinder.model.external;

import io.tackle.pathfinder.model.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="stakeholder")
@SQLDelete(sql = "UPDATE stakeholder SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Stakeholder extends AbstractEntity {
    String name;
}
