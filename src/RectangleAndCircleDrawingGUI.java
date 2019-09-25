
import javafx.application.*;
import javafx.event.ActionEvent;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;

public class RectangleAndCircleDrawingGUI extends Application {

    private boolean drawingAShape = false;
    private RadioButton redRadioButton, blueRadioButton, greenRadioButton, thickStrokeButton, thinStrokeButton,
            rectangleButton, circleButton;
    private Button clearButton, deleteLastDrawnButton;
    private CheckBox fillBox;

    private double startPointX;
    private double startPointY;
    private double endPointX;
    private double endPointY;

    Pane drawingPane = new Pane();

    @Override
    public void start(Stage primaryStage) {

        try {
            // Colors
            redRadioButton = new RadioButton("Red");
            redRadioButton.setTextFill(Color.RED);
            redRadioButton.setSelected(true);
            blueRadioButton = new RadioButton("Blue");
            blueRadioButton.setTextFill(Color.BLUE);
            greenRadioButton = new RadioButton("Green");
            greenRadioButton.setTextFill(Color.GREEN);

            ToggleGroup penColorGroup = new ToggleGroup();
            redRadioButton.setToggleGroup(penColorGroup);
            blueRadioButton.setToggleGroup(penColorGroup);
            greenRadioButton.setToggleGroup(penColorGroup);

            VBox leftControlBox = new VBox(redRadioButton, blueRadioButton, greenRadioButton);
            leftControlBox.setSpacing(10);

            // Thick and Thin Stroke Buttons
            thickStrokeButton = new RadioButton("Thick");
            thickStrokeButton.setFont(Font.font("Verdana", FontWeight.BOLD, 12));
            thinStrokeButton = new RadioButton("Thin");
            thinStrokeButton.setSelected(true);

            ToggleGroup strokeGroup = new ToggleGroup();
            thickStrokeButton.setToggleGroup(strokeGroup);
            thinStrokeButton.setToggleGroup(strokeGroup);

            HBox strokeBox = new HBox(thinStrokeButton, thickStrokeButton);
            strokeBox.setSpacing(10);

            // Fill CheckBox
            fillBox = new CheckBox("Fill");

            // delete last drawn shape button
            deleteLastDrawnButton = new Button("Delete Last Drawn");
            deleteLastDrawnButton.setOnAction(this::handleButtons);

            VBox rightControlBox = new VBox(strokeBox, fillBox, deleteLastDrawnButton);
            rightControlBox.setSpacing(7);

            // select shape Buttons
            rectangleButton = new RadioButton("Rectangle");
            rectangleButton.setFont(Font.font("Tacoma", 14));
            rectangleButton.setSelected(true);
            circleButton = new RadioButton("Circle");
            circleButton.setFont(Font.font("Tacoma", 14));

            ToggleGroup shapesGroup = new ToggleGroup();
            rectangleButton.setToggleGroup(shapesGroup);
            circleButton.setToggleGroup(shapesGroup);

            HBox shapesBox = new HBox(rectangleButton, circleButton);
            shapesBox.setSpacing(10);

            // Clear Button
            clearButton = new Button("Clear");
            clearButton.setFont(Font.font("Tacoma", 20));
            clearButton.setOnAction(this::handleButtons);

            VBox middleControlBox = new VBox(shapesBox, clearButton);
            middleControlBox.setSpacing(15);
            middleControlBox.setAlignment(Pos.CENTER);

            // HBOX that encompasses all control buttons
            HBox allButtonsBox = new HBox(leftControlBox, middleControlBox, rightControlBox);
            allButtonsBox.setSpacing(50);
            allButtonsBox.setAlignment(Pos.CENTER);
            allButtonsBox.setPadding(new Insets(15, 15, 15, 15));

            // Set up the buttons at the bottom of the main pane
            drawingPane.setPrefSize(600, 500);
            drawingPane.setBorder(new Border(new BorderStroke(Color.DARKSLATEGRAY, BorderStrokeStyle.SOLID,
                    new CornerRadii(0), new BorderWidths(3))));
            VBox mainPane = new VBox(drawingPane, allButtonsBox);
            mainPane.setAlignment(Pos.CENTER);

            drawingPane.setOnMouseClicked(this::handleMouseClick);
            drawingPane.setOnMouseMoved(this::handleMouseMotion);
            Scene scene = new Scene(mainPane, 500, 500);
            primaryStage.setTitle("Draw Rectangles and Circles");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (Exception ex) {
            System.out.println("Error - Please double check the program for any changes to the javaFX API");
        }
    }

    private void deleteLastNodeOnShapesPane() {
        if (!drawingPane.getChildren().isEmpty()) {
            int size = drawingPane.getChildren().size();
            drawingPane.getChildren().remove(size - 1);
        }
    }

    private void handleButtons(ActionEvent event) {

        if (event.getSource() == deleteLastDrawnButton) {
            deleteLastNodeOnShapesPane();
            if (drawingAShape) {
                drawingAShape = false;
            }
        } else if (event.getSource() == clearButton) {
            drawingPane.getChildren().clear();
        }
    }

    private void setShapeStyle(Shape shape) {

        Color fillColor;
        if (blueRadioButton.isSelected()) {
            shape.setStroke(Color.BLUE);
            fillColor = Color.BLUE;
        } else if (redRadioButton.isSelected()) {
            shape.setStroke(Color.RED);
            fillColor = Color.RED;
        } else if (greenRadioButton.isSelected()) {
            shape.setStroke(Color.GREEN);
            fillColor = Color.GREEN;
        } else {
            fillColor = Color.BLACK;
        }

        if (fillBox.isSelected()) {
            shape.setFill(fillColor);
        } else {
            shape.setFill(Color.TRANSPARENT);
        }

        if (thickStrokeButton.isSelected()) {
            shape.setStrokeWidth(5);
        } else {
            shape.setStrokeWidth(1);
        }

    }

    private void handleMouseClick(MouseEvent event) {

        if (!drawingAShape) {
            drawingAShape = true;

            startPointX = event.getX();
            startPointY = event.getY();

            // add one child to drawingPane as a buffer to make the deleteLastDrawn method
            // work
            // this node is to be deleted as soon as mouse starts moving, or remain
            // invisible
            // (0 width and height) if the mouse clicks on the EXACT SAME spot consecutively
            // TWICE
            drawingPane.getChildren().add(new Rectangle(-1, -1, 0, 0));

        } else { // to finish drawing
            drawingAShape = false;
        }

    }

    private void handleMouseMotion(MouseEvent event) {

        if (drawingAShape) {
            Shape dynamicShape;
            endPointX = event.getX();
            endPointY = event.getY();

            if (rectangleButton.isSelected()) {

                double width = Math.abs(endPointX - startPointX);
                double height = Math.abs(endPointY - startPointY);

                // start point from the MouseClick is not reset
                double dynamicStartPointX = Math.min(startPointX, endPointX);
                double dynamicStartPointY = Math.min(startPointY, endPointY);

                Rectangle dynamicRectangle = new Rectangle(dynamicStartPointX, dynamicStartPointY, width, height);
                dynamicShape = dynamicRectangle;
            } else { // (circleButton.isSelected() true)
                double radius = Math.sqrt(Math.pow(endPointX - startPointX, 2) + Math.pow(endPointY - startPointY, 2));
                Circle stillCircle = new Circle(startPointX, startPointY, radius);
                dynamicShape = stillCircle;
            }

            // delete the previous drawn dynamic shape every time mouse moves,
            // or the first shape added to the pane from initial MouseClick before drawing
            deleteLastNodeOnShapesPane();
            setShapeStyle(dynamicShape); // can I preset it somewhere so that no need to reset during mouse motion?
            drawingPane.getChildren().add(dynamicShape);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}