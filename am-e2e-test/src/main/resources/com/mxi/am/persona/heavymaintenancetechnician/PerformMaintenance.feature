Feature: Perform Maintenance

  Persona - Heavy Maintenance Technician
  Squad - Operator Base Maintenance

  Background:
    Given I am a superuser

  @E2ESmokeBuild1
  Scenario: Start Labour Row
    Given I have a task with a labour row not started
     When I start the time tracking on the labour row
     Then the labour row is IN WORK

  @E2ESmokeBuild1
  Scenario: Job Stop Labour Row
    Given I have a task with a labour row started
     When I stop the time tracking on the labour row
     Then the labour row is STOP

  @E2ESmokeBuild1
  Scenario: Record Work Performed
    Given a single INWORK labour row on a task
     When I record the maintenance performed
     Then the labour row is COMPLETE

  @E2ESmokeBuild1
  Scenario: Certification of Own Work
    Given a single INWORK labour row needs certification
     When I finish and certify the labour row
     Then the labour row is COMPLETE and certified

  @E2ESmokeBuild1
  Scenario: Review My Task
    Given I have work to do
     When I review My Tasks
     Then I can see the Task
