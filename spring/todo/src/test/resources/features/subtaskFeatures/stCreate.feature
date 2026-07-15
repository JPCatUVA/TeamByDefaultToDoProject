Feature: Subtask Creation

    Background: A user must be logged in and have a valid task created.
        Given   The user is on the login page
        When    The user enters a valid username
        When    The user enters the corresponding password
        When    The user is on their home page
        And     There is a task present
        And     The user clicks on a task
        And     The user clicks the Add Subtask button


    Scenario: As a user I should be allowed to not create a task if I change my mind midway 
        When    The user clicks the Cancel button
        Then    The Subtask editing fields should close and nothing is added

    Scenario Outline: As a user, I can create a nested subtask with varied input
        When    The user enters Title "<title>", Description "<description>", and picks a Due Date "<date>"
        And     The user clicks the Save Subtask button
        Then    A Subtask is created and is viewable in the Subtasks list
    
    Examples:
    |title|description|date|
    |TaskWithDescription|It's me, im a Subtask|2027-12-25T23:59:59|
    |TaskWithNoDescriptionInthePast||1066-01-01T01:01:01|


    Scenario Outline: As a user, I cannot create a nested subtask with no title
        When    The user enters Title "<title>", Description "<description>", and picks a Due Date "<date>"
        And     The user clicks the Save Subtask button
        Then    The Save Subtask button is invalid

    Examples:
    |title|description|date|
    ||This task has no title|2027-12-25T23:59:59|
    ||This task also has no title and is in the far past|1066-01-01T01:01:01|


