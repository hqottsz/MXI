@RunSCMaterials
Feature: Create Tool and Setup Tool Calibration
	As a Tool Manager
	I want to create records of my physical tools and coordinate their requisite calibrations
	In order to maximize the capabilities of my crib and maintain accurate records of those capabilities

Background:
	Given I am a superuser

	# First 2 lines should be kept as SmokeTest
	@RefactorNonSmoke
	Scenario: The Tool Manager captures a newly-received tool inventory in the system
		When I create a tracked tool with part number "T0000001" at location "AIRPORT1/STORE/BIN1-1"
		Then the inventory is created as a tool
			And the tool is checked in to the crib
			And the tool is ready to be checked out

	@RefactorNonSmoke
	Scenario: The Tool Manager defines a recurring calibration requirement for one of his or her tools
		When I define a calibration requirement "CALIB-REQ-TEST" for part number "T0000001"
			And the requirement "CALIB-REQ-TEST" recurs at an interval of "3" "CMON (Calendar Month)"s
			And I activate and initialize the requirement "CALIB-REQ-TEST" on all applicable tools
			And I check the upcoming tasks for a tool with part number "T0000001"
		Then an actual task instance of "CALIB-REQ-TEST" is scheduled for the tool

	@SmokeTest
	Scenario: The Tool Manager defines a repair shop as being able to calibrate one of his or her tools
		When I define a repair shop "AIRPORT1/REPAIR/SHOP2" to have repair capabilities for part number "T0000001"
		Then the repair shop "AIRPORT1/REPAIR/SHOP2" appears as a repair location for part number "T0000001"

