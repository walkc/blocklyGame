import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author clairewalker
 *
 */
public class Game implements IGame {
	private int maxDepth;
	private Color target;
	private IBlock root;
	private int boardSize;

	/**
	 * @param max_depth
	 * @param target
	 */
	public Game(int max_depth, Color target) {

		this.maxDepth = max_depth;
		this.target = target;
		this.root = random_init();

		// set board size to 2^max depth - this is the number of rows and columns in our
		// flattened board
		boardSize = (int) (Math.pow(2, maxDepth));

	}

	@Override
	public int max_depth() {
		return maxDepth;
	}

	@Override
	public IBlock random_init() {
		Point topL = new Point(0, 0);
		Point botR = new Point(8, 8);

		// create the root of this board to have the points (0,0) and (8,8) with depth 0
		// and no parent
		this.root = new Block(topL, botR, 0, null);
		// smash the root. We will never have a leaf as root, so always must smash at
		// least once
		root.smash(maxDepth);
		// after initial smash, the board has a depth of 1 and there are 5 blocks
		int depthReached = 1;
		int totalblocks = 5;
		// loop through and smash blocks until we have reached the max depth for the
		// board
		while (depthReached < maxDepth) {
			// select a random block from the board
			int blockToSelect = (int) (Math.random() * totalblocks);
			IBlock blockToSmash = getBlock(blockToSelect);
			// if that block is a leaf, smash it
			if (blockToSmash.isleaf()) {
				blockToSmash.smash(maxDepth);
				// increase count of total blocks by 4
				totalblocks += 4;
				// update the depth reached
				depthReached = blockToSmash.depth() + 1;
			}
		}
		// return the root of the board
		return root;
	}

	private Queue<IBlock> bfs() {
		Queue<IBlock> qTemp = new LinkedList<IBlock>();
		Queue<IBlock> qReturn = new LinkedList<IBlock>();
		qTemp.add(root);
		qReturn.add(root);
		IBlock temp = root;
		while (qTemp.size() != 0) {
			// poll the first block from the queue
			temp = qTemp.poll();
			// if the block is not a leaf, add its children to the queue
			if (!temp.isleaf()) {
				List<IBlock> kids = temp.children();
				qTemp.add(kids.get(0));
				qTemp.add(kids.get(1));
				qTemp.add(kids.get(2));
				qTemp.add(kids.get(3));
				qReturn.add(kids.get(0));
				qReturn.add(kids.get(1));
				qReturn.add(kids.get(2));
				qReturn.add(kids.get(3));
			}
		}
		return qReturn;
	}

	@Override
	public IBlock getBlock(int pos) {
		Queue<IBlock> q = bfs();
		// if pos is less than 0 or more than the size of the queue
		if (pos < 0 || pos >= q.size()) {
			return null;
		}

		// else, add all blocks from q to arrayList
		List<IBlock> bfsListOfBlocks = new ArrayList<IBlock>();
		while (q.size() != 0) {
			IBlock temp = q.poll();
			bfsListOfBlocks.add(temp);
		}

		// return the block at the desired position
		return bfsListOfBlocks.get(pos);
	}

	@Override
	public IBlock getRoot() {
		return this.root;
	}

	@Override
	public void swap(int x, int y) {
		// if the block numbers are equal or less than 1, do nothing
		if (x < 1 || y < 1 || x == y) {
			return;
		}

		// get the blocks at position x and y
		IBlock a = getBlock(x);
		IBlock b = getBlock(y);

		// if either block is null, means block number is invalid. do nothing
		if (a == null || b == null) {
			return;
		}

		// if those blocks don't have the same depth, do nothing
		if (a.depth() != b.depth()) {
			return;
		}

		// calculate the difference between the first block's position and its parent's
		// position
		int aDif = calculateDifBtwParentChildPositions(x);
		// calculate the difference between the second block's position and its parent's
		// position
		int bDif = calculateDifBtwParentChildPositions(y);

		// based on the difference in position between the parent and its child, we know
		// which child block a is
		// substitute block a with block b
		swapChild(aDif, a, b);

		// based on the difference in position between the parent and its child, we know
		// which child block b is
		// substitute block b with block a
		swapChild(bDif, b, a);

		// recursively update points for the parent of block a's children, and for the
		// parent of block b's children
		((Block) a).getParent().updatePoints(((Block) a).getParent().children());
		((Block) b).getParent().updatePoints(((Block) b).getParent().children());
	}

	private int calculateDifBtwParentChildPositions(int x) {
		// if child's position is smaller than 4, then difference is equal to child's
		// position
		if (x < 4) {
			return x;
		}
		// else, difference between parent and child's position is child's position % 4
		return x % 4;
	}

