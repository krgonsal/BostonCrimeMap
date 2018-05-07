
package bostoncrimemap;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class AlertBox {
    public static void diplay(String title, String message){
        Stage alertWindow = new Stage();    
        alertWindow.initModality(Modality.APPLICATION_MODAL);
        alertWindow.setTitle(title);
        alertWindow.setMinWidth(250);
        alertWindow.setMinHeight(150);
        
        VBox vbAlert = new VBox(5);
        vbAlert.setPadding(new Insets(5,5,5,5));
        vbAlert.setAlignment(Pos.CENTER);
        
        Label alertMessage = new Label(message);
        alertMessage.setAlignment(Pos.CENTER);
        Button btnClose = new Button("Okay");
        btnClose.setAlignment(Pos.BOTTOM_CENTER);
        
        btnClose.setOnAction( e -> alertWindow.close());
        vbAlert.getChildren().addAll(alertMessage, btnClose);

        Scene scene = new Scene(vbAlert);
        alertWindow.setScene(scene);
        alertWindow.showAndWait();
    }
}
