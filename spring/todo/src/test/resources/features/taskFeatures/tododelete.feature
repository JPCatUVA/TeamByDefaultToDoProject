@task
Feature: Todo Deletion

    Background: A user must be logged in and have a valid task created
        Given   A user is registered and logged in
        When    The user is on their home page
        And     There is a task submitted


    Scenario: As a user I should be able to delete a Task
        When    The user clicks the Task's delete button
        Then    The Task is removed from the list

    Scenario: As a user I should not be able to delete Tasks that are not there
        When    There are no Tasks present
        Then    The delete button is not found

