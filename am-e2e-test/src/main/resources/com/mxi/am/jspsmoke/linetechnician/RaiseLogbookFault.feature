Feature: Raise Logbook Fault

  As a line technician
  I would like to open a logbook fault

  @JspSmokeTest
  Scenario: I have Deferral References that share the same Deferral Reference Name/Description, Assembly, Fault Severity and have a different Operator and Deferral Class
    Given I am a line technician
    When I raise a logbook fault and defer it using the shared Deferral Reference via typing
    Then A warning message informing the user that the deferral reference has conflicts should appear
