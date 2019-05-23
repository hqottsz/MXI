Feature: Bulk Activation of Requirements

  As a system engineer I would like to activate revised task definitions in bulk mode.

  @JspSmokeTest
  Scenario: Bulk active requirements without providing revision information
    Given I am an engineer
    When I select multiple requirements for activation
    Then I am not able to enter revision information (reason and notes)
