package com.example.demo;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.TransferMode;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.io.File;
import java.util.Optional;
import javafx.scene.input.Dragboard;
import java.util.List;
import java.util.stream.Collectors;
import javafx.scene.input.*;

public class PhotoAlbumApp extends Application {
    private ObservableList<Album> albums = FXCollections.observableArrayList();
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Photo Album Manager");

        ListView<Album> albumListView = new ListView<>(albums);
        albumListView.setCellFactory(lv -> new ListCell<Album>() {
            @Override
            protected void updateItem(Album album, boolean empty) {
                super.updateItem(album, empty);
                if (empty || album == null) {
                    textProperty().unbind();
                    setText(null);
                } else {
                    textProperty().bind(album.nameProperty());
                }
            }
        });

        albumListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !event.isConsumed()) {
                event.consume();
                Album selected = albumListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openAlbum(selected);
                }
            }
        });

        TextField searchField = new TextField();
        searchField.setPromptText("Search Albums...");
        searchField.setOnKeyReleased(e -> {
            String searchText = searchField.getText().toLowerCase();
            if (searchText.isEmpty()) {
                albumListView.setItems(albums);
            } else {
                ObservableList<Album> filtered = albums.filtered(
                        album -> album.getName().toLowerCase().contains(searchText));
                albumListView.setItems(filtered);
            }
        });

        Button createButton = new Button("Create Album");
        createButton.setOnAction(e -> createAlbum());

        Button deleteButton = new Button("Delete Album");
        deleteButton.setOnAction(e -> {
            Album selected = albumListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                albums.remove(selected);
            }
        });

        Button renameButton = new Button("Rename Album");
        renameButton.setOnAction(e -> {
            Album selected = albumListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                renameAlbum(selected);
            }
        });

        HBox controlPanel = new HBox(10, createButton, deleteButton, renameButton);
        VBox albumListVBox = new VBox(10, new Label("Albums:"), searchField, albumListView, controlPanel);

        Scene scene = new Scene(albumListVBox, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void createAlbum() {
        TextInputDialog dialog = new TextInputDialog("New Album");
        dialog.setTitle("Create New Album");
        dialog.setHeaderText("Enter the Name of the New Album:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> albums.add(new Album(name)));
    }

    private void renameAlbum(Album album) {
        TextInputDialog dialog = new TextInputDialog(album.getName());
        dialog.setTitle("Rename Album");
        dialog.setHeaderText("Rename Album");
        dialog.setContentText("Enter new album name:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(album::setName);
    }

    private void openAlbum(Album album) {
        Stage albumStage = new Stage();
        albumStage.setTitle(album.getName());

        ListView<Photo> photoListView = new ListView<>(album.getPhotos());
        photoListView.setCellFactory(lv -> new ListCell<Photo>() {
            private ImageView imageView = new ImageView();

            private ContextMenu contextMenu = new ContextMenu();

            {

                MenuItem deleteItem = new MenuItem("Delete Photo");
                deleteItem.setOnAction(e -> {
                    Photo photo = getItem();
                    if (photo != null) {
                        getListView().getItems().remove(photo);
                    }
                });

                MenuItem copyItem = new MenuItem("Copy Photo to Another Album");
                copyItem.setOnAction(e -> {
                    Photo photo = getItem();
                    if (photo != null) {
                        copyPhotoToAnotherAlbum(photo);
                    }
                });

                contextMenu.getItems().addAll(deleteItem, copyItem);
            }


            @Override
            protected void updateItem(Photo photo, boolean empty) {
                super.updateItem(photo, empty);
                if (empty || photo == null) {
                    setText(null);
                    setGraphic(null);
                    setContextMenu(null);
                } else {
                    setText(photo.getName());
                    imageView.setImage(new Image(photo.getImagePath(), 100, 100, true, true));
                    imageView.setPreserveRatio(true);
                    setGraphic(imageView);
                    setContextMenu(contextMenu);
                }
            }

        });
        TextField photoSearchField = new TextField();
        photoSearchField.setPromptText("Search Photos...");
        photoSearchField.setOnKeyReleased(e -> {
            String searchText = photoSearchField.getText().toLowerCase();
            if (searchText.isEmpty()) {
                photoListView.setItems(album.getPhotos());
            } else {
                ObservableList<Photo> filtered = album.getPhotos().filtered(
                        photo -> photo.getName().toLowerCase().contains(searchText));
                photoListView.setItems(filtered);
            }
        });
        // Setup Drag-and-Drop
        setupDragAndDrop(photoListView, album);

        // Setup double-click to open photo
        photoListView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
                Photo selected = photoListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    openPhoto(selected);
                }
            }
        });
        photoListView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DELETE) {
                Photo selected = photoListView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    deletePhoto(selected, album);
                    event.consume();
                }
            }
        });
        Button addPhotoButton = new Button("Add Photo");
        addPhotoButton.setOnAction(e -> addPhotoToAlbum(album, photoListView));

        VBox photoControls = new VBox(10);
        photoControls.getChildren().addAll(new Label("Photos:"), photoSearchField, photoListView, addPhotoButton);
        VBox.setVgrow(photoListView, Priority.ALWAYS);

        VBox.setVgrow(photoListView, Priority.ALWAYS);

        Scene albumScene = new Scene(photoControls, 600, 400);
        albumStage.setScene(albumScene);
        albumStage.show();
    }
    private void deletePhoto(Photo photo, Album album) {
        if (photo != null && album != null) {
            album.getPhotos().remove(photo);
        }
    }
    private void openPhoto(Photo photo) {
        if (photo == null) return;
        Stage photoStage = new Stage();
        photoStage.setTitle(photo.getName());
        ImageView imageView = new ImageView(new Image(photo.getImagePath()));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(600);
        imageView.setFitWidth(800);
        StackPane pane = new StackPane(imageView);
        pane.setStyle("-fx-background-color: BLACK;");
        Scene scene = new Scene(pane, 800, 600);
        photoStage.setScene(scene);
        photoStage.show();
    }


    private void copyPhotoToAnotherAlbum(Photo photo) {
        List<String> choices = albums.stream()
                .map(Album::getName)
                .collect(Collectors.toList());

        ChoiceDialog<String> dialog = new ChoiceDialog<>(null, choices);
        dialog.setTitle("Copy Photo");
        dialog.setHeaderText("Select an album to copy the photo to:");
        dialog.setContentText("Choose an album:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(albumName -> {
            Album targetAlbum = albums.stream()
                    .filter(a -> a.getName().equals(albumName))
                    .findFirst()
                    .orElse(null);
            if (targetAlbum != null) {
                targetAlbum.getPhotos().add(new Photo(photo.getName(), photo.getImagePath()));
            }
        });
    }



    private void addPhotoToAlbum(Album album, ListView<Photo> photoListView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photos");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
        );
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null) {
            album.getPhotos().add(new Photo(file.getName(), file.toURI().toString()));
            photoListView.setItems(album.getPhotos());
        }
    }

    class PhotoCell extends ListCell<Photo> {
        private ImageView imageView = new ImageView();
        private ContextMenu contextMenu = new ContextMenu();

        public PhotoCell() {
            MenuItem deleteItem = new MenuItem("Delete Photo");
            deleteItem.setOnAction(e -> getListView().getItems().remove(getItem()));

            MenuItem copyItem = new MenuItem("Copy Photo to Another Album");
            copyItem.setOnAction(e -> copyPhotoToAnotherAlbum(getItem()));

            contextMenu.getItems().addAll(deleteItem, copyItem);
        }

        @Override
        protected void updateItem(Photo photo, boolean empty) {
            super.updateItem(photo, empty);
            if (empty || photo == null) {
                setText(null);
                setGraphic(null);
                setContextMenu(null);
            } else {
                setText(photo.getName());
                imageView.setImage(new Image(photo.getImagePath(), 100, 100, true, true));
                imageView.setPreserveRatio(true);
                setGraphic(imageView);
                setContextMenu(contextMenu);
            }
        }
    }
    private void setupDragAndDrop(ListView<Photo> photoListView, Album album) {
        photoListView.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.ANY);
            }
            event.consume();
        });
        photoListView.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            boolean success = false;
            if (db.hasFiles()) {
                db.getFiles().stream()
                        .filter(file -> file.getName().matches(".*\\.(jpg|jpeg|png|gif)$"))
                        .forEach(file -> {
                            Photo photo = new Photo(file.getName(), file.toURI().toString());
                            album.getPhotos().add(photo);
                        });
                success = true;
                photoListView.setItems(album.getPhotos()); // Refresh the ListView
            }
            event.setDropCompleted(success);
            event.consume();
        });
    }



    public static void main(String[] args) {
        launch(args);
    }
}

class Album {
    private StringProperty name;
    private ObservableList<Photo> photos = FXCollections.observableArrayList();

    public Album(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public StringProperty nameProperty() {
        return name;
    }

    public ObservableList<Photo> getPhotos() {
        return photos;
    }
}

class Photo {
    private String name;
    private String imagePath;

    public Photo(String name, String imagePath) {
        this.name = name;
        this.imagePath = imagePath;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }
}