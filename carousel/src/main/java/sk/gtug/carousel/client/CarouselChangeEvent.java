package sk.gtug.carousel.client;

import com.google.gwt.event.shared.GwtEvent;

public class CarouselChangeEvent extends GwtEvent<CarouselChangeHandler> {

    private static Type<CarouselChangeHandler> TYPE;

    public static Type<CarouselChangeHandler> getType() {
        return TYPE != null ? TYPE : (TYPE = new Type<CarouselChangeHandler>());
    }

    private final int selectedImageIndex;
    private final ImageHandle imageHandle;

    public CarouselChangeEvent(int selectedImageIndex, ImageHandle imageHandle) {
        this.selectedImageIndex = selectedImageIndex;
        this.imageHandle = imageHandle;
    }

    @Override
    public final Type<CarouselChangeHandler> getAssociatedType() {
        return getType();
    }

    @Override
    protected void dispatch(CarouselChangeHandler commandHandler) {
        commandHandler.onChange(this);
    }

    public int getSelectedImageIndex() {
        return selectedImageIndex;
    }

    public ImageHandle getImageHandle() {
        return imageHandle;
    }
}