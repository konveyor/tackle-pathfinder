package io.tackle.pathfinder.model.assessment;

import io.tackle.commons.entities.AbstractEntity;
import lombok.ToString;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = "assessment_category")
@SQLDelete(sql = "UPDATE assessment_category SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@ToString
public class AssessmentCategory extends AbstractEntity {
    @Column(name="category_order")
    int order;
    String name;

    @ManyToOne
    @JoinColumn(name="questionnaireId")
    AssessmentQuestionnaire questionnaire;

    @OneToMany(mappedBy = "category")
    List<AssessmentQuestion> questions;
}
