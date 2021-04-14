package io.tackle.pathfinder.model.assessment;

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
@Table(name = "assessment_stakeholdergroup")
@SQLDelete(sql = "UPDATE assessment_stakeholdergroup SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentStakeholdergroup extends AbstractEntity {
    @Column(name="stakeholdergroup_id", nullable=false)
    public Long stakeholdergroupId;
    
    @ManyToOne
    @JoinColumn(name="assessment_id", referencedColumnName="id")
    public Assessment assessment;
}
