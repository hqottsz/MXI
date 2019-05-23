@MaterialController
Feature: Add and update ETA for deferred fault part requests
As a material controller, I need to monitor the latest availability of part requests for deferred fault and then update the Estimated Arrival Date/time for them

# Note this could be at least two independent tests, but each would require
# refreshing the mview, costing time.



	Background:
	   Given I am a material controller
	   And I go to deferred fault part requests

	@E2ESmokeBuild1
	Scenario: Update ETA and notes
		When I select multiple fault part requests
		And I click Update ETA
		And I update ETA and Delivery Notes
		Then the part requests have newly added ETA and notes
		When I select one fault part request
		And I click Update ETA
		And I clear ETA
		Then the part request loses ETA
