@task
Feature: Todo Deletion

    Background: A user must be logged in and have a valid task created
        Given   The account "test@example.com" "P@ssw0rd" exists
        When    The user logs in to their home page with "test@example.com" "P@ssw0rd"
        And     There is a task submitted


    Scenario: As a user I should be able to delete a Task
        When    The user clicks the Task's delete button
        Then    The Task is removed from the list

    Scenario: As a user I should not be able to delete Tasks that are not there
        When    There are no Tasks present
        Then    The delete button is not found

