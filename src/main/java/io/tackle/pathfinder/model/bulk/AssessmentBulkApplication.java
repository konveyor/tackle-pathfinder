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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "assessment_bulk_app")
@SQLDelete(sql = "UPDATE assessment_bulk_app SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentBulkApplication extends PanacheEntity {
    @CreationTimestamp
    @Column(updatable=false)
    public Instant createTime;

    @UpdateTimestamp
    public Instant updateTime;

    public String createUser;
    public String updateUser;

    public Boolean deleted = false;

    public Long assessmentId;

    @Column(nullable = false)
    public Long applicationId;

    public String error;

    @ManyToOne
    @JoinColumn(name="assessment_bulk_id", referencedColumnName="id")
    public AssessmentBulk assessmentBulk;
}
