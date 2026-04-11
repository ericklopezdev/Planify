# Planify

![Kotlin](https://img.shields.io/badge/Kotlin-1.9-blue)
![Android](https://img.shields.io/badge/Platform-Android-green)
![Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-purple)

## Overview

Planify is a mobile application designed to help students organize their
time and study more effectively.

The goal is to provide a complete toolkit for academic productivity in a
single place. Instead of using multiple apps, students can manage their
academic life through one unified experience.

Core ideas of the product include:

-   Personal planner for tasks and schedules
-   Visualization of activities assigned by teachers
-   Study tools inspired by spaced repetition
-   Focus techniques such as Pomodoro
-   Centralized academic workflow

This repository contains the Android applications for the ecosystem.

## Ecosystem

| Project        | Description                     |
|----------------|---------------------------------|
| planify        | Android app for students        |
| teachly        | Android app for teachers        |
| api-plnf-tchl  | Shared backend                  |

Backend repository: https://github.com/ericklopezdev/api-plnf-tchl

Teacher platform (Teachly): https://github.com/brayanalaya/teachly


## Running Locally

### Requirements

-   Android SDK (API 34)
-   Java 17+
-   Gradle

### Steps

``` bash
# Clone repository
git clone https://github.com/your-org/planify-student.git
cd planify

# Create local.properties
echo "sdk.dir=$ANDROID_HOME" >> local.properties

# Build debug APK
./gradlew assembleDebug

# Install on device
adb install app/build/outputs/apk/debug/app-debug.apk
```

## Environment Variables

Create a local.properties file:

``` properties
SUPABASE_URL=https://xxxx.supabase.co
SUPABASE_ANON_KEY=your-anon-key
```

These values will be used once backend integration is enabled.

