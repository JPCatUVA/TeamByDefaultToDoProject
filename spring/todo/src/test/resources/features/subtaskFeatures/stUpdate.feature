Feature: Subtask Update

    Background: A user must be logged in and have a valid task created
        Given   The user is on the login page
        When    The user enters a valid username
        When    The user enters the corresponding password
        When    The user is on their home page
        And     There is a task present
        And     The user clicks on a valid task
        And     There is a subtask present
        And     The user clicks on a valid subtask

    Scenario: As a user I should be allowed to change the fields of a Subtask
        