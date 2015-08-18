# GWT Carousel #

The only aim of this project is to provide a pure GWT implementation of carousel widget.
GWT Carousel runs on variety of web browsers: Mozilla FireFox, Chrome, Safari, Opera.

The widget fits well into [new predictable layout](http://code.google.com/p/google-web-toolkit/wiki/LayoutDesign) introduced in GWT 2.0. I highly recommend reading relevant [documentation](http://code.google.com/webtoolkit/doc/latest/DevGuideUiPanels.html#Design). Image animation is based on [animation](http://code.google.com/webtoolkit/doc/latest/DevGuideUiPanels.html#Animation) of GWT 2.0.

### Example ###
Have a look at the [gallery example](http://gwt-carousel.googlecode.com/svn/examples/gallery/index.html) to see it in action.

## Usage ##
Include Carousel module in your project gwt.xml file:
```
<inherits name='sk.gtug.carousel.Carousel' />
```

and then use it as any other Widget in your application:
```
import sk.gtug.carousel.client.Carousel;
import sk.gtug.carousel.client.CarouselImageProvider;
import sk.gtug.carousel.client.ImageHandle;
import sk.gtug.carousel.client.ImageLoader;
import sk.gtug.carousel.client.ImageLoader.CallBack;
...
{
  Carousel carousel = new Carousel();
  carousel.setImageProvider(...);
  RootLayoutPanel rootLayoutPanel = RootLayoutPanel.get();
  rootLayoutPanel.add(carousel);
  ...
  rootLayoutPanel.forceLayout();
}
```

Don't forget to define DOCTYPE in html file to make new layouting work properly:
```
<!doctype html>
<html>
...
</html>
```

## Image Provider ##
In order to supply a collection of images `CarouselImageProvider` implementation has to be given to Carousel. It provides `ImageHandle` instances of images containing information regarding url, width and height of the image. `ImageLoader` is a convenient class to achive desired result.
`ImageHandler` and `ImageLoader` are classes from [SpeedTracer](http://code.google.com/p/speedtracer/) project. SpeedTracer (released together with GWT 2.0) also provides a great source of information and practices for developers using GWT.

## Roadmap ##
Current project is in early stage and very basic functionality is provided. Following features are planned:

### TODOs ###
**version 0.1**
  * ~~Add event handling to get information about "front" image~~
  * Click on image moves the image to the front (partially done)
  * Faster and smooth image rotation if needed
  * ~~Add support for Internet Explorer version 7 and higher~~


**version 0.2**
  * Support image reflection for browsers supporting 'Canvas'
  * Provide interface to connect to Flickr, Picassa and other similar services
  * Provide a set of transition paths to choose from (circle, ellipse, ...)
  * Use opacity for images in background as option
  * Set number of visible images
  * Support for mobile phones Android and iPhone (WebKit's animation and transformation CSS support)