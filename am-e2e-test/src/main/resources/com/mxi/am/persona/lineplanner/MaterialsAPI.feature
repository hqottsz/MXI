@RunAMPlanning
Feature: Materials API

   @E2ESmokeBuild1
   Scenario: User creates part request and response is provided thru API
       Given I am a superuser
       When I create part requests
       Then the request responses update the request statuses
