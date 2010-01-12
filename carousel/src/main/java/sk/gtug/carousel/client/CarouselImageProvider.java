package sk.gtug.carousel.client;

public interface CarouselImageProvider {

    long size();

    String getImageUrl(int index);

}
