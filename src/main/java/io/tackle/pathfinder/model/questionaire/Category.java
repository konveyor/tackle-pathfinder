package io.tackle.pathfinder.model.questionaire;

import io.tackle.pathfinder.model.AbstractEntity;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name="category")
@SQLDelete(sql = "UPDATE category SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
public class Category extends AbstractEntity {
    String name;
    int order;
    
    List<Question> questions;

}
