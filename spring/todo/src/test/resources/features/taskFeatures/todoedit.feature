@task
Feature: Todo Editing

  Background:
    Given an authenticated user with an existing todo

  Scenario: Successfully update the title of a todo
    When the user sends a PATCH request to the todo with title "Updated Title"
    Then the edit response status code should be 200
    And the edit response body should contain the title "Updated Title"

  Scenario: Successfully update the description of a todo
    When the user sends a PATCH request to the todo with description "New description here"
    Then the edit response status code should be 200
    And the edit response body should contain the description "New description here"

  Scenario: Successfully update the due date of a todo
    When the user sends a PATCH request to the todo with due date "2026-12-25T09:00:00"
    Then the edit response status code should be 200
    And the edit response body should contain the due date "2026-12-25T09:00:00"

  Scenario: Successfully mark a todo as completed
    When the user sends a PATCH request to the todo with isCompleted true
    Then the edit response status code should be 200
    And the edit response body should have isCompleted set to true

  Scenario: Successfully update multiple fields at once
    When the user sends a PATCH request to the todo with title "Multi-edit" and description "Changed both"
    Then the edit response status code should be 200
    And the edit response body should contain the title "Multi-edit"
    And the edit response body should contain the description "Changed both"

  Scenario: Fail to update a non-existent todo
    When the user sends a PATCH request to a non-existent todo with title "Ghost edit"
    Then the edit response status code should be 400
