package model;

import javafx.scene.control.Alert;

import java.util.regex.Pattern;

public class Utility {

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

}
