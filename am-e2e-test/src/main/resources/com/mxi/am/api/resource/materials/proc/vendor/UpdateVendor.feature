@CSI @AMAPI
Feature: Update Vendor

  Background:
    Given I am a superuser
    And I navigate to the following page
      | Page_Name     | Parent_Menu_Item  | Child_Menu_Item |
      | Vendor Search | Technical Records | Vendor Search   |

	@SmokeTest
  Scenario: Update Vendor message is sent to Maintenix with a vendor code which exists
    Given a Create Vendor API request is sent to Maintenix
      | code   | vendorName  | organizationCode | certificateExpiryDate        | vendorTypeCode | currencyCode | minPurchaseAmount | externalKey   | timeZone        | address1       | city   | country | name     |
      | SWA002 | SWA Vendor2 | MXI              | 2030-12-15T06:10:15.000+0000 | PURCHASE       | USD          |                25 | swa_test_key2 | America/Chicago | P.O. Box 36647 | Ottawa | CAN     | John Doe |
    When an Update Vendor API request is sent to Maintenix
      | timeZone         | currencyCode | code   | notes     | vendorName         | name       | faxNumber    | jobTitle     | phoneNumber  | emailAddress       | vendorTypeCode | organizationCode | certificateNumber | certificateExpiryDate        | approvalTypeCode | termsAndConds | spec2000Enabled | minPurchaseAmount | externalKey  | stdBorrowRate | defaultAirport                   | noteToReceiver | address1      | address2        | city    | country | state | zip     |
      | America/New_York | CAD          | SWA002 | TEST NOTE | Update Vendor Test | James Bond | 999-000-6666 | Secret Agent | 999-123-6666 | james.bond@mxi.com | REPAIR         | MXI              | CERT-2            | 2050-12-15T06:11:11.000+0000 | FINANCE          | NET15         | true            |               500 | swa-ext-key2 | IATA          | CD15E4F2C6D011E686664DF6B949C9DB | See Notes      | P.O. Box 3000 | 123 Fake Street | Toronto | CAN     | ON    | P0N 1K0 |
    Then the vendor is updated in Maintenix
      | Code   | Name               | Cert_No | Type            | Terms_Cond                                              | Currency               | Ext_Key      | Time_Zone                                    | Cert_Exp_Date         | Approv_Type       | Min_Purch_Amt | Borrow_Rate               | Add_1                         | City    | State | Country | Zip     | Contact_Name | Job_Title    | Phone_No     | Fax_No       | Email              |
      | SWA002 | Update Vendor Test | CERT-2  | REPAIR (REPAIR) | NET15 (Invoice must be paid within 15 days of receipt.) | CAD (Canadian Dollars) | swa-ext-key2 | America/New York - Eastern Standard Time EST | 15-DEC-2050 01:11 EST | FINANCE (Finance) | 500.00 CAD    | IATA (Standard IATA Rate) | P.O. Box 3000 123 Fake Street | Toronto | ON    | CANADA  | P0N 1K0 | James Bond   | Secret Agent | 999-123-6666 | 999-000-6666 | james.bond@mxi.com |
