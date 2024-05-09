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
    -jsons will just be replaced and new jar will be downloaded, then the old jar runs the new one, and stops itself.