package gui;


import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

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

public class MainViewController implements Initializable{
	
	@FXML
	private MenuItem menuItemSeller, menuItemDepartment, menuItemAbout;
	
	
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("onMenuItemSellerAction");
	}
	
	@FXML
	public void onMenuItemDepartmentAction() {
		loadView2("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAboutAction() {
		loadView("/gui/About.fxml");
	}
	
	
	
	
	@Override
	public void initialize(URL uri, ResourceBundle rb) {
		// TODO Auto-generated method stub
		
	}
	
	//synchronized garante que o processamento n�o ser� interrompido no multtread
	
	private synchronized void loadView(String absoluteName) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			//Filhos do novo Vbox
			VBox newVbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			//Pegando Uma Referencia do Vbox Principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//Pegando Uma referencia do menu
			Node mainMenu = mainVBox.getChildren().get(0);
			
			//Limpando todos os filhos do Vbox Principal
			mainVBox.getChildren().clear();
			
			//Adicionando o menu e o novo Vbox ao Vbox principal
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVbox.getChildren());
			
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao Carregar a Pagina", e.getMessage(), AlertType.ERROR);
		}
	}
	
	
private synchronized void loadView2(String absoluteName) {
		
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			//Filhos do novo Vbox
			VBox newVbox = loader.load();
			
			Scene mainScene = Main.getMainScene();
			//Pegando Uma Referencia do Vbox Principal
			VBox mainVBox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			//Pegando Uma referencia do menu
			Node mainMenu = mainVBox.getChildren().get(0);
			
			//Limpando todos os filhos do Vbox Principal
			mainVBox.getChildren().clear();
			
			//Adicionando o menu e o novo Vbox ao Vbox principal
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(newVbox.getChildren());
			
			//pegando uma referencia para o controller da view
			DepartmentListController controller = loader.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
			
		}
		catch (IOException e) {
			Alerts.showAlert("IO Exception", "Erro ao Carregar a Pagina", e.getMessage(), AlertType.ERROR);
		}
	}

}
