@CSI @FinanceAdapter
Feature: Receive Create Purchase Invoice and Send Error Response

  Background:
    Given I am a superuser
      And that the Create Order page has been navigated to

	# No Vendor Provided and No Order Provided, Invalid Order Provided
	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke
  Scenario: Create Purchase Invoice received with No Vendor Information nor Order Information, Invoice is NOT Created, Error Response Message sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No      | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | TRK-MISMATCH | AIRPORT1/DOCK    |                 1 | EA            |  30001 |                         1 | 01-JAN-2016       | Tracked   |                               1 |
    When the external system completes three way matching and sends an INVALID inbound Create Purchase Invoice message
      | Invalid Message Fields | Invoice Number | Invoice Date        | Currency | Line Number | Line Description     | Part Number  | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number | Error Message                                                                                                        | Namespace                                    |
      | No Vendor and No Order | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | Sample Description 1 | TRK-MISMATCH |                1 | EA                    |          1 |            2 |              1 | Vendor information must be provided because order information was not provided in the CreatePurchaseInvoice message. | http://xml.mxi.com/xsd/integration/error/1.1 |
    Then an invoice is NOT created and an Error Message is sent
      | Part Type | PO Invoice Number | Error Message                                                                                                        | Namespace                                    |
      | Tracked   | INVJS             | Vendor information must be provided because order information was not provided in the CreatePurchaseInvoice message. | http://xml.mxi.com/xsd/integration/error/1.1 |

	@RefactorNonSmoke
  Scenario: Create Purchase Invoice received with Invalid Order Information, Invoice is NOT Created, Error Response Message sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No      | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | TRK-MISMATCH | AIRPORT1/DOCK    |                 1 | EA            |  30001 |                         1 | 01-JAN-2016       | Tracked   |                               1 |
    When the external system completes three way matching and sends an INVALID inbound Create Purchase Invoice message
      | Invalid Message Fields | Invoice Number | Invoice Date        | Currency | Line Number | Line Description     | Part Number  | Invoice Quantity | Invoice Quantity Unit | Unit Price | Account Code | PO Line Number | Error Message                                                                                                                                                       | Namespace                                    |
      | Invalid Order          | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | Sample Description 1 | TRK-MISMATCH |                1 | EA                    |          1 |            2 |             99 | Order number or order line number provided in CreatePurchaseInvoice message is invalid. Please provide valid value for order number and order line number elements. | http://xml.mxi.com/xsd/integration/error/1.1 |
    Then an invoice is NOT created and an Error Message is sent
      | Part Type | PO Invoice Number | Error Message                                                                                                                                                       | Namespace                                    |
      | Tracked   | INVJS             | Order number or order line number provided in CreatePurchaseInvoice message is invalid. Please provide valid value for order number and order line number elements. | http://xml.mxi.com/xsd/integration/error/1.1 |

  # No Invoice Quantity Unit nor Order Info for Part, No Invoice Quantity Unit and Invalid Order Info for Part, No Invoice Quantity Unit and Invalid Order Info for Miscellaneous Line
  @RefactorNonSmoke
  Scenario: CreatePurchaseInv, No Inv QTY Unit nor Order Info, No Ven nor Line Desc nor Part Info, Inv NOT Created, ErrorResponseMessage sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No      | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | TRK-MISMATCH | AIRPORT1/DOCK    |                 1 | EA            |  30001 |                         1 | 01-JAN-2016       | Tracked   |                               1 |
    When the external system completes three way matching and sends an INVALID inbound Create Purchase Invoice message
      | Invalid Message Fields                     | Invoice Number | Invoice Date        | Currency | Line Number | Part Number  | Invoice Quantity | Unit Price | Account Code | PO Line Number | Error Message                                                                                                                              | Namespace                                    |
      | No Invoice Quantity Unit and No Order Info | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | TRK-MISMATCH |                1 |          1 |            2 |              1 | Quantity unit of measure of invoice line must be provided because order information was not provided in the CreatePurchaseInvoice message. | http://xml.mxi.com/xsd/integration/error/1.1 |
    Then an invoice is NOT created and an Error Message is sent
      | Part Type | PO Invoice Number | Error Message                                                                                                                              | Namespace                                    |
      | Tracked   | INVJS             | Quantity unit of measure of invoice line must be provided because order information was not provided in the CreatePurchaseInvoice message. | http://xml.mxi.com/xsd/integration/error/1.1 |

	@RefactorNonSmoke
  Scenario: CreatePurchaseInv, No Inv QTY Unit Invalid Order Info, No Ven nor Desc nor Part Info, Inv NOT Created, ErrorResponseMessage sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No      | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | TRK-MISMATCH | AIRPORT1/DOCK    |                 1 | EA            |  30001 |                         1 | 01-JAN-2016       | Tracked   |                               1 |
    When the external system completes three way matching and sends an INVALID inbound Create Purchase Invoice message
      | Invalid Message Fields                          | Invoice Number | Invoice Date        | Currency | Line Number | Part Number  | Invoice Quantity | Unit Price | Account Code | PO Line Number | Error Message                                                                                                                                                       | Namespace                                    |
      | No Invoice Quantity Unit and Invalid Order Info | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | TRK-MISMATCH |                1 |          1 |            2 |             99 | Order number or order line number provided in CreatePurchaseInvoice message is invalid. Please provide valid value for order number and order line number elements. | http://xml.mxi.com/xsd/integration/error/1.1 |
    Then an invoice is NOT created and an Error Message is sent
      | Part Type | PO Invoice Number | Error Message                                                                                                                                                       | Namespace                                    |
      | Tracked   | INVJS             | Order number or order line number provided in CreatePurchaseInvoice message is invalid. Please provide valid value for order number and order line number elements. | http://xml.mxi.com/xsd/integration/error/1.1 |

	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke
  Scenario: CreatePurchaseInv,NO Inv QTY Unit for Misc Invalid Order Info, Inv NOT Created, ErrorResponseMessage sent
    Given that I have created an order with a miscellaneous line
      | Ship To Location | Vendor | Part Type     | Miscellaneous Line Description | Quantity | Quantity Unit | Unit Price | Account |
      | AIRPORT1/DOCK    |  30001 | Miscellaneous | MISC SAMPLE LINE DESC 1        |        1 | EA            |          1 |       5 |
    When the external system completes two way matching and sends an INVALID inbound Create Purchase Invoice message
      | Invalid Message Fields                          | Invoice Number | Invoice Date        | Currency | Line Number | Invoice Quantity | Unit Price | Account Code | PO Line Number | Error Message                                                                                                                                                       | Namespace                                    |
      | No Invoice Quantity Unit and Invalid Order Info | INVJS          | 2016-03-11 10:00:00 | USD      |           1 |                1 |          1 |            5 |             99 | Order number or order line number provided in CreatePurchaseInvoice message is invalid. Please provide valid value for order number and order line number elements. | http://xml.mxi.com/xsd/integration/error/1.1 |
    Then an invoice is NOT created and an Error Message is sent
      | Part Type     | PO Invoice Number | Error Message                                                                                                                                                       | Namespace                                    |
      | Miscellaneous | INVJS             | Order number or order line number provided in CreatePurchaseInvoice message is invalid. Please provide valid value for order number and order line number elements. | http://xml.mxi.com/xsd/integration/error/1.1 |

  	# No Account nor Order Info for Part
  	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke
  Scenario: CreatePurchaseInv, NO Acc nor Order Info, No Ven nor Desc nor Part Info nor Inv QTY Unit, Inv Created, ErrorResponseMessage sent
    Given that I have received an order and inspected the inventory as serviceable
      | Part No      | Ship To Location | Standard Quantity | Standard Unit | Vendor | Receive Shipment Quantity | Manufactured Date | Part Type | Total Receive Shipment Quantity |
      | TRK-MISMATCH | AIRPORT1/DOCK    |                 1 | EA            |  30001 |                         1 | 01-JAN-2016       | Tracked   |                               1 |
    When the external system completes three way matching and sends an INVALID inbound Create Purchase Invoice message
      | Invalid Message Fields       | Invoice Number | Invoice Date        | Currency | Line Number | Part Number  | Invoice Quantity | Unit Price | PO Line Number | Error Message                                                                                                                  | Namespace                                    |
      | No Account and No Order Info | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | TRK-MISMATCH |                1 |          1 |              1 | Account code of invoice line must be provided because order information was not provided in the CreatePurchaseInvoice message. | http://xml.mxi.com/xsd/integration/error/1.1 |
    Then an invoice is NOT created and an Error Message is sent
      | Part Type | PO Invoice Number | Error Message                                                                                                                  | Namespace                                    |
      | Tracked   | INVJS             | Account code of invoice line must be provided because order information was not provided in the CreatePurchaseInvoice message. | http://xml.mxi.com/xsd/integration/error/1.1 |

  # Inventory Not Inspected as Serviceable
	@RefactorNonSmoke
  Scenario: CreatePurchaseInv, Issued, Not Received, No Part Number, Inv NOT Created, ErrorResponseMessage sent
    Given that I have filled in a tracked part
      | Tracked Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | TRK-MISMATCH    | AIRPORT1/DOCK    |                 1 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
    Given that I have requested authorization and issued the order
    When the external system completes three way matching and sends an INVALID inbound Create Purchase Invoice message
      | Invalid Message Fields           | Invoice Number | Invoice Date        | Currency | Line Number | Part Number  | Invoice Quantity | Unit Price | PO Line Number | Error Message                                                                                                                                                | Namespace                                    |
      | Not Received, No Optional Fields | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | TRK-MISMATCH |                1 |          1 |              1 | Error in business process com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice30.process: The available quantity for invoice must be positive value. | http://xml.mxi.com/xsd/integration/error/1.1 |
    Then an invoice is NOT created and an Error Message is sent
      | Part Type             | PO Invoice Number | Error Message                                                                                                                                                | Namespace                                    |
      | Tracked, Not Received | INVJS             | Error in business process com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice30.process: The available quantity for invoice must be positive value. | http://xml.mxi.com/xsd/integration/error/1.1 |

	@RefactorNonSmoke
  Scenario: CreatePurchaseInv, Issued and Received, Not Inspected, No Part Number, Inv NOT Created, ErrorResponseMessage sent
    Given that I have filled in a tracked part
      | Tracked Part No | Ship To Location | Standard Quantity | Standard Unit | Transportation Type | Terms & Conditions | Freight On Board  | Vendor |
      | TRK-MISMATCH    | AIRPORT1/DOCK    |                 1 | EA            | AIR (AIR)           | NET30 (NET30)      | RECEIPT (RECEIPT) |  30001 |
    Given that I have requested authorization and issued the order
    Given that I have received the order
      | Part No      | Receive Shipment Quantity | Manufactured Date | Part Type |
      | TRK-MISMATCH |                         1 | 01-JAN-2016       | Tracked   |
    When the external system completes three way matching and sends an INVALID inbound Create Purchase Invoice message
      | Invalid Message Fields       | Invoice Number | Invoice Date        | Currency | Line Number | Part Number  | Invoice Quantity | Unit Price | PO Line Number | Error Message                                                                                                                                                | Namespace                                    |
      | Received, No Optional Fields | INVJS          | 2016-03-11 10:00:00 | USD      |           1 | TRK-MISMATCH |                1 |          1 |              1 | Error in business process com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice30.process: The available quantity for invoice must be positive value. | http://xml.mxi.com/xsd/integration/error/1.1 |
    Then an invoice is NOT created and an Error Message is sent
      | Part Type         | PO Invoice Number | Error Message                                                                                                                                                | Namespace                                    |
      | Tracked, Received | INVJS             | Error in business process com.mxi.mx.core.adapter.finance.cmd.CmdCreatePurchaseInvoice30.process: The available quantity for invoice must be positive value. | http://xml.mxi.com/xsd/integration/error/1.1 |
