# CropCare AI
CropCare AI is an Android application designed to predict crop diseases using a TensorFlow Lite model. The app demonstrates how TensorFlow Lite can be integrated into Android applications to build AI-powered solutions. It also features Firebase integration for user authentication and profile management. AI powered Apps Like CropCare AI can empowers farmers by providing real-time disease predictions to protect their crops and improve yield.
### Note:
The current version of the model is trained specifically for potato diseases as a demo. However, models for other crops and diseases can be developed and integrated into the app, making it a versatile tool for detecting a wide range of crop diseases.
## Interface
![1](https://github.com/user-attachments/assets/27a85e53-d6e2-419e-8abd-f8d80beab434)
![2](https://github.com/user-attachments/assets/62e502ce-439d-4e67-bc78-0347e678f53b)
![3](https://github.com/user-attachments/assets/5ee11a3c-7607-4f11-8bfb-4a03250fa667)
![4](https://github.com/user-attachments/assets/cc52edc6-e442-4e82-89a0-1b09fb7d8907)
![5](https://github.com/user-attachments/assets/c2ff75ff-4405-4b23-9bc5-4d819b675709)
![6](https://github.com/user-attachments/assets/1a6d80b6-4a89-4504-8efd-697844118a80)












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
git clone https://github.com/aditya6021/CropCare_AI.git
cd CropCare_AI
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
  
## Tflite Model
Trained Model: https://drive.google.com/file/d/1-6GWS2XKvH_kqoqigTSZYNepUSVIWz1F/view?usp=sharing
Java code for using Tflite Model in Android app
```bash
try {
    Finalmodel model = Finalmodel.newInstance(context);

    // Creates inputs for reference.
    TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 256, 256, 3}, DataType.FLOAT32);
    inputFeature0.loadBuffer(byteBuffer);

    // Runs model inference and gets result.
    Finalmodel.Outputs outputs = model.process(inputFeature0);
    TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

    // Releases model resources if no longer used.
    model.close();
} catch (IOException e) {
    // TODO Handle the exception
}
```

## Usage

### Predict Crop Disease:
- Upload or take a photo of a crop leaf.
- Use the prediction feature to detect any disease affecting the crop.
- The app will display the results based on the TensorFlow Lite model's analysis.


### Profile Management:
- Log in with OTP.
- Edit your profile details, in the profile section.
- 
## APK of Application
https://drive.google.com/file/d/1199jn3GoH9peuyFlHKEqa6dfmyYi2zlm/view?usp=sharing

## Contact
For any questions or suggestions, feel free to contact adityapratapsingh273@gmail.com.

