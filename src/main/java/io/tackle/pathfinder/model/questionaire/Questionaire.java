package io.tackle.pathfinder.model.questionaire;

import io.tackle.pathfinder.model.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name="questionaire")
@SQLDelete(sql = "UPDATE questionaire SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Questionaire extends AbstractEntity {
    String language;
    String name;
    List<Category> categories;
}
