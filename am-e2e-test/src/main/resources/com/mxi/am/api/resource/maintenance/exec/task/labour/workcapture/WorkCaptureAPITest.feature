@CSI @AMAPI
Feature: Create Work Capture with electronic signature through Work Capture API test

  @Ignore @UniqueConfigSmokeTest @CSIContractTest
  Scenario: Create Job Stop Work Capture With All Fields
    Given I am "mxi"
    And I have an electronic signature certificate
    And there is a started work package with an adhoc task
    When I call the work capture API to generate the report
    Then the labour row is in work and a report is generated
    When I call the work capture API with the certificate password
    Then the labour row is complete and a new labour row is added


  @Ignore @UniqueConfigSmokeTest
  Scenario: Create Work Capture for Labour In a Task Assigned to a Work Package Not IN PROGRESS
   Given I am "mxi"
   And I have an electronic signature certificate
   And there is an active work package with an adhoc task
   When I call the work capture API to generate the report
   Then a bad request for Work Package not IN WORK response is returned

  @Ignore @UniqueConfigSmokeTest
  Scenario: Create Work Capture With a Missing Report
   Given I am "mxi"
   And I have an electronic signature certificate
   And there is a started work package with an adhoc task
   When I call the work capture API to generate the report
   Then the labour row is in work and a report is generated
   When I call the work capture API with a missing report
   Then a bad request for unexpected change in Work Capture response is returned
