package field;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class FieldChooser implements Initializable {

    private static final Stage stage = new Stage();
    private static final FileChooser fileChooser = new FileChooser();
    private static ImageView image = new ImageView();

    static {
        try {
            Parent parent = FXMLLoader
                    .load(FieldChooser.class.getResource("fieldChooser.fxml"));

            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Image", "*.png", "*.jpg"));
        fileChooser.setTitle("Choose field image");
    }

    private final ArrayList<Point2D> selectionPoints = new ArrayList<>();
    public Text selectionText;
    public ScrollPane scrollPane;
    private boolean selecting;

    public static ImageView pickField() {
        stage.showAndWait();
        return image;
    }

    public void showImage(ActionEvent actionEvent) throws IOException {
        File file = fileChooser.showOpenDialog(stage);

        if (file != null) {
            image.setImage(new Image("file:" + file.getCanonicalPath()));
            image.setPreserveRatio(true);
        }
    }

    public void selectAndCropImage(ActionEvent actionEvent) {
        selectionText.setText("Select 2 points");
        selecting = true;

//		TODO get make program wait for the user to select two points
        Timeline timeline = new Timeline();
        KeyFrame keyFrame = new KeyFrame(Duration.millis(100), event -> {
            System.out.println("Hello");
            if (selectionPoints.size() == 2)
//					timeline.getKeyFrames().add(new KeyFrame(Duration.millis(100), this));
            {
                selecting = false;
                selectionPoints.sort((o1, o2) -> (int) ((o1.getY() - o2.getY())));
                Point2D bottom = selectionPoints.get(0);
                Point2D up = selectionPoints.get(1);

                PixelReader reader = image.getImage().getPixelReader();
                WritableImage newImage = new WritableImage(reader,
                        (int) bottom.getX(),
                        (int) bottom.getY(),
                        (int) Math.abs(up.getX() - bottom.getX()),
                        (int) Math.abs(up.getY() - bottom.getY())
                );

                image.setImage(newImage);
            }
        });

        timeline.getKeyFrames().add(keyFrame);
//		timeline.setCycleCount(Timeline.INDEFINITE);
//		timeline.setAutoReverse(true);
        //FIXME make timeline repeat infitively

        timeline.play();
    }

    public void rotateImage(ActionEvent actionEvent) {
        image.setRotate(image.getRotate() + 90);
    }

    public void finish(ActionEvent actionEvent) {
        ((Node) actionEvent.getSource()).getScene().getWindow().hide();
    }

    public void changeImageScale(ActionEvent actionEvent) {
        TextField label = (TextField) actionEvent.getSource();

        image.setFitHeight(
                image.getImage().getHeight() * Double.parseDouble(label.getText()) / 100.0);
        image
                .setFitWidth(image.getImage().getWidth() * Double.parseDouble(label.getText()) / 100.0);
    }

    public void selectPoint(MouseEvent mouseEvent) {

        if (selecting) {
            Point2D point2D = new Point2D(mouseEvent.getX(), mouseEvent.getY());
            selectionPoints.add(point2D);

            System.out.println(selectionPoints.size());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scrollPane.setContent(image);
        image.setPreserveRatio(true);
        image.setOnMouseClicked(this::selectPoint);
    }

    public void setScale(ActionEvent actionEvent) {
        
    }

    public Double setDistance(ActionEvent actionEvent) {
        TextField meters = (TextField) actionEvent.getSource();
        double distance = Double.parseDouble(meters.getText());
        return distance;

    }


}

//TODO add button for 2 points then draw line

// press 2 dots then a line goes through the dots
// then pop up come up asking for real length --> save into static variable