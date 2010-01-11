package sk.gtug.carousel.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.gtug.carousel.client.FloatingImagePanel.AnimationInfo;
import sk.gtug.carousel.client.FloatingImagePanel.ImageRect;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RequiresResize;

public class CarouselPanel extends Composite implements RequiresResize {

	static {
		Style.INSTANCE.css().ensureInjected();
	}

	private List<FloatingImagePanel> panels = new ArrayList<FloatingImagePanel>();
	private LayoutPanel abs;
	private boolean active = false;

	public interface Style extends ClientBundle {
		Style INSTANCE = GWT.create(Style.class);

		@NotStrict
		@Source("style.css")
		CssResource css();
	}

	public CarouselPanel(ImageResource img1, ImageResource img2,
			ImageResource img3) {
		abs = new LayoutPanel();
		panels.add(new FloatingImagePanel(abs, img1, 0));
		panels.add(new FloatingImagePanel(abs, img2, 1));
		panels.add(new FloatingImagePanel(abs, img3, 2));
		panels.add(new FloatingImagePanel(abs, img1, 3));
		panels.add(new FloatingImagePanel(abs, img2, 4));
		panels.add(new FloatingImagePanel(abs, img3, 5));
		for (FloatingImagePanel panel : panels)
			abs.add(panel);
		initWidget(abs);
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

	public void onResize() {
		abs.onResize();
		int clientWidth = getElement().getClientWidth();
		int clientHeight = getElement().getClientHeight();
		int width = (int) (Math.min(clientHeight * 0.95, clientWidth * 0.3) - 30);

		for (FloatingImagePanel panel : panels) {
			panel.onResize(clientWidth, clientHeight, width, width);
		}
	}

	private void moveRectangle(int delta) {
		if (active)
			return;
		Map<FloatingImagePanel, FloatingImagePanel.AnimationInfo> panelAnimationInfo = new HashMap<FloatingImagePanel, FloatingImagePanel.AnimationInfo>();
		int steps = 10;
		for (FloatingImagePanel panel : panels) {
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
			final Map<FloatingImagePanel, FloatingImagePanel.AnimationInfo> animInfo,
			final int steps) {
		final Set<FloatingImagePanel> panels = animInfo.keySet();
		this.active = true;
		new Timer() {
			int duration = 150;
			int step = 0;

			{
				schedule(1);
			}

			@Override
			public void run() {
				if (step == steps) {
					for (FloatingImagePanel panel : panels) {
						panel.updatePanel(animInfo.get(panel).newRect);
					}
					CarouselPanel.this.active = false;
				} else {
					for (FloatingImagePanel panel : panels) {
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
	};
}
