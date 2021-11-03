package io.tackle.pathfinder.model.bulk;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "assessment_bulk")
@SQLDelete(sql = "UPDATE assessment_bulk SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentBulk extends PanacheEntity {
    @CreationTimestamp
    @Column(updatable=false)
    public Instant createTime;

    @UpdateTimestamp
    public Instant updateTime;

    public String createUser;
    public String updateUser;

    @Builder.Default
    public Boolean deleted = false;

    @Column(nullable=false, columnDefinition = " boolean default false")
    @Builder.Default
    public boolean completed = false;

    public Long fromAssessmentId;

    @Builder.Default
    @OneToMany(mappedBy = "assessmentBulk", cascade = CascadeType.ALL)
    public List<AssessmentBulkApplication> bulkApplications = new ArrayList<>();
}
