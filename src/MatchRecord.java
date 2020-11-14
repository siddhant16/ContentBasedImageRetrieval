import org.bytedeco.javacpp.opencv_core.IplImage;

public class MatchRecord { 
	IplImage image;

	public IplImage getImage() {
		return image;
	}

	public void setImage(IplImage image) {
		this.image = image;
	}

	private int count;
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public int getCount() {
		return count;
	}

	public String getName() {
		return name;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