	private void swapChild(int parentChildPositionDif, IBlock old, IBlock toSwap) {
		// if parent child position difference is 1, then child is the topLeftTree
		// set the parent of the old block's topLeftTree to the new toSwap block
		if (parentChildPositionDif == 1) {
			((Block) old).getParent().setTopLeftTree(toSwap);
		}
		// if parent child position difference is 1, then child is the topRightTree
		// set the parent of the old block's topRightTree to the new toSwap block
		if (parentChildPositionDif == 2) {
			((Block) old).getParent().setTopRightTree(toSwap);
		}
		// if parent child position difference is 1, then child is the botRightTree
		// set the parent of the old block's botRightTree to the new toSwap block
		if (parentChildPositionDif == 3) {
			((Block) old).getParent().setBotRightTree(toSwap);
		}
		// if parent child position difference is 1, then child is the botLeftTree
		// set the parent of the old block's botLeftTree to the new toSwap block
		if (parentChildPositionDif == 0) {
			((Block) old).getParent().setBotLeftTree(toSwap);
		}
	}

	@Override
	public IBlock[][] flatten() {
		// create a 2D array the size of the board at maxdepth, as calculated in
		// boardSize
		IBlock[][] flatBoard = new IBlock[boardSize][boardSize];
		// find difference btw 3 and the maxdepth of this board
		// the board's points are deafult (0,0) and (8,8). 8 is 2^3, so we say the
		// default depth is 3 so that we can use each block's top left and bottom
		// right points to calculate which portion of the 2D array it should fill
		int depthDif = 3 - maxDepth;
		// calculate depthDifPower as 2^depthDif as calculated above
		int depthDifPower = (int) Math.pow(2, Math.abs(depthDif));

		// use a breadth first search to go through the quadtree block by block
		Queue<IBlock> q = new LinkedList<IBlock>();
		q.add(root);
		IBlock temp = root;

		while (q.size() != 0) {
			temp = q.poll();
			// find the top left x and y points for this IBlock
			int topLeftX = temp.getTopLeft().getX();
			int topLeftY = temp.getTopLeft().getY();
			// find the bot right x and y points for this IBlock
			int botRightX = temp.getBotRight().getX();
			int botRightY = temp.getBotRight().getY();

			// assign spaces in the 2D array to this block based on its points on the board
			// if this board has a depth equal to or larger than 3
			if (depthDif >= 0) {
				// loop through the 2D array for x values from this IBlock's top left x
				// coordinate to its bottom right x coordinate-1
				// adjust each calculation by dividing by the depthDifPower
				for (int i = topLeftX / depthDifPower; i <= botRightX / depthDifPower - 1; i++) {
					// loop through the 2D array for y values from this IBlock's top left y
					// coordinate to its bottom right y coordinate-1
					// adjust each calculation by dividing by the depthDifPower
					for (int j = topLeftY / depthDifPower; j <= botRightY / depthDifPower - 1; j++) {
						flatBoard[j][i] = temp;
					}
				}
			}

			// else if board has depthDif smaller than 0
			else {
				// loop through the 2D array for x values from this IBlock's top left x
				// coordinate to its bottom right x coordinate-1
				// adjust each calculation by multiplying by the depthDifPower
				for (int i = topLeftX * depthDifPower; i <= botRightX * depthDifPower - 1; i++) {
					// loop through the 2D array for x values from this IBlock's top left y
					// coordinate to its bottom right y coordinate-1
					// adjust each calculation by multiplying by the depthDifPower
					for (int j = topLeftY * depthDifPower; j <= botRightY * depthDifPower - 1; j++) {
						flatBoard[j][i] = temp;
					}
				}
			}

			// if this block is not a leaf, add its children to the queue
			if (!temp.isleaf()) {
				List<IBlock> kids = temp.children();
				q.add(kids.get(0));
				q.add(kids.get(1));
				q.add(kids.get(2));
				q.add(kids.get(3));
			}
		}

		return flatBoard;
	}

	@Override
	public int perimeter_score() {
		int score = 0;
		IBlock[][] board = this.flatten();

		// if any corner block's color is equal to the target color, increase the score
		// by 2
		if (board[0][0].getColor().equals(this.target)) {
			score += 2;
		}
		if (board[0][boardSize - 1].getColor().equals(this.target)) {
			score += 2;
		}
		if (board[boardSize - 1][0].getColor().equals(this.target)) {
			score += 2;
		}
		if (board[boardSize - 1][boardSize - 1].getColor().equals(this.target)) {
			score += 2;
		}

		// increase score by 1 if a block in the top row (but not in a corner) is the
		// target color
		int i = 0;
		for (int j = 1; j < boardSize - 1; j++) {
			if (board[i][j].getColor().equals(this.target)) {
				score++;
			}
		}

		// increase score by 1 if a block in the bottom row (but not in a corner) is the
		// target color
		i = boardSize - 1;
		for (int j = 1; j < boardSize - 1; j++) {
			if (board[i][j].getColor().equals(this.target)) {
				score++;
			}
		}

		// increase score by 1 if a block in the first column (but not in a corner) is
		// the target color
		int j = 0;
		for (i = 1; i < boardSize - 1; i++) {
			if (board[i][j].getColor().equals(this.target)) {
				score++;
			}
		}

		// increase score by 1 if a block in the last column (but not in a corner) is
		// the target color
		j = boardSize - 1;
		for (i = 1; i < boardSize - 1; i++) {
			if (board[i][j].getColor().equals(this.target)) {
				score++;
			}
		}

		return score;
	}

	@Override
	public void setRoot(IBlock root) {
		this.root = root;
	}

}
