@CSI @FinanceAPI
Feature: Send Order Inventory Received - Inspection Required

  Background:
    Given I am a superuser
    Given that the Create Order page has been navigated to

	@RefactorNonSmoke
  Scenario: Partial Receive and Inspect as Serviceable a BATCH part which DOES require inspection
    Given that I have filled in a batch part
      | Batch Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | B000001       | AIRPORT1/DOCK    |                10 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
    Given that I have requested authorization and issued the order
    Given that I have received the order
      | Part No | Receive Shipment Quantity | Manufactured Date | Part Type |
      | B000001 |                         5 | 01-JAN-2016       | Batch     |
    When I inspect the inventory as serviceable
    Then Receipt Order message is sent
      | Part No | Standard Quantity | Standard Unit | Vendor Quantity | Vendor Unit | Order Line Number | Manufacturer Code |
      | B000001 |               5.0 | EA            |             5.0 | EA          |                 1 |        1234567890 |

	@RefactorNonSmoke
  Scenario: Receive and Inspect as Serviceable a SER part which DOES require inspection
    Given that I have filled in a serialized part
      | Serialized Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | S000003            | AIRPORT1/DOCK    |                 1 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
    Given that I have requested authorization and issued the order
    Given that I have received the order
      | Part No | Receive Shipment Quantity | Manufactured Date | Part Type  |
      | S000003 |                         1 | 01-JAN-2016       | Serialized |
    When I inspect the inventory as serviceable
    Then Receipt Order message is sent
      | Part No | Standard Quantity | Standard Unit | Vendor Quantity | Vendor Unit | Order Line Number | Manufacturer Code |
      | S000003 |               1.0 | EA            |             1.0 | EA          |                 1 |        1234567890 |

	@RefactorNonSmoke
  Scenario: Receive a TRK part which DOES require inspection
    Given that I have filled in a tracked part
      | Tracked Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | TRK-MISMATCH    | AIRPORT1/DOCK    |                 1 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
    Given that I have requested authorization and issued the order
    Given that I have received the order
      | Part No      | Receive Shipment Quantity | Manufactured Date | Part Type |
      | TRK-MISMATCH |                         1 | 01-JAN-2016       | Tracked   |
    Then Receipt Order message is not sent
