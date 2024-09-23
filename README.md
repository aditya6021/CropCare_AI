# CropCare AI
CropCare AI is an Android application designed to predict crop diseases using a TensorFlow Lite model. The app demonstrates how TensorFlow Lite can be integrated into Android applications to build AI-powered solutions. It also features Firebase integration for user authentication and profile management. AI powered Apps Like CropCare AI can empowers farmers by providing real-time disease predictions to protect their crops and improve yield.
### Note:
The current version of the model is trained specifically for potato diseases as a demo. However, models for other crops and diseases can be developed and integrated into the app, making it a versatile tool for detecting a wide range of crop diseases.
## Interface
![1](https://github.com/user-attachments/assets/27a85e53-d6e2-419e-8abd-f8d80beab434)
![6](https://github.com/user-attachments/assets/1a6d80b6-4a89-4504-8efd-697844118a80)
![5](https://github.com/user-attachments/assets/c2ff75ff-4405-4b23-9bc5-4d819b675709)
![4](https://github.com/user-attachments/assets/cc52edc6-e442-4e82-89a0-1b09fb7d8907)
![3](https://github.com/user-attachments/assets/5ee11a3c-7607-4f11-8bfb-4a03250fa667)
![2](https://github.com/user-attachments/assets/62e502ce-439d-4e67-bc78-0347e678f53b)








## Features
- **Crop Disease Prediction**: Leverages a TensorFlow Lite model to predict diseases in potato crops (for demo purposes), with future potential for other crops.
- **User Authentication**: Firebase Authentication to securely log in users.
- **Profile Management**: Users can edit their profiles and manage their details.
- **Data Storage**: Firebase Firestore is used to store user details.

## Dependencies and Permissions Required

The project uses the following dependencies:

```bash
dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation ("com.hbb20:ccp:2.5.0")
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation(libs.firebase.firestore)
    implementation(libs.tensorflow.lite.support)
    implementation(libs.tensorflow.lite.metadata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
```
The project uses the following permissions:
```bash
 <uses-permission android:name="android.permission.CAMERA" />
 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
```

## Installation and Setup

### Clone the repository:
```bash
git clone https://github.com/yourusername/chat-application.git
cd chat-application
```
### Open the project in Android Studio:

- Open Android Studio.
- Click on "Open an existing Android Studio project".
- Navigate to the cloned repository and select it.

### Configure Firebase:

- Go to the Firebase Console and create a new project.
- Register your app with Firebase by adding the Android package name.
- Download the google-services.json file and place it in the app directory of your project.
- Follow the Firebase setup instructions to configure your Firebase project.

### Build and run the project:

- Sync the project with Gradle files.
- Build and run the project on an emulator or a physical device.

## Usage

#### Login:

- Open the app and log in using your email and password.
- If you don't have an account, sign up using the provided sign-up option.

#### Chat:

- Search for other users by their username.
- Start a chat by selecting a user from the search results.
- Send and receive messages in real-time.

#### Profile:

- Upload a profile picture by selecting the profile picture option.
- Update your username in the profile settings.

## Contact
For any questions or suggestions, feel free to contact adityapratapsingh273@gmail.com.

