@RunAMPlanning
Feature: Record Flight

   Background:
       Given I am a superuser

	@SmokeTest
   Scenario: Record and edit historic flight
       When I record a historic flight
       And I edit the historic flight
       Then the historic flight is updated