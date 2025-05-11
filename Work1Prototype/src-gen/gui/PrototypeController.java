package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TabPane;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Modality;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Tooltip;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import java.io.File;
import javafx.scene.control.cell.PropertyValueFactory;
import java.util.List;
import java.time.LocalDate;
import java.util.LinkedList;

import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import gui.supportclass.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.util.Callback;
import services.*;
import services.impl.*;
import java.time.format.DateTimeFormatter;
import java.lang.reflect.Method;

import entities.*;

public class PrototypeController implements Initializable {


	DateTimeFormatter dateformatter;
	 
	@Override
	public void initialize(URL location, ResourceBundle resources) {
	
		work1system_service = ServiceManager.createWork1System();
		thirdpartyservices_service = ServiceManager.createThirdPartyServices();
		import_designservice_service = ServiceManager.createImport_DesignService();
		preview_design_designerservice_service = ServiceManager.createPreview_Design_DesignerService();
		preview_designservice_service = ServiceManager.createPreview_DesignService();
		export_designservice_service = ServiceManager.createExport_DesignService();
		ai_convertservice_service = ServiceManager.createAI_ConvertService();
		image_downloadservice_service = ServiceManager.createImage_DownloadService();
		preview_design_developerservice_service = ServiceManager.createPreview_Design_DeveloperService();
				
		this.dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		
	   	 //prepare data for contract
	   	 prepareData();
	   	 
	   	 //generate invariant panel
	   	 genereateInvairantPanel();
	   	 
		 //Actor Threeview Binding
		 actorTreeViewBinding();
		 
		 //Generate
		 generatOperationPane();
		 genereateOpInvariantPanel();
		 
		 //prilimariry data
		 try {
			DataFitService.fit();
		 } catch (PreconditionException e) {
			// TODO Auto-generated catch block
		 	e.printStackTrace();
		 }
		 
		 //generate class statistic
		 classStatisicBingding();
		 
		 //generate object statistic
		 generateObjectTable();
		 
		 //genereate association statistic
		 associationStatisicBingding();

		 //set listener 
		 setListeners();
	}
	
	/**
	 * deepCopyforTreeItem (Actor Generation)
	 */
	TreeItem<String> deepCopyTree(TreeItem<String> item) {
		    TreeItem<String> copy = new TreeItem<String>(item.getValue());
		    for (TreeItem<String> child : item.getChildren()) {
		        copy.getChildren().add(deepCopyTree(child));
		    }
		    return copy;
	}
	
