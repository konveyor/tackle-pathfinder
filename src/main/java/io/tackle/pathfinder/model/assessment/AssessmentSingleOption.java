/*
 * Copyright © 2021 the Konveyor Contributors (https://konveyor.io/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.tackle.pathfinder.model.assessment;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.tackle.pathfinder.model.Risk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "assessment_singleoption")
@SQLDelete(sql = "UPDATE assessment_singleoption SET deleted = true WHERE id = ?", check = ResultCheckStyle.COUNT)
@Where(clause = "deleted is not true")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentSingleOption extends PanacheEntity {
    @CreationTimestamp
    @Column(updatable=false)
    public Instant createTime;

    public String createUser;

    @UpdateTimestamp
    public Instant updateTime;

    public String updateUser;

    @Builder.Default
    public Boolean deleted = false;
    
    @Column(name="singleoption_order", nullable = false)
    public int order;

    @Basic(optional = false)
    @Enumerated(value = EnumType.STRING)
    public Risk risk;

    @Column(length = 500, nullable = false)
    public String option;

    @JoinColumn(name="assessment_question_id", referencedColumnName="id", nullable=false)
    @ManyToOne
    public AssessmentQuestion question;

    @Basic(optional = false)
    public boolean selected;

    public Long questionnaire_optionId;

}
