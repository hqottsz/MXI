@CSI @AMAPI
Feature: Vendor History Notes and Alerts

  Background:
    Given I am a superuser

	@RefactorNonSmoke
  Scenario: Verify the history notes for create, update, and unapprove vendor for inbound messages
    Given that a user has navigated to the user alerts page
      | Page_Name   | Parent_Menu_Item | Child_Menu_Item |
      | User Alerts | Options          | Alerts          |
    Given that a user has deleted all notifications
    And I navigate to the following page
      | Page_Name     | Parent_Menu_Item  | Child_Menu_Item |
      | Vendor Search | Technical Records | Vendor Search   |
    When a Create Vendor API request is sent to Maintenix
      | code   | vendorName  | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name     |
      | SWA004 | SWA Vendor1 | MXI              | 2010-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key4 | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | John Doe |
    And an Update Vendor API request is sent to Maintenix
      | timeZone         | currencyCode | code   | notes     | vendorName         | name       | faxNumber    | jobTitle     | phoneNumber  | emailAddress       | vendorTypeCode | organizationCode | certificateNumber | certificateExpiryDate        | approvalTypeCode | termsAndConds | spec2000Enabled | minPurchaseAmount | externalKey   | stdBorrowRate | defaultAirport                   | noteToReceiver | address1      | address2        | city    | country | state | zip     |
      | America/New_York | CAD          | SWA004 | TEST NOTE | Update Vendor Test | James Bond | 999-000-6666 | Secret Agent | 999-123-6666 | james.bond@mxi.com | REPAIR         | MXI              | CERT-2            | 2050-12-15T06:11:11.000+0000 | FINANCE          | NET15         | true            |               500 | swa_test_key4 | IATA          | CD15E4F2C6D011E686664DF6B949C9DB | See Notes      | P.O. Box 3000 | 123 Fake Street | Toronto | CAN     | ON    | P0N 1K0 |
    And I search for the organization page
      | Page_Name                   | Parent_Menu_Item | Child_Menu_Item             | Org_Code |
      | Organization Search by Type | Administrator    | Organization Search by Type | MXI      |
    And a vendor is approved in Maintenix
      | Org_Code | Code   | App_Date    | Time  |
      | MXI      | SWA004 | 23-MAY-2016 | 11:10 |
    And an Update Vendor API request is sent to Maintenix to unapprove the vendor
      | code   | vendorName       | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name       | status   | reasonCode | note             |
      | SWA004 | Unapprove Vendor | MXI              | 2030-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key4 | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | James Bond | UNAPPRVD | VNPERF     | Poor Performance |
    And the page is refreshed
    And I navigate to the vendor history notes tab
      | Parent_Menu_Item  | Child_Menu_Item | Code   |
      | Technical Records | Vendor Search   | SWA004 |
    Then the appropriate history notes are logged
      | Create_Vendor_Note                             | Update_Vendor_Note                             | Unapprove_Vendor_Note  |
      | Vendor has been created by an external system. | Vendor has been updated by an external system. | Vendor was unapproved. |

	@SmokeTest
  Scenario: Verify the user alerts for create, update, and unapprove vendor
    Given that a user has navigated to the user alerts page
      | Page_Name   | Parent_Menu_Item | Child_Menu_Item |
      | User Alerts | Options          | Alerts          |
    Then the appropriate user alerts were logged
      | Create_Vendor_Alert             | Update_Vendor_Alert             | Unapprove_Alert                                    |
      | Vendor SWA004 has been created. | Vendor SWA004 has been updated. | Vendor SWA004 was unapproved for organization MXI. |
