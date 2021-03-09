package io.tackle.pathfinder.model.assessment;

import io.tackle.pathfinder.model.AbstractEntity;
import io.tackle.pathfinder.model.common.Risk;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "assess_answer")
@SQLDelete(sql = "UPDATE assess_answer SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class AssessAnswer extends AbstractEntity {
    int order;

    @Enumerated(EnumType.STRING)
    Risk risk;

    String answer;
    boolean selected;
}
