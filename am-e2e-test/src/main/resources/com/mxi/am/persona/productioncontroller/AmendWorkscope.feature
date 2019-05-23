@RunAMBaseMaintenance
Feature: Amend Workscope Spreadsheet to Add/Edit/Cancel Tasks

	Description: As a production controller, I amend a workscope spreadsheet
	Product: Maintenix OPER Edition
	Persona: Production Controller
	Interaction: Amend existing workscope

	Background:
		Given I am a superuser

	@SmokeTest @Ignore
	Scenario: Production Controller amends a workscope spreadsheet to add additional adhoc tasks
		Given I have an active work package created with "AmendWorkscopeAddAdhocTask-Initial.xls"
		When I upload the amended workscope spreadsheet with an additional adhoc task
		Then the new adhoc task exists in the work package

	@RefactorNonSmoke @Ignore
	Scenario: Production Controller amends a workscope spreadsheet to add additional job standard
		Given I have an active work package created with "AmendWorkscopeAddJobStandard-Initial.xls"
		When I upload the amended workscope spreadsheet with an additional job standard
		Then the new job standard exists in the work package

	@RefactorNonSmoke @Ignore
	Scenario: Production Controller amends a workscope spreadsheet to cancel adhoc tasks
		Given I have an active work package created with "AmendWorkscopeCancelAdhocTask-Initial.xls"
		When I upload the amended workscope spreadsheet to cancel an adhoc task
		Then the adhoc task is cancelled

	@RefactorNonSmoke @Ignore
	Scenario: Production Controller amends a workscope spreadsheet to cancel job standards
	    Given I have an active work package created with "AmendWorkscopeCancelJobStandard-Initial.xls"
		When I upload the amended workscope spreadsheet to cancel a job standard
		Then the jobstandard is cancelled

    # Add Ignore, because it failed intermittent
	@RefactorNonSmoke @Ignore
	Scenario: Production Controller amends a workscope spreadsheet to edit an adhoc task
		Given I have an active work package created with "AmendWorkscopeEditAdhocTask-Initial.xls"
		When I upload the amended workscope spreadsheet to edit an adhoc task
		Then the adhoc task is updated

	@RefactorNonSmoke @Ignore
	Scenario: Production Controller amends a workscope spreadsheet to edit job standard tasks
		Given I have an active work package created with "AmendWorkscopeEditJobStandard-Initial.xls"
		When I upload the amended workscope spreadsheet to edit a job standard
		Then the jobstandard is updated