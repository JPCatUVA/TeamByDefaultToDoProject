Feature: User Registration

    Background: All Users Navigate to the Registration Page
        Given   The user is on the login page
        When    The user clicks the registration link

    Scenario: User can register with valid credentials
        And     The user enters valid credentials
        And     The user clicks the register button
        Then    The user should be sent to the home page

    Scenario Outline: Users cannot register with invalid credentials
        And     The user enters username "<email>" and password "<password>"
        And     The user clicks the register button
        Then    The user should see failure message "<message>"

    Examples:
    |email|password|message|
    |test@example.com|p@5s|Registration failed. Please try a different password.|
    |test@example.com|s0oPeRl0nGp@sSw0rd|Registration failed. Please try a different password.|
    |testuserwithoutemail|p@ssw0rd|Registration failed. Please enter an email.|

    