package field;

import java.io.File;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class FieldChooser {

	private static final Stage stage = new Stage();
	private static final FileChooser fileChooser = new FileChooser();


	static {
		try {
			Parent parent = FXMLLoader
				.load(FieldChooser.class.getResource("../field/filedChooser.fxml"));

			stage.setScene(new Scene(parent));
			stage.initModality(Modality.APPLICATION_MODAL);
		} catch (IOException e) {
			e.printStackTrace();
		}

		fileChooser.setSelectedExtensionFilter(new ExtensionFilter("Image", "*.png", "*.jpg"));
		fileChooser.setTitle("Choose field image");
	}

	public ImageView image;


	public ImageView pickField() {
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

	}

	public void rotateImage(ActionEvent actionEvent) {
		image.setRotate(image.getRotate() + 90);
	}

	public void finish(ActionEvent actionEvent) {

	}

	public void changeImageScale(ActionEvent actionEvent) {
		TextField label = (TextField) actionEvent.getSource();

		image.setFitHeight(image.getImage().getHeight()* Double.parseDouble(label.getText()) / 100.0);
		image.setFitWidth(image.getImage().getWidth() * Double.parseDouble(label.getText()) / 100.0);
//		image.setFitWidth(100);
//		image.setFitHeight(100);
	}
}
