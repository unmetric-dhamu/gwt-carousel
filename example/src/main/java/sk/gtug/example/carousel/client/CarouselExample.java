package sk.gtug.example.carousel.client;

import sk.gtug.carousel.client.Carousel;
import sk.gtug.carousel.client.CarouselImageProvider;
import sk.gtug.carousel.client.ImageHandle;
import sk.gtug.carousel.client.ImageLoader;
import sk.gtug.carousel.client.ImageLoader.CallBack;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.DockLayoutPanel;
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

    interface Binder extends UiBinder<DockLayoutPanel, CarouselExample> {
    }

    private static final Binder binder = GWT.create(Binder.class);

    @UiField
    Carousel albums;

    public void onModuleLoad() {
        RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
        DockLayoutPanel root = binder.createAndBindUi(this);
        rootLayoutPanel.add(root);

        final ImageResource img1 = Images.INSTANCE.image1();
        final ImageResource img2 = Images.INSTANCE.image2();
        final ImageResource img3 = Images.INSTANCE.image3();

        String[] imageUrls = new String[] { img1.getURL(), img2.getURL(), img3.getURL(), img1.getURL(), img2.getURL(), img3.getURL(), img1.getURL(),
                img2.getURL(), img3.getURL() };
        ImageLoader.loadImages(imageUrls, new CallBack() {
            public void onImagesLoaded(final ImageHandle[] imageHandles) {
                Window.alert("loaded images:" + imageHandles.length);
                CarouselImageProvider carouselImageProvider = new CarouselImageProvider() {

                    public long size() {
                        return 7;
                    }

                    public String getImageUrl(int index) {
                        if (index < 0 || index >= size())
                            return null;
                        return imageHandles[index].getUrl();
                    }
                };
                albums.setImageProvider(carouselImageProvider);
            }
        });
        
        rootLayoutPanel.animate(10);
    }
}