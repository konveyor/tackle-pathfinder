package io.tackle.pathfinder.model.assessment;

import io.tackle.pathfinder.model.AbstractEntity;
import io.tackle.pathfinder.model.external.Stakeholder;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="assess_stakeholder")
@SQLDelete(sql = "UPDATE assess_stakeholder SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class AssessStakeholder extends AbstractEntity {
    Stakeholder stakeholder;
}
