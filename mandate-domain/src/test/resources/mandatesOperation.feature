Feature: create a mandate

  In order to sign a succession contract
  For a bank client
  We will create and update mandate

  Scenario: a client should be able to create a succession mandate
    Given I am "Alfred"
    And I have designed "Katy" as my main heir
    And I have designed following people as other heirs:
      | Leon |
      | Brad |
    And my notary is "Master Langlois"
    When I want to sign succession contract with my bank
    Then a mandate is created
    And mandate's id is not null

  Scenario Outline: a client should be able to amend the mandate
    Given I am "Alfred"
    And I have signed a succession mandate with id "5412EDD"
    And "<FIELD>" is set with "<OLD>"
    When I want to amend the "<FIELD>" to "<NEW>"
    Then "<FIELD>" is equal to "<NEW>"

    Examples:
      | FIELD      | OLD        | NEW        |
      | Notary     |            | Master     |
      | MainHeir   |            | Katy       |
      | OtherHeirs | Leon       | Leon, Brad |
      | OtherHeirs | Leon, Brad | Leon       |