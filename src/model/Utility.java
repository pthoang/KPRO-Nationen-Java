package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;

import Main.MainApp;

public class Utility {

    public final static String STANDARD_IMAGE_PATH = "/resources/style/standard.png";

    public static void newAlertError(String headerText, String contentText) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Feilmeldinger");
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

    public static String validateDescription(String description) {
        if (description.length() <= 5 || description.equals(null)) {
            return "\n Beskrivelse mangler";
        }
        return "";
    }

    public static String validateName(String name) {
        Pattern pattern = Pattern.compile("^[A-ZÆØÅa-zæøå. \\-]++$");
        String errorMessage = "";

        if (name.length() <= 2) {
            errorMessage += "\n Navn må være lengre enn 2 bokstaver.";
        }
        if (!pattern.matcher(name).matches()) {
            errorMessage += "\n Navnet inneholder ugyldige bokstaver. Tillatt er: a-å, ., og -";
        }

        return errorMessage;
    }

    public static WritableImage convertFileToImage(File file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            return SwingFXUtils.toFXImage(bufferedImage, null);
        } catch (IOException ex) {
            System.out.println("Error when loading image: " + ex);
            return null;
        }
    }

    public static File getResourcesFile(String filePath) {
        return new File(MainApp.class.getResource(filePath).getFile());
    }
}
