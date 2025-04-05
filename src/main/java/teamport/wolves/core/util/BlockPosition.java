package teamport.wolves.core.util;

public class BlockPosition {
	public int x;
	public int y;
	public int z;

	public BlockPosition(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public void addOffset(int offset) {
		switch (offset) {
			case 0:
				y--;
				break;
			case 1:
				y++;
				break;
			case 2:
				z--;
				break;
			case 3:
				z++;
				break;
			case 4:
				x--;
				break;
			default:
				x++;
				break;
		}
	}
}
