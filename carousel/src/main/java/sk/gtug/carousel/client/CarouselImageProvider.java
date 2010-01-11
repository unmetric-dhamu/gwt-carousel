package sk.gtug.carousel.client;

public interface CarouselImageProvider {

	long size();

	ImageHandle getImageUrl(long index);

}
