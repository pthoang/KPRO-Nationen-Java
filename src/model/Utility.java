package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
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

    public static BufferedImage convertFileToImage(File file) {
        try {
            return ImageIO.read(file);
        } catch (IOException ex) {
            System.out.println("Error when loading image: " + ex);
            BufferedImage standardImage = getResourceAsImage(STANDARD_IMAGE_PATH);
            return standardImage;
        }
    }

    public static WritableImage convertBufferedImageToWritable(BufferedImage bfImage) {
        return SwingFXUtils.toFXImage(bfImage, null);
    }

    public static InputStream getResourceAsStream(String filePath) {
        InputStream inStream = MainApp.class.getResourceAsStream(filePath);
        return inStream;
    }

    public static BufferedImage getResourceAsImage(String imagePath) {
        InputStream stream = getResourceAsStream(imagePath);
        try {
            return ImageIO.read(stream);
        } catch (IOException e) {
            System.out.println("Error whwn getting resources as image: " + e);
            return null;
        }
    }

    public static BufferedImage getBufferedImageFromFile(File file) {
        try {
            BufferedImage bfImage = ImageIO.read(file);
            return bfImage;
        } catch (IOException e) {
            System.out.println("Error when getting bufferedImage from file: " + e);
            return null;
        }
    }

    public static InputStream convertFileToStream(File file) {
        try {
            return new FileInputStream(file);
        } catch (IOException e) {
            System.out.println("Error when converting file to stream: " + e);
            return null;
        }
    }

    public static File convertBufferedImageToFile(BufferedImage bfImage) {
        File file = new File("tempFiles/convertImage.png");
        try {
            ImageIO.write(bfImage, "png", file);
            return file;
        } catch (IOException e) {
            System.out.println("Error when converting bufferedImage to file: " + e);
            return null;
        }
    }
}
