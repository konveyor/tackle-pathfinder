package io.tackle.pathfinder.model.assessment;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.tackle.pathfinder.model.QuestionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessment_question")
@SQLDelete(sql = "UPDATE assessment_question SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentQuestion extends PanacheEntity {
    @CreationTimestamp
    @Column(updatable=false)
    public Instant createTime;

    public String createUser;

    @UpdateTimestamp
    public Instant updateTime;

    public String updateUser;

    @Builder.Default
    public Boolean deleted = false;
    
    @Column(name="question_order", nullable = false)
    public int order;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    public QuestionType type;

    @Column(nullable = false)
    public String name;

    @Column(length = 1000 )
    public String description;

    @Column(name="question_text", length = 500 )
    public String questionText;

    @OneToMany(mappedBy="question", cascade = CascadeType.ALL)
    @Builder.Default
    public List<AssessmentSingleOption> singleOptions=new ArrayList<>();

    @ManyToOne(optional = false)
    @JoinColumn(name="assessment_category_id", referencedColumnName="id", nullable = false)
    public AssessmentCategory category;

    public Long questionnaire_questionId;

}
