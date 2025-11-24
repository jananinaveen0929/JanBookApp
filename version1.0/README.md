# JanBookApp 📚

A multi-activity Android application demonstrating core Android development concepts including data passing, resource management, intent handling, and multi-language support.

## 📱 Project Overview

**JanBookApp** is a book information management application built for Android API 29 (Android 10). The app allows users to enter book details, capture photos, and perform various actions using implicit intents.

### 🎯 Key Features

- ✅ **Multi-Activity Architecture** - Three interconnected activities with data flow
- ✅ **Parcelable Implementation** - Efficient data passing between activities
- ✅ **Multi-Language Support** - English, French, and Tamil translations
- ✅ **Orientation Handling** - Separate layouts for portrait and landscape modes
- ✅ **Implicit Intents** - Camera, browser, and email functionality
- ✅ **State Preservation** - Data retained on device rotation
- ✅ **Resource Management** - Four types of Android resources
- ✅ **Runtime Permissions** - Camera permission handling
- ✅ **Modern Android APIs** - ActivityResultLauncher (no deprecated code)

---

## 🏗️ Architecture

### App Structure
```
JanBookApp/
├── MainActivity          → Book data input
├── SecondActivity        → Display details + Camera
└── ThirdActivity         → Web search + Email
```

### Data Flow
```
MainActivity
    ↓ (Book object via Intent)
SecondActivity
    ↓ (Book data via Intent extras)
ThirdActivity
```

---

## 📋 Features Breakdown

### Activity 1: MainActivity
**Purpose:** Book data collection

**Features:**
- Text input fields for book title, author, and publication year
- Input validation (ensures all fields are filled)
- State preservation on device rotation
- Creates Parcelable Book object
- Explicit intent to SecondActivity

**UI Components:**
- 3 EditText fields (Title, Author, Year)
- 1 Submit Button
- ConstraintLayout

---

### Activity 2: SecondActivity
**Purpose:** Display book details and capture photo

**Features:**
- Displays book information (title, author, year)
- Camera functionality using implicit intent
- Photo capture and display
- Runtime camera permission handling
- Different layouts for portrait/landscape orientation
- Language-specific book images
- Navigation to ThirdActivity

**UI Components:**
- 6 TextViews (labels and values)
- 1 ImageView (book image/captured photo)
- 2 Buttons (Take Photo, Next)

