Feature: Subtask Update

    Background: A user must be logged in and have a valid task created
        Given   The user is on the login page
        When    The user enters a valid username
        When    The user enters the corresponding password
        When    The user is on their home page
        And     There is a task submitted
        And     The user clicks on a valid task
        And     There is a subtask submitted
        And     The user clicks on a valid subtask

    Scenario: As a user I should be allowed to change the fields of a Subtask
        When    The user clicks on the edit button of a field
        And     The user enters "text" into the editing box
        And     The user clicks the save button
        Then    The field should now show "text" as updated 

    Scenario: As a user the app should gracefully handle editing a required title to blank
        When    The user clicks on the edit button of a field
        And     The user enters "blank text" into the editing box
        Then    The save button is invalid