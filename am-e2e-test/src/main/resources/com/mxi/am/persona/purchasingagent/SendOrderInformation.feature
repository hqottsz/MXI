@CSI @FinanceAPI
Feature: Send Send Order Information

Background:
    Given I am a superuser
      And that the Create Order page has been navigated to

	@RefactorNonSmoke
	Scenario: PO is created
	  	When I fill in at least the mandatory fields and click the OK button
	    Then PO is created

	@SmokeTest
	Scenario: PO is created using a Vendor with an External Key
	  	Given that the part Vendor has an External Key
	  	When I fill in at least the mandatory fields including a part and click the OK button
	  	Then Send Order Information message with action "create" contains both order creation date and external key values

	@RefactorNonSmoke
	Scenario: PO is created using a Vendor with no External Key
	  	Given that the part Vendor has no External Key
	  	When I fill in at least the mandatory fields including a part and click the OK button
	  	Then Send Order Information message with action Create contains order creation date value and no external key value

	@RefactorNonSmoke
	Scenario: PO is created using a Vendor with an External Key and issued
	  	Given that a PO has been created using a Vendor with an External Key
	  	Given that Request Authorization has been approved
	  	When I issue the order
	  	Then Send Order Information message with action "issue" contains both order creation date and external key values

	@RefactorNonSmoke
	Scenario: PO is created using a Vendor with an External Key and is both issued then cancelled
	    Given that a PO has been created using a Vendor with an External Key
	    Given that Request Authorization has been approved
	    Given that the PO has been issued
	    When I cancel the order
	    Then Send Order Information message with action "cancel" contains both order creation date and external key values