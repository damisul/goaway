package ru.goproject.goaway.midlet;

import java.util.Date;
import java.util.Random;
import java.util.Vector;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

import ru.goproject.goaway.collection.ProblemsCollection;
import ru.goproject.goaway.collection.ProblemsCollectionEventListener;
import ru.goproject.goaway.common.EditorAction;
import ru.goproject.goaway.common.Goban;
import ru.goproject.goaway.common.Label;
import ru.goproject.goaway.common.MoveAction;
import ru.goproject.goaway.common.Node;
import ru.goproject.goaway.common.Point;
import ru.goproject.goaway.common.Problem;
import ru.goproject.goaway.common.ProblemNavigator;
import ru.goproject.goaway.common.Stone;
import ru.goproject.goaway.exception.GoAwayException;

/**
 * GobanCanvas is responsible for drawing of go board and additional problem-related info
 * @author damir
 */
public class GobanCanvas extends Canvas implements CommandListener, ProblemsCollectionEventListener {
	/**
	 * Color constants
	 */
	/**
	 * Color of black stone and lines
	 */
	private final static int COLOR_BLACK = 0x00000000;
	/**
	 * Color of white stones
	 */
	private final static int COLOR_WHITE = 0x00FFFFFF;
	/**
	 * Board color
	 */
	private final static int COLOR_BOARD = 0x00C89632;
	/**
	 * Success notification color
	 */
	private final static int COLOR_RIGHT = 0x0000FFFF;
	/**
	 * Wrong notification color
	 */
	private final static int COLOR_WRONG = 0x00FF0000;
	
	private final static String RES_CMD_NEXT = "GobanCanvas.cmdNext";
	private final static String RES_CMD_PREVIOUS = "GobanCanvas.cmdPrevious";
	private final static String RES_CMD_SELECT_PROBLEM = "GobanCanvas.cmdSelectProblem";	
	private final static String RES_CMD_RANDOM = "GobanCanvas.cmdRandom";
	private final static String RES_CMD_UNDO = "GobanCanvas.cmdUndo";
	private final static String RES_CMD_COMMENT = "GobanCanvas.cmdComment";
	private final static String RES_CMD_SHOW_HINTS = "GobanCanvas.cmdShowHints";
	private final static String RES_CMD_HIDE_HINTS = "GobanCanvas.cmdHideHints";
	
	private final static String RES_NOTIFY_SOLVED = "GobanCanvas.solved";
	private final static String RES_NOTIFY_WRONG = "GobanCanvas.wrong";
	
	private final static String RES_PROBLEM_LOADING = "GobanCanvas.problemLoading";
	private final static String RES_DAMAGED_FILE = "GobanCanvas.damagedFile";
	
