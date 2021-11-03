# Internationalization

## How to add a new language

In order to translate the questionnaire you will need to create `INSERT` SQL statements. Creating SQL statements can be very boring; however, you can use [generate-translation-inserts-for-a-language.sql](/src/main/resources/db/scripts/generate-translation-inserts-for-a-language.sql) for generating a template.

Steps:

- Start this repo in dev mode following the instructions at [local-test](./README.md#local-test). This step involves starting PostgreSQL (port 5433), Keycloak (port 8180), and starting this project using `./mvnw quarkus:dev`.
- Use any tool you have available in order to connect to the database. Once you are connected to the DB open [generate-translation-inserts-for-a-language.sql](/src/main/resources/db/scripts/generate-translation-inserts-for-a-language.sql), replace (using any editor) `'ES'` by the language code you are going to translate the questionnaire to, and execute the script. Note that `'ES''` can be replaced by any `ISO 639-1` code value.
- The output of the script executed in the previous step should be written in a file `src/main/resources/db/migration/{myFile}.sql`.
- Before start translating the file `src/main/resources/db/migration/{myFile}.sql` make sure it doesn't contain invalid characters:
  - Make sure there are no double quotes at the begginning of every `INSERT` statement;
  - Make sure that every `INSERT` statement finishes with a semicolon `;`
  - Escape any single quote (`'`) character adding another single quote. Eg. If you have `'What best describes the application's runtime characteristics?'` in your SQL script you should change it to `'What best describes the application''s runtime characteristics?'` . Note that `application's` was replaced by `application''s`. Most of these cases appear with the characters `'s` so you can use your IDE and replace them all at once.
- Start translating the questionnaire replacing `'XXXXX'` from `src/main/resources/db/migration/{myFile}.sql` using the correct translation in the language selected.

> As soon as you feel confident, please open a new Pull Request with your changes and make it part of the official repository.
