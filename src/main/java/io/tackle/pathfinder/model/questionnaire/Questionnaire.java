 package io.tackle.pathfinder.model.questionnaire;

import io.tackle.commons.entities.AbstractEntity;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import java.util.List;

@Entity
@Table(name = "questionnaire")
@SQLDelete(sql = "UPDATE questionnaire SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted = false")
@ToString
public class Questionnaire extends AbstractEntity {
    @Column(name="language_code", nullable = false)
    public String languageCode = "EN";

    @Basic(optional = false)
    public String name;

    @OneToMany(mappedBy="questionnaire")
    public List<Category> categories;
}
