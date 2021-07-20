package io.tackle.pathfinder.model.assessment;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessment_category")
@SQLDelete(sql = "UPDATE assessment_category SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentCategory extends PanacheEntity {
    @CreationTimestamp
    @Column(updatable=false)
    public Instant createTime;

    public String createUser;

    @UpdateTimestamp
    public Instant updateTime;

    public String updateUser;

    @Builder.Default
    public Boolean deleted = false;
    
    @Column(name="category_order")
    public int order;

    public String name;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    @Builder.Default
    public List<AssessmentQuestion> questions=new ArrayList<>();

    public String comment;

    @ManyToOne
    @JoinColumn(name="assessment_questionnaire_id", referencedColumnName="id")
    public AssessmentQuestionnaire questionnaire;

    public Long questionnaire_categoryId;
}
