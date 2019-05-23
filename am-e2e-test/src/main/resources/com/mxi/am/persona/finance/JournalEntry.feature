@CSI @FinanceAPI
Feature: Journal Entry

  Background:
    Given I am a superuser
    Given I navigate to the part search page
      | Page_Name   | Parent_Menu_Item | Child_Menu_Item |
      | Part Search | Engineer         | Part Search     |

	@RefactorNonSmoke
  Scenario: An outbound message is sent when inventory is created, archived, unarchived and scrapped.
    Given I select a part
      | OEM_Part_No |
      | S000001     |
    When I create inventory
      | Location | Manufactured_Date |
      |    10001 | 23-MAY-2016       |
    Then CRTINV financial log message is generated including part details
      | Type   | Part_Number_OEM | Part_Number_Description     | Manufacturer_Code | Code |
      | CRTINV | S000001         | Serialized Part Standard EA |        1234567890 |    3 |
    When I archive inventory
      | Charge_To_Account | Password |
      |                 3 | password |
    Then ARCHIVE financial log message is generated including part details
      | Type    | Part_Number_OEM | Part_Number_Description     | Manufacturer_Code | Code |
      | ARCHIVE | S000001         | Serialized Part Standard EA |        1234567890 |    3 |
    When I unarchive inventory
      | Credit_To_Account | Location | Password | Condition             |
      |                 3 | AIRPORT1 | password | RFI (Ready for Issue) |
    Then UNARCH financial log message is generated including part details
      | Type   | Part_Number_OEM | Part_Number_Description     | Manufacturer_Code | Code |
      | UNARCH | S000001         | Serialized Part Standard EA |        1234567890 |    3 |
    When I change owner
      | New_Owner | Charge_To_Account |
      |     10002 |                 8 |
    Then CHGOWN financial log message is generated including part details
      | Type   | Part_Number_OEM | Part_Number_Description     | Manufacturer_Code | Code |
      | CHGOWN | S000001         | Serialized Part Standard EA |        1234567890 |    8 |
    Given I change owner
      | New_Owner | Charge_To_Account |
      | MXI       |                 8 |
    When I scrap the inventory
      | Password |
      | password |
    Then SCRAP financial log message is generated including part details
      | Type  | Part_Number_OEM | Part_Number_Description     | Manufacturer_Code | Code |
      | SCRAP | S000001         | Serialized Part Standard EA |        1234567890 |    2 |

	@RefactorNonSmoke
  Scenario: An outbound message is sent when unit price and quantity is adjusted for a batch part
    Given I select a part
      | OEM_Part_No |
      | B000001     |
    When I adjust unit price
      | New_Price | Charge_To_Account |
      |        12 |                 6 |
    Then ADJPRICE financial log message is generated including part details
      | Type     | Part_Number_OEM | Part_Number_Description | Manufacturer_Code | Code |
      | ADJPRICE | B000001         | Batch Part Force Insp   |        1234567890 |    6 |
    Given I create inventory for a batch part
      | Location | Manufactured_Date |
      |    10001 | 23-MAY-2016       |
    When I adjust quantity
      | Quantity | Charge_Adjustment_to_Account |
      |        3 |                            3 |
    Then QTYADJ financial log message is generated including part details
      | Type   | Part_Number_OEM | Part_Number_Description | Manufacturer_Code | Code |
      | QTYADJ | B000001         | Batch Part Force Insp   |        1234567890 |    3 |

	@SmokeTest
  Scenario: An outbound message is sent when total spares is adjusted for a tracked part
    Given I select a part
      | OEM_Part_No |
      | A0000002    |
    When I adjust total spares
      | Total_Spares | Note |
      |            5 | Note |
    Then SPARESQTYADJ financial log message is generated including part details
      | Type         | Part_Number_OEM | Part_Number_Description | Manufacturer_Code | Code |
      | SPARESQTYADJ | A0000002        | 2 Position Tracked Part |             11111 |    3 |
