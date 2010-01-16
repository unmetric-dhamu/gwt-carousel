package sk.gtug.carousel.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.gtug.carousel.client.CarouselImagePanel.AnimationInfo;
import sk.gtug.carousel.client.CarouselImagePanel.ImageRect;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.LayoutPanel;

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
		int steps = 20;
		for (CarouselImagePanel panel : panels) {
			int lastPhase = panel.updatePhase(delta > 0 ? 1 : -1);
			ImageRect rectForPhase = panel.getImageRectForCurrentPhase();
			AnimationInfo animInfo = panel.getAnimationInfo(rectForPhase,
					steps, lastPhase);
			panelAnimationInfo.put(panel, animInfo);
			panel.updateStyle(panelAnimationInfo.get(panel));
		}
		animate(panelAnimationInfo, steps, delta);
	}

	private void animate(
			final Map<CarouselImagePanel, CarouselImagePanel.AnimationInfo> animInfo,
			final int steps, int delta) {
		final Set<CarouselImagePanel> panels = animInfo.keySet();
		this.active = true;
		for (CarouselImagePanel panel : panels) {
			if (animInfo.get(panel).lastPhase == -3)
				panel
						.setImageHandle(delta > 0 ? getImageUrl(actualImageIndex + 2)
								: getImageUrl(actualImageIndex - 2));
		}
		new Timer() {
			int duration = 150;
			double step = 1;

			{
				schedule(1);
			}

			@Override
			public void run() {
				if (step == steps) {
					for (CarouselImagePanel panel : panels) {
						AnimationInfo aniInfo = animInfo.get(panel);
						panel.updatePanel(panel.calculateStepRect(step,
								aniInfo.stepX, aniInfo.stepY,
								aniInfo.stepWidth, aniInfo.lastRect));
					}
					Carousel.this.active = false;
				} else {
					for (CarouselImagePanel panel : panels) {
						AnimationInfo aniInfo = animInfo.get(panel);
						double a = (step / steps) * (Math.PI / 2);
						double myStep = Math.sin(a) * steps;
						panel.updatePanel(panel.calculateStepRect(myStep,
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

			panel.setImageHandle(getImageUrl(actualImageIndex
					+ panel.getPhase()));
			//        	
			// if (panel.getPhase() == 0)
			// panel.setImageHandle(getImageUrl(actualImageIndex));
			// if (panel.getPhase() == 1)
			// panel.setImageHandle(getImageUrl(actualImageIndex - 1));
			// if (panel.getPhase() == 2)
			// panel.setImageHandle(getImageUrl(actualImageIndex - 2));
			// if (panel.getPhase() == 3)
			// panel.setImageHandle(null);
			// if (panel.getPhase() == 4)
			// panel.setImageHandle(getImageUrl(actualImageIndex + 2));
			// if (panel.getPhase() == 5)
			// panel.setImageHandle(getImageUrl(actualImageIndex + 1));
		}
	}

	private String getImageUrl(int actualImageIndex) {
		if (actualImageIndex < 0 || actualImageIndex >= imageProvider.size())
			return null;
		return imageProvider.getImageUrl(actualImageIndex).getUrl();
	};
}
