@CSI @AMAPI
Feature: Create Vendor API Test

	@SmokeTest
  Scenario: An Create Vendor API request is sent to Maintenix to create the vendor
    When a Create Vendor API request is sent to Maintenix
      | code    | vendorName  | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey  | timeZone        | address1       | city   | country | name     |
      | SWA1A | SWA Vendor1 | MXI              | 2030-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | John Doe |
    Then the vendor is created with the provided information
      | code    | vendorName  | organizationCode | certificateExpiryDate | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey  | timeZone        | address1       | city   | country | name     |
      | SWA1A | SWA Vendor1 | MXI              | 2030-12-15T06:10:15Z  | PURCHASE       | USD          |                25 | swa_test_key | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | John Doe |

	@RefactorNonSmoke
  Scenario: An Create Vendor API request is sent to Maintenix with a vendor code which already exists
    When a Create Vendor API request is sent to Maintenix
      | code    | vendorName  | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name     |
      | SWA1A | SWA Vendor1 | MXI              | 2015-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key2 | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | John Doe |
    Then the error message "[MXERR-30147] The value entered, SWA1A, already exists as a Vendor Location  in the system." is verified

	@RefactorNonSmoke
  Scenario: An Create Vendor API request is sent to Maintenix with a null organization code
    When a Create Vendor API request is sent to Maintenix
      | code   | vendorName  | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name     |
      | SWA11 | SWA Vendor1 |                  | 2015-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key2 | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | John Doe |
    Then the error message "[MXERR-10000] The 'Organization' is a mandatory field.<br><br>Please enter a value for the 'Organization'." is verified

	@RefactorNonSmoke
  Scenario: An Create Vendor API request is sent to Maintenix with a null contact name
    When a Create Vendor API request is sent to Maintenix
      | code   | vendorName  | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name |
      | SWA11 | SWA Vendor1 | MXI              | 2015-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key2 | America/Chicago | P.O. Box 36647 | Ottawa | CAN     |      |
    Then the error message "[MXERR-10000] The 'Contact Name' is a mandatory field.<br><br>Please enter a value for the 'Contact Name'." is verified

	@RefactorNonSmoke
  Scenario: An Create Vendor API request is sent to Maintenix with a null vendor name
    When a Create Vendor API request is sent to Maintenix
      | code   | vendorName | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name |
      | SWA11 |            | MXI              | 2015-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key2 | America/Chicago | P.O. Box 36647 | Ottawa | CAN     |      |
    Then the error message "[MXERR-10000] The 'aName' is a mandatory field.<br><br>Please enter a value for the 'aName'." is verified

	@RefactorNonSmoke
  Scenario: An Create Vendor API request is sent to Maintenix with a null currency code
    When a Create Vendor API request is sent to Maintenix
      | code   | vendorName  | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name |
      | SWA11 | SWA Vendor1 | MXI              | 2015-12-15T06:10:15.000+0000 | PURCHASE       |              |                25 | swa_test_key2 | America/Chicago | P.O. Box 36647 | Ottawa | CAN     |      |
    Then the error message "[MXERR-10000] The 'aCurrency' is a mandatory field.<br><br>Please enter a value for the 'aCurrency'." is verified
