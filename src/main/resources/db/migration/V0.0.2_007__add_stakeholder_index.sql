-- Only unique stakeholders allowed per assessment
CREATE UNIQUE INDEX assesment_stakeholder_unique_idx
ON assessment_stakeholder (assessment_id, stakeholder_id)
WHERE (deleted is not true);

-- Only unique stakeholders allowed per assessment
CREATE UNIQUE INDEX assesment_stakeholdergroup_unique_idx
ON assessment_stakeholdergroup (assessment_id, stakeholdergroup_id)
WHERE (deleted is not true);
