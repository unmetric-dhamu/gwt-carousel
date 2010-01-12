package sk.gtug.carousel.client;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.RequiresResize;

class CarouselImagePanel extends Composite implements RequiresResize {

    private final LayoutPanel parent;
    private int phase;
    private int boardWidth;
    private int boardHeight;
    private int width;
    private final Image image;
    private ImageRect currentRect;

    public CarouselImagePanel(LayoutPanel parent, int phase) {
        this.parent = parent;
        this.image = new Image();
        this.phase = phase;
        initWidget(image);
        setVisible(false);
        this.addStyleName("phase" + phase);
    }

    private ImageRect getImageRectForPhase(int phase) {
        int x = boardWidth / 2;
        int y = boardHeight / 2;
        int width2 = width;
        if (phase == 0) {
            // no transformation needed
        } else if (phase == 1) {
            width2 = (int) (width2 * 0.71);
            x = x - 5 - (width2 / 2) - width / 2;
        } else if (phase == 2) {
            width2 = (int) (width2 * 0.47);
            x = (int) (x - (width / 2) - (width * 0.71) - width2 * 0.3);
        } else if (phase == 3) {
            width2 = (int) (width2 * 0.2);
        } else if (phase == 4) {
            width2 = (int) (width2 * 0.47);
            x = (int) (x + (width / 2) + (width * 0.71) + width2 * 0.3);
        } else if (phase == 5) {
            width2 = (int) (width2 * 0.71);
            x = x + 5 + (width2 / 2) + width / 2;
        }
        return new ImageRect(x, y, width2, width2);
    }

    public AnimationInfo getAnimationInfo(final ImageRect rect, final int steps, int lastPhase) {
        double stepX = (rect.x - currentRect.x) / steps;
        double stepY = (rect.y - currentRect.y) / steps;
        double stepWidth = (rect.width - currentRect.width) / steps;
        final AnimationInfo anStep = new AnimationInfo(stepX, stepY, stepWidth, currentRect, rect, steps, lastPhase, phase);
        return anStep;
    }

    public ImageRect calculateStepRect(int step, final double stepX, final double stepY, final double stepWidth, ImageRect rect) {
        int newX = (int) (rect.x + step * stepX);
        int newY = (int) (rect.y + step * stepY);
        int newWidth = (int) (rect.width + step * stepWidth);
        return new ImageRect(newX, newY, newWidth, newWidth);
    }

    public void updatePanel(ImageRect rect) {
        int x = rect.x;
        int y = rect.y;
        int width = rect.width;
        int height = rect.height;
        this.currentRect = rect;
        int posX = x - width / 2;
        int posY = y - height / 2;
        parent.setWidgetLeftWidth(this, posX, Unit.PX, width, Unit.PX);
        parent.setWidgetTopHeight(this, posY, Unit.PX, height, Unit.PX);
        image.setPixelSize(width, height);
    }

    public ImageRect getImageRectForCurrentPhase() {
        return getImageRectForPhase(phase);
    }

    public int updatePhase(int delta) {
        int lastPhase = phase;
        this.phase += delta;
        if (phase == 6)
            phase = 0;
        if (phase == -1)
            phase = 5;
        return lastPhase;
    }

    public void updateStyle(AnimationInfo animInfo) {
        this.removeStyleName("phase" + animInfo.lastPhase);
        this.addStyleName("phase" + animInfo.newPhase);
    }

    public int getPhase() {
        return phase;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setImageHandle(String imageHandle) {
        if (imageHandle == null)
            this.setVisible(false);
        else
            setVisible(true);
        this.image.setUrl(imageHandle);
    }

    public static class ImageRect {
        int x;
        int y;
        int width;
        int height;

        public ImageRect(int x, int y, int width, int height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
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

        public AnimationInfo(double stepX, double stepY, double stepWidth, ImageRect lastRect, ImageRect newRect, int steps, int lastPhase,
                int newPhase) {
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
        this.width = (int) (Math.min(boardHeight * 0.95, boardWidth * 0.3) - 30);
        updatePanel(getImageRectForPhase(phase));
    }
}
