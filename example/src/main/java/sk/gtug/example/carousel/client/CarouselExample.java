package sk.gtug.example.carousel.client;

import sk.gtug.carousel.client.Carousel;
import sk.gtug.carousel.client.CarouselImageProvider;
import sk.gtug.carousel.client.ImageHandle;
import sk.gtug.carousel.client.ImageLoader;
import sk.gtug.carousel.client.ImageLoader.CallBack;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.Window;
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
		final ImageResource img1 = Images.INSTANCE.image1();
		final ImageResource img2 = Images.INSTANCE.image2();
		final ImageResource img3 = Images.INSTANCE.image3();

		final Carousel imagePanel = new Carousel();
		String[] imageUrls = new String[] { img1.getURL(), img2.getURL(),
				img3.getURL() };
		ImageLoader.loadImages(imageUrls, new CallBack() {
			@Override
			public void onImagesLoaded(final ImageHandle[] imageHandles) {
				Window.alert("loaded images:" + imageHandles.length);
				CarouselImageProvider carouselImageProvider = new CarouselImageProvider() {
					@Override
					public long size() {
						return 7;
					}

					@Override
					public ImageHandle getImageUrl(long index) {
						if (index < 0 || index >= size())
							return null;
						if (index % 3 == 0)
							return imageHandles[0];
						if (index % 3 == 1)
							return imageHandles[1];
						return imageHandles[2];
					}
				};
				imagePanel.setImageProvider(carouselImageProvider);
			}
		});
		rootLayoutPanel.add(imagePanel);
		rootLayoutPanel.setWidgetTopBottom(imagePanel, 0, Unit.PX, 0, Unit.PX);
		rootLayoutPanel.setWidgetLeftRight(imagePanel, 0, Unit.PX, 0, Unit.PX);
		rootLayoutPanel.forceLayout();
	}
}