import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * @author clairewalker
 *
 */
public class Block implements IBlock {
	private Point topLeft;
	private Point botRight;
	private int depth;
	private Block parent;
	private Block topLeftTree;
	private Block botLeftTree;
	private Block topRightTree;
	private Block botRightTree;
	private Color color;

	/**
	 * @param topLeft
	 * @param botRight
	 * @param depth
	 * @param parent
	 */
	public Block(Point topLeft, Point botRight, int depth, Block parent) {
		this.topLeft = topLeft;
		this.botRight = botRight;
		this.depth = depth;
		this.parent = parent;
		topLeftTree = null;
		botLeftTree = null;
		topRightTree = null;
		botRightTree = null;
	}

	@Override
	public int depth() {
		return this.depth;
	}

	@Override
	public void smash(int maxDepth) {
		// if we are already at maxdepth, or the block isn't a leaf, do nothing
		if (!this.isleaf() || this.depth == maxDepth) {
			return;
		}

		// update this block's color to null
		this.color = null;

		// create top left and bottom right points for the block's new topLeftTree
		Point topL1 = new Point(topLeft.getX(), topLeft.getY());
		Point botR1 = new Point((topLeft.getX() + botRight.getX()) / 2, (topLeft.getY() + botRight.getY()) / 2);
		// create top left and bottom right points for the block's new topRightTree
		Point topL2 = new Point((topLeft.getX() + botRight.getX()) / 2, topLeft.getY());
		Point botR2 = new Point(botRight.getX(), (topLeft.getY() + botRight.getY()) / 2);
		// create top left and bottom right points for the block's new botRightTree
		Point topL3 = new Point((topLeft.getX() + botRight.getX()) / 2, (topLeft.getY() + botRight.getY()) / 2);
		Point botR3 = new Point(botRight.getX(), botRight.getY());
		// create top left and bottom right points for the block's new botLeftTree
		Point topL4 = new Point(topLeft.getX(), (topLeft.getY() + botRight.getY()) / 2);
		Point botR4 = new Point((topLeft.getX() + botRight.getX()) / 2, botRight.getY());

		// assign this block's topLeftTree to a new Block using the points calculated
		// above
		// depth is one more than this block's depth, and this block is its parent block
		topLeftTree = new Block(topL1, botR1, this.depth + 1, this);
		// select a random color from Colors array
		int colorSelector = (int) (Math.random() * COLORS.length);
		// assign topLeftTree color to this color
		topLeftTree.color = COLORS[colorSelector];

		// repeat these steps for the remaining children of this block
		botLeftTree = new Block(topL4, botR4, this.depth + 1, this);
		colorSelector = (int) (Math.random() * COLORS.length);
		botLeftTree.color = COLORS[colorSelector];
		topRightTree = new Block(topL2, botR2, this.depth + 1, this);
		colorSelector = (int) (Math.random() * COLORS.length);
		topRightTree.color = COLORS[colorSelector];
		botRightTree = new Block(topL3, botR3, this.depth + 1, this);
		colorSelector = (int) (Math.random() * COLORS.length);
		botRightTree.color = COLORS[colorSelector];
	}

	@Override
	public List<IBlock> children() {
		// create a new arrayList to hold the children of this block
		List<IBlock> children = new ArrayList<IBlock>();

		// if this block is a leaf, return the empty list
		if (this.isleaf()) {
			return children;
		}

		// else, add the children nodes in clockwise order beginning with topLeftTree
		children.add(this.topLeftTree);
		children.add(this.topRightTree);
		children.add(this.botRightTree);
		children.add(this.botLeftTree);

		// return the list of children
		return children;
	}

	@Override
	public void rotate() {
		// if the block is a leaf, do nothing
		if (this.isleaf()) {
			return;
		}

		// rotate the pointers of this block's children
		Block temp = topLeftTree;
		topLeftTree = botLeftTree;
		botLeftTree = botRightTree;
		botRightTree = topRightTree;
		topRightTree = temp;

		// call update points to recursively update the top left and bottom right points
		// for this block's subtree
		updatePoints(this.children());

	}

	/**
	 * @return parent of this block
	 */
	public Block getParent() {
		return this.parent;
	}

	/**
	 * @param b Takes an IBlock and sets it to this block's parent
	 */
	public void setParent(IBlock b) {
		// if the b's depth is not one less than this block's, then it can't be a valid
		// parent
		// do nothing
		if (b.depth() != this.depth - 1) {
			return;
		}
		// else, set block b's color to null
		b.setColor(null);
		// and assign it as the parent to this block
		this.parent = (Block) b;
	}

	/**
	 * @return List<IBlock> Retuns an arrayList of the children of this block
	 */
	public List<IBlock> getChildren() {
		return this.children();
	}

	/**
	 * @param kids Takes a list of blocks Sets the first to be topLeftTree Second to
	 *             be topRightTree third to be botRightTree fourth to be botLeftTree
	 */
	public void setChildren(List<IBlock> kids) {
		this.topLeftTree = (Block) kids.get(0);
		this.topRightTree = (Block) kids.get(1);
		this.botRightTree = (Block) kids.get(2);
		this.botLeftTree = (Block) kids.get(3);
	}

