This is the BackEnd of WindScript.
---
Newest BackEnd Version: 0.3
Newest BackEnd-Auth Version: 0.2.8

---
Changelog:

WindScript BackEnd (WSB 0.3):

    -Auto-Update 1.0 (Server will update on Startup)
    -Implemented json files
    -Added main server that runs the other servers
    -updateLogic Server updates version on database based on version in json file
    -If json files don't exist, they will be downloaded from latest release, then the server itself will be updated

WindScript BackEnd - auth 0.2.8:

    -(Checking that username and password don't match)
    -Implemented a Database instead of txt files
    
----
----

----

How AutoUpdate Works:

    -config.json and versions.json have the same name, just the windscriptbackend-version.jar has a different name
    -j