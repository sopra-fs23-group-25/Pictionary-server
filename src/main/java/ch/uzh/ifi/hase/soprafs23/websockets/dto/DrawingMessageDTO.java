package ch.uzh.ifi.hase.soprafs23.websockets.dto;

public class DrawingMessageDTO {

    private float prevX;
    private float prevY;
    private float currX;
    private float currY;
    private String color;
    private Integer lineWidth;

    public Integer getLineWidth() {
        return lineWidth;
    }

    public void setLineWidth(Integer lineWidth) {
        this.lineWidth = lineWidth;
    }

    public float getPrevX() {
        return prevX;
    }

    public void setPrevX(float prevX) {
        this.prevX = prevX;
    }

    public float getPrevY() {
        return prevY;
    }

    public void setPrevY(float prevY) {
        this.prevY = prevY;
    }

    public float getCurrX() {
        return currX;
    }

    public void setCurrX(float currX) {
        this.currX = currX;
    }

    public float getCurrY() {
        return currY;
    }

    public void setCurrY(float currY) {
        this.currY = currY;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
