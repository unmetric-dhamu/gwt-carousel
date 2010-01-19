package sk.gtug.carousel.client;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.layout.client.Layout;
import com.google.gwt.layout.client.Layout.Layer;
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
		update();
	}

	private void setZIndex(int zIndex) {
		parent.getWidgetContainerElement(this).getStyle().setZIndex(zIndex);
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

	private void updatePanel(ImageRect rect) {
		int posX = rect.x - rect.width / 2;
		int posY = rect.y - rect.height / 2;
		setPosAndSize(posX, posY, rect.height, rect.width);
		setVisible(rect.visible);
		setZIndex(-Math.abs(phase));
	}

	private void update() {
		updatePanel(getImageRectForCurrentPhase());
	}

	public void setPosAndSize(double left, double top, double height,
			double width) {
		Layer a = (Layout.Layer) getLayoutData();
		a.setLeftWidth(left, Unit.PX, width, Unit.PX);
		a.setTopHeight(top, Unit.PX, height, Unit.PX);
	}

	public ImageRect getImageRectForCurrentPhase() {
		return getImageRectForPhase(phase);
	}

	public void updatePhase(int delta) {
		lastPhase = phase;
		this.phase -= delta;
		if (phase == 4)
			phase = -3;
		if (phase == -4)
			phase = 3;
		update();
	}

	public int getLastPhase() {
		return lastPhase;
	}

	public int getPhase() {
		return phase;
	}

	public void setImageHandle(ImageHandle imageHandle) {
		this.imageHandle = imageHandle;
		if (imageHandle == null)
			this.setVisible(false);
		else {
			setVisible(true);
			image.setUrl(imageHandle.getUrl());
			update();
		}
	}

	public static class ImageRect {
		private int x;
		private int y;
		private int width;
		private int height;
		private boolean visible;

		public ImageRect(int x, int y, int width, int height, boolean visible) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
			this.visible = visible;
		}
	}

	public void onResize() {
		if (imageHandle == null)
			return;
		Element container = parent.getWidgetContainerElement(this);
		int clientHeight = container.getClientHeight();
		int clientWidth = container.getClientWidth();
		double imageWidth = imageHandle.getWidth();
		double imageHeight = imageHandle.getHeight();
		if (imageWidth < imageHeight)
			clientWidth = (int) (clientWidth * (imageWidth / imageHeight));
		else
			clientHeight = (int) (clientHeight * (imageHeight / imageWidth));
		image.setPixelSize(clientWidth, clientHeight);
	}

	public void setBoardSize(int boardWidth, int boardHeight) {
		this.boardWidth = boardWidth;
		this.boardHeight = boardHeight;
		this.width = (int) (Math.min(boardHeight * 1, boardWidth * 0.3333) - 30);
	}
}
