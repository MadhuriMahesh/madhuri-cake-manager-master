Cake Manager Micro Service (fictitious)
=======================================
I have migrated the project to modern technologies like Spring Boot and React. Changed the application to implement proper client-server separation via REST API.

Prerequisite for running the project:
-------------------------------------
1>Java 8
2>Spring Boot
3>Facebook developers account for OAuth 2.0 to work
4>Node14  -> https://nodejs.org/en/download/
5>yarn -> for running the frontend component

Steps to setting up the project:
--------------------------------
1> npm install --global yarn
    yarn --version

2> Need Facebook developer account for login
    https://developers.facebook.com/docs/development/register

   Create App in Development
    https://developers.facebook.com/docs/development/create-an-app
    https://developers.facebook.com/docs/development/create-an-app/developer-settings

    Once you create the test app. Please go to Seeting> Basic to get the App ID and App Secret

    which gives the 1> App ID     = client-id
                    2> App Secret = client-secret

    Please update the client-id and client-secret in the application and your OAuth2 login with facebook is ready!!!

    spring:
      security:
        oauth2:
          client:
            registration:
              facebook:
                client-id: YOUR CLIENT ID GOES HERE
                client-secret: YOUR CLIENT SECRET GOES HERE




Steps to run the project:
-------------------------
There is backend component and frontend component to the application. you need to run both.

1> Backend component:
run the spring boot application class -> com.madhuri.cake.manager.master.MadhuriCakeManagerMasterApplicationTests.class
which run on 8080 port

http://localhost:8080

2> Frontend component:
cd to <your project path>\madhuri-cake-manager-master\app>

yarn start <- before running this please install yarn as documented above. This will automatically bring up the browser for you.
which run on the port 3000

http://localhost:3000


Requirements Met:
-----------------
I have fixed the old project with 404 Error, and also made some Jetty pom issues to make it compatible with Java 8. you can find it in my Github too.

I have modernised the application with Spring Boot, React, H2 In memoryDB, lombok, new version of hibernate and OAuth2 with facebook.
Changed the application to implement proper client-server separation via REST API.

* By accessing the root of the server (/) it should be possible to list the cakes currently in the system. This must be presented in an acceptable format for a human to read.
   - you can list the cakes from server root (/) that is http://localhost:8080/

* It must be possible for a human to add a new cake to the server.
   - Added react pages for List cakes, have 2 pages for cake listing 1> User Cakes (based on the login user from OAuth2 with facebook)
                                                                     2> All Cakes

   - Added the pages for Add and Edit a cake
   - Added Delete functionality
   - You can Download the json for All the cakes and User Cakes

* By accessing an alternative endpoint (/cakes) with an appropriate client it must be possible to download a list of
the cakes currently in the system as JSON data.
   - User can Download a list of cakes currently in the system from User Cakes and All Cakes page.

* The /cakes endpoint must also allow new cakes to be created.
   - Added Add and Edit page to add and edit the cake.

* I tried to do test coverage as much as possible. I was tried to test the OAuth2 Part I could not complete it with time restriction.
    Test Coverage:
            Class coverage: 90%
            Method Coverage: 77%
            Line Coverage: 79%

* Used Intellij

Comments:

* Any problem with the building or running the project please contact me with mto email: madhuri.padli85@gmail.com
* I hope you like the project migration. Please give me feed back on the project that would be helpful.
* Please find bo the projects in my git hub link: https://github.com/MadhuriMahesh
    1> cake-manager-master
    2> madhuri-cake-manager-master


Thanks
