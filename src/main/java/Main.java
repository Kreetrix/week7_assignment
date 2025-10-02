import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private TextField inputField = new TextField();
    private ComboBox<String> conversionBox = new ComboBox<>();
    private Label resultLabel = new Label();
    private double lastResult;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        inputField.setPromptText("Enter value");

        conversionBox.getItems().addAll(
                "Celsius → Fahrenheit",
                "Fahrenheit → Celsius",
                "Kelvin → Celsius",
                "Celsius → Kelvin"
        );
        conversionBox.setPromptText("Select conversion");

        Button convertButton = new Button("Convert");
        convertButton.setOnAction(e -> convert());

        Button saveButton = new Button("Save to DB");
        saveButton.setOnAction(e -> {
            try {
                double input = Double.parseDouble(inputField.getText());
                Database.saveTemperature(input, lastResult, resultLabel);
            } catch (NumberFormatException ex) {
                resultLabel.setText("Invalid input to save.");
            }
        });

        VBox root = new VBox(10, inputField, conversionBox, convertButton, resultLabel, saveButton);
        root.setPadding(new Insets(15));

        Scene scene = new Scene(root, 300, 250);
        stage.setTitle("Temperature Converter");
        stage.setScene(scene);
        stage.show();
    }

    private void convert() {
        String type = conversionBox.getValue();
        if (type == null) {
            resultLabel.setText("Please select a conversion.");
            return;
        }

        try {
            double value = Double.parseDouble(inputField.getText());

            switch (type) {
                case "Celsius → Fahrenheit":
                    lastResult = (value * 9 / 5) + 32;
                    resultLabel.setText(String.format("%.2f °C = %.2f °F", value, lastResult));
                    break;

                case "Fahrenheit → Celsius":
                    lastResult = (value - 32) * 5 / 9;
                    resultLabel.setText(String.format("%.2f °F = %.2f °C", value, lastResult));
                    break;

                case "Kelvin → Celsius":
                    lastResult = value - 273.15;
                    resultLabel.setText(String.format("%.2f K = %.2f °C", value, lastResult));
                    break;

                case "Celsius → Kelvin":
                    lastResult = value + 273.15;
                    resultLabel.setText(String.format("%.2f °C = %.2f K", value, lastResult));
                    break;
            }

        } catch (NumberFormatException ex) {
            resultLabel.setText("Invalid number format!");
        }
    }
}
