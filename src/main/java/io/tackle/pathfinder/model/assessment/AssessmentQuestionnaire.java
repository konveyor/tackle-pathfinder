package io.tackle.pathfinder.model.assessment;

import io.tackle.commons.entities.AbstractEntity;
import io.tackle.pathfinder.model.questionnaire.Questionnaire;
import lombok.ToString;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = "assessment_questionnaire")
@SQLDelete(sql = "UPDATE assessment_questionnaire SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@ToString
public class AssessmentQuestionnaire extends AbstractEntity {
    @Column(name="language_code", nullable = false)
    String languageCode = "EN";

    @Basic(optional = false)
    String name;

    @OneToOne
    @JoinColumn(name = "assessment_id", nullable = false)
    Assessment assessment;

    @OneToMany(mappedBy="questionnaire")
    List<AssessmentCategory> categories;

    @ManyToOne
    @JoinColumn(name="questionnaire_id", nullable = false)
    Questionnaire questionnaire;
}
