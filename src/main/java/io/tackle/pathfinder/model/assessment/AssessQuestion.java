package io.tackle.pathfinder.model.assessment;

import io.tackle.pathfinder.model.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;


@Entity
@Table(name = "assess_question")
@SQLDelete(sql = "UPDATE assess_question SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class AssessQuestion extends AbstractEntity {
    String name;
    int order;
    String tooltip;
    String question;
    String comment;
    List<AssessAnswer> answers;
}
