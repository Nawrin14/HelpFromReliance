# HelpFromReliance
Users can share their location by SMS through this android app by a single click.

# Description
"Help From Reliance" is a location-sharing app. Whenever users fall into any danger or meet with an accident, they can quickly send the current location to their relatives or friends to get help through this app. All they need to do is to click a button. The location will be sent automatically by SMS.

# Functionalities
At first, users will have to create an account to use this app. All the user information along with the home address and emergency contact numbers will be saved in the database. If the users can log in successfully, they will be able to send SMS in two ways. Firstly, they can send an SMS after confirming the current location. Secondly, they can send an SMS without confirming if they are in their home. In this case, the users home address will be retrieved from the database. The SMS will be sent to two emergency contact numbers saved previously in the user database. The users will also be able to see their user profile and log out from their account anytime.

# Android Version
The targeted android version for this app is Android 5.1 (Lollipop). The users can face difficulty to use this app in Android versions lower than 5.1 due to the Google Maps feature. 

N.B.    It is recommended to run the project in a real android device. The project can crush and work abnormally in the Android Emulator.

# Additional Bindings
1.  Firebase Database: The project is connected to the Firebase Database. All the user information will be saved in a Firebase Database.

2.  Google Map: Google Map API has been used in this app to fetch the user's current location and to show the location in the Google Map. The user will have to permit the app to access the device's location. The location information should also be enabled to get the current location of the user.

3.  SMS Manager: This project uses the SMS Manager API to send message through the app. The user will have to permit the app to send an SMS from the device. 
