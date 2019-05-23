@Finance
Feature: PayInvoiceforUnsercieable

	@E2ESmokeBuild1
	Scenario: Pay invoice for a purchase order after we inspect the received inventory as unserviceable
    	Given inventory is inspected as unserviceable
       	  And I am an invoicing agent
          And I have a purchase order to be invoiced and the received inventory is inspected as unserviceable
      	 When I validate and pay the invoice
      	 Then the invoice is paid
       	  And the Purchase order is closed



