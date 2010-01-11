package sk.gtug.example.carousel.client;

import sk.gtug.carousel.client.CarouselPanel;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class CarouselExample implements EntryPoint {

	public interface Images extends ClientBundle {
		Images INSTANCE = GWT.create(Images.class);

		@Source("image1.jpg")
		ImageResource image1();

		@Source("image2.jpg")
		ImageResource image2();

		@Source("image3.jpg")
		ImageResource image3();
	}

	public void onModuleLoad() {
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		ImageResource img1 = Images.INSTANCE.image1();
		ImageResource img2 = Images.INSTANCE.image2();
		ImageResource img3 = Images.INSTANCE.image3();
		CarouselPanel imagePanel = new CarouselPanel(img1, img2, img3);
		rootLayoutPanel.add(imagePanel);
		rootLayoutPanel.setWidgetTopBottom(imagePanel, 0, Unit.PX, 0, Unit.PX);
		rootLayoutPanel.setWidgetLeftRight(imagePanel, 0, Unit.PX, 0, Unit.PX);
		rootLayoutPanel.forceLayout();
	}
}