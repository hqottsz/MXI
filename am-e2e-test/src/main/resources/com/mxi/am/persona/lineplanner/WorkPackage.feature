@RunAMPlanning
@MaintenancePlanner
Feature: Work Package

	@E2ESmokeBuild1
	Scenario: Create Work Package
		Given I am a line planner
		When I create a new work package
		Then a new work package is created

	@E2ESmokeBuild1
	@CompleteWorkPackage
	Scenario: Complete Work Package and Release Aircraft
		Given I am a line supervisor
		When I complete a work package and release aircraft
		Then the work package is completed and the aircraft is released