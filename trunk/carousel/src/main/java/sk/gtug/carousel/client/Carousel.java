package sk.gtug.carousel.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.ui.LayoutPanel;

public class Carousel extends LayoutPanel implements ClickHandler, MouseWheelHandler {

    private List<CarouselImagePanel> panels = new ArrayList<CarouselImagePanel>();
    private CarouselImageProvider imageProvider;
    private int actualImageIndex;

    public Carousel() {
        for (int i = -3; i <= 3; i++)
            panels.add(new CarouselImagePanel(this, i));
        addDomHandler(this, ClickEvent.getType());
        addDomHandler(this, MouseWheelEvent.getType());
    }

    private void rotate(int delta) {
        if (imageProvider == null)
            return;
        if (isFirstOrLastImage(delta))
            return;
        actualImageIndex += delta;
        update(delta);
        fireEvent(new CarouselChangeEvent(actualImageIndex, imageProvider.getImageUrl(actualImageIndex)));
    }

    private boolean isFirstOrLastImage(int delta) {
        return (delta < 0 && actualImageIndex == 0) || (delta > 0 && actualImageIndex == this.imageProvider.size() - 1);
    }

    private void update(int delta) {
        for (CarouselImagePanel panel : panels) {
            panel.updatePhase(delta);
            if (panel.getLastPhase() == -3 && delta > 0)
                panel.setImageHandle(getImageUrl(actualImageIndex + 3));
            if (panel.getLastPhase() == 3 && delta < 0)
                panel.setImageHandle(getImageUrl(actualImageIndex - 3));
        }
        int duration = 350;
        animate(duration);
    }

    public void setImageProvider(CarouselImageProvider imageProvider) {
        this.imageProvider = imageProvider;
        this.actualImageIndex = 0;
        for (CarouselImagePanel panel : panels) {
            panel.setImageHandle(getImageUrl(actualImageIndex + panel.getPhase()));
        }
        fireEvent(new CarouselChangeEvent(actualImageIndex, imageProvider.getImageUrl(actualImageIndex)));
    }

    private ImageHandle getImageUrl(int actualImageIndex) {
        if (actualImageIndex < 0 || actualImageIndex >= imageProvider.size())
            return null;
        return imageProvider.getImageUrl(actualImageIndex);
    };

    @Override
    public void onResize() {
        super.onResize();
        int boardWidth = getElement().getClientWidth();
        int boardHeight = getElement().getClientHeight();
        for (CarouselImagePanel imagePanel : panels)
            imagePanel.setBoardSize(boardWidth, boardHeight);
        update(0);
    }

    public void onClick(ClickEvent event) {
        rotate(1);
    }

    public void onMouseWheel(MouseWheelEvent event) {
        rotate(event.getDeltaY() < 0 ? -1 : 1);
    }

    public void addChangeHandler(CarouselChangeHandler handler) {
        addHandler(handler, CarouselChangeEvent.getType());
    }

}
