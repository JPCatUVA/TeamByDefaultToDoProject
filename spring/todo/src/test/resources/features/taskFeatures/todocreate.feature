@task
Feature: Todo Creation

  Background:
    Given a user exists in the system

  Scenario: Successfully create a todo with all fields
    When the user sends a POST request to "/task" with title "Buy groceries" and description "Milk, eggs, bread" and due date "2026-08-01T10:00:00"
    Then the response status code should be 201
    And the response body should contain the title "Buy groceries"
    And the response body should contain a generated task ID

  Scenario: Successfully create a todo with only the required title
    When the user sends a POST request to "/task" with title "Morning run"
    Then the response status code should be 201
    And the response body should contain the title "Morning run"

  Scenario: Fail to create a todo without a title
    When the user sends a POST request to "/task" with an empty title
    Then the response status code should be 400

  Scenario: Fail to create a todo without a user
    When a POST request to "/task" is sent without a user ID
    Then the response status code should be 400

  Scenario: Fail to create a todo with a non-existent user
    When the user sends a POST request to "/task" with a non-existent user ID and title "Ghost task"
    Then the response status code should be 400
