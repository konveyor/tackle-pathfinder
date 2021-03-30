package io.tackle.pathfinder.model.questionnaire;

import io.tackle.commons.entities.AbstractEntity;
import lombok.ToString;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = "question")
@SQLDelete(sql = "UPDATE question SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@ToString
public class Question extends AbstractEntity {
    @Column(name="question_order")
    int order;
    String type;
    String name;
    String tooltip;
    String text;
    String comment;

    @ManyToOne
    Category category;

    @OneToMany(mappedBy="question")
    List<SingleOption> singleOptions;
}
