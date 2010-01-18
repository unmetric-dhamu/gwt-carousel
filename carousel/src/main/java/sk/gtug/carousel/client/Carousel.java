package sk.gtug.carousel.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.layout.client.Layout.Layer;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.Widget;

public class Carousel extends LayoutPanel {

	private List<CarouselImagePanel> panels = new ArrayList<CarouselImagePanel>();
	private boolean active = false;
	private CarouselImageProvider imageProvider;
	private int actualImageIndex;

	public Carousel() {
		panels.add(new CarouselImagePanel(this, 0));
		panels.add(new CarouselImagePanel(this, -1));
		panels.add(new CarouselImagePanel(this, -2));
		panels.add(new CarouselImagePanel(this, -3));
		panels.add(new CarouselImagePanel(this, 1));
		panels.add(new CarouselImagePanel(this, 2));
		panels.add(new CarouselImagePanel(this, 3));

		addDomHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				moveRectangle(1);
			}
		}, ClickEvent.getType());
		addDomHandler(new MouseWheelHandler() {
			public void onMouseWheel(MouseWheelEvent event) {
				int deltaY = event.getDeltaY();
				moveRectangle(deltaY);
			}
		}, MouseWheelEvent.getType());
		for (CarouselImagePanel panel : panels)
			panel.updatePanel(panel.getImageRectForCurrentPhase());
	}

	private void moveRectangle(int delta) {
		if (imageProvider == null)
			return;
		if (active) {
			return;
		}
		if (delta < 0 && actualImageIndex == 0)
			return;
		if (delta > 0 && actualImageIndex == this.imageProvider.size() - 1)
			return;
		if (delta < 0)
			this.actualImageIndex--;
		else
			this.actualImageIndex++;
		for (CarouselImagePanel panel : panels) {
			panel.updatePhase(delta > 0 ? 1 : -1);
			panel.updatePanel(panel.getImageRectForCurrentPhase());
			panel.setZIndex(-Math.abs(panel.getPhase()));
		}
		animateImages(delta);
	}

	private void animateImages(int delta) {
		for (CarouselImagePanel panel : panels) {
			if (panel.getLastPhase() == -3 && delta > 0) {
				panel.setImageHandle(getImageUrl(actualImageIndex + 3));
				break;
			}
			if (panel.getLastPhase() == 3 && delta < 0) {
				panel.setImageHandle(getImageUrl(actualImageIndex - 3));
				break;
			}
		}
		animate(350);
	}

	public void setPosAndSize(Widget child, double left, double top,
			double height, double width) {
		Layer a = (Layout.Layer) child.getLayoutData();
		a.setLeftWidth(left, Unit.PX, width, Unit.PX);
		a.setTopHeight(top, Unit.PX, height, Unit.PX);
	}

	public void setImageProvider(CarouselImageProvider imageProvider) {
		this.imageProvider = imageProvider;
		this.actualImageIndex = 0;
		updateImageInPanels();
	}

	private void updateImageInPanels() {
		for (CarouselImagePanel panel : panels) {
			panel.setImageHandle(getImageUrl(actualImageIndex
					+ panel.getPhase()));
		}
	}

	private ImageHandle getImageUrl(int actualImageIndex) {
		if (actualImageIndex < 0 || actualImageIndex >= imageProvider.size())
			return null;
		return imageProvider.getImageUrl(actualImageIndex);
	};

	@Override
	public void onResize() {
		for (CarouselImagePanel panel : panels) {
			panel.updatePanel(panel.getImageRectForCurrentPhase());
		}
		super.onResize();
	}
}
