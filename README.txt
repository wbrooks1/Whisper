# Whisper
Will Almond, Jacob Tillett, Winfield Brooks
TCSS450 Project
Team4
Phase I


Features implemented:
-Logging in worked on by Jacob
-Registering a user worked on by Jacob
-Logging out worked on by Jacob
-Audio recording worked on by Will
-Audio playback worked on by Winfield
-Retrieving audio files using web service worked on by Winfield
-Uploading audio files to web service worked on by Winfield
-Deleting audio files worked on by Winfield
-Updating audio file information worked on by Winfield
-Sharing .3gp audio files after recording worked on by Winfield
-Sharing link to audio files stored in web service from listening screen worked on by Winfield

***
Web Services:
  The app is currently using web serviced to retrieve/stream previously uploaded audio
  files.  It is also used to store registration username and password.

Device Storage:
  The app is using shared preferences to save login state for a user.  If a user has
  logged in previously they will bypass the login screen and be taken to our WebServiceActivity
  where they are able to interact with the app.

Use cases implemented:
  Use Case 1: Registering a user:
    A user is able to register a new username and select a password.  This is saved
    to the web service.  Username must be unique and the password must be longer than 4
    characters.  Registering is important to retrieving files as it enables pulling the correct files for the user and creating unique urls for each uploaded file.
  
  Use Case 2: Logging in:
    Logging in verifies that a user has registered and the user name is used for retrieving files and for creating unique urls if two users have the same file names.  User and file name are our combined primary key for our SQL database.  Login is also save in system preferences to automatically log a user in.
  
  Use Case 3: Recording an audio file
    Able to record an audio file and play back.  Currently the file is saved to the
    device locally and will be overwritten by subsequent recordings.  Phase 2 the file
    will be saved to the web service for playback if the app data is cleared.
    
  Use Case 4: Store audio files to web service.
    After recording a file the app uploads the file to the cssgate web server.  This is handled by an Async-Task converting the file into a byte array and using php to move file.  This was one of the large difficulties we faced and were able to overcome. 
  
  Use Case 5: Retrieving an audio file from web service
    Audio files can be streamed to device via the web service. A list of file info uploaded by the user is presented at after login.  When selecting a file the unique url for that audio file is retrieved from the data base and used for use case 6.  
    
  Use Case 6: Play retrieved audio file
    Files stored in web service can be streamed in app for playback.  Supports playing
    and pausing the files. The playback is paused when activity is no longer in the foreground.
    
  Use Case 7: Share audio file
    This use case is implemented in two ways.  First when you record the audio file and have it currently stored in device memory you can share the actual .3gp file via any app that supports media sharing.  In the playback screen when the app is streaming the file from the web service you can share the link which can be shared with any app that supports sending text.
    
  Use Case 9: Edit audio files
    This is only implemented to edit the file information that is displayed to the user.  This use case was marked as secondary in our proposal and turned out to be very difficult to implement audio editing such as cropping, changing speed and equalizer affects.

***

Notes:
WARNING: 
    1) If you use the emulator and try to record audio it will crash.
    2) Some devices running Android 6.0 Marshmallow require you to give permissions to the app.
        a) This is done by opening the phone Settings then follow
            Settings-->Apps-->Whisper-->Permissions: then switch on Microphone and Storage.
    3) If the device has a weak internet connection the file list can take a second or three...maybe ten to load.
    4) The app will crash if you use the "Start Recording" button from the listening screen.
    5) We see strange behavior when navigating back to the main file list from the recording screen if the file list is scrolled.
