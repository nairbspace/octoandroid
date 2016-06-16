Change Log
==========

Version 1.4.7 *(2016-06-15)*
----------------------------

 * Updated OkHttp libraries including okhttp-ws which is used in JavaWebsockets library.
 * Added RecyclerView support library. Apparently need to actually include it for older devices or it will crash.
 * Slowed down websocket stream to sample every second due to overflow on certain devices (Samsung).
 * Cleaned up code in TempGraphFragment. 

Version 1.4.6 *(2016-06-13)*
----------------------------

 * Hotfix. Unsubscribe websocket on background thread till proper fix is found.

Version 1.4.4 *(2016-06-13)*
----------------------------

 * Crashlytics added.
 * Bugfix. Check if SSL Exception message is null.

Version 1.4.2 *(2016-06-11)*
----------------------------

 * Bugfix. Switched parsing method for websocket models and class types to primitives wherever possible.

Version 1.4.1 *(2016-06-10)*
----------------------------

 * General controls added.
 * Bugfix. Check if Exception message for subscriber is null.
 * Bugfix. Check if Tool0 or Tool1 is null in WebsocketModel.

Version 1.3.0 *(2016-06-09)*
----------------------------

 * Tool controls added.

Version 1.2.0 *(2016-06-09)*
----------------------------

 * Print head controls added.

Version 1.1.0 *(2016-06-06)*
----------------------------

 * Temperature graph and temperature controls added.

Version 1.0.0 *(2016-05-27)*
----------------------------

 * Initial release.