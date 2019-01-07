Feature: create a mandate

  In order to sign a succession contract
  For a bank client
  We will create and update mandate

  Scenario: a client should be able to create a succession mandate
    Given a client named "Alfred"
    And his main heir is "Katy"
    And hist other heirs are :
      | Leon |
      | Brad |
    And his notary is "Master"
    When this client grants bank "Neuflize" to operate succession when he will die
    Then a mandate is created

  Scenario Outline: a client should be able to amend the notary
    Given a client named "Alfred"
    And a succession mandate with id "5412EDDF"
    And <FIELD> is equal to <OLD>
    When this client set the <FIELD> to "<NEW>"
    Then the mandate is amended in consequence

    Examples:
      | FIELD      | OLD        | NEW        |
      | Notary     |            | Master     |
      | MainHeir   |            | Katy       |
      | OtherHeirs | Leon       | Leon, Brad |
      | OtherHeirs | Leon, Brad | Leon       |