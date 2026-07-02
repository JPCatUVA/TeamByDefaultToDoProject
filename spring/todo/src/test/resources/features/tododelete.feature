Feature: Todo Deletion

  Background:
    Given an authenticated user with a deletable todo

  Scenario: Successfully delete an existing todo
    When the user sends a DELETE request to the todo
    Then the delete response status code should be 204

  Scenario: Verify deleted todo is no longer retrievable
    When the user sends a DELETE request to the todo
    And the user sends a GET request for the deleted todo
    Then the get response status code should be 404

  Scenario: Fail to delete a non-existent todo
    When the user sends a DELETE request to a non-existent todo
    Then the delete response status code should be 400

  Scenario: Fail to delete the same todo twice
    When the user sends a DELETE request to the todo
    And the user sends another DELETE request to the same todo
    Then the delete response status code should be 400
