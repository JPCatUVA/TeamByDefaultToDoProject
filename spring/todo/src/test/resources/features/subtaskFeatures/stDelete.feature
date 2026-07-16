Feature: Subtask Delete

    Background: A user must be logged in and have a valid task created
        Given   The user is on the login page
        When    The user enters a valid username
        When    The user enters the corresponding password
        When    The user is on their home page
        And     There is a task submitted
        And     The user clicks on a valid task


    Scenario: As a user I should be able to delete a Subtask
        When    There are one or more Subtasks submitted
        And     The user clicks the Subtask's delete button
        Then    The task is removed from the list

    Scenario: As a user I not be able to delete Subtasks that are not there
        When    There are no Subtasks present
        And     The user tries to click the delete button
        Then    The button is not found