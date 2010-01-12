package sk.gtug.carousel.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.gtug.carousel.client.CarouselImagePanel.AnimationInfo;
import sk.gtug.carousel.client.CarouselImagePanel.ImageRect;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.LayoutPanel;

public class Carousel extends LayoutPanel {

	static {
		Style.INSTANCE.css().ensureInjected();
	}

	private List<CarouselImagePanel> panels = new ArrayList<CarouselImagePanel>();
	private boolean active = false;
	private CarouselImageProvider imageProvider;
	private int actualImageIndex;
	private int pendingDelta = 0;

	public interface Style extends ClientBundle {
		Style INSTANCE = GWT.create(Style.class);

		@NotStrict
		@Source("style.css")
		CssResource css();
	}

	public Carousel() {
		panels.add(new CarouselImagePanel(this, 0));
		panels.add(new CarouselImagePanel(this, 1));
		panels.add(new CarouselImagePanel(this, 2));
		panels.add(new CarouselImagePanel(this, 3));
		panels.add(new CarouselImagePanel(this, 4));
		panels.add(new CarouselImagePanel(this, 5));

		for (CarouselImagePanel panel : panels)
			add(panel);

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

	}

	private void moveRectangle(int delta) {
		if (imageProvider == null)
			return;
		if (active) {
			pendingDelta += delta;
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
		Map<CarouselImagePanel, CarouselImagePanel.AnimationInfo> panelAnimationInfo = new HashMap<CarouselImagePanel, CarouselImagePanel.AnimationInfo>();
		int steps = 10;
		for (CarouselImagePanel panel : panels) {
			int lastPhase = panel.updatePhase(delta > 0 ? 1 : -1);
			ImageRect rectForPhase = panel.getImageRectForCurrentPhase();
			AnimationInfo animInfo = panel.getAnimationInfo(rectForPhase,
					steps, lastPhase);
			panelAnimationInfo.put(panel, animInfo);
			panel.updateStyle(panelAnimationInfo.get(panel));
		}
		animate(panelAnimationInfo, steps);
	}

	private void animate(
			final Map<CarouselImagePanel, CarouselImagePanel.AnimationInfo> animInfo,
			final int steps) {
		final Set<CarouselImagePanel> panels = animInfo.keySet();
		this.active = true;
		new Timer() {
			int duration = 150;
			int step = 0;

			{
				if (Carousel.this.pendingDelta>3) duration = 50;
				schedule(1);
			}

			@Override
			public void run() {
				if (step == steps) {
					for (CarouselImagePanel panel : panels) {
						panel.updatePanel(animInfo.get(panel).newRect);
						updateImageInPanels();
					}
					Carousel.this.active = false;
					
					if (pendingDelta>0) {
						pendingDelta = 0;
						moveRectangle(3);
					}
				} else {
					for (CarouselImagePanel panel : panels) {
						AnimationInfo aniInfo = animInfo.get(panel);
						panel.updatePanel(panel.calculateStepRect(step,
								aniInfo.stepX, aniInfo.stepY,
								aniInfo.stepWidth, aniInfo.lastRect));
						this.schedule(duration / aniInfo.steps);
					}
					step++;
				}
				;
			};
		};
	}

	public void setImageProvider(CarouselImageProvider imageProvider) {
		this.imageProvider = imageProvider;
		this.actualImageIndex = 0;
		updateImageInPanels();
	}

	private void updateImageInPanels() {
		for (CarouselImagePanel panel : panels) {
			if (panel.getPhase() == 0)
				panel.setImageHandle(getImageUrl(actualImageIndex));
			if (panel.getPhase() == 1)
				panel.setImageHandle(getImageUrl(actualImageIndex - 1));
			if (panel.getPhase() == 2)
				panel.setImageHandle(getImageUrl(actualImageIndex - 2));
			if (panel.getPhase() == 3)
				panel.setImageHandle(null);
			if (panel.getPhase() == 4)
				panel.setImageHandle(getImageUrl(actualImageIndex + 2));
			if (panel.getPhase() == 5)
				panel.setImageHandle(getImageUrl(actualImageIndex + 1));
		}
	}

	private String getImageUrl(int actualImageIndex) {
		if (actualImageIndex < 0 || actualImageIndex >= imageProvider.size())
			return null;
		return imageProvider.getImageUrl(actualImageIndex).getUrl();
	};
}
