# Tackle Pathfinder application

It's the next version of the https://github.com/redhat-cop/pathfinder project.  

Pathfinder is an extensible, questionnaire based assessment tool for assessing the suitability of applications for deployment onto an enterprise Kubernetes platform.

# API 

* use Apicurio to design de API first
* use Microcks to mock the API to test it and decouple Frontend from Backend


# LOGIC

```plantuml
@startuml
left to right direction
actor User
package Select {
    usecase "List Apps with Assessment status" as list
    usecase "Select Apps to Assess" as selectapp
}
package Asses {
    usecase "Select Stakeholders" as selectstake
    usecase "Answer Questions" as answer
}
package Answer {
    usecase "Navigate Questions Categories" as navigate
    usecase "Save as Draft" as save
    usecase "Select option as answer" as select
    usecase "Add comment" as comment
    usecase "See question explanation" as explana
}
package Review {
    usecase "Select completed Assessment" as selectassessment
    usecase review as "Set action, effort,
    criticallity, priority, comments"
    usecase "see assessment summary" as summary
}
User --> selectapp
User --> selectstake
User --> answer
answer ..> navigate : include
answer ..> save : include
answer ..> select : include
answer ..> comment : include
answer ..> explana : include
User --> review
User --> summary
User --> selectassessment
@enduml
```

# MODEL

* flattened model (denormalised shcmema)
* no dependency between questions
* no mandatory/optional questions
* no multichoice questions

```plantuml
@startuml

package ExternalEntities {
    entity stakeholder {
        id : long
        name : string
    }
    entity application {
        id : long
        name : string
    }
}

package Assessment {

    entity assessment {
        date : timestamp
        user : long
    }
    entity assess_page {
        order : integer
        name : string
    }
    entity assess_question {
        name : string
        tooltip : string
        question : string
        comment : string
    }

    entity assess_answer {
        order : integer
        risk : [RED,AMBER,GREEN]
        answer : string
        selected : boolean
    }

    entity assess_application {
        id : long
        name : string
    }

    entity assess_stake {
        id : long
        name : string
    }

    entity assess_review {
        action : string
        effort : string
        criticality : string
        priority : string
    }
}

package QuestionaireDesign {
    entity questionaire {
        name : string
    }
    entity page {
        order : integer
        name : string
    }
    entity question {
        order : integer
        name : string
        question : string
        tooltip : string
    }
    entity answer {
        order : integer
        risk : [RED,AMBER,GREEN]
        answer : string
    }
}

assessment ||--o{ assess_application
assessment ||--o{ assess_stake
assessment ||--o{ assess_page
assessment ||--o| assess_review
assess_page ||--o{ assess_question
assess_question ||--o{ assess_answer

application ||..o| assess_application
stakeholder ||..o{ assess_stake

questionaire ||..o{ assessment

questionaire ||--o{ page
page ||--o{ question
question ||--o{ answer

@enduml
```
