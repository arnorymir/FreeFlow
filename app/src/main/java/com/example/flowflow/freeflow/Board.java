package com.example.flowflow.freeflow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.List;

/**
 * Created by bjornorri on 12/09/14.
 */
public class Board extends View {

    private int[] colors = {Color.BLUE, Color.RED, Color.GREEN};

    // Dimensions of the board
    private int mSize;

    // Cell dimensions
    private int mCellWidth;
    private int mCellHeight;

    private Rect mRect = new Rect();
    private Paint mPaintGrid = new Paint();
    private Paint mPaintDots = new Paint();
    private Paint mPaintPath = new Paint();
    private Path mPath = new Path();


    private Dot[] mDots;
    private CellPath[] mCellPaths;

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
            mCellPaths[i] = new CellPath();
        }
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
        for (Dot dot : mDots) {
            drawDot(canvas, dot);
        }
        mPath.reset();
        if (!mCellPaths[0].isEmpty()) {
            List<Coordinate> colist = mCellPaths[0].getCoordinates();
            Coordinate co = colist.get(0);
            mPath.moveTo(colToX(co.getCol()) + mCellWidth / 2,
                    rowToY(co.getRow()) + mCellHeight / 2);
            for (int i = 1; i < colist.size(); i++) {
                co = colist.get(i);
                mPath.lineTo(colToX(co.getCol()) + mCellWidth / 2,
                        rowToY(co.getRow()) + mCellHeight / 2 );
            }
        }
        canvas.drawPath(mPath, mPaintPath);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int c = xToCol(x);
        int r = yToRow(y);

        if (c >= mSize || r >= mSize) {
            return true;
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Coordinate coordinate = new Coordinate(c, r);
            Dot dot = getDotAtCoordinate(coordinate);
            if(dot != null) {
                mPaintPath.setColor(colors[dot.getColorID()]);
                mCellPaths[0].reset();
                mCellPaths[0].append(new Coordinate(c, r));
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (!mCellPaths[0].isEmpty()) {
                List<Coordinate> coordinateList = mCellPaths[0].getCoordinates();
                Coordinate last = coordinateList.get(coordinateList.size()-1);
                if (areNeighbours(last.getCol(),last.getRow(), c, r)) {
                    mCellPaths[0].append(new Coordinate(c, r));
                    invalidate();
                }
            }
        }
        return true;
    }

    private void drawDot(Canvas canvas, Dot dot) {
        mPaintDots.setColor(colors[dot.getColorID()]);
        Coordinate c = dot.getCell();
        canvas.drawCircle(colToX(c.getCol()) + mCellWidth / 2, rowToY(c.getRow()) + mCellHeight / 2,
                mCellWidth / 3, mPaintDots);
    }

    private boolean areNeighbours(int c1, int r1, int c2, int r2) {
        return Math.abs(c1 - c2) + Math.abs(r1 - r2) == 1;
    }

    public Dot getDotAtCoordinate(Coordinate c) {
        for(Dot dot : mDots) {
            if(dot.getCell().equals(c)) {
                return dot;
            }
        }
        return null;
    }
}
