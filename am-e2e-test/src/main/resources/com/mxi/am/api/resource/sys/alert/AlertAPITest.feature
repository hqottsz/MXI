@CSI @AMAPI
Feature: Verify Alert API GET request with UI Values

  Background:
    Given I am a superuser

  @SmokeTest
  Scenario: Verify Alert API POST with UI
    Given I have a new basic PO
    When I create an Alert using the Alert API PURCHASE_ORDER type
    Then PURCHASE_ORDER type Alert is returned correctly from the API

  @SmokeTest
  Scenario: Verify Alert API GET with UI
    Given I create an Alert using the Alert API STRING type
    When I send the Alert API message to get the Alert
    Then STRING type Alert is returned correctly from the API
