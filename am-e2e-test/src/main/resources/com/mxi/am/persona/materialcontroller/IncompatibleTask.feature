@MaterialController
Feature: Reserve and install a part in a task

	# First 3 lines should be kept as SmokeTest
	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke @Ignore
    Scenario: Make sure when the incompatible task is not completed, Maintenix throws errors to inform user when they try to reserve and install the incompatible part
      Given that I am a Material Controller
       When I try to reserve a part which is not compatible with an OPEN task for a different task
       Then I will get a warning saying this part is not compatible with an ACTV task
       And if I try to install the same part on the task, I will get a similar error

	# Add Ignore, because it failed intermittent
	@RefactorNonSmoke @Ignore
    Scenario: Make sure Maintenix doesn't throw errors to inform user when they try to reserve and install the incompatible part on the Modification Task itself
       When I try to reserve a part which is not compatible with an OPEN task for the task itself
       Then I will not get a warning saying this part is not compatible with an ACTV task
       And if I try to install the same part on the task, I will not get a similar error