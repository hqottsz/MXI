 Feature: Turn In Inventory

  As a storeroom clerk
  I would like to Turn In inventory

  Background:
  Given I am a storeroom clerk

@JspSmokeTest
  Scenario: Validating that user is able to turn-in a TRK inventory using inventory barcode
    When I try to turn in the TRK inventory using the inventory barcode
    Then the TRK inventory should be successfully turned in

@JspSmokeTest
  Scenario: Validating that user is able to turn-in a BATCH inventory using part and batch number
    When I try to turn in the BATCH inventory using the part number
    Then the BATCH inventory should be successfully turned in

