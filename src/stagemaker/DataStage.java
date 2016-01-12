package stagemaker;

import javafx.stage.*;
import javafx.util.converter.NumberStringConverter;
import mainSystem.ColorType;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.collections.*;
import javafx.event.EventHandler;


public class DataStage extends Stage{
	
	private Stage parentStage;
	public ObservableList<Figure> dataobslist;
	
	@SuppressWarnings("unchecked")
    public DataStage(StageWindow owner,MyStage ms){
		parentStage = owner;
        setTitle("Figures");
        setWidth(500);
        setHeight(500);
        setX(800);
        setY(getY()+400);
        Group dataroot = new Group();
        TableView<Figure> datatable = new TableView<Figure>();
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
         TableColumn<Figure, String> aColumn = new TableColumn<>("angle");
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
       
         typeColumn.setCellValueFactory(new PropertyValueFactory<Figure, String>("type"));
         xColumn.setCellValueFactory(new PropertyValueFactory<Figure, Number>("x"));
         yColumn.setCellValueFactory(new PropertyValueFactory<Figure, Number>("y"));
         wColumn.setCellValueFactory(new PropertyValueFactory<Figure, Number>("width"));
         hColumn.setCellValueFactory(new PropertyValueFactory<Figure, Number>("height"));
         a1Column.setCellValueFactory(new PropertyValueFactory<Figure, Number>("angle"));
         a2Column.setCellValueFactory(new PropertyValueFactory<Figure, Number>("angle2"));
         cColumn.setCellValueFactory(new PropertyValueFactory<Figure, String>("colorType"));
         a2Column.setCellValueFactory(new PropertyValueFactory<Figure, Number>("kind"));
         datatable.getColumns().addAll(typeColumn,posColumn,wColumn,hColumn,aColumn,cColumn,kColumn);

        dataroot.getChildren().addAll(datatable);
        
        setScene(new Scene(dataroot,0,0));
        show();
		
    }
}
