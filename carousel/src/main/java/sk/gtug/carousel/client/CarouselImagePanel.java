package sk.gtug.carousel.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RequiresResize;

class CarouselImagePanel extends Composite implements RequiresResize {

	private final Carousel parent;
	private int phase;
	private int boardWidth;
	private int boardHeight;
	private int width;
	private final Image image = new Image();
	private ImageHandle imageHandle;
	private int lastPhase;

	public CarouselImagePanel(Carousel parent, int phase) {
		this.parent = parent;
		this.phase = phase;
		HorizontalPanel imageContaienr = new HorizontalPanel();
		imageContaienr.add(this.image);
		imageContaienr.setCellHorizontalAlignment(this.image,
				HasHorizontalAlignment.ALIGN_CENTER);
		imageContaienr.setCellVerticalAlignment(this.image,
				HasVerticalAlignment.ALIGN_MIDDLE);
		initWidget(imageContaienr);
		setVisible(false);
		setHeight("100%");
		setWidth("100%");
		parent.add(this);
		setZIndex(-Math.abs(phase));
	}

	public void setZIndex(int zIndex) {
		try {
			parent.getWidgetContainerElement(this).getStyle().setZIndex(zIndex);
		} catch (Exception e) {
		}
	}

	private ImageRect getImageRectForPhase(int phase) {
		int x = (int) (boardWidth / 2);
		int y = (int) (boardHeight / 2);
		int width2 = (int) (width);
		if (phase == 0) {
			// no transformation needed
		} else {
			double aa = 0.71;
			if (phase == -1 || phase == 1) {
				width2 = (int) (width2 * aa);
				x = x + phase * (5 + (width2 / 2) + width / 2);
			} else if (phase == -2 || phase == 2) {
				width2 = (int) (width2 * 0.47);
				double d = (width / 2) + (width * aa) + width2 * 0.3;
				x = (int) (x + d * (phase / 2));
			} else if (phase == -3 || phase == 3) {
				width2 = (int) (width2 * 0.2);
			}
		}
		boolean visible = Math.abs(phase) < 3 && this.imageHandle != null ? true
				: false;
		return new ImageRect(x, y, width2, width2, visible);
	}

	public ImageRect calculateStepRect(double step, final double stepX,
			final double stepY, final double stepWidth, ImageRect rect) {
		int newX = (int) (rect.x + step * stepX);
		int newY = (int) (rect.y + step * stepY);
		int newWidth = (int) (rect.width + step * stepWidth);
		boolean visible = Math.abs(phase) < 3 && this.imageHandle != null ? true
				: false;
		return new ImageRect(newX, newY, newWidth, newWidth, visible);
	}

	public void updatePanel(ImageRect rect) {
		int x = rect.x;
		int y = rect.y;
		int width = rect.width;
		int height = rect.height;
		int posX = x - width / 2;
		int posY = y - height / 2;
		parent.setPosAndSize(this, posX, posY, height, width);
		this.setVisible(rect.visible);
	}

	public ImageRect getImageRectForCurrentPhase() {
		return getImageRectForPhase(phase);
	}

	public int updatePhase(int delta) {
		lastPhase = phase;
		this.phase -= delta;
		if (phase == 4)
			phase = -3;
		if (phase == -4)
			phase = 3;
		return lastPhase;
	}

	public int getLastPhase() {
		return lastPhase;
	}

	public void updateStyle(AnimationInfo animInfo) {
		this.setZIndex(getZIndex(animInfo));
	}

	private int getZIndex(AnimationInfo animInfo) {
		int newPhase = animInfo.newPhase;
		return -Math.abs(newPhase);
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public void setImageHandle(ImageHandle imageHandle) {
		this.imageHandle = imageHandle;
		if (imageHandle == null) {
			this.setVisible(false);
		} else {
			setVisible(true);
			this.image.setUrl(imageHandle.getUrl());
			updatePanel(getImageRectForPhase(phase));
		}
	}

	public static class ImageRect {
		int x;
		int y;
		int width;
		int height;
		boolean visible;

		public ImageRect(int x, int y, int width, int height, boolean visible) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.visible = visible;
		}
	}

	public static class AnimationInfo {
		double stepX;
		double stepY;
		double stepWidth;
		ImageRect lastRect;
		ImageRect newRect;
		int steps;
		int lastPhase;
		int newPhase;

		public AnimationInfo(double stepX, double stepY, double stepWidth,
				ImageRect lastRect, ImageRect newRect, int steps,
				int lastPhase, int newPhase) {
			super();
			this.stepX = stepX;
			this.stepY = stepY;
			this.stepWidth = stepWidth;
			this.lastRect = lastRect;
			this.newRect = newRect;
			this.steps = steps;
			this.lastPhase = lastPhase;
			this.newPhase = newPhase;
		}

	}

	public void onResize() {
		this.boardWidth = parent.getElement().getClientWidth();
		this.boardHeight = parent.getElement().getClientHeight();
		this.width = (int) (Math.min(boardHeight * 0.95, boardWidth * 0.3333) - 30);
		
		int clientHeight = parent.getWidgetContainerElement(this)
				.getClientHeight();
		int clientWidth = parent.getWidgetContainerElement(this)
				.getClientWidth();

		if (imageHandle != null) {
			double imageWidth = imageHandle.getWidth();
			double imageHeight = imageHandle.getHeight();

			if (imageWidth < imageHeight)
				image.setPixelSize(
						(int) (clientWidth * (imageWidth / imageHeight)),
						clientHeight);
			else
				image.setPixelSize(clientWidth,
						(int) (clientHeight * (imageHeight / imageWidth)));
		}
	}
}
