@CSI @AMAPI
Feature: Unapprove Vendor API Test

	@RefactorNonSmoke
  Scenario: An update Vendor API request is sent to Maintenix to unapprove the vendor
    Given a Create Vendor API request is sent to Maintenix
      | code    | vendorName       | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name       |
      | SWA3A | Unapprove Vendor | MXI              | 2030-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa-ext-key3A | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | James Bond |
    And an Update Vendor API request is sent to Maintenix to approve the vendor
      | code    | vendorName       | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name       | status   |
      | SWA3A | Unapprove Vendor | MXI              | 2030-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa-ext-key3A | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | James Bond | APPROVED |
    When an Update Vendor API request is sent to Maintenix to unapprove the vendor
      | code    | vendorName       | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name       | status   | reasonCode | note             |
      | SWA3A | Unapprove Vendor | MXI              | 2030-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa-ext-key3A | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | James Bond | UNAPPRVD | VNPERF     | Poor Performance |
    Then the vendor is unapproved with the provided information
      | code    | vendorName       | organizationCode | certificateExpiryDate | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name       | status   | reasonCode | note             |
      | SWA3A | Unapprove Vendor | MXI              | 2030-12-15T06:10:15Z  | PURCHASE       | USD          |                25 | swa-ext-key3A | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | James Bond | UNAPPRVD | PP         | Poor Performance |
