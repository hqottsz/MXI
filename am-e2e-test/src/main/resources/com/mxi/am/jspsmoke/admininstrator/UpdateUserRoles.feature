Feature: Update User Roles

  @JspSmokeTest
  Scenario: Adding a role to a user will display that roles menu item without the need of log out / log in
    Given I am user11
    When I add a role to the active user
    Then The menu dropdown includes the added role menu items

  @JspSmokeTest
  Scenario: Removing a role from a user will not display that roles menu item without the need of log out / log in
    Given I am user11
    When I remove an existing role from the active user
    Then The menu dropdown no longer includes the role menu items
