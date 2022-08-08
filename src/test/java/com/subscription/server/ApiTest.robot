*** Settings ***
Library    RequestsLibrary
Library    Collections
*** Variables ***
${base_url}    http://127.0.0.1:8080
*** Test Cases ***

PostTest
     Create Session    mysession    ${base_url}
     ${body}=       Create Dictionary    id=1     name=Ali        email=ali@gmail.com
     ${header}=     Create Dictionary    Content-Type=application/json
     ${response}=   Post Request       mysession       /users/   data=${body}    headers=${header}

     ${responseBody}=   Convert To String    ${response.content}
     Log To Console     ${responseBody}
     ${code}=   Convert To String    ${response.status_code}
     Should Be Equal As Integers     ${code}    202

GetTest
    Create Session    mysession    ${base_url}
    ${response} =    GET On Session    mysession   /users/1
    ${Status_code}=  Convert To String    ${response.status_code}
    Should Be Equal    ${Status_code}   200

    ${body}=   Convert To String     ${response.content}
    Should Contain  ${body}     Abdelrahman

    ${Content Type}=   Get From Dictionary    ${response.headers}   Content-Type
    Should Be Equal    ${Content Type}     application/json

UpdateTest
    Create Session    mysession    ${base_url}
    ${body}=       Create Dictionary    id=1   name=Emad   email=abdelrahman.tolba@gmailcom
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${response}=   Put Request   mysession      /users/      data=${body}       headers=${headers}

      ${responseBody}=   Convert To String    ${response.content}
     Log To Console     ${responseBody}
     ${code}=   Convert To String    ${response.status_code}
     Should Be Equal As Integers     ${code}    200
     Should Contain    ${responseBody}      Emad


DeleteTest

   Create Session    mysession    ${base_url}
    ${body}=       Create Dictionary    id=1   name=Abdelrahman   email=abdelrahman.tolba@gmailcom
    ${headers}=    Create Dictionary    Content-Type=application/json
    ${response}=   DELETE Request   mysession      /users/     data=${body}       headers=${headers}

      ${responseBody}=   Convert To String    ${response.content}
     Log To Console     ${responseBody}
     ${code}=   Convert To String    ${response.status_code}
     Should Be Equal As Integers     ${code}    200
     Should Contain    ${responseBody}      deleted successfully

