package com.example.flowflow.freeflow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.List;

/**
 * Created by bjornorri on 12/09/14.
 */
public class Board extends View {

    private int[] colors = {Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW, Color.MAGENTA, Color.DKGRAY, Color.CYAN};

    // Dimensions of the board
    private int mSize;

    // Cell dimensions
    private int mCellWidth;
    private int mCellHeight;

    private Rect mRect = new Rect();
    private Paint mPaintGrid = new Paint();
    private Paint mPaintDots = new Paint();
    private Paint mPaintPath = new Paint();

    private Dot[] mDots;
    private CellPath[] mCellPaths;
    private CellPath mActiveCellPath = null;

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintGrid.setStyle(Paint.Style.STROKE);
        mPaintGrid.setColor(Color.GRAY);
        mPaintDots.setStyle(Paint.Style.FILL);
        mPaintPath.setStyle(Paint.Style.STROKE);
        mPaintPath.setColor(Color.GREEN);
        mPaintPath.setStrokeWidth(32);
        mPaintPath.setStrokeCap(Paint.Cap.ROUND);
        mPaintPath.setStrokeJoin(Paint.Join.ROUND);
        mPaintPath.setAntiAlias(true);
    }

    public void setPuzzle(Puzzle puzzle) {
        mSize = puzzle.getSize();
        mDots = puzzle.getDots();
        mCellPaths = new CellPath[puzzle.getNumberOfColors()];
        for(int i = 0; i < mCellPaths.length; i++) {
            mCellPaths[i] = new CellPath(i);
        }
    }

    // Returns the number of cells occupied by cell paths.
    public int numberOfOccupiedCells() {
        int count = 0;
        for(CellPath cellPath : mCellPaths) {
            count += cellPath.length();
        }
        return count;
    }

    // Methods to map screen coordinates to grid cells
    private int xToCol(int x) {
        return (x - getPaddingLeft()) / mCellWidth;
    }

    private int yToRow(int y) {
        return (y - getPaddingTop()) / mCellHeight;
    }

    // Methods to map grid cell coordinates to screen coordinates
    private int colToX(int col) {
        return col * mCellWidth + getPaddingLeft();
    }

    private int rowToY(int row) {
        return row * mCellHeight + getPaddingTop();
    }

    // Overridden View methods

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
        int height = getMeasuredHeight() - getPaddingTop() - getPaddingBottom();
        int size = Math.min(width, height);
        setMeasuredDimension(size + getPaddingLeft() + getPaddingRight(), size + getPaddingTop() + getPaddingBottom());
    }

    @Override
    protected void onSizeChanged(int xNew, int yNew, int xOld, int yOld) {
        int strokeWidth = Math.max(1, (int) mPaintGrid.getStrokeWidth());
        mCellWidth = (xNew - getPaddingLeft() - getPaddingRight() - strokeWidth) / mSize;
        mCellHeight = (yNew - getPaddingTop() - getPaddingBottom() - strokeWidth) / mSize;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (int r = 0; r < mSize; r++) {
            for (int c = 0; c < mSize; c++) {
                int x = colToX(c);
                int y = rowToY(r);
                mRect.set(x, y, x + mCellWidth, y + mCellHeight);
                canvas.drawRect(mRect, mPaintGrid);
            }
        }

        // Draw the dots.
        for(Dot dot : mDots) {
            drawDot(canvas, dot);
        }

        // Draw the cell paths.
        for(CellPath cellPath : mCellPaths) {
            drawCellPath(canvas, cellPath);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int c = xToCol(x);
        int r = yToRow(y);

        if (c >= mSize || r >= mSize || c < 0 || r < 0) {
            return true;
        }
        Coordinate coordinate = new Coordinate(c, r);

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            CellPath cellPath = getCellPathAtCoordinate(coordinate);
            if(cellPath != null) {
                if(containsDot(coordinate)) {
                    cellPath.reset();
                }
                else {
                    cellPath.popPastCoordinate(coordinate);
                }
                cellPath.append(new Coordinate(c, r));
                mActiveCellPath = cellPath;
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (mActiveCellPath != null) {
                if (!mActiveCellPath.isEmpty()) {
                    if(moveIsAllowed(coordinate)) {
                        CellPath cellPathAtCoordinate = getCellPathAtCoordinate(coordinate);
                        if(cellPathAtCoordinate == null) {
                            mActiveCellPath.append(coordinate);
                        }
                        else {
                            // Check if the cell contains the end dot.
                            mActiveCellPath.append(coordinate);
                            Dot dotAtCoordinate = getDotAtCoordinate(coordinate);
                            if(dotAtCoordinate != null && !coordinate.equals(mActiveCellPath.getFirstCoordinate())) {
                                mActiveCellPath = null;
                            }
                            else {
                                cellPathAtCoordinate.popPastCoordinate(coordinate);
                            }
                        }
                    }
                }
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                mActiveCellPath = null;

            }
        }
        invalidate();
        ((PlayActivity)getContext()).update();
        return true;
    }

    private void drawDot(Canvas canvas, Dot dot) {
        mPaintDots.setColor(colors[dot.getColorID()]);
        Coordinate c = dot.getCell();
        canvas.drawCircle(colToX(c.getCol()) + mCellWidth / 2, rowToY(c.getRow()) + mCellHeight / 2,
                mCellWidth / 3, mPaintDots);
    }

    private void drawCellPath(Canvas canvas, CellPath cellPath) {
        if (!cellPath.isEmpty()) {
            Path path = new Path();
            List<Coordinate> colist = cellPath.getCoordinates();
            Coordinate co = colist.get(0);
            path.moveTo(colToX(co.getCol()) + mCellWidth / 2,
                    rowToY(co.getRow()) + mCellHeight / 2);
            for (int i = 1; i < colist.size(); i++) {
                co = colist.get(i);
                path.lineTo(colToX(co.getCol()) + mCellWidth / 2,
                        rowToY(co.getRow()) + mCellHeight / 2 );
            }
            mPaintPath.setColor(colors[cellPath.getColorID()]);
            canvas.drawPath(path, mPaintPath);
        }
    }

    private boolean areNeighbours(Coordinate c1, Coordinate c2) {
        return Math.abs(c1.getCol() - c2.getCol()) + Math.abs(c1.getRow() - c2.getRow()) == 1;
    }

    private Dot getDotAtCoordinate(Coordinate c) {
        for(Dot dot : mDots) {
            if(dot.getCell().equals(c)) {
                return dot;
            }
        }
        return null;
    }

    private CellPath getCellPathForColorID(int colorID) {
        return mCellPaths[colorID];
    }

    private CellPath getCellPathAtCoordinate(Coordinate coordinate) {

        // If the coordinate is in a cell path, return that cell path.
        for(CellPath cellPath : mCellPaths) {
            List<Coordinate> coordinates = cellPath.getCoordinates();
            if(coordinates.contains(coordinate)) {
                return cellPath;
            }
        }

        // If the coordinate contains a dot, start a cell path for that color.
        Dot dot = getDotAtCoordinate(coordinate);
        if(dot != null) {
            int colorID = dot.getColorID();
            return getCellPathForColorID(colorID);
        }
        return null;
    }

    private boolean containsDot(Coordinate coordinate) {
        for(Dot dot : mDots) {
            if(dot.getCell().equals(coordinate)) {
                return true;
            }
        }
        return false;
    }

    // Determine if the coordinate may be appended to the cell path.
    private boolean moveIsAllowed(Coordinate coordinate) {
        // Only allow moves to adjacent cells
        if(!areNeighbours(coordinate, mActiveCellPath.getLastCoordinate())) {
            return false;
        }

        // Don't allow move if cell contains a dot in another color.
        for(Dot dot : mDots) {
            if(dot.getCell().equals(coordinate) && dot.getColorID() != mActiveCellPath.getColorID()) {
                return false;
            }
        }
        return true;
    }
}
