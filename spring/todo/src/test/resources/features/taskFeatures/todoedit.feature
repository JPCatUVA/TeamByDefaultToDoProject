@task
Feature: Todo Editing

    Background: A user must be logged in and have a valid task created
        Given   A user is registered and logged in
        When    The user is on their home page
        And     There is a task submitted
        And     The user clicks on a valid task
        Then    The user is on the corresponding task page


    Scenario Outline: As a user I should be allowed to change the fields of a Task
        When    The user clicks on the edit button of a task field called "<field>"
        And     The user enters "<text>" into the task editing box
        And     The user clicks the task save button
        Then    The task field should now show "<text>" as updated

    Examples:
    |field|text|
    |Title|Updated Title|
    |Description|New description here|


    Scenario: As a user I should be allowed to update the due date of a Task
        When    The user clicks on the edit button of a task field called "Due Date"
        And     The user enters "2026-12-25T09:00" into the task date editing box
        And     The user clicks the task save button
        Then    The task due date field should be updated


    Scenario: As a user I should be allowed to mark a Task as completed
        When    The user clicks on the edit button of a task field called "Status"
        And     The user clicks the task save button
        Then    The task status field should show completed


    Scenario: As a user the app should gracefully handle editing a required title to blank
        When    The user clicks on the edit button of a task field called "Title"
        And     The user enters "" into the task editing box
        Then    The task save button is invalid


    Scenario: As a user I should not be able to edit a non-existent Task
        When    The user tries to manually enter a task path to a task that does not exist
        Then    The page will display a task error message

