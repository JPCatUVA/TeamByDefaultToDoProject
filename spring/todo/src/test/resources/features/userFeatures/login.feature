@user
Feature: User login

    Background: All unauthorized users land on the login page.
        Given   The account "test@example.com" "P@ssw0rd" exists
        And     The account "test2@example.com" "T3stpa55" exists
        When    The unauthorized user accesses the website

    Scenario: User enters existing, related credentials.
        And     The user enters the username "test@example.com" and password "P@ssw0rd"
        And     The user clicks the login button
        Then    The user is redirected to their todo manager

    Scenario Outline: User enters invalid credentials
        And     The user enters the username "<username>" and password "<password>"
        And     The user clicks the login button
        Then    The user should see login error message "Invalid/Unregistered username or password. Please try again."

    Examples:
    |username|password|
    |test@example.com|T3stp@55|
    |test2@example.com|n0nPa55!|
    |not@user.com|P@ssw0rd|
    |thisshouldntwork|howcouldthisreturn200?|