	private final static Font FONT_SMALL = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_SMALL);
	private final static Font FONT_MEDIUM = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
	private final static Font FONT_LARGE = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_PLAIN, Font.SIZE_LARGE);
	private final static int NOTIFICATION_AREA_HEIGHT = FONT_MEDIUM.getHeight() + 1;
	private final static int MIN_LABEL_CELLSIZE = FONT_SMALL.getHeight();
	private final static int MIN_CELLSIZE = FONT_SMALL.getHeight()/2;
	private final static int MAX_CELLSIZE = FONT_LARGE.getHeight()*2;
	
	private Displayable parent;
	
	private boolean damagedProblem;
	private String currentProblemDescription;
	
	private static Random random = new Random((new Date()).getTime());
	private int problemIndex = 0;
	private ProblemsCollection collection;
	private Problem problem;
	private Goban goban;
	private ProblemNavigator problemNavigator;

	private Font boardFont;
	private int cellSize;
	private int stoneRadius;
	
	private Point[] hoshi;
	
	private int xOffset;
	private int yOffset;
	private int gobanSize;
	private Command cmdUndo;
	private Command cmdComment;
	private Command cmdNext;
	private Command cmdPrevious;
	private Command cmdRandom;
	private Command cmdSelectProblem;
	private Command cmdShowHints;
	private Command cmdHideHints;
	private Command cmdMainMenu;
	private Command cmdInfo;
	private boolean isCmdCommentVisible = false;
	private boolean isCmdUndoVisible = false;
	private Point cursor;
	
	private boolean showHints = false;
	private boolean showLabels = true;

	private Rectangle canvasRect;
	private int canvasWidthInCells;
	private int canvasHeightInCells;

	public GobanCanvas(Displayable parent) {
		problemNavigator = new ProblemNavigator();
		this.parent = parent;
		cmdUndo = new Command(LocalizedStrings.getResource(RES_CMD_UNDO), Command.BACK, 1);
		cmdComment = new Command(LocalizedStrings.getResource(RES_CMD_COMMENT), Command.SCREEN, 2);
		cmdNext = new Command(LocalizedStrings.getResource(RES_CMD_NEXT), Command.SCREEN, 3);
		cmdPrevious = new Command(LocalizedStrings.getResource(RES_CMD_PREVIOUS), Command.SCREEN, 4);
		cmdRandom = new Command(LocalizedStrings.getResource(RES_CMD_RANDOM), Command.SCREEN, 5);
		cmdSelectProblem = new Command(LocalizedStrings.getResource(RES_CMD_SELECT_PROBLEM), Command.SCREEN, 6);
		cmdShowHints = new Command(LocalizedStrings.getResource(RES_CMD_SHOW_HINTS), Command.SCREEN, 7);
		cmdHideHints = new Command(LocalizedStrings.getResource(RES_CMD_HIDE_HINTS), Command.SCREEN, 7);
		cmdInfo = new Command(LocalizedStrings.getResource(ProblemInfoForm.TITLE), Command.SCREEN, 8);
		cmdMainMenu = new Command(LocalizedStrings.getResource(AbstractMainForm.RES_TITLE), Command.SCREEN, 9);
		
		setCommandListener(this);
		addCommand(cmdNext);
		addCommand(cmdPrevious);
		addCommand(cmdRandom);
		addCommand(cmdSelectProblem);
		addCommand(cmdInfo);
		addCommand(cmdMainMenu);
	}
	
	protected void keyPressed(int code) {
		// MidletUtils.showMessage("keyPressed", String.valueOf(code));
		if ((code == KEY_NUM2 || code == -1) && cursor.y > 0) {
			// up
			moveCursor(0, -1);			
		} else if ((code == KEY_NUM4 || code == -3) && cursor.x > 0) {
			// left
			moveCursor(-1, 0);
		} if ((code == KEY_NUM6 || code == -4) && cursor.x < gobanSize - 1) {
			// right
			moveCursor(1, 0);
		} else if ((code == KEY_NUM8 || code == -2) &&  cursor.y < gobanSize - 1) {
			// down
			moveCursor(0, 1);
		} else if (code == KEY_NUM5 || code == -5 || code == 57 /* nokia */ || code == 42 /* nokia */) {
			if (problem != null && goban.isMoveAllowed(cursor, problem.playerColor)) {
				try {
					problemNavigator.move(cursor);
				} catch (GoAwayException e) {
					MidletUtils.showError(e);
					damagedProblem = true;
				}
				initNode();
			}
		} else if ((code == KEY_NUM1 || code == 55/* nokia */) && (canvasRect.getWidth() < gobanSize || canvasRect.getHeight() < gobanSize)) {
			zoom(cellSize - 2);
		} else if (code == KEY_NUM3 || code == 35/* nokia */) {
			zoom(cellSize + 2);
		} else {
			super.keyPressed(code);
		}
		repaint();
	}
	
	private void drawMessages(Graphics g, String[] messages) {
		g.setColor(COLOR_BLACK);
		int letterHeight = g.getFont().getHeight();
		int y = getHeight() / 2 - (letterHeight + 2) * messages.length / 2;
		for (int i = 0; i < messages.length; ++i) {
			g.drawString(
					messages[i],
					getWidth()/2,
					y,
					Graphics.BASELINE | Graphics.HCENTER
				);
			y += letterHeight + 2;
		}
	}

	protected void paint(Graphics g) {
		try {
		
			g.setColor(COLOR_BOARD);
			g.fillRect(0, 0, getWidth(), getHeight());
			String[] msg = null;
			if (damagedProblem) {
				msg = new String[] {
					LocalizedStrings.getResource(RES_DAMAGED_FILE) + ":",
					currentProblemDescription
				};				
			} else if (problem == null) {
				msg = new String[] {
					LocalizedStrings.getResource(RES_PROBLEM_LOADING),
					currentProblemDescription
				};
			}
			
			if (msg != null) {
				drawMessages(g, msg);
				return;
			}
			
			drawGrid(g);
			for(int i = canvasRect.minX; i <= canvasRect.maxX; ++i) {
				for (int j = canvasRect.minY; j <= canvasRect.maxY; ++j) {
					byte c = goban.getPointColor(i, j); 
					switch (c) {
					case Stone.STONE_NONE:
						continue;
					case Stone.STONE_BLACK:
						g.setColor(COLOR_BLACK);
						break;
					case Stone.STONE_WHITE:
						g.setColor(COLOR_WHITE);
						break;
					}
					fillCircle(g, i, j, stoneRadius);
				}
			}
			
			Node current = problemNavigator.getCurrentNode();
			
			if (showHints) {
				drawHints(g, current.getChildren());
			}
			
			if (showLabels) {
				drawLabels(g, current);
			}
	
			// draw cursor
			g.setColor(COLOR_RIGHT);
			int x = getCanvasX(cursor.x),
				y = getCanvasY(cursor.y);
			g.drawRect(x - stoneRadius, y - stoneRadius, cellSize, cellSize);		
			
			drawNotificationArea(g, current);
		} catch (Exception e) {
			e.printStackTrace();
			MidletUtils.showError(e);			
		}
	}	
	
	private int getCanvasX(int gobanX) {
		return xOffset + (gobanX - canvasRect.minX) * cellSize + stoneRadius;
	}
	
	private int getCanvasY(int gobanY) {
		return yOffset + (gobanY - canvasRect.minY) * cellSize + stoneRadius;
	}
	
	private void drawLabels(Graphics g, Node current) {
		g.setFont(boardFont);
		Vector labels = current.getLabels();
		if (labels != null) {
			for(int i = 0; i < labels.size(); ++i) {
				Label lbl = (Label)labels.elementAt(i);
				if (!canvasRect.contain(lbl.getPoint())) {
					continue;
				}
				
				int x = lbl.getPoint().x,
					y = lbl.getPoint().y;
				
				switch(goban.getPointColor(x, y)) {
				case Stone.STONE_NONE:
					g.setColor(COLOR_WHITE);
					break;
				case Stone.STONE_BLACK:
					g.setColor(COLOR_WHITE);
					break;
				default: 
					g.setColor(COLOR_BLACK);
				}
				switch (lbl.getType()) {				
				case Label.LABEL_TEXT:
					char c = lbl.getLabel().charAt(0);
					g.drawChar(c, getCanvasX(x) - boardFont.charWidth(c)/2, getCanvasY(y) + boardFont.getHeight()/2, Graphics.BOTTOM | Graphics.LEFT);
					break;
				case Label.LABEL_CIRCLE:
					drawCircle(g, x, y, stoneRadius/2);
					break;
				case Label.LABEL_SQUARE:
					drawSquare(g, x, y, stoneRadius);
					break;
				case Label.LABEL_TRIANGLE:
					drawTriangle(g, x, y);
					break;					
				case Label.LABEL_MARK:
					drawCross(g, x, y);
					break;
				}
			}
		}		
	}
	
	private void drawNotificationArea(Graphics g, Node current) {
		if (problem.playerColor == Stone.STONE_BLACK) {
			g.setColor(COLOR_BLACK);
		} else {
			g.setColor(COLOR_WHITE);
		}
		g.fillArc(1, getHeight() - NOTIFICATION_AREA_HEIGHT + 1, NOTIFICATION_AREA_HEIGHT - 2, NOTIFICATION_AREA_HEIGHT - 2, 0, 360);

		StringBuffer stNotify = new StringBuffer();
		stNotify.append(problemIndex + 1)
			.append('/')
			.append(collection.size());

		if (problemNavigator.getState() == ProblemNavigator.STATE_SOLVED) {
			stNotify.append(' ').append(LocalizedStrings.getResource(RES_NOTIFY_SOLVED));
			g.setColor(COLOR_RIGHT);
		} else if (problemNavigator.getState() == ProblemNavigator.STATE_WRONG) {
			stNotify.append(' ').append(LocalizedStrings.getResource(RES_NOTIFY_WRONG));
			g.setColor(COLOR_WRONG);
		} else {
			g.setColor(COLOR_BLACK);
		}
		
		if (current.getComment() != null) {			
			stNotify.append(' ').append(current.getComment());
		}
		String st = stNotify.toString();
		g.setFont(FONT_MEDIUM);
		g.drawString(st, NOTIFICATION_AREA_HEIGHT + 2, getHeight() - NOTIFICATION_AREA_HEIGHT, Graphics.TOP | Graphics.LEFT);
	}
	
	private void drawHints(Graphics g, Vector nextMoves) {
		for(int i = 0; i < nextMoves.size(); ++i) {
			Node n = (Node)nextMoves.elementAt(i);
			MoveAction action = (MoveAction)n.getAction();
			
			if (action.isPass() || !canvasRect.contain(action.getPoint())) {
				continue;
			}
			
			if (n.isRight()) {
				g.setColor(COLOR_RIGHT);
			} else {
				g.setColor(COLOR_WRONG);
			}			
			fillCircle(g, action.getPoint().x, action.getPoint().y, stoneRadius/2);
		}
	}
	
	private void drawGrid(Graphics g) {
		g.setColor(COLOR_BLACK);
 		
		int d1 = getCanvasX(canvasRect.minX),
			d2 = getCanvasX(canvasRect.maxX);
		
		if (canvasRect.minX != 0) {
			d1 -= stoneRadius;
		}
		
		if (canvasRect.maxX < gobanSize - 1) {
			d2 += stoneRadius;
		}
		
		for(int c = canvasRect.minY; c <= canvasRect.maxY; ++c) {
			int y = getCanvasY(c);
			g.drawLine(d1, y, d2, y);			
		}
		
		d1 = getCanvasY(canvasRect.minY);
		d2 = getCanvasY(canvasRect.maxY);
		
		if (canvasRect.minY != 0) {
			d1 -= stoneRadius;
		}
		
		if (canvasRect.maxY < gobanSize - 1) {
			d2 += stoneRadius;
		}		
		
		for(int c = canvasRect.minX; c <= canvasRect.maxX; ++c) {
			int x = getCanvasX(c);
			g.drawLine(x, d1, x, d2);
		}
		
		if (hoshi != null) {
			for (int i = 0; i < hoshi.length; ++i) {
				Point pt = hoshi[i];
				if (canvasRect.contain(pt)) {
					fillCircle(g, pt.x, pt.y, stoneRadius/3);
				}
			}
		}
	}
	
	private void initNode() {
		Node node = problemNavigator.getCurrentNode();
		
		boolean need = node.getComment() != null;
		if (need != isCmdCommentVisible) {
			if (need) {
				addCommand(cmdComment);
			} else {
				removeCommand(cmdComment);
			}
			isCmdCommentVisible = need;
		}
		need = (node.getParent() != null);
		if (need != isCmdUndoVisible) {
			if (need) {
				addCommand(cmdUndo);
			} else {
				removeCommand(cmdUndo);
			}
			isCmdUndoVisible = need;
		}
	}

	private void drawTriangle(Graphics g, int x, int y) {
		int cX = getCanvasX(x),
			cY = getCanvasY(y),
			d = stoneRadius/2;
			
		g.drawLine(cX - d, cY + d, cX, cY - d);
		g.drawLine(cX - d, cY + d, cX + d, cY + d);
		g.drawLine(cX, cY - d, cX + d, cY + d);
	}
	
	private void drawCross(Graphics g, int x, int y) {
		int cX = getCanvasX(x),
			cY = getCanvasY(y),
			d = stoneRadius/2;
		g.drawLine(cX - d, cY -d, cX + d, cY + d);
		g.drawLine(cX + d, cY -d, cX - d, cY + d);
	}
	
	private void drawSquare(Graphics g, int x, int y, int size) {
		g.drawRect(getCanvasX(x) - size/2, getCanvasY(y) - size/2, size, size);
	}	
	
	private void drawCircle(Graphics g, int x, int y, int radius) {
		g.drawArc(getCanvasX(x) - radius, getCanvasY(y) - radius, 2*radius, 2*radius, 0, 360);
	}
	
	private void fillCircle(Graphics g, int x, int y, int radius) {
		g.fillArc(getCanvasX(x) - radius, getCanvasY(y) - radius, 2*radius, 2*radius, 0, 360);
	}
	
	public void requestProblem() {
		damagedProblem = false;
		problem = null;
		goban = null;
		if (problemIndex < collection.size() && problemIndex >= 0) {
			currentProblemDescription = collection.getProblemTitle(problemIndex);
			collection.requestProblem(problemIndex);	
		}		
		repaint();
	}
		
	private void showComment() {
		if (problemNavigator.getCurrentNode().getComment() != null && MidletUtils.isVisible(this)) {
			MidletUtils.showMessage(
				LocalizedStrings.getResource(RES_CMD_COMMENT),
				problemNavigator.getCurrentNode().getComment()
			);
		}		
	}

	public void commandAction(Command cmd, Displayable canvas) {
		if (cmd == cmdUndo) {
			try {
				problemNavigator.undo();
				initNode();
			} catch (GoAwayException e) {
				MidletUtils.showError(e);
				damagedProblem = true;
			}
			repaint();
		} else if (cmd == cmdComment) {
			showComment();
		} else if (cmd == cmdShowHints) {
			removeCommand(cmdShowHints);
			addCommand(cmdHideHints);
			showHints = true;
			repaint();
		} else if (cmd == cmdHideHints) {
			removeCommand(cmdHideHints);
			addCommand(cmdShowHints);
			showHints = false;
			repaint();
		} else if (cmd == cmdNext) {
			if (problemIndex >= collection.size() - 1) {
				problemIndex = 0;
			} else {
				++problemIndex; 
			}
			requestProblem();
		} else if (cmd == cmdPrevious) {
			if (problemIndex > 0) {
				--problemIndex;
			} else {
				problemIndex = collection.size() - 1;
			}
			requestProblem();
		} else if (cmd == cmdRandom) {
			problemIndex = Math.abs(random.nextInt()) % collection.size();
			requestProblem();
		} else if (cmd == cmdSelectProblem) {
			ProblemSelectorForm f = new ProblemSelectorForm(this);
			MidletUtils.show(f);
		} else if (cmd == cmdInfo) { 
			if (problem != null) {
				ProblemInfoForm f = new ProblemInfoForm(this, problem);
				MidletUtils.show(f);
			}
		} else if (cmd == cmdMainMenu) {
			MidletUtils.show(parent);
		}
	}
	
	public void setProblemsCollection(ProblemsCollection val) {		
		collection = val;
		onCollectionChanged();
	}

	public void onCollectionChanged() {
		if (problemIndex >= collection.size()) {
			problemIndex = 0;
		}
		requestProblem();
	}

	public void onProblemLoadingFailed(Exception e) {
		damagedProblem = true;
		MidletUtils.showError(e);		
	}

	public void onProblemLoaded(Problem p, int problemIndex) {
		try {
		problem = p;
		this.problemIndex = problemIndex;		
		try {
			problemNavigator.setProblem(p);
		} catch (GoAwayException e) {
			MidletUtils.showError(e);
			damagedProblem = true;
		}
		if (problem == null) {
			cursor = new Point(0, 0);
			removeCommand(cmdHideHints);
			removeCommand(cmdShowHints);			
			repaint();
		}
		
		goban = problemNavigator.getGoban();
		gobanSize = goban.getSize();
		

		Rectangle problemDimensions = getProblemRectangle(p, 2);
		cursor = problemDimensions.getCenter();

		zoom(getCellSize(problemDimensions));

		hoshi = getStarPoints(gobanSize);
		commandAction(cmdHideHints, this);
		initNode();
		showComment();
		repaint();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private int getCellSize(Rectangle rect) {
		int width = getWidth(),
			height = getHeight() - NOTIFICATION_AREA_HEIGHT;
		int dx = width/(rect.getWidth()),
			dy = height/(rect.getHeight());
		return dx < dy ? dx : dy;
	}
	
	private void zoom(int newCellSize) {
		if (newCellSize < MIN_CELLSIZE) {
			cellSize = MIN_CELLSIZE;
		} else if (newCellSize > MAX_CELLSIZE) {
			cellSize = MAX_CELLSIZE;
		} else {
			cellSize = newCellSize;
		}
		
		// cellSize must be odd number
		if (cellSize%2 == 0) {
			cellSize -= 1;
		}
		// stone radius is a half of cellSize truncated to lower int 
		stoneRadius = cellSize/2;
		
		// label font must be small enough to fit into board cell
		// otherwise labels to be disabled
		if (FONT_LARGE.getHeight() < cellSize) {
			boardFont = FONT_LARGE;
		} else if (FONT_MEDIUM.getHeight() < cellSize) {
			boardFont = FONT_MEDIUM;
		} else {
			boardFont = FONT_SMALL;
		}
		showLabels = cellSize >= MIN_LABEL_CELLSIZE;

		int width = getWidth(),
			height = getHeight() - NOTIFICATION_AREA_HEIGHT;
		
		canvasWidthInCells = width/cellSize;
		if (canvasWidthInCells > gobanSize) {
			canvasWidthInCells = gobanSize;
		}
		
		canvasHeightInCells = height/cellSize;
		if (canvasHeightInCells > gobanSize) {
			canvasHeightInCells = gobanSize;
		}
		canvasRect = new Rectangle();
		canvasRect.minX = cursor.x - canvasWidthInCells/2;
		canvasRect.maxX = canvasRect.minX + canvasWidthInCells - 1;
		canvasRect.minY = cursor.y - canvasHeightInCells/2;
		canvasRect.maxY = canvasRect.minY + canvasHeightInCells - 1;
		
		int dx = 0,
			dy = 0;
		if (canvasRect.minX < 0) {
			dx = -canvasRect.minX;
		} else if (canvasRect.maxX > gobanSize - 1) {
			dx = gobanSize - 1 - canvasRect.maxX;
		}
		if (canvasRect.minY < 0) {
			dy = -canvasRect.minY;
		} else if (canvasRect.maxY > gobanSize - 1) {
			dy = gobanSize - 1 - canvasRect.maxY;
		}
		canvasRect.move(dx, dy);
		xOffset = (width - cellSize*canvasWidthInCells)/2;
		yOffset = (height - cellSize*canvasHeightInCells)/2;
	}	
	
	private void moveCursor(int dX, int dY) {
		cursor.x += dX;
		cursor.y += dY;
		canvasRect.moveToIncludePoint(cursor);
	}

	public void onCollectionError(Exception e) {
		problem = null;
		MidletUtils.showError(e);
	}

	public int getProblemIndex() {
		return problemIndex;
	}
	
	public void setProblemIndex(int problemIndex) {
		this.problemIndex = problemIndex;
	}
	
	public int getCollectionSize() {
		return collection.size();
	}
	
	private static Rectangle getProblemRectangle(Problem p, int margin) {
		int gobanSize = p.getSize();
		Rectangle problemDimensions = new Rectangle(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
		Vector nodes = p.getNodeList();
		for (int i = 0; i < nodes.size(); ++i) {
			Node n = (Node)nodes.elementAt(i);
			Vector labels = n.getLabels();
			if (labels != null) {
				for (int j = 0; j < labels.size(); ++j) {
					Label l = (Label)labels.elementAt(j);
					problemDimensions.resizeToIncludePoint(l.getPoint());
				}
			}
			if (n.getAction() instanceof EditorAction) {
				EditorAction a = (EditorAction)n.getAction();
				Vector stones = a.getAddedBlackStones();
				for (int j = 0; j < stones.size(); ++j) {
					problemDimensions.resizeToIncludePoint((Point)stones.elementAt(j));
				}
				stones = a.getAddedWhiteStones();
				for (int j = 0; j < stones.size(); ++j) {
					problemDimensions.resizeToIncludePoint((Point)stones.elementAt(j));
				}				
			}
			if (n.getAction() instanceof MoveAction) {
				MoveAction a = (MoveAction)n.getAction();
				problemDimensions.resizeToIncludePoint(a.getPoint());
			}
		}
		
		if (problemDimensions.minX <= margin) {
			problemDimensions.minX = 0;
		} else {
			problemDimensions.minX -= margin;
		}
		
		if (problemDimensions.maxX > gobanSize - margin - 1) {
			problemDimensions.maxX = gobanSize - 1;
		} else {
			problemDimensions.maxX += margin;
		}

		if (problemDimensions.minY <= margin) {
			problemDimensions.minY = 0;
		} else {
			problemDimensions.minY -= margin;
		}
		
		if (problemDimensions.maxY > gobanSize - margin - 1) {
			problemDimensions.maxY = gobanSize - 1;
		} else {
			problemDimensions.maxY += margin;
		}
		return problemDimensions;
	}
	
	private static Point[] getStarPoints(int gobanSize) {
		if (gobanSize < 7) {
			return null;
		}
		boolean drawTengen = gobanSize >= 13 && gobanSize%2 == 1;
		Point[] hoshi;
		if (drawTengen) {
			hoshi = new Point[9];	
		} else {
			hoshi = new Point[4];	
		}

		int d1 = 3;				
		if (gobanSize <= 13) {
			d1 = 2;
		}
		int d2 = gobanSize - 1 - d1;
		hoshi[0] = new Point(d1, d1);
		hoshi[1] = new Point(d1, d2);
		hoshi[2] = new Point(d2, d1);
		hoshi[3] = new Point(d2, d2);
		if (drawTengen) {
			int d3 = gobanSize/2;					 
			hoshi[4] = new Point(d1, d3);
			hoshi[5] = new Point(d3, d1);
			hoshi[6] = new Point(d3, d3);
			hoshi[7] = new Point(d2, d3);
			hoshi[8] = new Point(d3, d2);					
		}
		return hoshi;
	}
}