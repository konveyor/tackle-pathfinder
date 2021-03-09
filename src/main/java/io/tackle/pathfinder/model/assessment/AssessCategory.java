package io.tackle.pathfinder.model.assessment;

import io.tackle.pathfinder.model.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = "assess_category")
@SQLDelete(sql = "UPDATE assess_category SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class AssessCategory extends AbstractEntity {
    int order;
    String name;
    List<AssessQuestion> assessQuestion;
}
