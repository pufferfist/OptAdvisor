# OptAdvisor Options Investment Advice System Software Requirements Specification

### Nanjing University utf-8 development team

2018-09-08

## Catalog

[TOC]

## 1.Introduction

### 1.1 The goal

The document describes the functional requirements of the investment advice system. The development team should obey the document's introductions on software system implementation and verification work.

Except for special instruction, all the requirements in the document are taking high priority.

### 1.2 Range

The OptAdvisor is developed for option investors to help them plan for investing. The OptAdvisor includes asset allocation, hedging and custom portfolio as core functions and portfolio management, portfolio tracking and market display as additional functions.

## 2.General Description

### 2.1 Business Requirements

BR1:The system should increase investors returns by at least 8%.

BR2:The system should help investors make decisions to reduce burdens for investors.

BR3:The system should track investmentand reduce loss for investors.

### 2.2 Software Functions

SF1.1:Automotically generating the beast investment portfolio taking the advantage of high computing speed.

SF1.2:Offering hedging portfolio for investors to reduce loss.

SF2.1:Evaluating the portfolio built by investors themselves.

SF2.2:Offering real time updating option market display.

SF3.1:Backstage tracking the portfolio revenue in real time, and sending the alert message when the loss reaches a certain thresold.

### 2.3 Constraints

CON1:The system runs on Linux cloud server.

CON2:The system uses browser and android app to interact with users.

CON3:The project is developed in waterfall model and in method of continious integration.

## 3.Specific Requiremnts Description

### 3.1 External Interfaces Requiremnts

#### 3.1.1 Hardware Interfaces

The system can run on whatever normal mobilephone system or smart mobilephone system. The data is imput through mouse and keyboard(touch screen) and is output through display screen.

#### 3.1.2 Software Interfaces

The operating environment on the Web side should be Windows XP or higher, which supports mainstream browsers such as Chrome, Firefox, Edge, and may not support IE9 or lower.

The operating environment on the android side can run on Android or higher, which may not support lower versions.

#### 3.1.3 Communication Interfaces

The client and the server communicate through http.

### 3.2 Functional Requiremnts

#### 3.2.1 Sign in and Sign up

##### 3.2.1.1 Characterization

Realting to signing in and signing up by users, retrieving password and relating permissions.

##### 3.2.1.2 Stimulation/Response Sequences

Stimulation: User clicks on sign in button

Response: System displays the view

Stimulation: User inputs the account and the password then clicks on signing in button

Response: System checks the validity of the account and the password. If valid, system displays the view of 50ETF market

Stimulation: User clicks on signing up button

Response: System displays view of sign up and guides users to fill in the registration questionnaire

Stimulation: User clicks forgetting password button

Response: System displays the view of resetting password

Stimulation: User inputs user name and clicks to get verification code

Response: System sends the SMS verification to the number the user reserves when signing up

Stimulation: User enters the new password and clicks the modify password button

Response: System check whether the verification code is valid. If valid, system changes the password and automatically log the account in.

Stimulation: User enters views except for signing in, signing up, forgetting password and market display view.

Response: System checks the current login status. If not signed in, system jumps to the signing in view.

##### 3.2.1.3 Related Functional Requiremnts

| User.login           | System checks the validity of the account and password and provides session and authority for users who has already signed in.|
| -------------------- | ------------------------------------------------------------ |
| User.signUp          | System adds new user                                               |
| User.isUsernameUsed  | System checks if tje user name is used by others                                      |
| User.sendVerifyCode  | System send SMS message to the corresponding number of the user                             |
| User.checkVerifyCode | System checks the validity of the corresponding verification code of session. If valid, system resets the password    |
| User.auth            | System checks the login status of the session and returns status code              |

#### 3.2.2 Personal Information Management

##### 3.2.2.1 Characterization

Users view and modify their personal information after login, including password modification.

##### 3.2.2.2 Stimulation/Response Sequences

Stimulation: User clicks on personal information button

Response: System displays personal information view

Stimulation: User midifies personal information and clicks on saving button

Response: System modifies the information, promots users that the data is saved succesfully and displays the new infomation

Stimulation: User clicks on modifying password button

Response: System displays resetting password view

Stimulation: User inputs the old password and the new password and clicks on confirm button

Response: System saves new password and promts that the password is reset succesfully

##### 3.2.2.3 Related Functional Requiremnts

| User.getInfo       | System queries user information according to the user name of the current user  |
| ------------------ | --------------------------------------- |
| User.modifyInfo    | System modifies user information of the current user              |
| User.resetPassword | System checks the validity of the current password and changes the password if it is valid  |

#### 3.2.3 Market quotations display

##### 3.2.3.1 Characterization

Display 50ETF and quotations of 50ETF option

##### 3.2.3.2 Stimulation/Response Sequences

Stimulation: User clicks on displaying quotations button

Response: System shows quotations display view

##### 3.2.3.3 Related Functional Requiremnts

None

#### 3.2.4 Asset Allocation

##### 3.2.4.1 Characterization

