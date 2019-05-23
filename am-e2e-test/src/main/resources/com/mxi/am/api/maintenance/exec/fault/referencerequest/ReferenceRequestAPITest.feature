@CSI @AMAPI
Feature: Retrieve reference request By reference request API Test

  Background:
    Given I am "mxi"

  @SmokeTest @CSIContractTest
  Scenario: Retrieve reference request list by status code and fault id
    Given there is a reference request
    When search reference request by status code and fault id
    Then the reference request list is returned

  @SmokeTest
  Scenario: Retrieve reference request list without status code and fault id
    When search reference request without status code and fault id

  @SmokeTest
  Scenario: Retrieve reference request list with unauthorized credentials
    When search reference request with unauthorized credentials