**Orientation Handling:**
- **Portrait Mode:**
    - Light gray background (#ECF0F1)
    - Vertical layout
    - Language-specific portrait book image

- **Landscape Mode:**
    - Light green background (#D5F4E6)
    - Horizontal layout (image on right, details on left)
    - Language-specific landscape book image

---

### Activity 3: ThirdActivity
**Purpose:** Additional implicit intents

**Features:**
- Web search with custom query input
- Opens browser with Google search
- Sends email with book information
- URL encoding for search queries

**Implicit Intents:**
1. **ACTION_VIEW** - Opens web browser with search query
2. **ACTION_SENDTO** - Opens email app with pre-filled content

**UI Components:**
- 1 EditText (search query input)
- 2 Buttons (Open Browser, Send Email)

---

## 🛠️ Technical Implementation

### 1. Parcelable (Book.java)

**Why Parcelable over Serializable?**
- 10x faster performance
- More memory efficient
- Android-optimized
- Industry best practice

**Implementation:**
```java
public class Book implements Parcelable {
    private String title;
    private String author;
    private String year;
    
    // Constructor, getters, writeToParcel(), CREATOR
}
```

**Usage:**
```java
// Send data
intent.putExtra("BOOK_DATA", book);

// Receive data
Book book = intent.getParcelableExtra("BOOK_DATA");
```

---

### 2. Android Resources (4 Types)

#### A. Colors (res/values/colors.xml)
```xml
<color name="background_light">#ECF0F1</color>
<color name="background_landscape">#D5F4E6</color>
<color name="button_color">#3498DB</color>
<color name="accent_color">#E74C3C</color>
```

#### B. Strings (Multi-language)
- **res/values/strings.xml** - English (default)
- **res/values-fr/strings.xml** - French
- **res/values-ta/strings.xml** - Tamil

**How it works:**
Android automatically selects the appropriate strings.xml based on device language settings.

#### C. Dimensions (res/values/dimens.xml)
```xml
<dimen name="padding_standard">16dp</dimen>
<dimen name="text_size_title">24sp</dimen>
<dimen name="button_height">56dp</dimen>
```

#### D. Drawables (Language-specific images)
```
res/
├── drawable/          → English images (default)
│   ├── book_portrait.jpg
│   └── book_landscape.jpg
├── drawable-fr/       → French images
│   ├── book_portrait.jpg
│   └── book_landscape.jpg
└── drawable-ta/       → Tamil images
    ├── book_portrait.jpg
    └── book_landscape.jpg
```

**Total: 6 images** (2 per language)

---

### 3. Intent Types

#### Explicit Intent
**Purpose:** Navigate within app
```java
Intent intent = new Intent(MainActivity.this, SecondActivity.class);
intent.putExtra("BOOK_DATA", book);
startActivity(intent);
```

#### Implicit Intents

**1. Camera (MediaStore.ACTION_IMAGE_CAPTURE)**
```java
Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
takePictureLauncher.launch(cameraIntent);
```

**2. Browser (Intent.ACTION_VIEW)**
```java
String url = "https://www.google.com/search?q=" + encodedQuery;
Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
startActivity(browserIntent);
```

**3. Email (Intent.ACTION_SENDTO)**
```java
Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
emailIntent.setData(Uri.parse("mailto:"));
emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Book Recommendation");
startActivity(Intent.createChooser(emailIntent, "Send email using:"));
```

---

### 4. Runtime Permissions

**Modern Approach - ActivityResultLauncher:**
```java
private ActivityResultLauncher<String> requestPermissionLauncher;

requestPermissionLauncher = registerForActivityResult(
    new ActivityResultContracts.RequestPermission(),
    isGranted -> {
        if (isGranted) {
            openCamera();
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
);

// Request permission
requestPermissionLauncher.launch(Manifest.permission.CAMERA);
```

**Benefits:**
- Type-safe
- No deprecated APIs
- Cleaner code structure

---

### 5. State Preservation

**Save state before rotation:**
```java
@Override
protected void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putString(KEY_TITLE, editTextTitle.getText().toString());
    outState.putParcelable(KEY_PHOTO, photoBitmap);
}
```

**Restore state after rotation:**
```java
if (savedInstanceState != null) {
    editTextTitle.setText(savedInstanceState.getString(KEY_TITLE));
    photoBitmap = savedInstanceState.getParcelable(KEY_PHOTO);
}
```

---

### 6. ConstraintLayout

**Modern layout system with advantages:**
- Flat view hierarchy (better performance)
- Flexible positioning
- Responsive design
- Easy to maintain

**Key constraints used:**
```xml
app:layout_constraintTop_toBottomOf="@id/previousView"
app:layout_constraintStart_toStartOf="parent"
app:layout_constraintEnd_toEndOf="parent"
```

---

### 7. Font Styles

**8 different font families used:**
1. `serif` - Labels
2. `sans-serif` - Input fields
3. `monospace` - Submit button
4. `cursive` - Activity 2 title
5. `sans-serif-light` - Book details
6. `sans-serif-medium` - Action buttons
7. `sans-serif-condensed` - Activity 3 title
8. `casual` - Third activity buttons

---

## 📱 Screenshots

### MainActivity (Portrait)
- Book data entry form
- Three input fields
- Submit button

### SecondActivity (Portrait)
- Book details display
- Default book image (language-specific)
- Camera button
- Captured photo display

### SecondActivity (Landscape)
- Green background
- Horizontal layout
- Different book image

### ThirdActivity
- Search input field
- Browser and Email buttons

---

## 🚀 Installation & Setup

### Prerequisites
- Android Studio (Arctic Fox or later)
- Android SDK API 29 or higher
- Physical device or emulator with API 29

### Steps

1. **Clone the repository:**
```bash
git clone https://github.com/yourusername/JanBookApp.git
cd JanBookApp
```

2. **Open in Android Studio:**
    - File → Open
    - Select the JanBookApp folder

3. **Sync Gradle:**
    - Android Studio will automatically sync
    - Wait for sync to complete

4. **Run the app:**
    - Connect Android device or start emulator
    - Click Run (green play button)
    - Select device
    - App will install and launch

---

## 🧪 Testing

### Test Case 1: Basic Flow
1. Open app
2. Enter book details:
    - Title: "Harry Potter"
    - Author: "J.K. Rowling"
    - Year: "1997"
3. Click Submit
4. Verify details displayed in Activity 2
5. Click Next
6. Verify Activity 3 opens

### Test Case 2: Camera Functionality
1. Navigate to Activity 2
2. Click "Take Photo"
3. Grant camera permission (first time)
4. Take photo
5. Verify photo appears in ImageView

### Test Case 3: Web Search
1. Navigate to Activity 3
2. Enter search query: "Android tutorials"
3. Click "Open Browser"
4. Verify browser opens with Google search

### Test Case 4: Email
1. Navigate to Activity 3
2. Click "Send Email"
3. Verify email app opens with book details

### Test Case 5: State Preservation
1. Enter data in Activity 1
2. Rotate device
3. Verify data still present

### Test Case 6: Multi-language
1. Change device language to French
2. Open app
3. Verify French text displayed
4. Verify French book images shown

### Test Case 7: Orientation
1. Navigate to Activity 2 (portrait)
2. Note background color (light gray)
3. Rotate to landscape
4. Verify background changes (light green)
5. Verify layout changes

---

## 📂 Project Structure
```
app/src/main/
├── java/com/example/janbookapp/
│   ├── Book.java              → Parcelable data model
│   ├── MainActivity.java      → Activity 1
│   ├── SecondActivity.java    → Activity 2
│   └── ThirdActivity.java     → Activity 3
│
├── res/
│   ├── drawable/              → English images
│   │   ├── book_portrait.jpg
│   │   └── book_landscape.jpg
│   ├── drawable-fr/           → French images
│   ├── drawable-ta/           → Tamil images
│   │
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── activity_second.xml
│   │   └── activity_third.xml
│   │
│   ├── layout-land/
│   │   └── activity_second.xml  → Landscape layout
│   │
│   ├── values/
│   │   ├── colors.xml
│   │   ├── strings.xml         → English
│   │   └── dimens.xml
│   │
│   ├── values-fr/
│   │   └── strings.xml         → French
│   │
│   └── values-ta/
│       └── strings.xml         → Tamil
│
└── AndroidManifest.xml
```

---

## 🔧 Configuration

### build.gradle (Module: app)
```gradle
android {
    namespace 'com.example.janbookapp'
    compileSdk 34

    defaultConfig {
        applicationId "com.example.janbookapp"
        minSdk 29
        targetSdk 34
        versionCode 1
        versionName "1.0"
    }
}
```

### AndroidManifest.xml

**Permissions:**
```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-feature android:name="android.hardware.camera" android:required="false" />
```

**Activities:**
- MainActivity (LAUNCHER)
- SecondActivity
- ThirdActivity

---

## 🎓 Key Learning Outcomes

### Android Concepts Demonstrated

1. ✅ **Activity Lifecycle** - onCreate, onSaveInstanceState
2. ✅ **Intent System** - Explicit and implicit intents
3. ✅ **Parcelable** - Efficient data serialization
4. ✅ **Resource Qualifiers** - Language, orientation
5. ✅ **Runtime Permissions** - Modern permission handling
6. ✅ **UI Layouts** - ConstraintLayout
7. ✅ **State Management** - Configuration change handling
8. ✅ **Modern APIs** - ActivityResultLauncher
9. ✅ **Internationalization** - Multi-language support
10. ✅ **Best Practices** - No deprecated code

---

## 📊 Technical Specifications

| Specification | Value |
|---------------|-------|
| **Language** | Java |
| **Min SDK** | API 29 (Android 10) |
| **Target SDK** | API 34 |
| **Build Tool** | Gradle |
| **UI Framework** | ConstraintLayout |
| **Architecture** | Multi-Activity |
| **Data Passing** | Parcelable |
| **Permission Model** | Runtime permissions |
| **Localization** | 3 languages |
| **Orientations** | Portrait & Landscape |

---

## 🐛 Known Issues & Limitations

1. **Camera on Emulator:**
    - Some emulators don't have camera support
    - Solution: Test on physical device or configure emulator camera

2. **Photo Size:**
    - Camera intent returns thumbnail, not full resolution
    - This is intentional for memory efficiency

3. **Internet Required:**
    - Web search requires internet connection
    - No offline functionality

---

## 🔮 Future Enhancements

### Potential Improvements:
- [ ] Database integration (Room) for persistent storage
- [ ] Book search API (Google Books)
- [ ] Barcode scanner for ISBN lookup
- [ ] Full-resolution photo storage
- [ ] Book favorites list
- [ ] Share book details to social media
- [ ] Dark mode support
- [ ] More language translations

---

## 📝 Assignment Requirements Met

✅ **Three Activities** - MainActivity, SecondActivity, ThirdActivity  
✅ **Data Passing** - Book object via Parcelable  
✅ **Object Passing** - Book class with three fields  
✅ **Display Data** - Shows title, author, year in Activity 2  
✅ **Camera Intent** - Take photo functionality (implicit intent)  
✅ **Two More Intents** - Browser and Email (implicit intents)  
✅ **Three Resource Types** - Colors, Strings, Dimensions, Drawables (4 types!)  
✅ **Alternative Resources** - Language-specific (fr, ta) and orientation (land)  
✅ **State Saving** - Device rotation handled  
✅ **No Persistent Storage** - As required

---

## 👨‍💻 Author

**Janani Madasamy**

- Project: JanBookApp
- Course: Android Development
- Date: November 2025

---

## 📄 License

This project is created for educational purposes as part of an Android development course assignment.

---

## 🙏 Acknowledgments

- Android Documentation
- Material Design Guidelines
- Stack Overflow Community
- Course Instructor

---


**Built with ❤️ using Android Studio**
