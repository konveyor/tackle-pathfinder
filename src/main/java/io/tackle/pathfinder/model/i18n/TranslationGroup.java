package io.tackle.pathfinder.model.i18n;

import io.tackle.commons.entities.AbstractEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "translation_group")
@SQLDelete(sql = "UPDATE translation_group SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TranslationGroup extends AbstractEntity {
    public String name;
}
