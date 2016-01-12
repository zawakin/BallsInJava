package stagemaker;

import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.converter.NumberStringConverter;
import mainSystem.ColorType;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javafx.collections.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;


public class DataStage extends Stage{
	
	private Stage parentStage;
	public ObservableList<Figure> dataobslist;
	private TableView<Figure> datatable;
	
	@SuppressWarnings("unchecked")
    public DataStage(StageWindow owner,MyStage ms){
		parentStage = owner;
        setTitle("Figures");
        setWidth(600);
        setHeight(500);
        setX(800);
        setY(getY()+400);
        

        VBox dataroot = new VBox();

		MenuBar menubar = new MenuBar();
		menubar.setUseSystemMenuBar(true);
//		menubar.setPrefHeight(40);
		Menu file = new Menu("ƒtƒ@ƒCƒ‹");
		MenuItem restart = new MenuItem("New");
		MenuItem load = new MenuItem("Load");
		MenuItem save = new MenuItem("Save");
		file.getItems().addAll(restart,load,save);
		menubar.getMenus().addAll(file);

		
		restart.setOnAction(e -> {
			ms.init(true);
			owner.drawAllFigure();
			dataobslist.removeAll(dataobslist);
			for(int i=0;i<ms.figureList.size();i++){
				dataobslist.add(ms.figureList.get(i));
			}
		});
		
		FileChooser filechooser = new FileChooser();
		filechooser.setInitialFileName("MyStage01.stage");
		
		save.setOnAction(event -> {
			File savefile = filechooser.showSaveDialog(this);
			if(savefile != null){
				ms.save(savefile);
			}
		});
		
		load.setOnAction(event -> {
			File loadfile = filechooser.showOpenDialog(this);
			if(loadfile != null){
				ms.load(loadfile);
				dataobslist.removeAll(dataobslist);
				for(int i=0;i<ms.figureList.size();i++){
					dataobslist.add(ms.figureList.get(i));
				}
				owner.drawAllFigure();
			}
		});
        
        datatable = new TableView<Figure>();
        datatable.setEditable(true);
        dataobslist = FXCollections.observableArrayList(
            ms.figureList
            );
        datatable.itemsProperty().setValue(dataobslist);
        TableColumn<Figure, String> typeColumn = new TableColumn<>("type");
        TableColumn<Figure, String> posColumn = new TableColumn<>("pos");
         TableColumn<Figure, Number> xColumn = new TableColumn<>("x");
         xColumn.setCellFactory(TextFieldTableCell.<Figure,Number>forTableColumn(new NumberStringConverter()));
         xColumn.setOnEditCommit(
                 new EventHandler<CellEditEvent<Figure, Number>>() {
                     @Override
                     public void handle(CellEditEvent<Figure, Number> t) {
                         ((Figure) t.getTableView().getItems().get(
                                 t.getTablePosition().getRow())
                                 ).setX(t.getNewValue());
                         owner.drawAllFigure();
                     }
                 }
             );
      
         TableColumn<Figure, Number> yColumn = new TableColumn<>("y");
         yColumn.setCellFactory(TextFieldTableCell.<Figure,Number>forTableColumn(new NumberStringConverter()));
         yColumn.setOnEditCommit(
                 new EventHandler<CellEditEvent<Figure, Number>>() {
                     @Override
                     public void handle(CellEditEvent<Figure, Number> t) {
                         ((Figure) t.getTableView().getItems().get(
                                 t.getTablePosition().getRow())
                                 ).setY(t.getNewValue());
                         owner.drawAllFigure();
                     }
                 }
             );
         posColumn.getColumns().addAll(xColumn,yColumn);
         TableColumn<Figure, Number> wColumn = new TableColumn<>("width");    
         wColumn.setCellFactory(TextFieldTableCell.<Figure,Number>forTableColumn(new NumberStringConverter()));
         wColumn.setOnEditCommit(
                 new EventHandler<CellEditEvent<Figure, Number>>() {
                     @Override
                     public void handle(CellEditEvent<Figure, Number> t) {
                         ((Figure) t.getTableView().getItems().get(
                                 t.getTablePosition().getRow())
                                 ).setWidth(t.getNewValue());
                     owner.drawAllFigure();
                     }
                 }
             );
         TableColumn<Figure, Number> hColumn = new TableColumn<>("height");
         hColumn.setCellFactory(TextFieldTableCell.<Figure,Number>forTableColumn(new NumberStringConverter()));
         hColumn.setOnEditCommit(
                 new EventHandler<CellEditEvent<Figure, Number>>() {
                     @Override
                     public void handle(CellEditEvent<Figure, Number> t) {
                         ((Figure) t.getTableView().getItems().get(
                                 t.getTablePosition().getRow())
                                 ).setHeight(t.getNewValue());
                         owner.drawAllFigure();
                     }
                 }
             );
         TableColumn<Figure, String> aColumn = new TableColumn<>("angle(unable");
         TableColumn<Figure, Number> a1Column = new TableColumn<>("1");
         a1Column.setCellFactory(TextFieldTableCell.<Figure,Number>forTableColumn(new NumberStringConverter()));
         a1Column.setOnEditCommit(
                 new EventHandler<CellEditEvent<Figure, Number>>() {
                     @Override
                     public void handle(CellEditEvent<Figure, Number> t) {
                    	 
                         ((Figure) t.getTableView().getItems().get(
                                 t.getTablePosition().getRow())
                                 ).setAngle1(t.getNewValue());
                         owner.drawAllFigure();
                     }
                 }
             );
         TableColumn<Figure, Number> a2Column = new TableColumn<>("2");
         a2Column.setCellFactory(TextFieldTableCell.<Figure,Number>forTableColumn(new NumberStringConverter()));
         a2Column.setOnEditCommit(
                 new EventHandler<CellEditEvent<Figure, Number>>() {
                     @Override
                     public void handle(CellEditEvent<Figure, Number> t) {
                    	 
                         ((Figure) t.getTableView().getItems().get(
                                 t.getTablePosition().getRow())
                                 ).setAngle2(t.getNewValue());
                         owner.drawAllFigure();
                     }
                 }
             );
         aColumn.getColumns().addAll(a1Column,a2Column);
         
         TableColumn<Figure, String> cColumn = new TableColumn<>("Color");

         TableColumn<Figure, Number> kColumn = new TableColumn<>("ColorNum");
          kColumn.setCellFactory(TextFieldTableCell.<Figure,Number>forTableColumn(new NumberStringConverter()));
       kColumn.setOnEditCommit(
       new EventHandler<CellEditEvent<Figure, Number>>() {
           @Override
           public void handle(CellEditEvent<Figure, Number> t) {
               ((Figure) t.getTableView().getItems().get(
                       t.getTablePosition().getRow())
                       ).setKind(t.getNewValue());
               owner.drawAllFigure();
           }
       }
    		   );
       
       TableColumn<Figure, String> bColumn = new TableColumn<>("Delete");
       bColumn.setCellFactory(
               new Callback<TableColumn<Figure, String>, TableCell<Figure, String>>() {

           @Override
           public TableCell<Figure, String> call(TableColumn<Figure, String> p) {
               return new ButtonCell(ms,owner);
           }
        
       });
       
         typeColumn.setCellValueFactory(new PropertyValueFactory<Figure, String>("type"));
         xColumn.setCellValueFactory(new PropertyValueFactory<Figure, Number>("x"));
         yColumn.setCellValueFactory(new PropertyValueFactory<Figure, Number>("y"));
         wColumn.setCellValueFactory(new PropertyValueFactory<Figure, Number>("width"));
         hColumn.setCellValueFactory(new PropertyValueFactory<Figure, Number>("height"));
         a1Column.setCellValueFactory(new PropertyValueFactory<Figure, Number>("angle"));
         a2Column.setCellValueFactory(new PropertyValueFactory<Figure, Number>("angle2"));
         cColumn.setCellValueFactory(new PropertyValueFactory<Figure, String>("colorType"));
         kColumn.setCellValueFactory(new PropertyValueFactory<Figure, Number>("kind"));
         datatable.getColumns().addAll(typeColumn,posColumn,wColumn,hColumn,aColumn,cColumn,kColumn,bColumn);

        dataroot.getChildren().addAll(menubar,datatable);
        
        setScene(new Scene(dataroot,0,0));
        show();
		
    }
	private class ButtonCell extends TableCell<Figure, String> {
        final Button cellButton = new Button("Delete");
         
        ButtonCell(MyStage ms,StageWindow owner){
             
            cellButton.setOnAction(new EventHandler<ActionEvent>(){
 
                @Override
                public void handle(ActionEvent t) {
                	final int ind = getTableRow().getIndex();
                	final ArrayList<Figure> figlist = ms.figureList;
                	figlist.remove(ind);
                	dataobslist.removeAll(dataobslist);
                	for(int i=0;i<figlist.size();i++){
                		dataobslist.add(figlist.get(i));
                	}
                	owner.drawAllFigure();
                }
            });
        }
 
        //Display button if the row is not empty
        @Override
        protected void updateItem(String t, boolean empty) {
            super.updateItem(t, empty);
            if(!empty){
                setGraphic(cellButton);
            }else{
            	setGraphic(null);
            }
        }
    }
}
