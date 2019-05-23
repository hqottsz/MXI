@CSI @FinanceAdapter
Feature: Receive Create Purchase Invoice and Send Purchase Invoice Created - Happy Path, No Vendor, No Line Desc, No PN, No Inv Qty Unit, No Account

  Background:
    Given I am a superuser
      And that the Create Order page has been navigated to

	@SmokeTest
  Scenario: Create Purchase Invoice received, Invoice Created, Purchase Invoice Created sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type  | Total Receive Shipment Quantity |
      | S000003 | AIRPORT1/DOCK    |                 1 | EA            |  30001 |                         1 | 01-JAN-2016       | Serialized |                               1 |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message
      | Invoice Number | Invoice Date        | Currency | Line Number | Line Description     | Part Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number | Vendor Code | Vendor Name    |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | Sample Description 1 | S000003     |                1 | EA                    |          1 |            2 |              1 |       30001 | Vendor ExKey 1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description              | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | S000003 (SER Part Force Insp) | 1 EA |                      1 | 30001 (Vendor ExKey 1) |                 2 | CLOSED (The order has been marked for payment and is therefore closed.) |

  # No Vendor Provided and Order Is Provided
  @RefactorNonSmoke
  Scenario: Create Purchase Invoice received with No Vendor Information, Invoice still Created, Purchase Invoice Created sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | B000001 | AIRPORT1/DOCK    |                 5 | EA            |  30001 |                         5 | 01-JAN-2016       | Batch     |                               5 |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Vendor Information
      | Invoice Number | Invoice Date        | Currency | Line Number | Line Description     | Part Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | Sample Description 1 | B000001     |                5 | EA                    |          1 |            2 |              1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description                | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | B000001 (Batch Part Force Insp) | 5 EA |                      1 | 30001 (Vendor ExKey 1) |                 2 | CLOSED (The order has been marked for payment and is therefore closed.) |

  # No Line Description for Part, No Line Description for Miscellaneous Line
  @RefactorNonSmoke
  Scenario: CreatePurchaseInv, NO Line Desc, Inv Created, Line Desc is received part Shipment Line, PurchaseInvCreated sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | B000001 | AIRPORT1/DOCK    |                 5 | EA            |  30001 |                         5 | 01-JAN-2016       | Batch     |                               5 |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Line Description
      | Invoice Number | Invoice Date        | Currency | Line Number | Part Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | B000001     |                5 | EA                    |          1 |            2 |              1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description                | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | B000001 (Batch Part Force Insp) | 5 EA |                      1 | 30001 (Vendor ExKey 1) |                 2 | CLOSED (The order has been marked for payment and is therefore closed.) |

  @RefactorNonSmoke
  Scenario: CreatePurchaseInv, NO Line Desc for Misc, Inv Created, Line Desc set to PO Misc Desc, PurchaseInvCreated sent
    Given that I have created an order with a miscellaneous line
      | Ship To Location | Vendor | Part Type     | Miscellaneous Line Description | Quantity | Quantity Unit | Unit Price | Account |
      | AIRPORT1/DOCK    |  30001 | Miscellaneous | MISC SAMPLE LINE DESC 1        |        1 | EA            |          1 |       5 |
    When the external system completes two way matching and sends an VALID inbound Create Purchase Invoice message without Line Description
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                1 | EA                    |          1 |            5 |              1 |
    Then an invoice with Line Description set to Order Miscellaneous Line Description is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Miscellaneous Line Description | Qty  | Purchase Order Line No | Vendor                 |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | MISC SAMPLE LINE DESC 1        | 1 EA |                      1 | 30001 (Vendor ExKey 1) |

  # No Part Number, One Shipment Line with One Order Line and Receive Same PNs, Multiple Shipment Lines with One Order Line and Receive Alternate PNs
  # Add Ignore, because it failed intermittent
  @RefactorNonSmoke
  Scenario: CreatePurchaseInv, No Part Info, No Vendor nor Line Desc, Inv Created, PurchaseInvCreated sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | B000001 | AIRPORT1/DOCK    |                 5 | EA            |  30001 |                         5 | 01-JAN-2016       | Batch     |                               5 |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Part Information
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                5 | EA                    |          1 |            2 |              1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description                | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | B000001 (Batch Part Force Insp) | 5 EA |                      1 | 30001 (Vendor ExKey 1) |                 2 | CLOSED (The order has been marked for payment and is therefore closed.) |

  # Add Ignore, because it failed intermittent
  @RefactorNonSmoke
  Scenario: Rec ORDERED PN QTY greater than one, CreatePurchaseInv, No Part Info, No Ven nor Line Desc, Inv Created, PurchaseInvCreated sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type  | Total Receive Shipment Quantity |
      | S000003 | AIRPORT1/DOCK    |                 2 | EA            |  30001 |                         1 | 01-JAN-2016       | Serialized |                               2 |
      |         |                  |                   |               |        |                         1 |                   |            |                                 |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Part Information
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number | Vendor Code | Vendor Name    |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                2 | EA                    |          2 |            2 |              1 |       30001 | Vendor ExKey 1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description              | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | S000003 (SER Part Force Insp) | 2 EA |                      1 | 30001 (Vendor ExKey 1) |                 2 | CLOSED (The order has been marked for payment and is therefore closed.) |

  # Add Ignore, because it failed intermittent
  @RefactorNonSmoke
  Scenario: Rec ALT PN QTY greater than one, CreatePurchaseInv, No Part Info, No Ven nor Line Desc, Inv Created, PurchaseInvCreated sent
    Given that I have received an order with all alternate parts and inspected the inventory as serviceable
      | Part No | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type  | Received Part No |
      | S000003 | AIRPORT1/DOCK    |                 2 | EA            |  30001 |                         2 | 01-JAN-2016       | Serialized | S000004          |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Part Information
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number | Vendor Code | Vendor Name    |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                2 | EA                    |          2 |            2 |              1 |       30001 | Vendor ExKey 1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description                  | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | S000004 (SER ALT Part Force Insp) | 2 EA |                      1 | 30001 (Vendor ExKey 1) |                 2 | CLOSED (The order has been marked for payment and is therefore closed.) |

  # No Part Number, Multiple Shipment Lines with One Order Line and Receive both STANDARD and Alternate PNs
  # Add Ignore, because it failed intermittent
  @RefactorNonSmoke
  Scenario: Rec STANDARD and ALT PNs, CreatePurchaseInv, No Part Info, No Ven nor Line Desc, Inv Created, PurchaseInvCreated sent
    Given that I have received an order with some alternate parts and inspected the inventory as serviceable
      | Part No | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type  | Received Part No Shipment Line One | Received Part No Shipment Line Two |
      | S000003 | AIRPORT1/DOCK    |                 2 | EA            |  30001 |                         2 | 01-JAN-2016       | Serialized | S000004                            | S000003                            |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Part Information
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number | Vendor Code | Vendor Name    |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                2 | EA                    |          2 |            2 |              1 |       30001 | Vendor ExKey 1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description              | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | S000003 (SER Part Force Insp) | 2 EA |                      1 | 30001 (Vendor ExKey 1) |                 2 | CLOSED (The order has been marked for payment and is therefore closed.) |

  # No Invoice Quantity Unit for Part, No Invoice Quantity Unit for Miscellaneous Line
  # Add Ignore, because it failed intermittent
  @RefactorNonSmoke
  Scenario: CreatePurchaseInv, No Inv QTY Unit, No Ven nor Line Desc nor Part Info, Inv Created, PurchaseInvCreated sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No      | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | TRK-MISMATCH | AIRPORT1/DOCK    |                 1 | EA            |  30001 |                         1 | 01-JAN-2016       | Tracked   |                               1 |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Invoice Quantity Unit Information
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Unit Price | Account Code | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                1 |          1 |            2 |              1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description            | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | TRK-MISMATCH (TRK-MISMATCH) | 1 EA |                      1 | 30001 (Vendor ExKey 1) |                 1 | CLOSED (The order has been marked for payment and is therefore closed.) |

  # Add Ignore, because it failed intermittent
  @RefactorNonSmoke
  Scenario: CreatePurchaseInv, NO Inv QTY Unit for Misc, Inv Created, Inv QTY Unit set to PO Misc QTY Unit, PurchaseInvCreated sent
    Given that I have created an order with a miscellaneous line
      | Ship To Location | Vendor | Part Type     | Miscellaneous Line Description | Quantity | Quantity Unit | Unit Price | Account |
      | AIRPORT1/DOCK    |  30001 | Miscellaneous | MISC SAMPLE LINE DESC 1        |        1 | EA            |          1 |       5 |
    When the external system completes two way matching and sends an VALID inbound Create Purchase Invoice message without Invoice Quantity Unit Information
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                1 | EA                    |          1 |            5 |              1 |
    Then an invoice with Invoice Quantity Unit set to Order Miscellaneous Line Quantity Unit is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Miscellaneous Line Description | Qty  | Purchase Order Line No | Vendor                 |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | MISC SAMPLE LINE DESC 1        | 1 EA |                      1 | 30001 (Vendor ExKey 1) |

  # No Account for Part, No Account for Miscellaneous Line
  # Add Ignore, because it failed intermittent
  @RefactorNonSmoke
  Scenario: CreatePurchaseInv, NO Acc, No Ven nor Desc nor Part Info nor Inv QTY Unit, Inv Created, PurchaseInvCreated sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No      | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | TRK-MISMATCH | AIRPORT1/DOCK    |                 1 | EA            |  30001 |                         1 | 01-JAN-2016       | Tracked   |                               1 |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Account
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Unit Price | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                1 |          1 |              1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description            | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | TRK-MISMATCH (TRK-MISMATCH) | 1 EA |                      1 | 30001 (Vendor ExKey 1) |                 1 | CLOSED (The order has been marked for payment and is therefore closed.) |

  @RefactorNonSmoke
  Scenario: CreatePurchaseInv, NO Acc for Misc, No Ven nor Desc nor Part Info nor Inv QTY Unit, Inv Created, PurchaseInvCreated sent
    Given that I have created an order with a miscellaneous line
      | Ship To Location | Vendor | Part Type     | Miscellaneous Line Description | Quantity | Quantity Unit | Unit Price | Account |
      | AIRPORT1/DOCK    |  30001 | Miscellaneous | MISC SAMPLE LINE DESC 1        |        1 | EA            |          1 |       5 |
    When the external system completes two way matching and sends an VALID inbound Create Purchase Invoice message without Account
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Invoice Quantity Unit | Unit Price | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                1 | EA                    |          1 |              1 |
    Then an invoice with Account set to Order Miscellaneous Line Account is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Miscellaneous Line Description | Qty  | Purchase Order Line No | Vendor                 | Charge To Account |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | MISC SAMPLE LINE DESC 1        | 1 EA |                      1 | 30001 (Vendor ExKey 1) |                 5 |

  # Separate Purchase Orders with Same PN and No Optional Fields, Partial Receipt of Tracked PN on Purchase Order and Invoice for Only One Line with No Optional Fields
  @RefactorNonSmoke
  Scenario: Rec same PN from separate POs, CreatePurchaseInv, No Optional Fields, PurchaseInvCreated sent
    Given that I have received both orders and inspected both inventory as serviceable
      | Part No | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type  | Total Receive Shipment Quantity |
      | S000003 | AIRPORT1/DOCK    |                 1 | EA            |  30001 |                         1 | 01-JAN-2016       | Serialized |                               1 |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message with two Invoice Lines
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Unit Price | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                1 |          1 |              1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description              | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                                            |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | S000003 (SER Part Force Insp) | 1 EA |                      1 | 30001 (Vendor ExKey 1) |                 2 | CLOSED (The order has been marked for payment and is therefore closed.) |
      | INVJS             | 11-MAR-2016 10:00 EST |       2 | S000003 (SER Part Force Insp) | 1 EA |                      1 | 30001 (Vendor ExKey 1) |                 2 | CLOSED (The order has been marked for payment and is therefore closed.) |

  @RefactorNonSmoke
  Scenario: Rec QTY 1 of 2, CreatePurchaseInv, No Optional Fields, PurchaseInvCreated sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No      | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | TRK-MISMATCH | AIRPORT1/DOCK    |                 2 | EA            |  30001 |                         1 | 01-JAN-2016       | Tracked   |                               1 |
      |              |                  |                   |               |        |                         0 |                   |           |                                 |
    When the external system completes three way matching and sends an VALID inbound Create Purchase Invoice message without Account
      | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Unit Price | PO Line Number |
      | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                1 |          1 |              1 |
    Then an invoice is created and a Purchase Invoice Created message is sent
      | PO Invoice Number | PO Invoice Date       | Line No | Line Description            | Qty  | Purchase Order Line No | Vendor                 | Charge To Account | Order Status                                     |
      | INVJS             | 11-MAR-2016 10:00 EST |       1 | TRK-MISMATCH (TRK-MISMATCH) | 1 EA |                      1 | 30001 (Vendor ExKey 1) |                 1 | PARTIAL (The order has been partially received.) |
