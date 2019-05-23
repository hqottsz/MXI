@CSI @FinanceAPI
Feature: Send Order Inventory Received

  Background:
    Given I am a superuser
      And that the Create Order page has been navigated to

	@SmokeTest
  Scenario: Inventory Information and Current Date - Receive SER part which does not require inspection
    Given that I have filled in a serialized part
      | Serialized Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | S000002            | AIRPORT1/DOCK    |                 1 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
      And that I have requested authorization and issued the order
    When I receive the order
      | Part No | Receive Shipment Quantity | Manufactured Date | Part Type  |
      | S000002 |                         1 | 01-JAN-2016       | Serialized |
    Then Receipt Order message is sent and includes part information
      | Part No | Standard Quantity | Standard Unit | Vendor Quantity | Vendor Unit | Manufacturer Code |
      | S000002 |               1.0 | EA            |               1 | EA          |        1234567890 |

	@RefactorNonSmoke
  Scenario: Order Information - Receive SER part which does not require inspection however was purchased using an Alternate Purchase Unit of Measure
    Given that I have filled in a serialized part using alternate purchase unit of measure
      | Serialized Part No | Ship To Location | Vendor Quantity | Vendor Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | S000001            | AIRPORT1/DOCK    |               1 | BOX_5DEC    | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
      And that I have requested authorization and issued the order
    When I receive the order
      | Part No | Receive Shipment Quantity | Manufactured Date | Part Type  |
      | S000001 |                         1 | 01-JAN-2016       | Serialized |
    Then Receipt Order message is sent and includes order information
      | Part No | Standard Quantity | Standard Unit | Vendor Quantity | Vendor Unit | Order Line Number |
      | S000001 |               1.0 | EA            |         0.16667 | BOX_5DEC    |                 1 |

	@RefactorNonSmoke
  Scenario: Order Information - Partially receive BATCH part which does not require inspection
    Given that I have filled in a batch part
      | Batch Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | B000002       | AIRPORT1/DOCK    |                10 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
    Given that I have requested authorization and issued the order
    When I receive the order
      | Part No | Receive Shipment Quantity | Manufactured Date | Part Type |
      | B000002 |                         5 | 01-JAN-2016       | Batch     |
    Then Receipt Order message is sent and includes order information
      | Part No | Standard Quantity | Standard Unit | Vendor Quantity | Vendor Unit | Order Line Number |
      | B000002 |               5.0 | EA            |             5.0 | EA          |                 1 |
