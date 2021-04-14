insert into questionnaire (id, language_code, name)
values (
        nextval('hibernate_sequence'),
        'EN',
        'Pathfinder'
    );
insert into category (id, category_order, name, questionnaire_id)
values (
        nextval('hibernate_sequence'),
        1,
        'Application Details',
        (
            select max(id)
            from questionnaire
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'SINGLE',
        'DEVOWNER',
        'Does the application development team understand and actively develop the application?',
        'How much knowledge does the team have regarding the development or usage of the application',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'External 3rd party or COTS application ',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'In maintenance mode, no app SME knowledge, poor documentation',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Maintenance mode, SME knowledge available',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Actively developed, SME knowledge available',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'New Greenfield application',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'SINGLE',
        'OPSOWNER',
        'How is the application supported in Production?',
        'How much knowledge does the team have to support the application in production',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Application production support outsourced to 3rd party support provider. Ticket driven escalation process, no inhouse support resources.',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Production support provided by separate internal team, little interaction with development team.',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Multiple teams support the application using an established escalation model',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'SRE based approach with knowledgeable and experienced operations team',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Pure DevOps model, the team that builds it is responsible for running it in Production',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'SINGLE',
        'LEADTIME',
        'How long does it take from when code is committed to it being deployed to a stage that is production ready?',
        'Gauge the development latency',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Not tracked',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'More than six months',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Between one month and six months',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Between one week and one month',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Between one day and one week',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'Less than one day',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'SINGLE',
        'DEPLOYFREQ',
        'Deployment frequency',
        'Gauge deployment frequency',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Not tracked',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Greater than once every six months',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Between once per month and once every six months',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Weekly deployments',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Daily deployments',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'On demand (multiple deploys per day)',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'SINGLE',
        'MTTR',
        'What is the Mean Time to Recover (MTTR) when a fault is found with the application in production?',
        'Gauge the problem resolution time, MTTR (mean time to recover) is the average time it takes to repair/recover a system',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Not tracked',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'One month or greater',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Between one day and one week',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Between one hour and one day',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Less than one hour',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'SINGLE',
        'COMPLIANCE',
        'Does the application have any legal compliance requirements? e.g. PCI, HIPAA etc. Does the application have any licensing requirements? e.g. per core licensing',
        'Assess the level of compliance and licensing required, think about what impact this will have when using a container platform to host the applications e.g. cost, fault reporting etc',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'High compliance requirements - both Legal and licensing',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Licensing compliance - licensing servers',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Legal compliance - distinct hardware, isolated clusters, compliance certification',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        ' None',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        7,
        'SINGLE',
        'ARCHTYPE',
        'Which statement best describes the application architecture?',
        'In simple terms describe the application architecture',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Massive Monolith (high memory, high CPU), singleton deployment, vertical scale only',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Massive Monolith (high memory, high CPU), non singleton, complex to scale horizontally',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Complex Monolith -  strict runtime dependency startup order, non resilient architecture',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Modern resilient monolith e.g. retries, circuit breaker etc',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Independently deployable components',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into category (id, category_order, name, questionnaire_id)
values (
        nextval('hibernate_sequence'),
        2,
        'Application Dependencies',
        (
            select max(id)
            from questionnaire
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'SINGLE',
        'DEPSHW',
        'Does the application require specific hardware capabilities to run?',
        'OpenShift only runs on X86 and IBM Power/Z systems',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Non supported CPU requirements',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Custom or legacy hardware required',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'GPU, specific worker node hardware requirements',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Supported CPU requirements',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'SINGLE',
        'DEPSOS',
        'What operating system does the application require?',
        'Only Linux and certain versions of Windows are supported in containers. Check the latest versions and requirements',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Non-supported Operating Systems - OSX, AIX, UNIX, SOLARIS',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Linux with custom kernel drivers or specific kernel version',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Linux with custom capabilities e.g. setcomp, root access',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Microsoft Windows',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Standard Linux',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'SINGLE',
        'DEPS3RD',
        'Are 3rd party/Vendor components supported in containers?',
        'While you can potentially run anything in a container doesn''t mean that the vendor will support it',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Not recommended to run component in containers',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Component not supported by vendor when running in a container',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Supported but with restricted functionality/untested',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Supported but relies on self built images',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Fully vendor supported, certified images available',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'No 3rd party components required',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'SINGLE',
        'DEPSIN',
        'Dependencies - (Incoming/Northbound)',
        'Systems/Applications that calls the assessed application',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Difficult/Expensive to change dependent systems - legacy, 3rd party, external',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Many dependent systems, possible to change but expensive and time consuming',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Many dependent systems, possible to change as they''re internally managed',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Internal dependencies only',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'No dependent systems',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'SINGLE',
        'DEPSOUT',
        'Dependencies - (Outgoing/Southbound)',
        'Systems/Applications that the assessed application calls',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Availability only verified when processing traffic',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Complex strict startup order required',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Application not ready until dependencies are verified available ',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Limited processing available if dependencies are unavailable',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'No dependencies',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into category (id, category_order, name, questionnaire_id)
values (
        nextval('hibernate_sequence'),
        3,
        'Application Architecture',
        (
            select max(id)
            from questionnaire
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'SINGLE',
        'RESILIENCY',
        'How resilient is the application and how well does it recover from outages/restarts?',
        'If the assessed application or one of its dependencies fails how does the application currently recover from the failure and is manual intervention required ',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Application cannot be restarted cleanly and requires manual intervention',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Application errors when southbound dependencies are unavailable and doesn''t recover automatically',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Application functionality limited when dependency is unavailable but recovers once dependency is available',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Application employs resilient architecture patterns e.g. circuit breaker, retries etc',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Chaos Engineering principals followed, application containers randomly terminated to test resiliency in production',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'SINGLE',
        'COMMS',
        'How does the external world communicate with the application?',
        'How do external clients communicate with the application',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Non-IP protocols e.g. serial, IPX, AppleTalk',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'IP based - hostname/ip encapsulated in payload',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'TCP/UDP Traffic without host addressing e.g. SSH ',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'TCP/UDP encapsulated using SSL with SNI header',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'HTTP/HTTPS Web traffic ',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'SINGLE',
        'STATE',
        'How does the application handle it''s internal state?',
        'Does the assessed application have internal state that needs to be managed/retained and if so how?',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Shared memory between application components',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Managed/Coordinated externally from application e.g. external Zookeeper, data grid etc',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'State maintained in non-shared, non-ephemeral storage',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Shared disk between application instances',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Stateless/Ephemeral container storage',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'SINGLE',
        'HA',
        'Does the application have any unusual concerns around service discovery?',
        'How does the application perform service discovery and how is it discovered',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Uses proprietary discovery technologies that are not kubernetes suitable e.g. hardcoded ip addresses, custom cluster manager',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Application requires restart on cluster changes to discover new service instances',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Service discovery compatible with kubernetes but requires specific libraries/services e.g. Hashicorp Consul, Netflix Eureka',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Standard kubernetes DNS name resolution',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'None required',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'SINGLE',
        'CLUSTER',
        'How is the application clustered?',
        'Does the application need to be clustered and managed (if required)',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Manually configured clustering mechanism e.g. static clusters',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Application clustering handled by external off-PAAS cluster manager',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Application clustering mostly provided by application runtime using a kubernetes suitable mechanism',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'None required',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into category (id, category_order, name, questionnaire_id)
values (
        nextval('hibernate_sequence'),
        4,
        'Application Observability',
        (
            select max(id)
            from questionnaire
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'SINGLE',
        'LOGS',
        'How does the application use logging and how is it accessed?',
        'How do we get access the applications logs',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'No logging available or internal only with no export mechanism',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Custom binary logs exposed using non-standard protocols',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Logs exposed via syslog',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Log entries written to filesystem potentially in multiple files',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Application forwards logs to external system e.g. Splunk',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'Configurable logging e.g. can be sent to STDOUT',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'SINGLE',
        'METRICS',
        'Does the application provide metrics?',
        'How do we get access the applications metrics if required, OCP defaults to CPU/Memory metrics',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'No metrics exposed',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Internal metrics but not exposed externally',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Metrics exposed via binary protocols e.g. SNMP, JMX',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        '3rd party metrics solution e.g. Dynatrace, AppDynamics etc',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'InBuilt Prometheus endpoint support',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'SINGLE',
        'HEALTH',
        'How easy is it to determine the application health (liveness) and if it''s ready to handle traffic (readiness)?',
        'How can we determine if the app is ready to handle traffic and if its in good health as opposed to deadlocked/crashed/stuck but without crashing the container',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'No health or readiness query functionality available',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Custom watchdog process monitoring and managing the application',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Basic application health requires semi-complex scripting',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Dedicated, independent liveness and readiness endpoints',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Probes execute synthetic transactions to verify application health',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'SINGLE',
        'PROFILE',
        'What best describes the applications runtime characteristics?',
        'When running what does the profile of the application resemble e.g. how would it look if the cpu/memory usage was graphed, consider serverless as well',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Deterministic predictable real time execution/control requirements',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Latency sensitive applications e.g. voice, HFT',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Constant Traffic, High range of memory/cpu needs',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Intermittent traffic, predictable CPU/Memory Needs',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Constant Traffic, predictable CPU/Memory Needs',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'SINGLE',
        'STARTUPTIME',
        'How long does it take the application to be ready to handle traffic?',
        'How long does it take the application to boot',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        ' 5+ minutes',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        ' 2-5 minutes',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        ' 1-2 minutes',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        ' > 10 seconds but < 1 minute',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        ' < 10 seconds',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into category (id, category_order, name, questionnaire_id)
values (
        nextval('hibernate_sequence'),
        5,
        'Application Cross-Cutting concerns',
        (
            select max(id)
            from questionnaire
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'SINGLE',
        'TEST',
        'What kind of testing does the application undergo?',
        'Is the application tested, is it easily tested, is it tested in production',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'None, minimal manual testing only',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Minimal automated testing, UI focused only ',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Some Automated unit & regression testing, basic CI pipelines, modern test practices not followed',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Highly repeatable automated testing - Unit, Integration, smoke tests before production deployment, modern test practices followed',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Chaos Engineering principals followed. Constant testing in production e.g. A/B, experimentation',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'SINGLE',
        'CONFIG',
        'How is the application configured?',
        'How is the application currently configured and how might that look in a container platform, External servers are runtime dependencies hence AMBER',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Configuration compiled/patched into the application at installation time, application configured via user interface',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Externally stored e.g. DB and accessed using specific environment key e.g. hostname, ip address',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Multiple configuration files in multiple filesystem locations',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'All environment configuration built into the application and enabled via system property at runtime',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'External configuration server e.g. Spring Cloud Config Server, Hashicorp Consul ',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'Configuration loaded from files in a single configurable location, environment variables',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'SINGLE',
        'SECURITY',
        'How does the application acquire the necessary security credentials/certificates?',
        'External systems are amber as they''re a runtime dependency',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'HSM, hardware based encryption devices',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Certs, Keys bound to application IP addresses, generated at runtime per application instance',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Keys/Certs compiled into application',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Loaded via shared disk',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Retrieved from external system e.g. HasiCorp Vault, CyberArk Conjur',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'Loaded via files',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        7,
        'None needed',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'SINGLE',
        'DEPLOY',
        'How is the application currently deployed?',
        'Understand the current deployment process and how might that look in a container platform',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Manual documented steps, user interface driven interaction',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Manual documented steps, some basic automation',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Simple automated deployment scripts',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Automated deployment, but manual or complex promotion through pipeline stages',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Automated deployment, Full CD Pipeline in place, promoting Applications through the pipeline stages with minimal intervention',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'GitOps approach and/or B/G + Canary capable',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'SINGLE',
        'CURRHOST',
        'Where is the application currently deployed?',
        'where does the application run',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Bare metal server',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Virtual machine e.g. Red Hat Virtualisation, VMWare Virtualisation',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Private cloud e.g. OpenStack',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Public cloud provider compute instance e.g. AWS,Azure,GCP',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Platform as a service e.g. Heroku, Force.com, Google App Engine',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'Other, please put in comments box below',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into question (
        id,
        question_order,
        type,
        name,
        question_text,
        description,
        category_id
    )
values (
        nextval('hibernate_sequence'),
        6,
        'SINGLE',
        'CONTAINERS',
        'How mature is the existing containerisation process, if any?',
        'If the team has used container previously how have they used them',
        (
            select max(id)
            from category
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        0,
        'Unknown',
        'UNKNOWN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        1,
        'Desktop-led container implementation designed to support running application on a laptop. Container treated like a VM with multiple services',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        2,
        'Use of a init process within the container to manage multiple container processes that run independently but are tightly integrated',
        'RED',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        3,
        'Some experience with containers but not fully defined as yet',
        'AMBER',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        4,
        'Proficient with containers and container platforms e.g. Swarm, K8S',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
insert into single_option (
        id,
        singleoption_order,
        option,
        risk,
        question_id
    )
values (
        nextval('hibernate_sequence'),
        5,
        'Application containerisation not attempted as yet',
        'GREEN',
        (
            select max(id)
            from question
        )
    );
