@task
Feature: Todo Creation

    Background: A user must be logged in
        Given   The account "test@example.com" "P@ssw0rd" exists
        When    The user logs in to their home page with "test@example.com" "P@ssw0rd"
        And     The user clicks the Add Task button


    Scenario Outline: As a user, I can create a task with varied input
        When    The user enters Task Title "<title>", Description "<description>", and picks a Due Date "<date>"
        And     The user clicks the Save Task button
        Then    A Task is created and is viewable in the Tasks list with title "<title>"

    Examples:
    |title|description|date|
    |Buy groceries (yum)|Milk, eggs, bread|2026-08-01T10:00|
    |Morning run (gross)||2026-12-31T23:59|


    Scenario Outline: As a user, I cannot create a task with no title
        When    The user enters Task Title "<title>", Description "<description>", and picks a Due Date "<date>"
        Then    The Save Task button is invalid

    Examples:
    |title|description|date|
    ||Some description|2026-12-31T23:59|
    |||2026-12-31T23:59|

