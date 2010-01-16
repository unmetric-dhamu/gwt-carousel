package sk.gtug.example.carousel.client;

import java.util.ArrayList;

import sk.gtug.carousel.client.Carousel;
import sk.gtug.carousel.client.CarouselImageProvider;
import sk.gtug.carousel.client.ImageHandle;
import sk.gtug.carousel.client.ImageLoader;
import sk.gtug.carousel.client.ImageLoader.CallBack;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.DataResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.Widget;

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
        final DataResource img4 = Images.INSTANCE.image4();
        final DataResource img5 = Images.INSTANCE.image5();
        final DataResource img6 = Images.INSTANCE.image6();
        final DataResource img7 = Images.INSTANCE.image7();
        final DataResource img8 = Images.INSTANCE.image8();
        final DataResource img9 = Images.INSTANCE.image9();

        String[] imageUrls = new String[] { img1.getUrl(), img2.getUrl(), img3.getUrl(), img4.getUrl(), img5.getUrl(), img6.getUrl(), img7.getUrl(),
                img8.getUrl(), img9.getUrl() };
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

        rootLayoutPanel.animate(10);
    }

    private int y = 10;

    public void onModuleLoad2() {
        Images.INSTANCE.style().ensureInjected();
        final RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
        final ArrayList<Widget> frames = new ArrayList<Widget>();
        for (int i = 0; i < 300; i++)
            frames.add(createFrame(rootLayoutPanel));
        placeWidget(rootLayoutPanel, frames, y);
        Button button = new Button("move");
        rootLayoutPanel.add(button);
        button.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                y+=10;
                placeWidget(rootLayoutPanel, frames, y);
            }
        });
        move(rootLayoutPanel, button, 15, 200, 200, 50);
    }

    private void placeWidget(RootLayoutPanel rootLayoutPanel, ArrayList<Widget> frames, int y) {
        for (int i = -150; i < 150; i++)
            move(rootLayoutPanel, frames.get(i + 150), i * 8, y, 100, 100);
    }

    private void move(RootLayoutPanel rootLayoutPanel, Widget panel1, int x, int y, int width, int height) {
        rootLayoutPanel.setWidgetLeftWidth(panel1, x, Unit.PX, width, Unit.PX);
        rootLayoutPanel.setWidgetTopHeight(panel1, y, Unit.PX, height, Unit.PX);
        panel1.setPixelSize(width, height);
    }

    private Widget createFrame(RootLayoutPanel rootLayoutPanel) {
        Image panel1 = new Image(Images.INSTANCE.image1().getUrl());
        panel1.setStyleName("border");
        rootLayoutPanel.add(panel1);
        return panel1;
    }
}