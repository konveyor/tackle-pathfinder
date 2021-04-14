package io.tackle.pathfinder.model.assessment;

import io.tackle.commons.entities.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessment_question")
@SQLDelete(sql = "UPDATE assessment_question SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentQuestion extends AbstractEntity {
    @Column(name="question_order", nullable = false)
    public int order;

    @Column(nullable = false)
    public String type;

    @Column(nullable = false)
    public String name;

    @Column(length = 1000 )
    public String description;

    @Column(name="question_text", length = 500 )
    public String questionText;

    @OneToMany(mappedBy="question", cascade = CascadeType.REMOVE)
    @Builder.Default
    public List<AssessmentSingleOption> singleOptions=new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name="assessment_category_id", referencedColumnName="id", nullable = false)
    public AssessmentCategory category;
}
