import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.Test;

/**
 * @author clairewalker
 *
 */
public class BlockTest {

	/**
	 * Tests that a block's depth matches depth passed into constructor. Then when
	 * block is smashed, tests that its botLeftTree has a depth equal to the block's
	 * depth plus 1
	 */
	@Test
	public void testDepth() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		assertEquals(b.depth(), 0);
		b.smash(2);
		assertEquals(b.getBotLeftTree().depth(), 1);
	}

	/**
	 * Tests that the size of a leaf block's children list is 0
	 */
	@Test
	public void testChildrenSizeRoot() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		assertEquals(b.children().size(), 0);
	}

	/**
	 * Tests that the size of an internal node's children list is 4
	 */
	@Test
	public void testChildrenSizeSmashed() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(2);
		assertEquals(b.children().size(), 4);
	}

	/**
	 * Tests that colors of subtrees rotate clockwise when rotate called on parent
	 * block
	 */
	@Test
	public void testRotate() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(2);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);
		b.rotate();
		assertEquals(b.getTopLeftTree().getColor(), Color.GREEN);
		assertEquals(b.getTopRightTree().getColor(), Color.RED);
		assertEquals(b.getBotRightTree().getColor(), Color.BLUE);
		assertEquals(b.getBotLeftTree().getColor(), Color.YELLOW);
	}

	/**
	 * Tests that when flatten is called on a game with depth 3, the flatten board
	 * is correctly populated
	 */
	@Test
	public void testFlattenDepth3() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(3);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(3, Color.BLUE);
		g.setRoot(b);
		IBlock[][] board = g.flatten();

		assertEquals(board[0][0].getColor(), Color.RED);
		assertEquals(board[0][4].getColor(), Color.BLUE);
		assertEquals(board[4][4].getColor(), Color.YELLOW);
		assertEquals(board[7][0].getColor(), Color.GREEN);
	}

	/**
	 * Tests that when flatten is called on a game with depth 2, the flatten board
	 * is correctly populated
	 */
	@Test
	public void testFlattenDepth2() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(2);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(2, Color.BLUE);
		g.setRoot(b);
		IBlock[][] board = g.flatten();

		assertEquals(board[0][0].getColor(), Color.RED);
		assertEquals(board[0][2].getColor(), Color.BLUE);
		assertEquals(board[2][2].getColor(), Color.YELLOW);
		assertEquals(board[2][0].getColor(), Color.GREEN);
	}

	/**
	 * Tests that when flatten is called on a game with depth 1, the flatten board
	 * is correctly populated
	 */
	@Test
	public void testFlattenDepth1() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(1);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(1, Color.BLUE);
		g.setRoot(b);
		IBlock[][] board = g.flatten();
		assertEquals(board[0][0].getColor(), Color.RED);
		assertEquals(board[0][1].getColor(), Color.BLUE);
		assertEquals(board[1][1].getColor(), Color.YELLOW);
		assertEquals(board[1][0].getColor(), Color.GREEN);
	}

	/**
	 * Tests that when flatten is called on a game with depth 4, the flatten board
	 * is correctly populated
	 */
	@Test
	public void testFlattenDepth4() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(4);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(4, Color.BLUE);
		g.setRoot(b);
		IBlock[][] board = g.flatten();
		assertEquals(board[0][0].getColor(), Color.RED);
		assertEquals(board[0][10].getColor(), Color.BLUE);
		assertEquals(board[15][15].getColor(), Color.YELLOW);
		assertEquals(board[15][0].getColor(), Color.GREEN);
	}

	/**
	 * Tests the setX correctly resets X coordinate of a point
	 */
	@Test
	public void testPointSetX() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		P1.setX(1);
		assertEquals(1, b.getTopLeft().getX());
	}

	/**
	 * Tests that setY correctly resets Y coordinate of a point
	 */
	@Test
	public void testPointSetY() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		P1.setY(1);
		assertEquals(1, b.getTopLeft().getY());
	}

	/**
	 * Tests that Point's toString() produces the expected string
	 */
	@Test
	public void testPointToString() {
		Point P1 = new Point(0, 0);
		assertEquals("x: 0, y: 0", P1.toString());
	}

	/**
	 * Tests that a perimeter score of a board that does not have the target color
	 * at all is 0
	 */
	@Test
	public void testGetPerimeterScore0() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(3);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(3, Color.CYAN);
		g.setRoot(b);
		g.flatten();
		assertEquals(0, g.perimeter_score());
	}

	/**
	 * Tests that a perimeter score is 8 for a board with maxdepth 3 and target
	 * color at depth 1
	 */
	@Test
	public void testGetPerimeterScoreDepth3TargetBlue() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(3);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(3, Color.BLUE);
		g.setRoot(b);
		g.flatten();
		assertEquals(8, g.perimeter_score());
	}

	/**
	 * Tests that a perimeter score is 2 for a board with maxdepth 1 and target
	 * color at depth 1
	 */
	@Test
	public void testGetPerimeterScoreDepth1TargetBlue() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(1);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(1, Color.BLUE);
		g.setRoot(b);
		g.flatten();
		assertEquals(2, g.perimeter_score());
	}

	/**
	 * Tests that swapping child 1 and child 3 correctly swaps their colors
	 */
	@Test
	public void testSwap13() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(1);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(1, Color.BLUE);
		g.setRoot(b);
		g.swap(1, 3);
		assertEquals(b.getTopLeftTree().getColor(), Color.YELLOW);
	}

	/**
	 * Tests that swapping child 2 and child 4 correctly swaps their colors
	 */
	@Test
	public void testSwap24() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(1);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(1, Color.BLUE);
		g.setRoot(b);
		g.swap(2, 4);
		assertEquals(b.getTopRightTree().getColor(), Color.GREEN);
	}

	/**
	 * Tests that when try to swap the same block, that block remains unchanged
	 */
	@Test
	public void testSwapSameBlock() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(1);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(1, Color.BLUE);
		g.setRoot(b);
		g.swap(1, 1);
		assertEquals(b.getTopLeftTree().getColor(), Color.RED);
	}

	/**
	 * Tests that when try to swap the root, the tree remains unchanged
	 */
	@Test
	public void testSwapRoot() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(1);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);

		Game g = new Game(1, Color.BLUE);
		g.setRoot(b);
		g.swap(0, 1);
		assertEquals(b.getTopLeftTree().getColor(), Color.RED);
	}

	/**
	 * Creates a tree and sets the game's root to that tree. Then performs two swaps
	 * and verifies that the blocks are the expected colors
	 */
	@Test
	public void testSwap() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(2);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);
		b.getBotRightTree().smash(2);
		b.getBotRightTree().getTopLeftTree().setColor(Color.WHITE);
		b.getBotRightTree().getTopRightTree().setColor(Color.GRAY);
		b.getBotRightTree().getBotRightTree().setColor(Color.BLACK);
		b.getBotRightTree().getBotLeftTree().setColor(Color.CYAN);

		Game g = new Game(2, Color.BLUE);
		g.setRoot(b);
		g.swap(5, 7);
		assertEquals(b.getBotRightTree().getTopLeftTree().getColor(), Color.BLACK);
		assertEquals(b.getBotRightTree().getBotRightTree().getColor(), Color.WHITE);
		g.swap(1, 3);
		assertNull(b.getTopLeftTree().getColor());
		assertEquals(g.getBlock(5).getColor(), Color.BLACK);
		assertEquals(b.getTopLeftTree().getTopLeftTree().getColor(), Color.BLACK);
	}

	/**
	 * Tests that when blocks of different depths are swapped, nothing in the tree
	 * changes
	 */
	@Test
	public void testSwapDiftDepths() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(2);
		b.getTopLeftTree().setColor(Color.RED);
		b.getTopRightTree().setColor(Color.BLUE);
		b.getBotRightTree().setColor(Color.YELLOW);
		b.getBotLeftTree().setColor(Color.GREEN);
		b.getBotRightTree().smash(2);
		b.getBotRightTree().getTopLeftTree().setColor(Color.WHITE);
		b.getBotRightTree().getTopRightTree().setColor(Color.GRAY);
		b.getBotRightTree().getBotRightTree().setColor(Color.BLACK);
		b.getBotRightTree().getBotLeftTree().setColor(Color.CYAN);

		Game g = new Game(2, Color.BLUE);
		g.setRoot(b);
		g.swap(1, 5);
		assertEquals(b.getBotRightTree().getTopLeftTree().getColor(), Color.WHITE);
		assertEquals(b.getTopLeftTree().getColor(), Color.RED);
	}

	/**
	 * Tests that when a block is smashed, its color is null
	 */
	@Test
	public void testSmashRootColorNull() {
		Point P1 = new Point(0, 0);
		Point P2 = new Point(8, 8);
		IBlock b = new Block(P1, P2, 0, null);
		b.smash(1);
		assertNull(b.getColor());
	}

	/**
	 * Tests that, using the game constructor, the game's root color is null
	 */
	@Test
	public void testGameInit() {
		Game g = new Game(1, Color.BLACK);
		assertNull(g.getRoot().getColor());
	}

	/**
	 * Tests that, using the random_init, the game's root color is null
	 */
	@Test
	public void testGameInit2() {
		Game g = new Game(1, Color.BLACK);
		IBlock rt = g.random_init();
		assertNull(rt.getColor());
	}

}