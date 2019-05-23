@RunAMLine
@LineTechnician
Feature: Resolution Config Slot

	As a Line Technician
	I would like to log and close a fault with a resolution config slot
	So that I can record the actual system the fault was resolved on

	Background:
		Given I am a line technician

	# Ignore temporarily due to WebLogic 12.2.1
	@E2ESmokeBuild1 @EnsureTheRecurringFaultsPageIsNotTriggered @Ignore
	Scenario: Line Technician selects a resolution config slot in the log and close fault workflow
  		Given I want to record a fault in Maintenix
		When I log and close the fault with a resolution config slot
		Then the resolution config slot is recorded on the fault

	# Ignore temporarily due to WebLogic 12.2.1
    @E2ESmokeBuild1 @WorkCaptureResolutionConfigSlot @Ignore
	Scenario: Line Technician sets a resolution config slot during edit work capture on corrective task
        When I set the resolution config slot during edit work capture on a fault's corrective task
        Then the resolution config slot is recorded on the fault


