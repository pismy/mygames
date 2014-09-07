package pismy.mygames.app;

import java.io.File;
import java.io.IOException;

import netscape.javascript.JSObject;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebErrorEvent;
import javafx.scene.web.WebEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage stage) throws Exception {
		StackPane root = new StackPane();

		WebView view = new WebView();
		final WebEngine engine = view.getEngine();
		engine.setOnError(new EventHandler<WebErrorEvent>() {
			@Override
			public void handle(WebErrorEvent event) {
				System.out.println("on error: " + event);
				event.consume();
			}
		});
		engine.setOnAlert(new EventHandler<WebEvent<String>>() {
			@Override
			public void handle(WebEvent<String> event) {
				System.out.println("on alert: " + event);
				event.consume();
			}
		});
		File index = new File("browse/index.html");
		engine.load(index.toURL().toExternalForm());
		root.getChildren().add(view);

		Scene scene = new Scene(root, 1280, 720);
		stage.setScene(scene);
		stage.show();
		
//		new Thread() {
//			public void run() {
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				JSObject win = (JSObject) engine.executeScript("window");
//				win.setMember("backend", new Object() {
//					public void test() {
//						System.out.println("test");
//					}
//					public void runGame(String rom, String emulator) {
//						System.out.println("runGame: "+rom+", "+emulator);
//					}
//					public String getPageData(String pageId) {
//						System.out.println("getPageData: "+pageId);
//						return "coucou";
//					}
//				});
//			};
//		}.start();
	}

	public static void main(String[] args) throws IOException {
		Application.launch(args);
	}
}