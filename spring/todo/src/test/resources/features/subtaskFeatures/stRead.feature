Feature: Subtask Read

    Background: A user must be logged in and have a valid task created
        Given   The user is on the login page
        When    The user enters a valid username
        When    The user enters the corresponding password
        When    The user is on their home page
        And     There is a task submitted
        And     The user clicks on a valid task


    Scenario: As a user I should be able to view the list of Subtasks
        When    There are one or more Subtasks submitted
        Then    The subtasks should be viewable on the page

    Scenario: As a user I should be able to view a specific Subtask
        When    The user clicks on a specific subtask
        Then    The user is taken to the subtasks view

    Scenario: As a user I should not be able to view the subtasks of others
        When    The user tries to manually enter a subtask path to a subtask that is not theirs 
        Then    The page will display the error message "Failed to load subtask. Please try again"

