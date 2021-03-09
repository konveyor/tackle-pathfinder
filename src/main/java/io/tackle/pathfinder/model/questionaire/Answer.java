package io.tackle.pathfinder.model.questionaire;

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
@Table(name="answer")
@SQLDelete(sql = "UPDATE answer SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Answer extends AbstractEntity {
    String title;
    int order;

    @Enumerated(EnumType.STRING)
    Risk risk;
}
