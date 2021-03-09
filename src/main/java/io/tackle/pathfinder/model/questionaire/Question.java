package io.tackle.pathfinder.model.questionaire;

import io.tackle.pathfinder.model.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name="question")
@SQLDelete(sql = "UPDATE question SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Question extends AbstractEntity {
    String title;
    String tooltip;
    List<Answer> answers;
}
