package com.lab12.javafxlab12;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;

@FunctionalInterface
interface Calculation {
    double calculate(double left, double right, Operator operator);
}

public class Calculator extends Application {
    private TextField leftField;
    private TextField rightField;
    private ChoiceBox<Operator> choiceBox;
    private TextField resultField;
    private Button solveButton;
    private Calculation calculation;

    public void setCalculation(Calculation calculation) {
        this.calculation = calculation;
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Calculator");
        leftField = new TextField();
        rightField = new TextField();
        rightField.setLayoutX(100);
        rightField.setLayoutY(15);

        choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(Operator.values());
        choiceBox.setValue(Operator.ADDITION);

        resultField = new TextField();
        resultField.setEditable(false);

        solveButton = new Button("Solve");
        solveButton.setMaxWidth(Double.MAX_VALUE);
        solveButton.setOnAction(actionEvent -> solve());

        GridPane pane = new GridPane();
        pane.addRow(0, leftField, choiceBox, rightField, new Label("="), resultField);
        pane.add(solveButton, 0, 1, 5, 1);

        Scene scene = new Scene(pane);

        stage.setScene(scene);
        stage.show();
    }

    private void solve() {
        double left = Double.parseDouble(leftField.getText());
        double right = Double.parseDouble(rightField.getText());
        Operator operator = choiceBox.getValue();
        try {
            double result = calculation.calculate(left, right, operator);
            resultField.setText(String.valueOf(result));
        } catch (ArithmeticException e) {
            showError(e.getMessage());
        }
    }

//    private double calculate(double left, double right, Operator operator) {
//        return switch (operator) {
//            case ADDITION ->  left + right;
//            case SUBTRACTION -> left - right;
//            case MULTIPLICATION -> left * right;
//            case DIVISION -> left / right;
//        };
//    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }

    @Override
    public void init() throws Exception {
        super.init();
        this.setCalculation((left, right, operator) -> switch (operator) {
            case ADDITION -> left + right;
            case SUBTRACTION -> left - right;
            case MULTIPLICATION -> left * right;
            case DIVISION -> {
                if (right != 0)
                    yield left / right;
                else
                    throw new ArithmeticException("Division by zero!");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}