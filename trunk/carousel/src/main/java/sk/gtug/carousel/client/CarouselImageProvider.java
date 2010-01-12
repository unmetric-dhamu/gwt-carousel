package sk.gtug.carousel.client;

public interface CarouselImageProvider {

    int size();

    ImageHandle getImageUrl(int index);

}
