package io.tackle.pathfinder.model.assessment;

import io.tackle.commons.entities.AbstractEntity;
import io.tackle.pathfinder.dto.AssessmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "assessment")
@SQLDelete(sql = "UPDATE assessment SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assessment extends AbstractEntity {
    @Enumerated(value = EnumType.STRING)
    public AssessmentStatus status;

    @Basic(optional = false)
    @Column(name="application_id")
    public Long applicationId;

    @Column(length = 1000)
    public String notes;

    @OneToOne(mappedBy = "assessment")
    public AssessmentQuestionnaire assessmentQuestionnaire;
}
