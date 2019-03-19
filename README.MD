![alt text](/images/banner.png "Banner")

# Global Gym - Android
[![Build Status](https://app.bitrise.io/app/f14c58c94e802fdb/status.svg?token=zJ0oZ4xl2xYiiKxxzsZ1Aw&branch=master)](https://app.bitrise.io/app/f14c58c94e802fdb)  

GlobalGym is an app that allows users to access any member gym across Africa.

See backend API - [GlobalGym API](https://github.com/MarkNjunge/GlobalGym-api).

NB: GlobalGym was created as a school project. It is not used in production.

## Installation
1. Clone the repository.
2. Add a `keys.properties` file to to the root directory with the following details
```
GOOGLE_API_KEY="AIza***"
UBER_CLIENT_ID="uber_client_id"
UBER_SERVER_TOKEN="uber_server_token"
```
- An Uber Client Id and Server token. Only required for fare estimates. [See docs](https://developer.uber.com/docs/riders/ride-requests/tutorials/button/introduction)
3. Create a project on Firebase and enable Email/Password sign in.
4. Go to the [Google Cloud Developer console](https://console.cloud.google.com/apis/library) and enable **Geocoding API** and **Maps SDK for Android** for the project.
5. Replace the `google-services.json` file in `app` with your own.
6. Open and build the project in Android Studio/

## Screenshots
![alt text](/images/screenshots.png "Screenshots")