	/**
	 * check all invariant and update invariant panel
	 */
	public void invairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}				
			}
			
			for (Entry<String, Label> inv : service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {				
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	

	/**
	 * check op invariant and update op invariant panel
	 */		
	public void opInvairantPanelUpdate() {
		
		try {
			
			for (Entry<String, Label> inv : op_entity_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String entityName = invt[0];
				for (Object o : EntityManager.getAllInstancesOf(entityName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
			for (Entry<String, Label> inv : op_service_invariants_label_map.entrySet()) {
				String invname = inv.getKey();
				String[] invt = invname.split("_");
				String serviceName = invt[0];
				for (Object o : ServiceManager.getAllInstancesOf(serviceName)) {
					 Method m = o.getClass().getMethod(invname);
					 if ((boolean)m.invoke(o) == false) {
						 inv.getValue().setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #af0c27 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
						 break;
					 }
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/* 
	*	generate op invariant panel 
	*/
	public void genereateOpInvariantPanel() {
		
		opInvariantPanel = new HashMap<String, VBox>();
		op_entity_invariants_label_map = new LinkedHashMap<String, Label>();
		op_service_invariants_label_map = new LinkedHashMap<String, Label>();
		
		VBox v;
		List<String> entities;
		v = new VBox();
		
		//entities invariants
		entities = Import_DesignServiceImpl.opINVRelatedEntity.get("upload_image");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("upload_image" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("Import_DesignService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("upload_image", v);
		
		v = new VBox();
		
		//entities invariants
		entities = Preview_Design_DeveloperServiceImpl.opINVRelatedEntity.get("preview_todo_code");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("preview_todo_code" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("Preview_Design_DeveloperService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("preview_todo_code", v);
		
		v = new VBox();
		
		//entities invariants
		entities = Preview_Design_DesignerServiceImpl.opINVRelatedEntity.get("preview_list");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("preview_list" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("Preview_Design_DesignerService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("preview_list", v);
		
		v = new VBox();
		
		//entities invariants
		entities = Image_DownloadServiceImpl.opINVRelatedEntity.get("download_image");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("download_image" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("Image_DownloadService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("download_image", v);
		
		v = new VBox();
		
		//entities invariants
		entities = Preview_DesignServiceImpl.opINVRelatedEntity.get("preview_design_list");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("preview_design_list" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("Preview_DesignService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("preview_design_list", v);
		
		v = new VBox();
		
		//entities invariants
		entities = Preview_DesignServiceImpl.opINVRelatedEntity.get("preview_develop_list");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("preview_develop_list" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("Preview_DesignService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("preview_develop_list", v);
		
		v = new VBox();
		
		//entities invariants
		entities = Export_DesignServiceImpl.opINVRelatedEntity.get("fetch_design_info");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("fetch_design_info" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("Export_DesignService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("fetch_design_info", v);
		
		v = new VBox();
		
		//entities invariants
		entities = AI_ConvertServiceImpl.opINVRelatedEntity.get("design_to_code");
		if (entities != null) {
			for (String opRelatedEntities : entities) {
				for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
					
					String invname = inv.getKey();
					String[] invt = invname.split("_");
					String entityName = invt[0];
		
					if (opRelatedEntities.equals(entityName)) {
						Label l = new Label(invname);
						l.setStyle("-fx-max-width: Infinity;" + 
								"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
							    "-fx-padding: 6px;" +
							    "-fx-border-color: black;");
						Tooltip tp = new Tooltip();
						tp.setText(inv.getValue());
						l.setTooltip(tp);
						
						op_entity_invariants_label_map.put(invname, l);
						
						v.getChildren().add(l);
					}
				}
			}
		} else {
			Label l = new Label("design_to_code" + " is no related invariants");
			l.setPadding(new Insets(8, 8, 8, 8));
			v.getChildren().add(l);
		}
		
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			String invname = inv.getKey();
			String[] invt = invname.split("_");
			String serviceName = invt[0];
			
			if (serviceName.equals("AI_ConvertService")) {
				Label l = new Label(invname);
				l.setStyle("-fx-max-width: Infinity;" + 
						   "-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
						   "-fx-padding: 6px;" +
						   "-fx-border-color: black;");
				Tooltip tp = new Tooltip();
				tp.setText(inv.getValue());
				l.setTooltip(tp);
				
				op_entity_invariants_label_map.put(invname, l);
				
				v.getChildren().add(l);
			}
		}
		opInvariantPanel.put("design_to_code", v);
		
		
	}
	
	
	/*
	*  generate invariant panel
	*/
	public void genereateInvairantPanel() {
		
		service_invariants_label_map = new LinkedHashMap<String, Label>();
		entity_invariants_label_map = new LinkedHashMap<String, Label>();
		
		//entity_invariants_map
		VBox v = new VBox();
		//service invariants
		for (Entry<String, String> inv : service_invariants_map.entrySet()) {
			
			Label l = new Label(inv.getKey());
			l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			service_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		//entity invariants
		for (Entry<String, String> inv : entity_invariants_map.entrySet()) {
			
			String INVname = inv.getKey();
			Label l = new Label(INVname);
			if (INVname.contains("AssociationInvariants")) {
				l.setStyle("-fx-max-width: Infinity;" + 
					"-fx-background-color: linear-gradient(to right, #099b17 0%, #F0FFFF 100%);" +
				    "-fx-padding: 6px;" +
				    "-fx-border-color: black;");
			} else {
				l.setStyle("-fx-max-width: Infinity;" + 
									"-fx-background-color: linear-gradient(to right, #7FFF00 0%, #F0FFFF 100%);" +
								    "-fx-padding: 6px;" +
								    "-fx-border-color: black;");
			}	
			Tooltip tp = new Tooltip();
			tp.setText(inv.getValue());
			l.setTooltip(tp);
			
			entity_invariants_label_map.put(inv.getKey(), l);
			v.getChildren().add(l);
			
		}
		ScrollPane scrollPane = new ScrollPane(v);
		scrollPane.setFitToWidth(true);
		all_invariant_pane.setMaxHeight(850);
		
		all_invariant_pane.setContent(scrollPane);
	}	
	
	
	
	/* 
	*	mainPane add listener
	*/
	public void setListeners() {
		 mainPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
			 
			 	if (newTab.getText().equals("System State")) {
			 		System.out.println("refresh all");
			 		refreshAll();
			 	}
		    
		    });
	}
	
	
	//checking all invariants
	public void checkAllInvariants() {
		
		invairantPanelUpdate();
	
	}	
	
	//refresh all
	public void refreshAll() {
		
		invairantPanelUpdate();
		classStatisticUpdate();
		generateObjectTable();
	}
	
	
	//update association
	public void updateAssociation(String className) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber();
		}
		
	}
	
	public void updateAssociation(String className, int index) {
		
		for (AssociationInfo assoc : allassociationData.get(className)) {
			assoc.computeAssociationNumber(index);
		}
		
	}	
	
	public void generateObjectTable() {
		
		allObjectTables = new LinkedHashMap<String, TableView>();
		
		TableView<Map<String, String>> tableDesigner = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableDesigner_UserId = new TableColumn<Map<String, String>, String>("UserId");
		tableDesigner_UserId.setMinWidth("UserId".length()*10);
		tableDesigner_UserId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserId"));
		    }
		});	
		tableDesigner.getColumns().add(tableDesigner_UserId);
		TableColumn<Map<String, String>, String> tableDesigner_Name = new TableColumn<Map<String, String>, String>("Name");
		tableDesigner_Name.setMinWidth("Name".length()*10);
		tableDesigner_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableDesigner.getColumns().add(tableDesigner_Name);
		TableColumn<Map<String, String>, String> tableDesigner_Type = new TableColumn<Map<String, String>, String>("Type");
		tableDesigner_Type.setMinWidth("Type".length()*10);
		tableDesigner_Type.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Type"));
		    }
		});	
		tableDesigner.getColumns().add(tableDesigner_Type);
		
		//table data
		ObservableList<Map<String, String>> dataDesigner = FXCollections.observableArrayList();
		List<Designer> rsDesigner = EntityManager.getAllInstancesOf("Designer");
		for (Designer r : rsDesigner) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getUserId() != null)
				unit.put("UserId", String.valueOf(r.getUserId()));
			else
				unit.put("UserId", "");
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			unit.put("Type", String.valueOf(r.getType()));

			dataDesigner.add(unit);
		}
		
		tableDesigner.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableDesigner.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Designer", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableDesigner.setItems(dataDesigner);
		allObjectTables.put("Designer", tableDesigner);
		
		TableView<Map<String, String>> tableDesignImage = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableDesignImage_DesignId = new TableColumn<Map<String, String>, String>("DesignId");
		tableDesignImage_DesignId.setMinWidth("DesignId".length()*10);
		tableDesignImage_DesignId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("DesignId"));
		    }
		});	
		tableDesignImage.getColumns().add(tableDesignImage_DesignId);
		TableColumn<Map<String, String>, String> tableDesignImage_FileUrl = new TableColumn<Map<String, String>, String>("FileUrl");
		tableDesignImage_FileUrl.setMinWidth("FileUrl".length()*10);
		tableDesignImage_FileUrl.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("FileUrl"));
		    }
		});	
		tableDesignImage.getColumns().add(tableDesignImage_FileUrl);
		TableColumn<Map<String, String>, String> tableDesignImage_DesignStatus = new TableColumn<Map<String, String>, String>("DesignStatus");
		tableDesignImage_DesignStatus.setMinWidth("DesignStatus".length()*10);
		tableDesignImage_DesignStatus.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("DesignStatus"));
		    }
		});	
		tableDesignImage.getColumns().add(tableDesignImage_DesignStatus);
		TableColumn<Map<String, String>, String> tableDesignImage_Id = new TableColumn<Map<String, String>, String>("Id");
		tableDesignImage_Id.setMinWidth("Id".length()*10);
		tableDesignImage_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableDesignImage.getColumns().add(tableDesignImage_Id);
		
		//table data
		ObservableList<Map<String, String>> dataDesignImage = FXCollections.observableArrayList();
		List<DesignImage> rsDesignImage = EntityManager.getAllInstancesOf("DesignImage");
		for (DesignImage r : rsDesignImage) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getDesignId() != null)
				unit.put("DesignId", String.valueOf(r.getDesignId()));
			else
				unit.put("DesignId", "");
			if (r.getFileUrl() != null)
				unit.put("FileUrl", String.valueOf(r.getFileUrl()));
			else
				unit.put("FileUrl", "");
			unit.put("DesignStatus", String.valueOf(r.getDesignStatus()));
			if (r.getId() != null)
				unit.put("Id", String.valueOf(r.getId()));
			else
				unit.put("Id", "");

			dataDesignImage.add(unit);
		}
		
		tableDesignImage.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableDesignImage.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("DesignImage", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableDesignImage.setItems(dataDesignImage);
		allObjectTables.put("DesignImage", tableDesignImage);
		
		TableView<Map<String, String>> tableDeveloper = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableDeveloper_UserId = new TableColumn<Map<String, String>, String>("UserId");
		tableDeveloper_UserId.setMinWidth("UserId".length()*10);
		tableDeveloper_UserId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("UserId"));
		    }
		});	
		tableDeveloper.getColumns().add(tableDeveloper_UserId);
		TableColumn<Map<String, String>, String> tableDeveloper_Name = new TableColumn<Map<String, String>, String>("Name");
		tableDeveloper_Name.setMinWidth("Name".length()*10);
		tableDeveloper_Name.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Name"));
		    }
		});	
		tableDeveloper.getColumns().add(tableDeveloper_Name);
		TableColumn<Map<String, String>, String> tableDeveloper_Type = new TableColumn<Map<String, String>, String>("Type");
		tableDeveloper_Type.setMinWidth("Type".length()*10);
		tableDeveloper_Type.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Type"));
		    }
		});	
		tableDeveloper.getColumns().add(tableDeveloper_Type);
		
		//table data
		ObservableList<Map<String, String>> dataDeveloper = FXCollections.observableArrayList();
		List<Developer> rsDeveloper = EntityManager.getAllInstancesOf("Developer");
		for (Developer r : rsDeveloper) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getUserId() != null)
				unit.put("UserId", String.valueOf(r.getUserId()));
			else
				unit.put("UserId", "");
			if (r.getName() != null)
				unit.put("Name", String.valueOf(r.getName()));
			else
				unit.put("Name", "");
			unit.put("Type", String.valueOf(r.getType()));

			dataDeveloper.add(unit);
		}
		
		tableDeveloper.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableDeveloper.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Developer", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableDeveloper.setItems(dataDeveloper);
		allObjectTables.put("Developer", tableDeveloper);
		
		TableView<Map<String, String>> tableMessage = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableMessage_Title = new TableColumn<Map<String, String>, String>("Title");
		tableMessage_Title.setMinWidth("Title".length()*10);
		tableMessage_Title.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Title"));
		    }
		});	
		tableMessage.getColumns().add(tableMessage_Title);
		TableColumn<Map<String, String>, String> tableMessage_Content = new TableColumn<Map<String, String>, String>("Content");
		tableMessage_Content.setMinWidth("Content".length()*10);
		tableMessage_Content.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Content"));
		    }
		});	
		tableMessage.getColumns().add(tableMessage_Content);
		TableColumn<Map<String, String>, String> tableMessage_Id = new TableColumn<Map<String, String>, String>("Id");
		tableMessage_Id.setMinWidth("Id".length()*10);
		tableMessage_Id.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Id"));
		    }
		});	
		tableMessage.getColumns().add(tableMessage_Id);
		
		//table data
		ObservableList<Map<String, String>> dataMessage = FXCollections.observableArrayList();
		List<Message> rsMessage = EntityManager.getAllInstancesOf("Message");
		for (Message r : rsMessage) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getTitle() != null)
				unit.put("Title", String.valueOf(r.getTitle()));
			else
				unit.put("Title", "");
			if (r.getContent() != null)
				unit.put("Content", String.valueOf(r.getContent()));
			else
				unit.put("Content", "");
			if (r.getId() != null)
				unit.put("Id", String.valueOf(r.getId()));
			else
				unit.put("Id", "");

			dataMessage.add(unit);
		}
		
		tableMessage.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableMessage.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("Message", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableMessage.setItems(dataMessage);
		allObjectTables.put("Message", tableMessage);
		
		TableView<Map<String, String>> tableAIProcess = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableAIProcess_DesignId = new TableColumn<Map<String, String>, String>("DesignId");
		tableAIProcess_DesignId.setMinWidth("DesignId".length()*10);
		tableAIProcess_DesignId.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("DesignId"));
		    }
		});	
		tableAIProcess.getColumns().add(tableAIProcess_DesignId);
		TableColumn<Map<String, String>, String> tableAIProcess_Result = new TableColumn<Map<String, String>, String>("Result");
		tableAIProcess_Result.setMinWidth("Result".length()*10);
		tableAIProcess_Result.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Result"));
		    }
		});	
		tableAIProcess.getColumns().add(tableAIProcess_Result);
		
		//table data
		ObservableList<Map<String, String>> dataAIProcess = FXCollections.observableArrayList();
		List<AIProcess> rsAIProcess = EntityManager.getAllInstancesOf("AIProcess");
		for (AIProcess r : rsAIProcess) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getDesignId() != null)
				unit.put("DesignId", String.valueOf(r.getDesignId()));
			else
				unit.put("DesignId", "");
			if (r.getResult() != null)
				unit.put("Result", String.valueOf(r.getResult()));
			else
				unit.put("Result", "");

			dataAIProcess.add(unit);
		}
		
		tableAIProcess.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableAIProcess.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("AIProcess", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableAIProcess.setItems(dataAIProcess);
		allObjectTables.put("AIProcess", tableAIProcess);
		
		TableView<Map<String, String>> tableDesignList = new TableView<Map<String, String>>();

		//super entity attribute column
						
		//attributes table column
		TableColumn<Map<String, String>, String> tableDesignList_List = new TableColumn<Map<String, String>, String>("List");
		tableDesignList_List.setMinWidth("List".length()*10);
		tableDesignList_List.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("List"));
		    }
		});	
		tableDesignList.getColumns().add(tableDesignList_List);
		TableColumn<Map<String, String>, String> tableDesignList_Type = new TableColumn<Map<String, String>, String>("Type");
		tableDesignList_Type.setMinWidth("Type".length()*10);
		tableDesignList_Type.setCellValueFactory(new Callback<CellDataFeatures<Map<String, String>, String>, ObservableValue<String>>() {	   
			@Override
		    public ObservableValue<String> call(CellDataFeatures<Map<String, String>, String> data) {
		        return new ReadOnlyStringWrapper(data.getValue().get("Type"));
		    }
		});	
		tableDesignList.getColumns().add(tableDesignList_Type);
		
		//table data
		ObservableList<Map<String, String>> dataDesignList = FXCollections.observableArrayList();
		List<DesignList> rsDesignList = EntityManager.getAllInstancesOf("DesignList");
		for (DesignList r : rsDesignList) {
			//table entry
			Map<String, String> unit = new HashMap<String, String>();
			
			if (r.getList() != null)
				unit.put("List", String.valueOf(r.getList()));
			else
				unit.put("List", "");
			unit.put("Type", String.valueOf(r.getType()));

			dataDesignList.add(unit);
		}
		
		tableDesignList.getSelectionModel().selectedIndexProperty().addListener(
							 (observable, oldValue, newValue) ->  { 
							 										 //get selected index
							 										 objectindex = tableDesignList.getSelectionModel().getSelectedIndex();
							 			 				 			 System.out.println("select: " + objectindex);

							 			 				 			 //update association object information
							 			 				 			 if (objectindex != -1)
										 			 					 updateAssociation("DesignList", objectindex);
							 			 				 			 
							 			 				 		  });
		
		tableDesignList.setItems(dataDesignList);
		allObjectTables.put("DesignList", tableDesignList);
		

		
	}
	
	/* 
	* update all object tables with sub dataset
	*/ 
	public void updateDesignerTable(List<Designer> rsDesigner) {
			ObservableList<Map<String, String>> dataDesigner = FXCollections.observableArrayList();
			for (Designer r : rsDesigner) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("Type", String.valueOf(r.getType()));
				dataDesigner.add(unit);
			}
			
			allObjectTables.get("Designer").setItems(dataDesigner);
	}
	public void updateDesignImageTable(List<DesignImage> rsDesignImage) {
			ObservableList<Map<String, String>> dataDesignImage = FXCollections.observableArrayList();
			for (DesignImage r : rsDesignImage) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getDesignId() != null)
					unit.put("DesignId", String.valueOf(r.getDesignId()));
				else
					unit.put("DesignId", "");
				if (r.getFileUrl() != null)
					unit.put("FileUrl", String.valueOf(r.getFileUrl()));
				else
					unit.put("FileUrl", "");
				unit.put("DesignStatus", String.valueOf(r.getDesignStatus()));
				if (r.getId() != null)
					unit.put("Id", String.valueOf(r.getId()));
				else
					unit.put("Id", "");
				dataDesignImage.add(unit);
			}
			
			allObjectTables.get("DesignImage").setItems(dataDesignImage);
	}
	public void updateDeveloperTable(List<Developer> rsDeveloper) {
			ObservableList<Map<String, String>> dataDeveloper = FXCollections.observableArrayList();
			for (Developer r : rsDeveloper) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("Type", String.valueOf(r.getType()));
				dataDeveloper.add(unit);
			}
			
			allObjectTables.get("Developer").setItems(dataDeveloper);
	}
	public void updateMessageTable(List<Message> rsMessage) {
			ObservableList<Map<String, String>> dataMessage = FXCollections.observableArrayList();
			for (Message r : rsMessage) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getTitle() != null)
					unit.put("Title", String.valueOf(r.getTitle()));
				else
					unit.put("Title", "");
				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				if (r.getId() != null)
					unit.put("Id", String.valueOf(r.getId()));
				else
					unit.put("Id", "");
				dataMessage.add(unit);
			}
			
			allObjectTables.get("Message").setItems(dataMessage);
	}
	public void updateAIProcessTable(List<AIProcess> rsAIProcess) {
			ObservableList<Map<String, String>> dataAIProcess = FXCollections.observableArrayList();
			for (AIProcess r : rsAIProcess) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getDesignId() != null)
					unit.put("DesignId", String.valueOf(r.getDesignId()));
				else
					unit.put("DesignId", "");
				if (r.getResult() != null)
					unit.put("Result", String.valueOf(r.getResult()));
				else
					unit.put("Result", "");
				dataAIProcess.add(unit);
			}
			
			allObjectTables.get("AIProcess").setItems(dataAIProcess);
	}
	public void updateDesignListTable(List<DesignList> rsDesignList) {
			ObservableList<Map<String, String>> dataDesignList = FXCollections.observableArrayList();
			for (DesignList r : rsDesignList) {
				Map<String, String> unit = new HashMap<String, String>();
				
				
				if (r.getList() != null)
					unit.put("List", String.valueOf(r.getList()));
				else
					unit.put("List", "");
				unit.put("Type", String.valueOf(r.getType()));
				dataDesignList.add(unit);
			}
			
			allObjectTables.get("DesignList").setItems(dataDesignList);
	}
	
	/* 
	* update all object tables
	*/ 
	public void updateDesignerTable() {
			ObservableList<Map<String, String>> dataDesigner = FXCollections.observableArrayList();
			List<Designer> rsDesigner = EntityManager.getAllInstancesOf("Designer");
			for (Designer r : rsDesigner) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("Type", String.valueOf(r.getType()));
				dataDesigner.add(unit);
			}
			
			allObjectTables.get("Designer").setItems(dataDesigner);
	}
	public void updateDesignImageTable() {
			ObservableList<Map<String, String>> dataDesignImage = FXCollections.observableArrayList();
			List<DesignImage> rsDesignImage = EntityManager.getAllInstancesOf("DesignImage");
			for (DesignImage r : rsDesignImage) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getDesignId() != null)
					unit.put("DesignId", String.valueOf(r.getDesignId()));
				else
					unit.put("DesignId", "");
				if (r.getFileUrl() != null)
					unit.put("FileUrl", String.valueOf(r.getFileUrl()));
				else
					unit.put("FileUrl", "");
				unit.put("DesignStatus", String.valueOf(r.getDesignStatus()));
				if (r.getId() != null)
					unit.put("Id", String.valueOf(r.getId()));
				else
					unit.put("Id", "");
				dataDesignImage.add(unit);
			}
			
			allObjectTables.get("DesignImage").setItems(dataDesignImage);
	}
	public void updateDeveloperTable() {
			ObservableList<Map<String, String>> dataDeveloper = FXCollections.observableArrayList();
			List<Developer> rsDeveloper = EntityManager.getAllInstancesOf("Developer");
			for (Developer r : rsDeveloper) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getUserId() != null)
					unit.put("UserId", String.valueOf(r.getUserId()));
				else
					unit.put("UserId", "");
				if (r.getName() != null)
					unit.put("Name", String.valueOf(r.getName()));
				else
					unit.put("Name", "");
				unit.put("Type", String.valueOf(r.getType()));
				dataDeveloper.add(unit);
			}
			
			allObjectTables.get("Developer").setItems(dataDeveloper);
	}
	public void updateMessageTable() {
			ObservableList<Map<String, String>> dataMessage = FXCollections.observableArrayList();
			List<Message> rsMessage = EntityManager.getAllInstancesOf("Message");
			for (Message r : rsMessage) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getTitle() != null)
					unit.put("Title", String.valueOf(r.getTitle()));
				else
					unit.put("Title", "");
				if (r.getContent() != null)
					unit.put("Content", String.valueOf(r.getContent()));
				else
					unit.put("Content", "");
				if (r.getId() != null)
					unit.put("Id", String.valueOf(r.getId()));
				else
					unit.put("Id", "");
				dataMessage.add(unit);
			}
			
			allObjectTables.get("Message").setItems(dataMessage);
	}
	public void updateAIProcessTable() {
			ObservableList<Map<String, String>> dataAIProcess = FXCollections.observableArrayList();
			List<AIProcess> rsAIProcess = EntityManager.getAllInstancesOf("AIProcess");
			for (AIProcess r : rsAIProcess) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getDesignId() != null)
					unit.put("DesignId", String.valueOf(r.getDesignId()));
				else
					unit.put("DesignId", "");
				if (r.getResult() != null)
					unit.put("Result", String.valueOf(r.getResult()));
				else
					unit.put("Result", "");
				dataAIProcess.add(unit);
			}
			
			allObjectTables.get("AIProcess").setItems(dataAIProcess);
	}
	public void updateDesignListTable() {
			ObservableList<Map<String, String>> dataDesignList = FXCollections.observableArrayList();
			List<DesignList> rsDesignList = EntityManager.getAllInstancesOf("DesignList");
			for (DesignList r : rsDesignList) {
				Map<String, String> unit = new HashMap<String, String>();


				if (r.getList() != null)
					unit.put("List", String.valueOf(r.getList()));
				else
					unit.put("List", "");
				unit.put("Type", String.valueOf(r.getType()));
				dataDesignList.add(unit);
			}
			
			allObjectTables.get("DesignList").setItems(dataDesignList);
	}
	
	public void classStatisicBingding() {
	
		 classInfodata = FXCollections.observableArrayList();
	 	 designer = new ClassInfo("Designer", EntityManager.getAllInstancesOf("Designer").size());
	 	 classInfodata.add(designer);
	 	 designimage = new ClassInfo("DesignImage", EntityManager.getAllInstancesOf("DesignImage").size());
	 	 classInfodata.add(designimage);
	 	 developer = new ClassInfo("Developer", EntityManager.getAllInstancesOf("Developer").size());
	 	 classInfodata.add(developer);
	 	 message = new ClassInfo("Message", EntityManager.getAllInstancesOf("Message").size());
	 	 classInfodata.add(message);
	 	 aiprocess = new ClassInfo("AIProcess", EntityManager.getAllInstancesOf("AIProcess").size());
	 	 classInfodata.add(aiprocess);
	 	 designlist = new ClassInfo("DesignList", EntityManager.getAllInstancesOf("DesignList").size());
	 	 classInfodata.add(designlist);
	 	 
		 class_statisic.setItems(classInfodata);
		 
		 //Class Statisic Binding
		 class_statisic.getSelectionModel().selectedItemProperty().addListener(
				 (observable, oldValue, newValue) ->  { 
				 										 //no selected object in table
				 										 objectindex = -1;
				 										 
				 										 //get lastest data, reflect updateTableData method
				 										 try {
												 			 Method updateob = this.getClass().getMethod("update" + newValue.getName() + "Table", null);
												 			 updateob.invoke(this);			 
												 		 } catch (Exception e) {
												 		 	 e.printStackTrace();
												 		 }		 										 
				 	
				 										 //show object table
				 			 				 			 TableView obs = allObjectTables.get(newValue.getName());
				 			 				 			 if (obs != null) {
				 			 				 				object_statics.setContent(obs);
				 			 				 				object_statics.setText("All Objects " + newValue.getName() + ":");
				 			 				 			 }
				 			 				 			 
				 			 				 			 //update association information
							 			 				 updateAssociation(newValue.getName());
				 			 				 			 
				 			 				 			 //show association information
				 			 				 			 ObservableList<AssociationInfo> asso = allassociationData.get(newValue.getName());
				 			 				 			 if (asso != null) {
				 			 				 			 	association_statisic.setItems(asso);
				 			 				 			 }
				 			 				 		  });
	}
	
	public void classStatisticUpdate() {
	 	 designer.setNumber(EntityManager.getAllInstancesOf("Designer").size());
	 	 designimage.setNumber(EntityManager.getAllInstancesOf("DesignImage").size());
	 	 developer.setNumber(EntityManager.getAllInstancesOf("Developer").size());
	 	 message.setNumber(EntityManager.getAllInstancesOf("Message").size());
	 	 aiprocess.setNumber(EntityManager.getAllInstancesOf("AIProcess").size());
	 	 designlist.setNumber(EntityManager.getAllInstancesOf("DesignList").size());
		
	}
	
	/**
	 * association binding
	 */
	public void associationStatisicBingding() {
		
		allassociationData = new HashMap<String, ObservableList<AssociationInfo>>();
		
		ObservableList<AssociationInfo> Designer_association_data = FXCollections.observableArrayList();
		AssociationInfo Designer_associatition_DesignertoDesignList = new AssociationInfo("Designer", "DesignList", "DesignertoDesignList", false);
		Designer_association_data.add(Designer_associatition_DesignertoDesignList);
		AssociationInfo Designer_associatition_DesignertoMessage = new AssociationInfo("Designer", "Message", "DesignertoMessage", true);
		Designer_association_data.add(Designer_associatition_DesignertoMessage);
		
		allassociationData.put("Designer", Designer_association_data);
		
		ObservableList<AssociationInfo> DesignImage_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("DesignImage", DesignImage_association_data);
		
		ObservableList<AssociationInfo> Developer_association_data = FXCollections.observableArrayList();
		AssociationInfo Developer_associatition_DevelopertoDesignList = new AssociationInfo("Developer", "DesignList", "DevelopertoDesignList", false);
		Developer_association_data.add(Developer_associatition_DevelopertoDesignList);
		AssociationInfo Developer_associatition_DevelopertoAIProcess = new AssociationInfo("Developer", "AIProcess", "DevelopertoAIProcess", false);
		Developer_association_data.add(Developer_associatition_DevelopertoAIProcess);
		
		allassociationData.put("Developer", Developer_association_data);
		
		ObservableList<AssociationInfo> Message_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("Message", Message_association_data);
		
		ObservableList<AssociationInfo> AIProcess_association_data = FXCollections.observableArrayList();
		
		allassociationData.put("AIProcess", AIProcess_association_data);
		
		ObservableList<AssociationInfo> DesignList_association_data = FXCollections.observableArrayList();
		AssociationInfo DesignList_associatition_DesignListtoDesignImage = new AssociationInfo("DesignList", "DesignImage", "DesignListtoDesignImage", true);
		DesignList_association_data.add(DesignList_associatition_DesignListtoDesignImage);
		
		allassociationData.put("DesignList", DesignList_association_data);
		
		
		association_statisic.getSelectionModel().selectedItemProperty().addListener(
			    (observable, oldValue, newValue) ->  { 
	
							 		if (newValue != null) {
							 			 try {
							 			 	 if (newValue.getNumber() != 0) {
								 				 //choose object or not
								 				 if (objectindex != -1) {
									 				 Class[] cArg = new Class[1];
									 				 cArg[0] = List.class;
									 				 //reflect updateTableData method
										 			 Method updateob = this.getClass().getMethod("update" + newValue.getTargetClass() + "Table", cArg);
										 			 //find choosen object
										 			 Object selectedob = EntityManager.getAllInstancesOf(newValue.getSourceClass()).get(objectindex);
										 			 //reflect find association method
										 			 Method getAssociatedObject = selectedob.getClass().getMethod("get" + newValue.getAssociationName());
										 			 List r = new LinkedList();
										 			 //one or mulity?
										 			 if (newValue.getIsMultiple() == true) {
											 			 
											 			r = (List) getAssociatedObject.invoke(selectedob);
										 			 }
										 			 else {
										 				r.add(getAssociatedObject.invoke(selectedob));
										 			 }
										 			 //invoke update method
										 			 updateob.invoke(this, r);
										 			  
										 			 
								 				 }
												 //bind updated data to GUI
					 				 			 TableView obs = allObjectTables.get(newValue.getTargetClass());
					 				 			 if (obs != null) {
					 				 				object_statics.setContent(obs);
					 				 				object_statics.setText("Targets Objects " + newValue.getTargetClass() + ":");
					 				 			 }
					 				 		 }
							 			 }
							 			 catch (Exception e) {
							 				 e.printStackTrace();
							 			 }
							 		}
					 		  });
		
	}	
	
	

    //prepare data for contract
	public void prepareData() {
		
		//definition map
		definitions_map = new HashMap<String, String>();
		
		//precondition map
		preconditions_map = new HashMap<String, String>();
		preconditions_map.put("upload_image", "true");
		preconditions_map.put("preview_todo_code", "true");
		preconditions_map.put("preview_list", "true");
		preconditions_map.put("download_image", "true");
		preconditions_map.put("preview_design_list", "true");
		preconditions_map.put("preview_develop_list", "true");
		preconditions_map.put("fetch_design_info", "true");
		preconditions_map.put("design_to_code", "true");
		
		//postcondition map
		postconditions_map = new HashMap<String, String>();
		postconditions_map.put("upload_image", "self.DesignImage.FileUrl = image");
		postconditions_map.put("preview_todo_code", "result = self.DesignImage");
		postconditions_map.put("preview_list", "result = self.DesignList");
		postconditions_map.put("download_image", "result = self.DesignImage.FileUrl");
		postconditions_map.put("preview_design_list", "result = self.DesignList");
		postconditions_map.put("preview_develop_list", "result = self.DesignList");
		postconditions_map.put("fetch_design_info", "result = self.DesignImage");
		postconditions_map.put("design_to_code", "result = self.DesignImage");
		
		//service invariants map
		service_invariants_map = new LinkedHashMap<String, String>();
		
		//entity invariants map
		entity_invariants_map = new LinkedHashMap<String, String>();
		
	}
	
	public void generatOperationPane() {
		
		 operationPanels = new LinkedHashMap<String, GridPane>();
		
		 // ==================== GridPane_upload_image ====================
		 GridPane upload_image = new GridPane();
		 upload_image.setHgap(4);
		 upload_image.setVgap(6);
		 upload_image.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> upload_image_content = upload_image.getChildren();
		 Label upload_image_file_label = new Label("file:");
		 upload_image_file_label.setMinWidth(Region.USE_PREF_SIZE);
		 upload_image_content.add(upload_image_file_label);
		 GridPane.setConstraints(upload_image_file_label, 0, 0);
		 
		 upload_image_file_t = new TextField();
		 upload_image_content.add(upload_image_file_t);
		 upload_image_file_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(upload_image_file_t, 1, 0);
		 operationPanels.put("upload_image", upload_image);
		 
		 // ==================== GridPane_preview_todo_code ====================
		 GridPane preview_todo_code = new GridPane();
		 preview_todo_code.setHgap(4);
		 preview_todo_code.setVgap(6);
		 preview_todo_code.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> preview_todo_code_content = preview_todo_code.getChildren();
		 Label preview_todo_code_label = new Label("This operation is no intput parameters..");
		 preview_todo_code_label.setMinWidth(Region.USE_PREF_SIZE);
		 preview_todo_code_content.add(preview_todo_code_label);
		 GridPane.setConstraints(preview_todo_code_label, 0, 0);
		 operationPanels.put("preview_todo_code", preview_todo_code);
		 
		 // ==================== GridPane_preview_list ====================
		 GridPane preview_list = new GridPane();
		 preview_list.setHgap(4);
		 preview_list.setVgap(6);
		 preview_list.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> preview_list_content = preview_list.getChildren();
		 Label preview_list_label = new Label("This operation is no intput parameters..");
		 preview_list_label.setMinWidth(Region.USE_PREF_SIZE);
		 preview_list_content.add(preview_list_label);
		 GridPane.setConstraints(preview_list_label, 0, 0);
		 operationPanels.put("preview_list", preview_list);
		 
		 // ==================== GridPane_download_image ====================
		 GridPane download_image = new GridPane();
		 download_image.setHgap(4);
		 download_image.setVgap(6);
		 download_image.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> download_image_content = download_image.getChildren();
		 Label download_image_design_id_label = new Label("design_id:");
		 download_image_design_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 download_image_content.add(download_image_design_id_label);
		 GridPane.setConstraints(download_image_design_id_label, 0, 0);
		 
		 download_image_design_id_t = new TextField();
		 download_image_content.add(download_image_design_id_t);
		 download_image_design_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(download_image_design_id_t, 1, 0);
		 operationPanels.put("download_image", download_image);
		 
		 // ==================== GridPane_preview_design_list ====================
		 GridPane preview_design_list = new GridPane();
		 preview_design_list.setHgap(4);
		 preview_design_list.setVgap(6);
		 preview_design_list.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> preview_design_list_content = preview_design_list.getChildren();
		 Label preview_design_list_label = new Label("This operation is no intput parameters..");
		 preview_design_list_label.setMinWidth(Region.USE_PREF_SIZE);
		 preview_design_list_content.add(preview_design_list_label);
		 GridPane.setConstraints(preview_design_list_label, 0, 0);
		 operationPanels.put("preview_design_list", preview_design_list);
		 
		 // ==================== GridPane_preview_develop_list ====================
		 GridPane preview_develop_list = new GridPane();
		 preview_develop_list.setHgap(4);
		 preview_develop_list.setVgap(6);
		 preview_develop_list.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> preview_develop_list_content = preview_develop_list.getChildren();
		 Label preview_develop_list_label = new Label("This operation is no intput parameters..");
		 preview_develop_list_label.setMinWidth(Region.USE_PREF_SIZE);
		 preview_develop_list_content.add(preview_develop_list_label);
		 GridPane.setConstraints(preview_develop_list_label, 0, 0);
		 operationPanels.put("preview_develop_list", preview_develop_list);
		 
		 // ==================== GridPane_fetch_design_info ====================
		 GridPane fetch_design_info = new GridPane();
		 fetch_design_info.setHgap(4);
		 fetch_design_info.setVgap(6);
		 fetch_design_info.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> fetch_design_info_content = fetch_design_info.getChildren();
		 Label fetch_design_info_design_id_label = new Label("design_id:");
		 fetch_design_info_design_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 fetch_design_info_content.add(fetch_design_info_design_id_label);
		 GridPane.setConstraints(fetch_design_info_design_id_label, 0, 0);
		 
		 fetch_design_info_design_id_t = new TextField();
		 fetch_design_info_content.add(fetch_design_info_design_id_t);
		 fetch_design_info_design_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(fetch_design_info_design_id_t, 1, 0);
		 operationPanels.put("fetch_design_info", fetch_design_info);
		 
		 // ==================== GridPane_design_to_code ====================
		 GridPane design_to_code = new GridPane();
		 design_to_code.setHgap(4);
		 design_to_code.setVgap(6);
		 design_to_code.setPadding(new Insets(8, 8, 8, 8));
		 
		 ObservableList<Node> design_to_code_content = design_to_code.getChildren();
		 Label design_to_code_design_id_label = new Label("design_id:");
		 design_to_code_design_id_label.setMinWidth(Region.USE_PREF_SIZE);
		 design_to_code_content.add(design_to_code_design_id_label);
		 GridPane.setConstraints(design_to_code_design_id_label, 0, 0);
		 
		 design_to_code_design_id_t = new TextField();
		 design_to_code_content.add(design_to_code_design_id_t);
		 design_to_code_design_id_t.setMinWidth(Region.USE_PREF_SIZE);
		 GridPane.setConstraints(design_to_code_design_id_t, 1, 0);
		 operationPanels.put("design_to_code", design_to_code);
		 
	}	

	public void actorTreeViewBinding() {
		
		 

		TreeItem<String> treeRootadministrator = new TreeItem<String>("Root node");
		
		
					 			
						 		
		treeRootadministrator.getChildren().addAll(Arrays.asList(
				));	
				
	 			treeRootadministrator.setExpanded(true);

		actor_treeview_administrator.setShowRoot(false);
		actor_treeview_administrator.setRoot(treeRootadministrator);
	 		
		actor_treeview_administrator.getSelectionModel().selectedItemProperty().addListener(
		 				 (observable, oldValue, newValue) -> { 
		 				 								
		 				 							 //clear the previous return
		 											 operation_return_pane.setContent(new Label());
		 											 
		 				 							 clickedOp = newValue.getValue();
		 				 							 GridPane op = operationPanels.get(clickedOp);
		 				 							 VBox vb = opInvariantPanel.get(clickedOp);
		 				 							 
		 				 							 //op pannel
		 				 							 if (op != null) {
		 				 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
		 				 								 
		 				 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
		 				 								 choosenOperation = new LinkedList<TextField>();
		 				 								 for (Node n : l) {
		 				 								 	 if (n instanceof TextField) {
		 				 								 	 	choosenOperation.add((TextField)n);
		 				 								 	  }
		 				 								 }
		 				 								 
		 				 								 definition.setText(definitions_map.get(newValue.getValue()));
		 				 								 precondition.setText(preconditions_map.get(newValue.getValue()));
		 				 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
		 				 								 
		 				 						     }
		 				 							 else {
		 				 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
		 				 								 l.setPadding(new Insets(8, 8, 8, 8));
		 				 								 operation_paras.setContent(l);
		 				 							 }	
		 				 							 
		 				 							 //op invariants
		 				 							 if (vb != null) {
		 				 							 	ScrollPane scrollPane = new ScrollPane(vb);
		 				 							 	scrollPane.setFitToWidth(true);
		 				 							 	invariants_panes.setMaxHeight(200); 
		 				 							 	//all_invariant_pane.setContent(scrollPane);	
		 				 							 	
		 				 							 	invariants_panes.setContent(scrollPane);
		 				 							 } else {
		 				 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
		 				 							     l.setPadding(new Insets(8, 8, 8, 8));
		 				 							     invariants_panes.setContent(l);
		 				 							 }
		 				 							 
		 				 							 //reset pre- and post-conditions area color
		 				 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
		 				 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
		 				 							 //reset condition panel title
		 				 							 precondition_pane.setText("Precondition");
		 				 							 postcondition_pane.setText("Postcondition");
		 				 						} 
		 				 				);

		
		
		 
		TreeItem<String> treeRootdesigner = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_import_Design = new TreeItem<String>("import_Design");
			subTreeRoot_import_Design.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("upload_image")
				 		));	
			TreeItem<String> subTreeRoot_preview_Design_Designer = new TreeItem<String>("preview_Design_Designer");
			subTreeRoot_preview_Design_Designer.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("preview_list")
				 		));	
		
		treeRootdesigner.getChildren().addAll(Arrays.asList(
			subTreeRoot_import_Design,
			subTreeRoot_preview_Design_Designer
					));
		
		treeRootdesigner.setExpanded(true);

		actor_treeview_designer.setShowRoot(false);
		actor_treeview_designer.setRoot(treeRootdesigner);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_designer.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
		TreeItem<String> treeRootdeveloper = new TreeItem<String>("Root node");
			TreeItem<String> subTreeRoot_export_Design = new TreeItem<String>("export_Design");
			subTreeRoot_export_Design.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("fetch_design_info")
				 		));	
			TreeItem<String> subTreeRoot_preview_Design_Developer = new TreeItem<String>("preview_Design_Developer");
			subTreeRoot_preview_Design_Developer.getChildren().addAll(Arrays.asList(			 		    
					 	new TreeItem<String>("preview_todo_code")
				 		));	
		
		treeRootdeveloper.getChildren().addAll(Arrays.asList(
			subTreeRoot_export_Design,
			subTreeRoot_preview_Design_Developer
					));
		
		treeRootdeveloper.setExpanded(true);

		actor_treeview_developer.setShowRoot(false);
		actor_treeview_developer.setRoot(treeRootdeveloper);
		
		//TreeView click, then open the GridPane for inputing parameters
		actor_treeview_developer.getSelectionModel().selectedItemProperty().addListener(
						 (observable, oldValue, newValue) -> { 
						 								
						 							 //clear the previous return
													 operation_return_pane.setContent(new Label());
													 
						 							 clickedOp = newValue.getValue();
						 							 GridPane op = operationPanels.get(clickedOp);
						 							 VBox vb = opInvariantPanel.get(clickedOp);
						 							 
						 							 //op pannel
						 							 if (op != null) {
						 								 operation_paras.setContent(operationPanels.get(newValue.getValue()));
						 								 
						 								 ObservableList<Node> l = operationPanels.get(newValue.getValue()).getChildren();
						 								 choosenOperation = new LinkedList<TextField>();
						 								 for (Node n : l) {
						 								 	 if (n instanceof TextField) {
						 								 	 	choosenOperation.add((TextField)n);
						 								 	  }
						 								 }
						 								 
						 								 definition.setText(definitions_map.get(newValue.getValue()));
						 								 precondition.setText(preconditions_map.get(newValue.getValue()));
						 								 postcondition.setText(postconditions_map.get(newValue.getValue()));
						 								 
						 						     }
						 							 else {
						 								 Label l = new Label(newValue.getValue() + " is no contract information in requirement model.");
						 								 l.setPadding(new Insets(8, 8, 8, 8));
						 								 operation_paras.setContent(l);
						 							 }	
						 							 
						 							 //op invariants
						 							 if (vb != null) {
						 							 	ScrollPane scrollPane = new ScrollPane(vb);
						 							 	scrollPane.setFitToWidth(true);
						 							 	invariants_panes.setMaxHeight(200); 
						 							 	//all_invariant_pane.setContent(scrollPane);	
						 							 	
						 							 	invariants_panes.setContent(scrollPane);
						 							 } else {
						 							 	 Label l = new Label(newValue.getValue() + " is no related invariants");
						 							     l.setPadding(new Insets(8, 8, 8, 8));
						 							     invariants_panes.setContent(l);
						 							 }
						 							 
						 							 //reset pre- and post-conditions area color
						 							 precondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF ");
						 							 postcondition.setStyle("-fx-background-color:#FFFFFF; -fx-control-inner-background: #FFFFFF");
						 							 //reset condition panel title
						 							 precondition_pane.setText("Precondition");
						 							 postcondition_pane.setText("Postcondition");
						 						} 
						 				);
	}

	/**
	*    Execute Operation
	*/
	@FXML
	public void execute(ActionEvent event) {
		
		switch (clickedOp) {
		case "upload_image" : upload_image(); break;
		case "preview_todo_code" : preview_todo_code(); break;
		case "preview_list" : preview_list(); break;
		case "download_image" : download_image(); break;
		case "preview_design_list" : preview_design_list(); break;
		case "preview_develop_list" : preview_develop_list(); break;
		case "fetch_design_info" : fetch_design_info(); break;
		case "design_to_code" : design_to_code(); break;
		
		}
		
		System.out.println("execute buttion clicked");
		
		//checking relevant invariants
		opInvairantPanelUpdate();
	}

	/**
	*    Refresh All
	*/		
	@FXML
	public void refresh(ActionEvent event) {
		
		refreshAll();
		System.out.println("refresh all");
	}		
	
	/**
	*    Save All
	*/			
	@FXML
	public void save(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save State to File");
		fileChooser.setInitialFileName("*.state");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showSaveDialog(stage);
		
		if (file != null) {
			System.out.println("save state to file " + file.getAbsolutePath());				
			EntityManager.save(file);
		}
	}
	
	/**
	*    Load All
	*/			
	@FXML
	public void load(ActionEvent event) {
		
		Stage stage = (Stage) mainPane.getScene().getWindow();
		
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Open State File");
		fileChooser.getExtensionFilters().addAll(
		         new ExtensionFilter("RMCode State File", "*.state"));
		
		File file = fileChooser.showOpenDialog(stage);
		
		if (file != null) {
			System.out.println("choose file" + file.getAbsolutePath());
			EntityManager.load(file); 
		}
		
		//refresh GUI after load data
		refreshAll();
	}
	
	
	//precondition unsat dialog
	public void preconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Precondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}
	
	//postcondition unsat dialog
	public void postconditionUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("Postcondtion is not satisfied");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}

	public void thirdpartyServiceUnSat() {
		
		Alert alert = new Alert(AlertType.WARNING, "");
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.initOwner(mainPane.getScene().getWindow());
        alert.getDialogPane().setContentText("third party service is exception");
        alert.getDialogPane().setHeaderText(null);
        alert.showAndWait();	
	}		
	
	
	public void upload_image() {
		
		System.out.println("execute upload_image");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: upload_image in service: Import_DesignService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(import_designservice_service.upload_image(
			upload_image_file_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void preview_todo_code() {
		
		System.out.println("execute preview_todo_code");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: preview_todo_code in service: Preview_Design_DeveloperService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(preview_design_developerservice_service.preview_todo_code(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void preview_list() {
		
		System.out.println("execute preview_list");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: preview_list in service: Preview_Design_DesignerService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(preview_design_designerservice_service.preview_list(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void download_image() {
		
		System.out.println("execute download_image");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: download_image in service: Image_DownloadService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(image_downloadservice_service.download_image(
			download_image_design_id_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void preview_design_list() {
		
		System.out.println("execute preview_design_list");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: preview_design_list in service: Preview_DesignService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(preview_designservice_service.preview_design_list(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void preview_develop_list() {
		
		System.out.println("execute preview_develop_list");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: preview_develop_list in service: Preview_DesignService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(preview_designservice_service.preview_develop_list(
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void fetch_design_info() {
		
		System.out.println("execute fetch_design_info");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: fetch_design_info in service: Export_DesignService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(export_designservice_service.fetch_design_info(
			fetch_design_info_design_id_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}
	public void design_to_code() {
		
		System.out.println("execute design_to_code");
		String time = String.format("%1$tH:%1$tM:%1$tS", System.currentTimeMillis());
		log.appendText(time + " -- execute operation: design_to_code in service: AI_ConvertService ");
		
		try {
			//invoke op with parameters
			//return value is primitive type, bind result to label.
			String result = String.valueOf(ai_convertservice_service.design_to_code(
			design_to_code_design_id_t.getText()
			));	
			Label l = new Label(result);
			l.setPadding(new Insets(8, 8, 8, 8));
			operation_return_pane.setContent(l);
		    log.appendText(" -- success!\n");
		    //set pre- and post-conditions text area color
		    precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #7CFC00");
		    //contract evaluation result
		    precondition_pane.setText("Precondition: True");
		    postcondition_pane.setText("Postcondition: True");
		    
		    
		} catch (PreconditionException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (PostconditionException e) {
			log.appendText(" -- failed!\n");
			postcondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");	
			postcondition_pane.setText("Postcondition: False");
			postconditionUnSat();
			
		} catch (NumberFormatException e) {
			log.appendText(" -- failed!\n");
			precondition.setStyle("-fx-background-color:#000000; -fx-control-inner-background: #FF0000");
			precondition_pane.setText("Precondition: False");	
			preconditionUnSat();
			
		} catch (Exception e ) {		
			if (e instanceof ThirdPartyServiceException)
				thirdpartyServiceUnSat();
		}
	}




	//select object index
	int objectindex;
	
	@FXML
	TabPane mainPane;

	@FXML
	TextArea log;
	
	@FXML
	TreeView<String> actor_treeview_designer;
	@FXML
	TreeView<String> actor_treeview_developer;
	
	@FXML
	TreeView<String> actor_treeview_administrator;


	@FXML
	TextArea definition;
	@FXML
	TextArea precondition;
	@FXML
	TextArea postcondition;
	@FXML
	TextArea invariants;
	
	@FXML
	TitledPane precondition_pane;
	@FXML
	TitledPane postcondition_pane;
	
	//chosen operation textfields
	List<TextField> choosenOperation;
	String clickedOp;
		
	@FXML
	TableView<ClassInfo> class_statisic;
	@FXML
	TableView<AssociationInfo> association_statisic;
	
	Map<String, ObservableList<AssociationInfo>> allassociationData;
	ObservableList<ClassInfo> classInfodata;
	
	Work1System work1system_service;
	ThirdPartyServices thirdpartyservices_service;
	Import_DesignService import_designservice_service;
	Preview_Design_DesignerService preview_design_designerservice_service;
	Preview_DesignService preview_designservice_service;
	Export_DesignService export_designservice_service;
	AI_ConvertService ai_convertservice_service;
	Image_DownloadService image_downloadservice_service;
	Preview_Design_DeveloperService preview_design_developerservice_service;
	
	ClassInfo designer;
	ClassInfo designimage;
	ClassInfo developer;
	ClassInfo message;
	ClassInfo aiprocess;
	ClassInfo designlist;
		
	@FXML
	TitledPane object_statics;
	Map<String, TableView> allObjectTables;
	
	@FXML
	TitledPane operation_paras;
	
	@FXML
	TitledPane operation_return_pane;
	
	@FXML 
	TitledPane all_invariant_pane;
	
	@FXML
	TitledPane invariants_panes;
	
	Map<String, GridPane> operationPanels;
	Map<String, VBox> opInvariantPanel;
	
	//all textfiled or eumntity
	TextField upload_image_file_t;
	TextField download_image_design_id_t;
	TextField fetch_design_info_design_id_t;
	TextField design_to_code_design_id_t;
	
	HashMap<String, String> definitions_map;
	HashMap<String, String> preconditions_map;
	HashMap<String, String> postconditions_map;
	HashMap<String, String> invariants_map;
	LinkedHashMap<String, String> service_invariants_map;
	LinkedHashMap<String, String> entity_invariants_map;
	LinkedHashMap<String, Label> service_invariants_label_map;
	LinkedHashMap<String, Label> entity_invariants_label_map;
	LinkedHashMap<String, Label> op_entity_invariants_label_map;
	LinkedHashMap<String, Label> op_service_invariants_label_map;
	

	
}
