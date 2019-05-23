Feature: Create User

  @JspSmokeTest
  Scenario: Data should persist in the create user page after an unsuccessful submission of data or if an Organization Search page redirects.

    Given I am a superuser
    And I can access the create user page
    And I attempt creating a user in the create user page, leaving HR code blank
    When I receive an error message about the HR code and get redirected to the create user page
    Then The data I previously entered persists in the fields
    And I perform an Organization Search and I return
    Then The data I previously entered persists in the fields