	/**
	 * @param kids Recursively updates top left and bottom right points for the
	 *             children of an IBlock and every block in the children's subtrees
	 */
	public void updatePoints(List<IBlock> kids) {

		// get the parent block for this list of child blocks
		Block parentBlock = ((Block) kids.get(0)).parent;
		// get the x and y coordinates for the parent's top left point
		int parentTopLX = parentBlock.getTopLeft().getX();
		int parentTopLY = parentBlock.getTopLeft().getY();
		// get the x and y coordinates for the parent's bottom right point
		int parentBotRX = parentBlock.getBotRight().getX();
		int parentBotRY = parentBlock.getBotRight().getY();

		// update the topLeftTree's top left and bottom right points based on the parent
		// blocks points
		((Block) kids.get(0)).setTopLeft(parentTopLX, parentTopLY);
		((Block) kids.get(0)).setBotRight((parentTopLX + parentBotRX) / 2, (parentTopLY + parentBotRY) / 2);

		// update the topRightTree's top left and bottom right points based on the
		// parent blocks points
		((Block) kids.get(1)).setTopLeft((parentTopLX + parentBotRX) / 2, parentTopLY);
		((Block) kids.get(1)).setBotRight(parentBotRX, (parentTopLY + parentBotRY) / 2);

		// update the botRightTree's top left and bottom right points based on the
		// parent blocks points
		((Block) kids.get(2)).setTopLeft((parentTopLX + parentBotRX) / 2, (parentTopLY + parentBotRY) / 2);
		((Block) kids.get(2)).setBotRight(parentBotRX, parentBotRY);

		// update the botLeftTree's top left and bottom right points based on the parent
		// blocks points
		((Block) kids.get(3)).setTopLeft(parentTopLX, (parentTopLY + parentBotRY) / 2);
		((Block) kids.get(3)).setBotRight((parentTopLX + parentBotRX) / 2, parentBotRY);

		// if the topLeftTree is a leaf, recursively update points for its children
		// blocks
		if (!kids.get(0).isleaf()) {
			updatePoints(kids.get(0).children());
		}

		// if the topRightTree is a leaf, recursively update points for its children
		// blocks
		if (!kids.get(1).isleaf()) {
			updatePoints(kids.get(1).children());
		}

		// if the botRightTree is a leaf, recursively update points for its children
		// blocks
		if (!kids.get(2).isleaf()) {
			updatePoints(kids.get(2).children());
		}

		// if the botLeftTree is a leaf, recursively update points for its children
		// blocks
		if (!kids.get(3).isleaf()) {
			updatePoints(kids.get(3).children());
		}

	}

	@Override
	public Color getColor() {
		return this.color;
	}

	@Override
	public void setColor(Color c) {
		// if the block is not a leaf, do nothing. internal nodes can't have color
		if (!this.isleaf()) {
			return;
		}

		// else, set this block's color to c
		this.color = c;
	}

	@Override
	public Point getTopLeft() {
		return topLeft;
	}

	private void setTopLeft(int x, int y) {
		// if x or y are outside the range of the board's points, do nothing
		if (x < 0 || y < 0 || x > 8 || y > 8) {
			return;
		}
		// create a new point with x and y coordinates
		Point p = new Point(x, y);
		// set topLeft to this point
		this.topLeft = p;
	}

	private void setBotRight(int x, int y) {
		// if x or y are outside the range of the board's points, do nothing
		if (x < 0 || y < 0 || x > 8 || y > 8) {
			return;
		}
		// create a new point with x and y coordinates
		Point p = new Point(x, y);
		// set botRight to this point
		this.botRight = p;
	}

	@Override
	public Point getBotRight() {
		return botRight;
	}

	@Override
	public boolean isleaf() {
		return topLeftTree == null && botLeftTree == null && topRightTree == null && botRightTree == null;
	}

	@Override
	public IBlock getTopLeftTree() {
		return topLeftTree;
	}

	/**
	 * @param b Sets this block's topLeftTree to IBlock b
	 */
	public void setTopLeftTree(IBlock b) {
		// if this block is a leaf, set it's color to null (as it will no longer be a
		// leaf)
		if (this.isleaf()) {
			this.setColor(null);
		}
		// set topLeftTree equal to b
		this.topLeftTree = (Block) b;
	}

	/**
	 * @param b Sets this block's botLeftTree to IBlock b
	 */
	public void setBotLeftTree(IBlock b) {
		// if this block is a leaf, set it's color to null (as it will no longer be a
		// leaf)
		if (this.isleaf()) {
			this.setColor(null);
		}
		// set botLeftTree equal to b
		this.botLeftTree = (Block) b;
	}

	/**
	 * @param b Sets this block's topRightTree to IBlock b
	 */
	public void setTopRightTree(IBlock b) {
		// if this block is a leaf, set it's color to null (as it will no longer be a
		// leaf)
		if (this.isleaf()) {
			this.setColor(null);
		}
		// set topRightTree equal to b
		this.topRightTree = (Block) b;
	}

	/**
	 * @param b Sets this block's botRightTree to IBlock b
	 */
	public void setBotRightTree(IBlock b) {
		// if this block is a leaf, set it's color to null (as it will no longer be a
		// leaf)
		if (this.isleaf()) {
			this.setColor(null);
		}
		// set botRightTree equal to b
		this.botRightTree = (Block) b;
	}

	@Override
	public IBlock getTopRightTree() {
		return topRightTree;
	}

	@Override
	public IBlock getBotLeftTree() {
		return botLeftTree;
	}

	@Override
	public IBlock getBotRightTree() {
		return botRightTree;
	}

}
