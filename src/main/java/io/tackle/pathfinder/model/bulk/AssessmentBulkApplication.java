package io.tackle.pathfinder.model.bulk;

import io.tackle.commons.entities.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "assessment_bulk_app")
@SQLDelete(sql = "UPDATE assessment_bulk_app SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentBulkApplication extends AbstractEntity {
    public Long assessmentId;

    @Column(nullable = false)
    public Long applicationId;

    public String error;

    @ManyToOne
    @JoinColumn(name="assessment_bulk_id", referencedColumnName="id")
    public AssessmentBulk assessmentBulk;
}
