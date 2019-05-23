@CSI @AMAPI
Feature: Unapprove Vendor

	@RefactorNonSmoke
  Scenario: An approved vendor in Maintenix is unapproved using vendor code
    Given I am a superuser
    And I navigate to the following page
      | Page_Name     | Parent_Menu_Item  | Child_Menu_Item |
      | Vendor Search | Technical Records | Vendor Search   |
    And a Create Vendor API request is sent to Maintenix
      | code   | vendorName       | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name     |
      | SWA003 | Unapprove Vendor | MXI              | 2030-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key3 | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | John Doe |
    And I search for the organization page
      | Parent_Menu_Item | Child_Menu_Item             | Org_Code |
      | Administrator    | Organization Search by Type | MXI      |
    And a vendor is approved in Maintenix
      | Org_Code | Code   | Ext_Key       | Org_Code | App_Date    | Time  |
      | MXI      | SWA003 | swa-ext-key-3 | MXI      | 23-MAY-2020 | 11:10 |
    When an Update Vendor API request is sent to Maintenix to unapprove the vendor
      | code    | vendorName       | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name       | status   | reasonCode | note             |
      | SWA003  | Unapprove Vendor | MXI              | 2030-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa-ext-key3A | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | James Bond | UNAPPRVD | VNPERF     | Poor Performance |
    Then the vendor is unapproved in Maintenix
      | Code   | Ext_Key       | Org_Code | Status   | Reason | Note             |
      | SWA003 | swa-ext-key-3 | MXI      | UNAPPRVD | PP     | Poor Performance |
