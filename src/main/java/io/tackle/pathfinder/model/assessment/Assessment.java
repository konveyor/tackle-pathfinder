package io.tackle.pathfinder.model.assessment;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.tackle.pathfinder.dto.AssessmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "assessment")
@SQLDelete(sql = "UPDATE assessment SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment extends PanacheEntity {
    @CreationTimestamp
    @Column(updatable=false)
    public Instant createTime;

    public String createUser;

    @UpdateTimestamp
    public Instant updateTime;

    public String updateUser;

    @Builder.Default
    public Boolean deleted = false;
        
    @Enumerated(value = EnumType.STRING)
    public AssessmentStatus status;

    @Basic(optional = false)
    @Column(name="application_id")
    public Long applicationId;

    @Column(length = 1000)
    public String comment;

    @OneToOne(mappedBy = "assessment", cascade = CascadeType.ALL)
    public AssessmentQuestionnaire assessmentQuestionnaire;

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    @Builder.Default
    public List<AssessmentStakeholder> stakeholders= new ArrayList<>();

    @OneToMany(mappedBy = "assessment", cascade = CascadeType.ALL)
    @Builder.Default
    public List<AssessmentStakeholdergroup> stakeholdergroups = new ArrayList<>();
}
