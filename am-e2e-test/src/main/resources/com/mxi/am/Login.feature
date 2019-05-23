Feature: Login

  @Login @E2ESmokeBuild1
   Scenario: User can log in

      Given I am "mxi"
       Then I should be logged in

  @Login @RefactorNonSmoke
   Scenario: User can log in

      Given I am "crewlead"
       Then I should be logged in

	@RefactorNonSmoke
   Scenario: User cannot log in

      Given I am "Wong Name"
       Then I should not be logged in

	@RefactorNonSmoke
   Scenario: API messages cannot be sent and received if authentication was not done

      Given I am not authenticated for APIs
       When I send an api request
       Then the api response should be forbidden

	@RefactorNonSmoke
   Scenario: API message can be sent and received

      Given I am authenticated as "mxi" for APIs
       When I send an api request
       Then the api response should be received