System provides investment advice portfolio based on user information and market expectations.

##### 3.2.4.2 Stimulation/Response Sequences

Stimulation: User clicks on asset allocation button

Response: System displays asset allocation view

Stimulation: User chooses market expectations, fills in the principle, maximum loss allowance and clicks next

Response: System calculates the best portfolio and displays it

Stimulation: User clicks the last step button

Response: System asks user if the portfolio needs saved

Stimulation: User chooses No

Response: View jumps to the last view of inputting infomation

Stimulation: User clicks add to my combination button

Response: System pops out the combination name dialog for user to input the name to add the current combination to my combination

##### 3.2.4.3 Related Functional Requiremnts

| RecommendService.recommendPortfolio | System calculates best recommendations based on input information |
| ----------------------------------- | ---------------------------- |
| Portfolio.addPortfolio              | System adds the current combination to my portfolio |

#### 3.2.5 Hedging

##### 3.2.5.1 Characterization

System gives hedging recommendations based on user information and market status

##### 3.2.5.2 Stimulation/Response Sequences

Stimulation: User clicks the hedging button

Response: System displays hedging view

Stimulation: User fills in the proportion of positions and then clicks the query results button

Response: System calculates the best hedging option and displays it

Stimulation: User clicks add to my combination button

Response:System pops out the combination name dialog for user to input the name to add the current combination to my combination

##### 3.2.5.3 Related Functional Requiremnts

| RecommendService.hedging | System calculates best recommendations based on input information |
| ------------------------ | ---------------------------- |
| Portfolio.addPortfolio   | System adds the current combination to my portfolio |

#### 3.2.6 Custom Portfolio

##### 3.2.6.1 Characterization

System allows users to design their own portfolio and gives evaluations to the portfolio

##### 3.2.6.2 Stimulation/Response Sequences

Stimulation: User clicks the DIY button

Response: System displays custom portfolio view

Stimulation: User chooses option and number

Response: System displays content of current portfolio

Stimulation: User clicks preview button

Response: System calculates the evaluations of the current portfolio and displays them

Stimulation: User clicks add to my combination portfolio

Response: System displays evaluations of current portfolio and add the current portfolio to my combination

##### 3.2.6.3 Related Functional Requiremnts

| RecommendService.customPortfolio | System calculates custom portfolio evaluations      |
| -------------------------------- | ---------------------------- |
| Portfolio.addPortfolio           | System add the current portfolio to my combination |

#### 3.2.7 Risk tracking

##### 3.2.7.1 Characterization

System tracks the user's portfolio in backstage

##### 3.2.7.2 Stimulation/Response Sequences

Stimulation: User clicks track portfolio button

Response: System displays that tracks the portfolio succesfully and tracks in backstage

##### 3.2.7.3 Related Functional Requiremnts

| Portfolio.track | System adjusts the tracking status of the corresponding portfolio of the id and track the portfolio  |
| --------------- | --------------------------------------------- |
|                 |                                               |

#### 3.2.8 Portfolio Management

##### 3.2.8.1 Characterization

Managing the portfolios that the current user holds

##### 3.2.8.2 Stimulation/Response Sequences

Stimulation: User clicks my portfolio button

Response: System displays my portfolio view

Stimulation: User clicks whatever a portfolio name

Response: System displays base infomation of the portfolio and the evaluations of the portfolio

Stimulation: User right clicks whatever a portfolio name

Response: System displays right menu providing delete and modify operation

Stimulation: User clicks delete portfolio button

Response: System deletes choosen portfolio

Stimulation: User clicks rename portfolio button

Response: System displays rename dialog

Stimulation: User inputs new name of the portfolio and clicks confirm button

Response: System renames the choosen portfolio

Stimulation: User clicks build a new portfolio

Response: View jumps to building corresponding portfolio view

##### 3.2.8.3 Related Functional Requiremnts

| Portfolio.portfolioList | System queries the list of portfolios that current account holds     |
| ----------------------- | ---------------------------------- |
| Portfolio.get           | System queries the base infomation and evaluations of the portfolio in corresponding id |
| Portfolio.update        | System modifies the name of the portfolio in corresponding id         |
| Portfolio.delete        | System deletes the portfolio in corresponding id               |

#### 3.2.9 View Message

##### 3.2.9.1 Characterization

Viewing and managing the message that the user receives

##### 3.2.9.2 Stimulation/Response Sequences

Stimulation: User clicks message button

Response: System displays my message view

Stimulation: User clicks one unread message

Response: System shows the Specific content of the message and set the message has read

Stimulation: User clicks whatever a read message

Response: System shows the Specific content of the message

Stimulation: User clicks delete button

Response: System deletes the selected message

##### 3.2.9.3 Related Functional Requiremnts

| Message.getUnreadMessageNum | System obtains the number of unread message and shows it on message button |
| --------------------------- | ---------------------------------- |
| Message.getMessage          | System obtains all the read message and unread message of the account  |
| Message.setMessageRead      | System sets the message has read           |
| Message.deleteMessage       | System deletes the message                       |
