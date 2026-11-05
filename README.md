Load-Shedding Tracker

Project Overview

Load-Shedding Tracker is an Android application designed to help South African users stay informed about electricity load-shedding schedules and manage their daily activities around planned power outages.

The application provides users with access to load-shedding information, suburb management, schedules, notifications and personalised settings.

The project is developed as part of the OPSC6312 Portfolio of Evidence.

Technologies Used

* Kotlin
* Android Studio
* Jetpack Compose
* Material 3
* MVVM Architecture
* Repository Pattern
* Kotlin Coroutines and Flow
* Room Database
* DataStore Preferences
* Retrofit
* REST API
* SQL Database
* Firebase Authentication
* WorkManager
* GitHub Actions

Main Features

User Authentication

* User registration and login.
* Secure password handling.
* User authentication and session management.
* Google Sign-In support, where configured.

Load-Shedding Dashboard

* Displays current load-shedding information.
* Provides access to saved suburbs.
* Presents relevant electricity outage information.

Suburb Management

* Search for suburbs.
* Add and manage saved locations.
* Access load-shedding information for different locations.

Load-Shedding Schedules

* View daily and weekly schedules.
* Access outage information for selected suburbs.
* View previously cached schedule information.

Notifications

* Receive load-shedding notifications.
* Configure notification preferences.
* Schedule advance alerts before planned outages.

Application Settings

* Change application language.
* Switch between light and dark themes.
* Manage notification preferences.

Offline Support

* Access previously cached information.
* Store selected application data locally using Room Database.
* Synchronise information when an internet connection becomes available.

Application Architecture

The application follows the Model-View-ViewModel (MVVM) architectural pattern, with the Repository Pattern and Unidirectional Data Flow (UDF).

The architecture separates the application into different responsibilities:

* UI Layer: Jetpack Compose screens and user interface components.
* ViewModel Layer: Manages screen state and application logic.
* Repository Layer: Coordinates data from remote and local sources.
* Data Layer: Handles API communication, Room Database and DataStore.
* Backend Layer: Provides REST API services and database connectivity.

This structure improves maintainability, code organisation and separation of concerns.

Backend and Database

The application communicates with an online REST API to retrieve and manage application data.

The backend is responsible for processing requests and interacting with the connected database.

The application also uses Room Database for local data storage and offline access.

Project Setup

Requirements

* Android Studio
* Android SDK
* Kotlin
* An Android device or emulator
* Internet connection for online services

Installation

1. Clone the repository:

git clone YOUR_GITHUB_REPOSITORY_URL

2. Open the project in Android Studio.
3. Allow Gradle to synchronise the project.
4. Configure the required backend API address and application credentials.
5. Build and run the application on an Android emulator or physical device.

Building the Application

The application can be built using Android Studio.

Debug APK

1. Open the project in Android Studio.
2. Select Build.
3. Select Build APK(s).
4. Locate the generated APK in the application build output directory.

Signed Release APK

A signed release APK has been generated using Android Studio’s Generate Signed Bundle / APK wizard.

Android App Bundle

A signed Android App Bundle (AAB) has also been generated for Google Play distribution preparation.

Testing

The application is tested to verify its main functionality and reliability.

Testing areas include:

* User registration and authentication.
* Dashboard functionality.
* Suburb searching and management.
* Load-shedding schedule retrieval.
* Application settings.
* Notification functionality.
* Local data storage.
* API communication.
* Offline functionality.

Automated tests are included where implemented.

Publication Preparation

The application has progressed through the release packaging stage.

The following publication preparation activities have been completed:

* Generation of a signed release APK.
* Generation of a signed Android App Bundle (AAB).
* Preparation of the application for installation and release testing.

Google Play Console submission and public publication are separate activities and should only be considered completed once the corresponding release has actually been uploaded or published.

Continuous Integration

GitHub Actions is used for automated project workflows where configured.

The workflow can be used to build the application and execute automated tests to help identify issues during development.

Version Control

Git and GitHub are used for source-code management and project collaboration.

Changes are committed and pushed to the repository throughout the development process to maintain an updated project history.

Developers

Name: Ludumo Mathetha, Phelo Gusha, Chulumanco Iviwe Ngongomana

Project: Load-Shedding Tracker

Module: OPSC6312

Platform: Android

Licence

This project was developed for academic purposes as part of the OPSC6312 Portfolio of Evidence.

YouTube video demonstration link - https://youtube.com/shorts/bs2g_AIxB-I?si=Qt8UpFZtKTwgi-ZY
