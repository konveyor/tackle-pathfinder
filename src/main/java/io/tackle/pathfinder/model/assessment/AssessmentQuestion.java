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
@Table(name = "assessment_question")
@SQLDelete(sql = "UPDATE assessment_question SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@ToString
public class AssessmentQuestion extends AbstractEntity {
    @Column(name="question_order", nullable = false)
    int order;

    @Column(nullable = false)
    String type;

    @Column(nullable = false)
    String name;

    @Column(length = 1000 )
    String tooltip;

    @Column(length = 500 )
    String questionText;

    @Column(length = 1000)
    String comment;

    @ManyToOne(optional = false)
    @JoinColumn(name="category_id", nullable = false)
    AssessmentCategory category;

    @OneToMany(mappedBy="question")
    List<AssessmentSingleOption> singleOptions;
}
