import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ColorPicker;
import javafx.scene.layout.BorderPane;
import java.util.ArrayList;
import java.lang.Math;

public class Controller {
    
    int pinX;//position du curseur de la souris en x au début d'un drag and drop
    int pinY;//position du curseur de la souris en y au début d'un drag and drop
    
    boolean newShape;//true : après le départ d'un drag an drop pendant toute sa durée; false : sinon
    
    ArrayList<Rectangle> listeRectangle;//liste des rectangles créés
    ArrayList<Ellipse> listeEllipse;//liste des ellipses créées
    ArrayList<Line> listeLine;//liste des lignes créées
    
    //au départ je voulais juste utiliser canvas.getChildren() à la place de ces listes, mais le cast de ces enfants en Line, Ellipse ou Rectangle s'est avéré trop compliqué
    
    //je voulais également pour mon feedback mettre le contour des formes géométriques en le négatif de sa couleur. Mais je n'ai pas pu car un Paint ne permet pas d'accèder aux composantes RVB d'une figure
    
    @FXML
    private BorderPane border;
	@FXML
	private AnchorPane canvas;
	@FXML
	private RadioButton select;
	@FXML
	private RadioButton ellipse;
	@FXML
	private RadioButton rectangle;
	@FXML
	private RadioButton line;
	@FXML
	private ColorPicker colorPicker;
	@FXML
	private Button delete;
	@FXML
	private Button clone;
	
    public Controller() {
    	super();
    	newShape = false;
    	listeRectangle = new ArrayList<Rectangle>();
    	listeEllipse = new ArrayList<Ellipse>();
    	listeLine = new ArrayList<Line>();
    }
    
