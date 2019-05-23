@Ignore
@RunAMLine
@Angular
Feature: Station Monitoring

   Background:
       Given I am a line supervisor


   @E2ESmokeBuild1 @BeforeStationMonitoring
   Scenario: Line Supervisor launches the station monitoring application
       When I want to monitor the flight information for flights at my current location
       Then I see the flight information for flights related to my current station

   @E2ESmokeBuild1 @BeforeWorkPackageAndTask
   Scenario: Line Supervisor Views Work Package Details from Station Monitoring Flight Card
       Given I want to view work package details from Station Monitoring
	   When I click a work package link on a flight card
	   Then I can see the work package details in a new tab