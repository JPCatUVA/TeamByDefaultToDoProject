@subtask
Feature: Subtask Update

    Background: A user must be logged in and have a valid task created
        Given   The account "test@example.com" "P@ssw0rd" exists
        When    The user logs in to their home page with "test@example.com" "P@ssw0rd"
        And     There is a task submitted
        And     The user clicks on a valid task
        Then    The user is on the corresponding task page
        And     There is a subtask submitted
        And     The user clicks on a valid subtask
        Then    The user is on the corresponding Subtask page



    Scenario Outline: As a user I should be allowed to change the fields of a Subtask
        When    The user clicks on the edit button of a field called "<field>"
        And     The user enters "<text>" into the editing box
        And     The user clicks the save button
        Then    The field should now show "<text>" as updated

    Examples:
    |field|text|
    |Title|I am a brand new Title|
    |Description|I am new Subtask descriptive text|

    Scenario: As a user the app should gracefully handle editing a required title to blank
        When    The user clicks on the edit button of a the title
        And     The user enters "" into the editing box
        Then    The save button is invalid
