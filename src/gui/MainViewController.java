package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.services.DepartmentService;

public class MainViewController implements Initializable {

	@FXML
	private MenuItem menuItemSeller, menuItemDepartment, menuItemAbout;

	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}

	@FXML
	public void onMenuItemDepartmentAction() {
		loadView("/gui/DepartmentList.fxml", (DepartmentListController controller) -> {
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		});
	}

	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml",x -> {});
	}

	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub

	}

	// synchronized garante que o processamento não será interrompido no multtread

	private synchronized <T> void  loadView(String absoluteName, Consumer<T> initializingAction) {

		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			// Filhos do novo Vbox
			VBox newVbox = loader.load();

			Scene mainScene = Main.getMainScene();
			// Pegando Uma Referencia do Vbox Principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			// Pegando Uma referencia do menu
			Node mainMenu = mainVBox.getChildren().get(0);

			// Limpando todos os filhos do Vbox Principal
			mainVBox.getChildren().clear();

			// Adicionando o menu e o novo Vbox ao Vbox principal
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVbox.getChildren());
			
			
			//executa a função lambda passada por parametro para carregar a tableview
			T controller = loader.getController();
			initializingAction.accept(controller);
			

		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao Carregar a Pagina", e.getMessage(), AlertType.ERROR);
		}
	}


}
