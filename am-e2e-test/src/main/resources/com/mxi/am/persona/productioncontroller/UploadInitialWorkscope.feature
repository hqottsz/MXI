@RunAMBaseMaintenance
Feature: Upload Initial Workscope Spreadsheet

	Description: As a production controller, I upload an initial workscope spreadsheet
	Product: Maintenix OPER Edition
	Persona: Production Controller
	Interaction: Upload initial workscope

	Background:
		Given I am a superuser

	@SmokeTest @Ignore
	Scenario: Production Controller creates work package setup
		When I create work package setup
		Then the work package setup is available for workscope upload

	# First 3 lines should be kept as SmokeTest
	@RefactorNonSmoke @Ignore
	Scenario: Production Controller uploads a workscope spreadsheet that contains adhoc tasks against root slot
		Given I have a work package setup "UploadWorkscopeSpreadsheetContainingAdhocTasksAgainstRootSlot"
		When I upload the workscope spreadsheet "UploadInitialWorkscopeAdhocTaskAgainstRootSlot.xls"
		Then I receive a success notification
		And the task "Upload Initial Workscope - Create Adhoc Against Root" is created against the root slot in the work package

	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke @Ignore
	Scenario: Production Controller uploads a workscope spreadsheet that contains adhoc tasks against system slot
		Given I have a work package setup "UploadWorkscopeSpreadsheetContainingAdhocTasksAgainstSystemSlot"
		When I upload the workscope spreadsheet "UploadInitialWorkscopeAdhocTaskAgainstSystemSlot.xls"
		Then I receive a success notification
		And the task "Upload Initial Workscope - Create Adhoc Against System" is created against the system slot in the work package

    @RefactorNonSmoke @Ignore
    Scenario: Production Controller uploads a workscope spreadsheet that contains adhoc tasks with certification and inspection required
		Given I have a work package setup "UploadWorkscopeSpreadsheetContainingAdhocTasksCertInspReq"
		When I upload the workscope spreadsheet "UploadInitialWorkscopeAdhocTaskCertInspReq.xls"
		Then I receive a success notification
		And the task "Upload Initial Workscope - Create Adhoc Certification Inspection Required" is created with certification and inspection required in the work package

	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke @Ignore
	Scenario: Production Controller uploads a workscope spreadsheet that contains errors
		Given I have a work package setup "UploadWorkbookContainingErrors"
		When I upload the workscope spreadsheet "UploadInitialWorkscopeError.xls"
		Then I receive an error notification

	@RefactorNonSmoke @Ignore
	Scenario: Production Controller uploads a workscope spreadsheet that contains job standards
		Given I have a work package setup "UploadWorkbookContainingJobStandards"
		When I upload the workscope spreadsheet "UploadInitialWorkscopeJobStandard.xls"
		Then I receive a success notification
		And the task "AILERON CABLES" exists in the work package

	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke @Ignore
	Scenario: Production Controller uploads a workscope spreadsheet that contains warnings
		Given I have a work package setup "UploadWorkbookContainingWarnings"
		When I upload the workscope spreadsheet "UploadInitialWorkscopeWarning.xls"
		Then I receive a warning notification
		And the task "RUDDER CABLES" exists in the work package