@MaterialManager
Feature: Add Auto-Reservation Ignore Location
	As a Material Manager
	I want to ignore a sub-location for auto-reservation under a supply location
	So that the inventory on this sub-location won't be picked by auto-reservation

Background:
	Given I am a superuser

	@E2ESmokeBuild1
	Scenario: The Material Manager adds a sub-location to a supply location's auto-reservation ignore location
		Given I am a Material Manager and I want to add ignore location to a supply location
		When I add a sub-location to Ignore Location list
		Then the sub-location is added successfully

