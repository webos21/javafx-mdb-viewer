package com.gmail.webos21.fx.mdbviewer.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import com.healthmarketscience.jackcess.Column;
import com.healthmarketscience.jackcess.DataType;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import com.healthmarketscience.jackcess.Row;
import com.healthmarketscience.jackcess.Table;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MdbSceneController implements Initializable {

	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

	@FXML
	private VBox rootContainer;

	@FXML
	private MenuItem mnuOpen;

	@FXML
	private MenuItem mnuClose;

	@FXML
	private MenuItem mnuPref;

	@FXML
	private MenuItem mnuQuit;

	@FXML
	private TabPane tabControl;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}

	private void uninitialize() {
		System.out.println("MdbSceneContatiner.uninitialize()");
	}

	@FXML
	private void handleMenuOpen() {
		Stage stage = (Stage) rootContainer.getScene().getWindow();
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter mdb = new FileChooser.ExtensionFilter("Access Database (2003 and earlier) (*.mdb)",
				"*.mdb");
		FileChooser.ExtensionFilter accdb = new FileChooser.ExtensionFilter(
				"Access Database (2007 and later) (*.accdb)", "*.accdb");
		fileChooser.getExtensionFilters().add(mdb);
		fileChooser.getExtensionFilters().add(accdb);

		File file = fileChooser.showOpenDialog(stage);
		if (file != null) {
			tabControl.getTabs().clear();

			try {
				Database db = DatabaseBuilder.open(file);
				Set<String> tbNames = db.getTableNames();
				for (String tbn : tbNames) {
					StringBuilder sb = new StringBuilder();
					TextArea txtData = new TextArea();

					Tab tab = new Tab(tbn);
					tab.setContent(txtData);

					Table tb = db.getTable(tbn);
					List<? extends Column> columns = tb.getColumns();

					/* Column Title Print */
					for (Column c : columns) {
						if (sb.length() > 0) {
							sb.append(" | ");
						}
						sb.append(c.getName()).append('(').append(c.getType()).append(')');
					}
					sb.append('\n');

					/* Row Print */
					for (Row r : tb) {
						for (Column c : columns) {
							String v;
							if (c.getType() == DataType.SHORT_DATE_TIME) {
								Date ts = r.getDate(c.getName());
								v = (ts == null) ? "null" : SDF.format(ts);
							} else if (c.getType() == DataType.TEXT) {
								v = r.getString(c.getName());
							} else {
								v = (String) r.get(c.getName());
							}
							sb.append(v + " | ");
						}
						sb.append('\n');
					}

					txtData.setText(sb.toString());
					tabControl.getTabs().add(tab);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@FXML
	private void handleMenuClose() {
		tabControl.getTabs().clear();
	}

	@FXML
	private void handleMenuPref() {
	}

	@FXML
	private void handleMenuQuit() {
		rootContainer.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_HIDING, event -> {
			uninitialize();
		});

		Stage stage = (Stage) rootContainer.getScene().getWindow();
		stage.close();
	}

}
