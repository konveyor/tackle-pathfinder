package io.tackle.pathfinder.model.assessment;

import io.tackle.pathfinder.dto.AssessmentDto;
import io.tackle.pathfinder.model.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = "assessment")
@SQLDelete(sql = "UPDATE assessment SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Assessment extends AbstractEntity {
    @Enumerated(value = EnumType.STRING)
    AssessmentDto.Status status;

    @Basic(optional = false)
    @Column(name="application_id")
    Long applicationId;

    String notes;
}
