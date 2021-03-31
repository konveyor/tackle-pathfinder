package io.tackle.pathfinder.model.assessment;

import io.tackle.commons.entities.AbstractEntity;
import io.tackle.pathfinder.model.Risk;
import lombok.ToString;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "assessment_singleoption")
@SQLDelete(sql = "UPDATE assessment_singleoption SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@ToString
public class AssessmentSingleOption extends AbstractEntity {
    @Column(name="singleoption_order", nullable = false)
    int order;

    @Basic(optional = false)
    @Enumerated(value = EnumType.STRING)
    Risk risk;

    @Column(length = 500, nullable = false)
    String option;

    @JoinColumn(name="question_id", nullable=false)
    @ManyToOne
    AssessmentQuestion question;

    @Basic(optional = false)
    boolean selected;
}
