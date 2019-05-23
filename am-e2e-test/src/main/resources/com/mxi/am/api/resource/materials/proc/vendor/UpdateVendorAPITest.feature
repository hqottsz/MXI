@CSI @AMAPI
Feature: Update Vendor API Test

	@RefactorNonSmoke
  Scenario: An Update Vendor API request is sent to Maintenix to update the vendor
    Given a Create Vendor API request is sent to Maintenix
      | code    | vendorName  | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city    | country | name     |
      | SWA2A | SWA Vendor2 | MXI              | 2030-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa-ext-key2A | America/Chicago | P.O. Box 36647 | Chicago | USA     | John Doe |
    When an Update Vendor API request is sent to Maintenix
      | timeZone         | currencyCode | code    | notes     | vendorName         | name       | faxNumber    | jobTitle     | phoneNumber  | emailAddress       | vendorTypeCode | organizationCode | certificateNumber | certificateExpiryDate        | approvalTypeCode | termsAndConds | spec2000Enabled | minPurchaseAmount | externalKey   | stdBorrowRate | defaultAirport                   | noteToReceiver | address1      | address2        | city    | country | state | zip     |
      | America/New_York | CAD          | SWA2A | TEST NOTE | Update Vendor Test | James Bond | 999-000-6666 | Secret Agent | 999-123-6666 | james.bond@mxi.com | REPAIR         | MXI              | CERT-2            | 2050-12-15T06:11:11.000+0000 | FINANCE          | NET15         | true            |               500 | swa-ext-key2A | IATA          | CD15E4F2C6D011E686664DF6B949C9DB | See Notes      | P.O. Box 3000 | 123 Fake Street | Toronto | CAN     | ON    | P0N 1K0 |
    Then the vendor is updated with the provided information
      | timeZone         | currencyCode | code    | notes     | vendorName         | name       | faxNumber    | jobTitle     | phoneNumber  | emailAddress       | vendorTypeCode | organizationCode | certificateNumber | certificateExpiryDate | approvalTypeCode | termsAndConds | spec2000Enabled | minPurchaseAmount | externalKey   | stdBorrowRate | defaultAirport                   | noteToReceiver | address1      | address2        | city    | country | state | zip     |
      | America/New_York | CAD          | SWA2A | TEST NOTE | Update Vendor Test | James Bond | 999-000-6666 | Secret Agent | 999-123-6666 | james.bond@mxi.com | REPAIR         | MXI              | CERT-2            | 2050-12-15T06:11:11Z  | FINANCE          | NET15         | true            |               500 | swa-ext-key2A | IATA          | CD15E4F2C6D011E686664DF6B949C9DB | See Notes      | P.O. Box 3000 | 123 Fake Street | Toronto | CAN     | ON    | P0N 1K0 |

	@RefactorNonSmoke
  Scenario: An Update Vendor API request is sent to Maintenix with an invalid state code
    When an Update Vendor API request is sent to Maintenix
      | timeZone         | currencyCode | code    | notes     | vendorName         | name       | faxNumber    | jobTitle     | phoneNumber  | emailAddress       | vendorTypeCode | organizationCode | certificateNumber | certificateExpiryDate        | approvalTypeCode | termsAndConds | spec2000Enabled | minPurchaseAmount | externalKey   | stdBorrowRate | defaultAirport                   | noteToReceiver | address1      | address2        | city    | country | state | zip     |
      | America/New_York | CAD          | SWA2A | TEST NOTE | Update Vendor Test | James Bond | 999-000-6666 | Secret Agent | 999-123-6666 | james.bond@mxi.com | REPAIR         | MXI              | CERT-2            | 2050-12-15T06:11:11.000+0000 | FINANCE          | NET15         | true            |               500 | swa-ext-key2A | IATA          | CD15E4F2C6D011E686664DF6B949C9DB | See Notes      | P.O. Box 3000 | 123 Fake Street | Toronto | CAN     | IL    | P0N 1K0 |
    Then the update vendor error message "[MXERR-30332] The state 'IL' does not belong to country 'CAN'." is verified

	@RefactorNonSmoke
  Scenario: An Update Vendor API request is sent to Maintenix with a null contact name
    When an Update Vendor API request is sent to Maintenix
      | timeZone         | currencyCode | code    | notes     | vendorName         | name | faxNumber    | jobTitle     | phoneNumber  | emailAddress       | vendorTypeCode | organizationCode | certificateNumber | certificateExpiryDate        | approvalTypeCode | termsAndConds | spec2000Enabled | minPurchaseAmount | externalKey   | stdBorrowRate | defaultAirport                   | noteToReceiver | address1      | address2        | city    | country | state | zip     |
      | America/New_York | CAD          | SWA2A | TEST NOTE | Update Vendor Test |      | 999-000-6666 | Secret Agent | 999-123-6666 | james.bond@mxi.com | REPAIR         | MXI              | CERT-2            | 2050-12-15T06:11:11.000+0000 | FINANCE          | NET15         | true            |               500 | swa-ext-key2A | IATA          | CD15E4F2C6D011E686664DF6B949C9DB | See Notes      | P.O. Box 3000 | 123 Fake Street | Toronto | CAN     | ON    | P0N 1K0 |
    Then the update vendor error message "[MXERR-10000] The 'Contact Name' is a mandatory field.<br><br>Please enter a value for the 'Contact Name'." is verified

	@RefactorNonSmoke
  Scenario: An Update Vendor API request is sent to Maintenix with a null vendor name
    When an Update Vendor API request is sent to Maintenix
      | timeZone         | currencyCode | code    | notes     | vendorName | name       | faxNumber    | jobTitle     | phoneNumber  | emailAddress       | vendorTypeCode | organizationCode | certificateNumber | certificateExpiryDate        | approvalTypeCode | termsAndConds | spec2000Enabled | minPurchaseAmount | externalKey   | stdBorrowRate | defaultAirport                   | noteToReceiver | address1      | address2        | city    | country | state | zip     |
      | America/New_York | CAD          | SWA2A | TEST NOTE |            | James Bond | 999-000-6666 | Secret Agent | 999-123-6666 | james.bond@mxi.com | REPAIR         | MXI              | CERT-2            | 2050-12-15T06:11:11.000+0000 | FINANCE          | NET15         | true            |               500 | swa-ext-key2A | IATA          | CD15E4F2C6D011E686664DF6B949C9DB | See Notes      | P.O. Box 3000 | 123 Fake Street | Toronto | CAN     | ON    | P0N 1K0 |
    Then the update vendor error message "[MXERR-10000] The 'aName' is a mandatory field.<br><br>Please enter a value for the 'aName'." is verified

	@RefactorNonSmoke
  Scenario: An Update Vendor API request is sent to Maintenix with a null currency code
    When an Update Vendor API request is sent to Maintenix
      | timeZone         | currencyCode | code    | notes     | vendorName         | name       | faxNumber    | jobTitle     | phoneNumber  | emailAddress       | vendorTypeCode | organizationCode | certificateNumber | certificateExpiryDate        | approvalTypeCode | termsAndConds | spec2000Enabled | minPurchaseAmount | externalKey   | stdBorrowRate | defaultAirport                   | noteToReceiver | address1      | address2        | city    | country | state | zip     |
      | America/New_York |              | SWA2A | TEST NOTE | Update Vendor Test | James Bond | 999-000-6666 | Secret Agent | 999-123-6666 | james.bond@mxi.com | REPAIR         | MXI              | CERT-2            | 2050-12-15T06:11:11.000+0000 | FINANCE          | NET15         | true            |               500 | swa-ext-key2A | IATA          | CD15E4F2C6D011E686664DF6B949C9DB | See Notes      | P.O. Box 3000 | 123 Fake Street | Toronto | CAN     | ON    | P0N 1K0 |
    Then the update vendor error message "[MXERR-10000] The 'aCurrency' is a mandatory field.<br><br>Please enter a value for the 'aCurrency'." is verified
