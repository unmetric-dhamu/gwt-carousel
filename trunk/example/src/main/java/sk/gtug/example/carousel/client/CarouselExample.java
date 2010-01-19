package sk.gtug.example.carousel.client;

import sk.gtug.carousel.client.Carousel;
import sk.gtug.carousel.client.CarouselImageProvider;
import sk.gtug.carousel.client.ImageHandle;
import sk.gtug.carousel.client.ImageLoader;
import sk.gtug.carousel.client.ImageLoader.CallBack;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.GwtCreateResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
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

		@Source("image4.jpg")
		DataResource image4();

		@Source("image5.jpg")
		DataResource image5();

		@Source("image6.jpg")
		DataResource image6();

		@Source("image7.jpg")
		DataResource image7();

		@Source("image8.jpg")
		DataResource image8();

		@Source("image9.jpg")
		DataResource image9();

		@NotStrict
		@Source("style.css")
		CssResource style();

	}

	interface Binder extends UiBinder<Carousel, CarouselExample> {
	}

	@UiField
	Carousel albums;

	public void onModuleLoad() {
		GWT.<Binder> create(Binder.class).createAndBindUi(this);
		RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
		rootLayoutPanel.add(albums);

		final DataResource img1 = Images.INSTANCE.image1();
		final DataResource img2 = Images.INSTANCE.image2();
		final DataResource img3 = Images.INSTANCE.image3();
		final DataResource img4 = Images.INSTANCE.image4();
		final DataResource img5 = Images.INSTANCE.image5();
		final DataResource img6 = Images.INSTANCE.image6();
		final DataResource img7 = Images.INSTANCE.image7();
		final DataResource img8 = Images.INSTANCE.image8();
		final DataResource img9 = Images.INSTANCE.image9();
		String baseUrl = GWT.getModuleBaseURL();

		String[] imageUrls = new String[] { baseUrl + "/image01.jpg",
				baseUrl + "/image02.jpg", baseUrl + "/image03.jpg", baseUrl + "/image04.jpg", baseUrl + "/image05.jpg",
				baseUrl + "/image06.jpg", baseUrl + "/image07.jpg", baseUrl + "/image08.jpg", baseUrl + "/image09.jpg" };
		ImageLoader.loadImages(imageUrls, new CallBack() {
			public void onImagesLoaded(final ImageHandle[] imageHandles) {
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
		rootLayoutPanel.forceLayout();
	}
}