package io.tackle.pathfinder.model.assessment;

import io.tackle.commons.entities.AbstractEntity;
import io.tackle.pathfinder.dto.AssessmentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Entity
@Table(name = "assessment")
@SQLDelete(sql = "UPDATE assessment SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
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
    public String comment;

    @OneToOne(mappedBy = "assessment", cascade = CascadeType.REMOVE)
    public AssessmentQuestionnaire assessmentQuestionnaire;
}
