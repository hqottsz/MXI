@CSI @AMAPI
Feature: Create Vendor

	@SmokeTest
  Scenario: Create vendor message is sent to Maintenix with a vendor code which does not exist
    Given I am a superuser
    And a vendor with vendor code "SWA001" does not exist
    When a Create Vendor API request is sent to Maintenix
      | code   | vendorName  | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey  | timeZone        | address1       | city   | country | name     |
      | SWA001 | SWA Vendor1 | MXI              | 2020-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | John Doe |
    Then the vendor is created in Maintenix with the provided information
