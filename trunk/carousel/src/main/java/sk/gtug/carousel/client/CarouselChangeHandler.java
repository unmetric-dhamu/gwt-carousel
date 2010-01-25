package sk.gtug.carousel.client;

import com.google.gwt.event.shared.EventHandler;

public interface CarouselChangeHandler extends EventHandler {

    void onChange(CarouselChangeEvent carouselChangeEvent);

}
