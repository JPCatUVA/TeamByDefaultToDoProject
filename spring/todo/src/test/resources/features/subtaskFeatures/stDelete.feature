@subtask
Feature: Subtask Deletion

    Background: A user must be logged in and have a valid task created
        Given   The account "test@example.com" "P@ssw0rd" exists
        When    The user logs in to their home page with "test@example.com" "P@ssw0rd"
        And     There is a task submitted
        And     The user clicks on a valid task
        Then    The user is on the corresponding task page



    Scenario: As a user I should be able to delete a Subtask
        When    There are one or more Subtasks submitted
        And     The user clicks the Subtask's delete button
        Then    The Subtask is removed from the list

    Scenario: As a user I should not be able to delete Subtasks that are not there
        When    There are no Subtasks present
        And     The user tries to click the delete button
        Then    The button is not found
