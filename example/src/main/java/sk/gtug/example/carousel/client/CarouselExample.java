package sk.gtug.example.carousel.client;

import sk.gtug.carousel.client.Carousel;
import sk.gtug.carousel.client.CarouselImageProvider;
import sk.gtug.carousel.client.ImageHandle;
import sk.gtug.carousel.client.ImageLoader;
import sk.gtug.carousel.client.ImageLoader.CallBack;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;

public class CarouselExample implements EntryPoint {

	public interface Images extends ClientBundle {
		Images INSTANCE = GWT.create(Images.class);

		@Source("image1.jpg")
		DataResource image1();

		@Source("image2.jpg")
		DataResource image2();

		@Source("image3.jpg")
		DataResource image3();
	}

	interface Binder extends UiBinder<DockLayoutPanel, CarouselExample> {
	}

	private static final Binder binder = GWT.create(Binder.class);

	@UiField
	Carousel albums;

	public void onModuleLoad() {
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		DockLayoutPanel root = binder.createAndBindUi(this);
		rootLayoutPanel.add(root);

		final DataResource img1 = Images.INSTANCE.image1();
		final DataResource img2 = Images.INSTANCE.image2();
		final DataResource img3 = Images.INSTANCE.image3();

		String[] imageUrls = new String[] { img1.getUrl(), img2.getUrl(),
				img3.getUrl(), img3.getUrl(), img2.getUrl(), img2.getUrl(),
				img2.getUrl(), img1.getUrl(), img3.getUrl() };
		ImageLoader.loadImages(imageUrls, new CallBack() {
			public void onImagesLoaded(final ImageHandle[] imageHandles) {
				Window.alert("loaded images:" + imageHandles.length);
				CarouselImageProvider carouselImageProvider = new CarouselImageProvider() {

					public int size() {
						return 9;
					}

					public ImageHandle getImageUrl(int index) {
						return imageHandles[index];
					}
				};
				albums.setImageProvider(carouselImageProvider);
			}
		});

		rootLayoutPanel.animate(10);
	}
}