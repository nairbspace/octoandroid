Change Log
==========

Version 1.8.0 *(2016-07-01)*
----------------------------

 * Feature: Terminal console
 
 * Fixed slicing output filename displaying incorrectly
 * Fixed temperature graph margins
 
Version 1.7.0 *(2016-06-29)*
----------------------------

 * Feature: Able to add/edit/delete multiple printers
 
 * Added help guide for adding printer
 * Added additional checks for some edge case crashes
 
Version 1.6.1 *(2016-06-26)*
----------------------------

 * Fixed playback_controls fragment not clickable due to negative dp and added margin due to shadow clash with navigation view.
 
Version 1.6.0 *(2016-06-25)*
----------------------------

 * Feature: Slicer controls and slicing progress display
 * Feature: Slicer command button for STL files in FilesFragment
 
 * Switched models for connection to List type instead of HashMap for displaying in Spinner
 * Fixed UI for ImageView onClicks, Button style, and horizontal ProgressBar based on SDK
 * Fixed NavPresenter not refreshing on network changes
 
Version 1.5.1 *(2016-06-21)*
----------------------------

 * Hotfix. Run observables in WebsocketService to all run in background including observeOn.
 
Version 1.5.0 *(2016-06-20)*
----------------------------

 * Feature: Notifications
 
 * Fixed crashing on pre-Lollipop tablets due to negative dp in CardView
 * Added check for Play Services and rear camera
 * Fixed IME action in AddPrinterActivity
 * Added log data in AddPrinterActivity to see how users login
 * Updated preview image in README

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