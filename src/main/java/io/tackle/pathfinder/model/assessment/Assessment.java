package io.tackle.pathfinder.model.assessment;

import io.tackle.pathfinder.model.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = "assessment")
@SQLDelete(sql = "UPDATE assessment SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Assessment extends AbstractEntity {
    String status;
    Long applicationId;
    String applicationName;
    String notes;
    AssessQuestionaire assessQuestionaire;
    List<AssessStakeholder> assessStakeholders;
}
