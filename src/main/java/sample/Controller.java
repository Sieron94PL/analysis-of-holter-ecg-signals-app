package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import model.Sample;
import qrs.Normalization;
import utils.ReadCardioPathSimple;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable{

    @FXML
    private MenuBar menuBar;
    @FXML
    private TableView<Sample> tableView;

    private File file = null;

    @FXML
    private void open(final ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        file = fileChooser.showOpenDialog(null);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) { }


}
