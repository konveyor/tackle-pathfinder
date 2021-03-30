package io.tackle.pathfinder.model.assessment;

import io.tackle.commons.entities.AbstractEntity;
import lombok.ToString;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = "assessment_question")
@SQLDelete(sql = "UPDATE assessment_question SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@ToString
public class AssessmentQuestion extends AbstractEntity {
    @Column(name="question_order")
    int order;
    String type;
    String name;
    String tooltip;
    String text;
    String comment;

    @ManyToOne
    AssessmentCategory category;
    
    @OneToMany(mappedBy="question")
    List<AssessmentSingleOption> singleOptions;
}
