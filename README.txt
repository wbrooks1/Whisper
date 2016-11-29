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
-Audio playback worked on by Win
-Retrieving audio files using web service worked on by Win

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
    to the web service.  Username must be unique and the password must be longer than
    characters.
  Use Case 2: Logging in:
    Currently the login does not verify the user has registered.  This will be implement
    for phase 2.  Login does verify valid email address.
  Use Case 3: Recording an audio file
    Able to record an audio file and play back.  Currently the file is saved to the
    device locally and will be overwritten by subsequent recordings.  Phase 2 the file
    will be saved to the web service for playback if the app data is cleared.
  Use Case 5: Retrieving an audio file from web service
    Audio files can be streamed to device via the web service.  We currently have 3 test
    audio files.  Files recorded on device will be 3gp file so the app is currently limited
    to playing only 3gp audio files.  Playback of other file types will be implemented in
    phase 2.
  Use Case 6: Play retrieved audio file
    Files stored in web service can be streamed in app for playback.  Supports playing
    and pausing the files. The playback is paused when activity is no longer in the foreground.

***

Notes:
WARNING: If you use the emulator and try to record audio it will crash.
