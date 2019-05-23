Feature: Check-out and Check-in Tool
	As a Storeroom Clerk
	I want to be able to check-out and check-in tools to users and verify history notes are created to keep track of tools

Background:
	Given I am a storeroom clerk

@E2ESmokeBuild1
@BulkToolCheckOut
	Scenario: The Storeroom Clerk checks-out tools to a user
		When a user checks out tools from the Storeroom Clerk
		Then the tools appear under the Checked Out Tools tab
		And the tools are marked as Checked Out
		And a checked-out history note is added to the inventory

@E2ESmokeBuild1
@BulkToolCheckIn
	Scenario: The Storeroom Clerk checks-in tools from a user
		When a user checks in tools to the Storeroom Clerk
		Then the tools are not marked as Checked Out
		And a checked-in history note is added to the inventory