    @FXML
    public void initialize() {
    	border.setOnMouseClicked(majorEvent -> {//Le controlleur agit sur le reste du programme depuis ici
    		canvas.setOnMouseDragged(event -> {//gestion du drag and drop...
        		if(rectangle.isSelected()) {//...si en mode rectangle
        			if(!newShape) {//création d'une nouvelle figure
        				newShape = true;
        				pinX = (int)event.getSceneX()-205;
            	    	pinY = (int)event.getSceneY();
            			Rectangle rc = new Rectangle();
            			rc.setFill(colorPicker.getValue());
            	    	rc.setStroke(new Color(0,0,0,1));
            	    	rc.setX(pinX);
            	    	rc.setY(pinY);
            	    	canvas.getChildren().add(rc);
            	    	listeRectangle.add(rc);
        			}else {//resize de la figure (la resize du rectangle ne peut se faire que vesr les coordonnées positives)
        				if(event.getSceneX()-205<458 && event.getSceneX()>205) {//pour ne pas dépasser du canevas
        					listeRectangle.get(listeRectangle.size()-1).setWidth((int)event.getSceneX()-205-pinX);
        				}
        				if(event.getSceneY()<578 && event.getSceneY()>0) {
        					listeRectangle.get(listeRectangle.size()-1).setHeight((int)event.getSceneY()-pinY);
        				}
        			}
        			canvas.setOnMouseReleased(eventEnd -> {
        	    		newShape = false;
        	    	});
        		}
        		else if(ellipse.isSelected()) {//même procédé si en mode ellipse
        			if(!newShape) {
        				newShape = true;
        				pinX = (int)event.getSceneX()-205;
            	    	pinY = (int)event.getSceneY();
            			Ellipse el = new Ellipse();
            			el.setFill(colorPicker.getValue());
            	    	el.setStroke(new Color(0,0,0,1));
            	    	el.setCenterX(pinX);
            	    	el.setCenterY(pinY);
            	    	canvas.getChildren().add(el);
            	    	listeEllipse.add(el);
        			}else {
        				if(event.getSceneX()-205<458 && event.getSceneX()>205 && pinX+Math.abs(pinX-event.getSceneX()+205)<458 && pinX>Math.abs(pinX-event.getSceneX()+205)) {
        					listeEllipse.get(listeEllipse.size()-1).setRadiusX(Math.abs((int)event.getSceneX()-205-pinX));
        				}
        				if(event.getSceneY()<578 && event.getSceneY()>0 && pinY+Math.abs(pinY-event.getSceneY())<578 && pinY>Math.abs(pinY-event.getSceneY())) {
        					listeEllipse.get(listeEllipse.size()-1).setRadiusY(Math.abs((int)event.getSceneY()-pinY));
        				}
        			}
        			canvas.setOnMouseReleased(eventEnd -> {
        	    		newShape = false;
        	    	});
        		}
        		else if(line.isSelected()) {//pareillmeent si en mode line
        			if(!newShape) {
        				newShape = true;
        				pinX = (int)event.getSceneX()-205;
            	    	pinY = (int)event.getSceneY();
            			Line ln = new Line();
            			ln.setStroke(colorPicker.getValue());
            	    	ln.setStrokeWidth(3);
            	    	ln.setStartX(pinX);
            	    	ln.setStartY(pinY);
            	    	ln.setEndX(pinX);
            	    	ln.setEndY(pinY);
            	    	canvas.getChildren().add(ln);
            	    	listeLine.add(ln);
        			}else {
        				if(event.getSceneX()-205<458 && event.getSceneX()>205) {
        					listeLine.get(listeLine.size()-1).setEndX(Math.abs((int)event.getSceneX()-205));
        				}
        				if(event.getSceneY()<578 && event.getSceneY()>0) {
        					listeLine.get(listeLine.size()-1).setEndY(Math.abs((int)event.getSceneY()));
        				}
        			}
        			canvas.setOnMouseReleased(eventEnd -> {
        	    		newShape = false;
        	    	});
        		}
            });
    		/*On va parcourir ici les trois listes de figures pour voir gérer la sélection et le drag and drop des figures créées
    		 * le procédé est le même pour chaque type de figure, seuls les méthodes utilisées et le feedback spécifique à chaque figure est différent
    		 */
    		for(int i = 0; i<listeRectangle.size(); i++) {
        		Rectangle shape = listeRectangle.get(i);
        		shape.setOnMouseDragged(event -> {//déplacement par drag and drop
            		if(shape.getStrokeWidth()==3) {//Un objet sélectionné est le seul à avoir un contour plus gros que la normale, c'est à cela qu'on le reconnait
            			canvas.setLeftAnchor(shape, event.getSceneX()-205);
            			canvas.setTopAnchor(shape, event.getSceneY());
            			if(event.getSceneX()-205<0) {//gestion du dépassement des frontières du canevas
            				canvas.setLeftAnchor(shape, 0d);
            			}else if(event.getSceneX()-205>458-shape.getWidth()) {
            				canvas.setLeftAnchor(shape, 458-shape.getWidth());
            			}
            			if(event.getSceneY()<0) {
            				canvas.setTopAnchor(shape, 0d);
            			}
            			else if(event.getSceneY()>578-shape.getHeight()) {
            				canvas.setTopAnchor(shape, 578-shape.getHeight());
            			}
            		}
            	});
            	shape.setOnMouseClicked(event -> {//sélection de la figure
            		if(select.isSelected()) {//uniquement en mode sélection
            			//On réinitialise la taille du contour de chaque figure (le feedback)
            			for(int j = 0; j<listeRectangle.size(); j++) {
            				listeRectangle.get(j).setStroke(new Color(0,0,0,1));
            				listeRectangle.get(j).setStrokeWidth(1);
            			}
            			for(int j = 0; j<listeEllipse.size(); j++) {
            				listeEllipse.get(j).setStroke(new Color(0,0,0,1));
            				listeEllipse.get(j).setStrokeWidth(1);
            			}
            			for(int j = 0; j<listeLine.size(); j++) {
            				listeLine.get(j).setStrokeWidth(3);
            			}
            			shape.setStroke(new Color(0,0,0.5,1));//on change la taille et la couleur du contour de la figure sélectionnée
            			shape.setStrokeWidth(3);
            		}
            	});
        	}
    		for(int i = 0; i<listeEllipse.size(); i++) {
        		Ellipse shape = listeEllipse.get(i);
        		shape.setOnMouseDragged(event -> {
            		if(shape.getStrokeWidth()==3) {
            			canvas.setLeftAnchor(shape, event.getSceneX()-205);
            			canvas.setTopAnchor(shape, event.getSceneY());
            			if(event.getSceneX()-205<0) {
            				canvas.setLeftAnchor(shape, 0d);
            			}else if(event.getSceneX()-205>458-2*shape.getRadiusX()) {
            				canvas.setLeftAnchor(shape, 458-2*shape.getRadiusX());
            			}
            			if(event.getSceneY()<0) {
            				canvas.setTopAnchor(shape, 0d);
            			}
            			else if(event.getSceneY()>578-2*shape.getRadiusY()) {
            				canvas.setTopAnchor(shape, 578-2*shape.getRadiusY());
            			}
            		}
            	});
            	shape.setOnMouseClicked(event -> {
            		if(select.isSelected()) {
            			for(int j = 0; j<listeRectangle.size(); j++) {
            				listeRectangle.get(j).setStroke(new Color(0,0,0,1));
            				listeRectangle.get(j).setStrokeWidth(1);
            			}
            			for(int j = 0; j<listeEllipse.size(); j++) {
            				listeEllipse.get(j).setStroke(new Color(0,0,0,1));
            				listeEllipse.get(j).setStrokeWidth(1);
            			}
            			for(int j = 0; j<listeLine.size(); j++) {
            				listeLine.get(j).setStrokeWidth(3);
            			}
            			shape.setStroke(new Color(0,0,0.5,1));
            			shape.setStrokeWidth(3);
            		}
            	});
        	}
    		for(int i = 0; i<listeLine.size(); i++) {
        		Line shape = listeLine.get(i);
        		shape.setOnMouseDragged(event -> {
            		if(shape.getStrokeWidth()==5) {
            			canvas.setLeftAnchor(shape, event.getSceneX()-205);
            			canvas.setTopAnchor(shape, event.getSceneY());
            			if(event.getSceneX()-205<0) {
            				canvas.setLeftAnchor(shape, 0d);
            			}else if(event.getSceneX()-205>458-Math.abs(shape.getStartX()-shape.getEndX())-6) {
            				canvas.setLeftAnchor(shape, 458-Math.abs(shape.getStartX()-shape.getEndX())-6);
            			}
            			if(event.getSceneY()<0) {
            				canvas.setTopAnchor(shape, 0d);
            			}
            			else if(event.getSceneY()>578-Math.abs(shape.getStartY()-shape.getEndY())-6) {
            				canvas.setTopAnchor(shape, 578-Math.abs(shape.getStartY()-shape.getEndY())-6);
            			}
            		}
            	});
            	shape.setOnMouseClicked(event -> {
            		if(select.isSelected()) {
            			for(int j = 0; j<listeRectangle.size(); j++) {
            				listeRectangle.get(j).setStroke(new Color(0,0,0,1));
            				listeRectangle.get(j).setStrokeWidth(1);
            			}
            			for(int j = 0; j<listeEllipse.size(); j++) {
            				listeEllipse.get(j).setStroke(new Color(0,0,0,1));
            				listeEllipse.get(j).setStrokeWidth(1);
            			}
            			for(int j = 0; j<listeLine.size(); j++) {
            				listeLine.get(j).setStrokeWidth(3);
            			}
            			shape.setStrokeWidth(5);
            		}
            	});
        	}
    		//lorsqu'on sort du mode sélection, on enlève la sélection des figures, ça évite un peu de code et pas mal de bugs
    		ellipse.setOnAction(event->{
    			for(int j = 0; j<listeRectangle.size(); j++) {
    				listeRectangle.get(j).setStroke(new Color(0,0,0,1));
    				listeRectangle.get(j).setStrokeWidth(1);
    			}
    			for(int j = 0; j<listeEllipse.size(); j++) {
    				listeEllipse.get(j).setStroke(new Color(0,0,0,1));
    				listeEllipse.get(j).setStrokeWidth(1);
    			}
    			for(int j = 0; j<listeLine.size(); j++) {
    				listeLine.get(j).setStrokeWidth(3);
    			}
    		});
    		rectangle.setOnAction(event->{
    			for(int j = 0; j<listeRectangle.size(); j++) {
    				listeRectangle.get(j).setStroke(new Color(0,0,0,1));
    				listeRectangle.get(j).setStrokeWidth(1);
    			}
    			for(int j = 0; j<listeEllipse.size(); j++) {
    				listeEllipse.get(j).setStroke(new Color(0,0,0,1));
    				listeEllipse.get(j).setStrokeWidth(1);
    			}
    			for(int j = 0; j<listeLine.size(); j++) {
    				listeLine.get(j).setStrokeWidth(3);
    			}
    		});
    		line.setOnAction(event->{
    			for(int j = 0; j<listeRectangle.size(); j++) {
    				listeRectangle.get(j).setStroke(new Color(0,0,0,1));
    				listeRectangle.get(j).setStrokeWidth(1);
    			}
    			for(int j = 0; j<listeEllipse.size(); j++) {
    				listeEllipse.get(j).setStroke(new Color(0,0,0,1));
    				listeEllipse.get(j).setStrokeWidth(1);
    			}
    			for(int j = 0; j<listeLine.size(); j++) {
    				listeLine.get(j).setStrokeWidth(3);
    			}
    		});
    		colorPicker.setOnAction(eventColor ->{//On change la couleur de l'objet sélectionné (donc en mode sélection)
    			for(int i=0; i<listeRectangle.size(); i++) {
    				Rectangle shape = listeRectangle.get(i);
    				if(shape.getStrokeWidth()==3) {
            			shape.setFill(colorPicker.getValue());
            		}
    			}
    			for(int i=0; i<listeEllipse.size(); i++) {
    				Ellipse shape = listeEllipse.get(i);
    				if(shape.getStrokeWidth()==3) {
            			shape.setFill(colorPicker.getValue());
            		}
    			}
    			for(int i=0; i<listeLine.size(); i++) {
    				Line shape = listeLine.get(i);
    				if(shape.getStrokeWidth()==5) {
            			shape.setStroke(colorPicker.getValue());
            		}
    			}
            });
    		delete.setOnAction(eventDelete ->{//suppression de l'objet sélectionné
    				for(int i=0; i<listeRectangle.size(); i++) {
        				Rectangle shape = listeRectangle.get(i);
        				if(shape.getStrokeWidth()==3) {
        					listeRectangle.remove(shape);
        					canvas.getChildren().remove(shape);
        				}
        			}
        			for(int i=0; i<listeEllipse.size(); i++) {
        				Ellipse shape = listeEllipse.get(i);
        				if(shape.getStrokeWidth()==3) {
        					listeEllipse.remove(shape);
        					canvas.getChildren().remove(shape);
        				}
        			}
        			for(int i=0; i<listeLine.size(); i++) {
        				Line shape = listeLine.get(i);
        				if(shape.getStrokeWidth()==5) {
        					listeLine.remove(shape);
        					canvas.getChildren().remove(shape);
        				}
        			}
            });
    		clone.setOnAction(eventClone ->{//copie la figure sélectionnée
    			for(int i=0; i<listeRectangle.size(); i++) {
        			Rectangle shape = listeRectangle.get(i);
        			if(shape.getStrokeWidth()==3) {
        				Rectangle rc = new Rectangle();
        				if(shape.getX()+shape.getWidth()+10<458 && shape.getY()+shape.getHeight()+10<579) {//On vérifie les limites pour l'affichage du clone
        					rc.setX(shape.getX()+10);
       						rc.setY(shape.getY()+10);
        				}
        				else {//Je n'ai pas vérifié si on pouvais aussi cloner l'objet en haut à gauche, mais je part du principe que ce n'est pas grave, si je ne clone pas je n'ai aucun feedback et ce serait dommage
        					rc.setX(shape.getX()-10);
        					rc.setY(shape.getY()-10);
        				}
       					rc.setWidth(shape.getWidth());
       					rc.setHeight(shape.getHeight());
                       	rc.setFill(shape.getFill());
                       	rc.setStroke(new Color(0,0,0,1));
                       	rc.setStrokeWidth(1);
                       	shape.setStroke(new Color(0,0,0,1));
                       	shape.setStrokeWidth(1);
                       	listeRectangle.add(rc);
                       	canvas.getChildren().add(rc);
        			}
        		}
        		for(int i=0; i<listeEllipse.size(); i++) {
       				Ellipse shape = listeEllipse.get(i);
       				if(shape.getStrokeWidth()==3) {
       					Ellipse el = new Ellipse();
       					if(shape.getCenterX()+shape.getRadiusX()+10<458 && shape.getCenterY()+shape.getRadiusY()+10<579) {
       						el.setCenterX(shape.getCenterX()+10);
       						el.setCenterY(shape.getCenterY()+10);
       					}
       					else {
        					el.setCenterX(shape.getCenterX()-10);
        					el.setCenterY(shape.getCenterY()-10);
        				}
        				el.setRadiusX(shape.getRadiusX());
        				el.setRadiusY(shape.getRadiusY());
                       	el.setFill(shape.getFill());
                       	el.setStroke(new Color(0,0,0,1));
                        el.setStrokeWidth(1);
                       	shape.setStroke(new Color(0,0,0,1));
                       	shape.setStrokeWidth(1);
                       	listeEllipse.add(el);
                       	canvas.getChildren().add(el);
        			}
        		}
        		for(int i=0; i<listeLine.size(); i++) {
       				Line shape = listeLine.get(i);
       				if(shape.getStrokeWidth()==5) {
       					Line ln = new Line();
        				if(shape.getStartX()+shape.getEndX()+10<458 && shape.getStartY()+shape.getEndY()+10<579) {
        					ln.setStartX(shape.getStartX()+10);
        					ln.setStartY(shape.getStartY()+10);
        					ln.setEndX(shape.getEndX()+10);
           					ln.setEndY(shape.getEndY()+10);
        				}
       					else {
       						ln.setStartX(shape.getStartX()-10);
       						ln.setStartY(shape.getStartY()-10);
       						ln.setEndX(shape.getEndX()-10);
           					ln.setEndY(shape.getEndY()-10);
        				}
        				ln.setStrokeWidth(3);
                       	shape.setStrokeWidth(3);
                       	ln.setStroke(shape.getStroke());
                       	listeLine.add(ln);
                       	canvas.getChildren().add(ln);
        			}
        		}
            });
    	});
    }
}






