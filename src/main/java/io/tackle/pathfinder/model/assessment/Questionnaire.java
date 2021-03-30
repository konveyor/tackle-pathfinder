 package io.tackle.pathfinder.model.assessment;

import io.tackle.commons.entities.AbstractEntity;
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
  
    @Basic(optional = false)
    @Column(name="language_code", nullable = false)
    String languageCode = "EN";

    String name;

    @OneToMany(mappedBy="questionnaireId")
    List<Category> categories;
    
}
