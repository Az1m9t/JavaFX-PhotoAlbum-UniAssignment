---

# JavaFX Photo Album Manager — University Project

This is a **JavaFX-based photo album manager** developed as part of a **university programming course project**. It demonstrates skills in object-oriented design, GUI development, user interaction handling, and JavaFX component integration.

---

## 🎓 About the Project

The goal of this application is to allow users to:
- Manage photo albums (create, rename, delete)
- Add and view photos in albums
- Search through albums and photos
- Use drag-and-drop to import images
- Use context menus for operations like delete and copy
- Open full-size images in a viewer

This project showcases JavaFX development with `ListView`, `ImageView`, custom cell rendering, file handling, and event-driven programming.

---

## ✅ Key Features

### Album Management
- 📁 Create new albums
- ✏️ Rename existing albums
- 🗑️ Delete albums
- 🔎 Search albums in real-time

### Photo Management
- ➕ Add photos using file picker
- 📂 Drag-and-drop image files directly into albums
- 🔎 Search photos by name within an album
- 📤 Copy photos to other albums
- 🗑️ Delete individual photos
- 👀 Open photos in a new window at full size

### UI / UX
- Built entirely using **JavaFX components** (no FXML)
- Uses `ObservableList` to ensure UI auto-updates
- Context menus on photo items
- Responsive layout with `VBox`, `HBox`, `StackPane`

---

## 📷 Example Workflows

- **Double-click** an album to open its photos
- **Drag** image files (PNG, JPG, JPEG, GIF) into the album
- **Right-click** a photo to:
  - Delete it
  - Copy it to another album
- **Double-click** a photo to preview it in full screen
- **Type** in the search bar to filter albums or photos dynamically

---

## 🛠 Technologies Used

- Java 17+
- JavaFX 17+
- Standard JavaFX GUI libraries (no external frameworks)
- `java.io.File`, `java.util.Optional`, `javafx.scene.control.*`, `javafx.scene.image.*`, etc.

---

## 📂 Project Structure

```bash
📦 src/
 ┣ 📄 PhotoAlbumApp.java      # Main application class
 ┣ 📄 Album.java              # Album model (name, list of photos)
 ┗ 📄 Photo.java              # Photo model (name, image path)
```

---

## ▶️ How to Run

### 🧪 Requirements
- Java SDK 17 or higher
- JavaFX SDK (download from [https://openjfx.io/](https://openjfx.io/))

### 🧩 Run from command line:
```bash
javac --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml PhotoAlbumApp.java
java --module-path /path/to/javafx/lib --add-modules javafx.controls,javafx.fxml com.example.demo.PhotoAlbumApp
```

### 💡 Or:
- Open in **IntelliJ IDEA** or **Eclipse**
- Configure the JavaFX SDK in the project SDK settings
- Run the `PhotoAlbumApp.java` file

---

## 🧠 Educational Value

This application was built as a hands-on project to practice:
- JavaFX UI development without FXML
- Clean OOP design: separation of concerns between `Album`, `Photo`, and UI logic
- Java event handling: mouse events, drag-and-drop, key events
- GUI state management using `ObservableList` and `StringProperty`
- Contextual interaction via `ContextMenu`

---

## 🗂️ Future Improvements

- Data persistence (e.g., saving albums/photos to disk as JSON or XML)
- Ability to edit photo metadata (tags, descriptions)
- Dark mode / theme toggle
- Multi-selection for batch photo deletion or movement
- Internationalization (i18n)

---

## 👨‍🎓 Author

This project was created by a **second-year university student** as part of a practical programming course.  
Feel free to use, fork, or learn from it!

---

## 📄 License

This repository is available for educational and non-commercial use. Attribution is appreciated.

---
