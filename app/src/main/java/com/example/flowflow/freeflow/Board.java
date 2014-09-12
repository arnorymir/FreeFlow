package com.example.flowflow.freeflow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.List;

/**
 * Created by bjornorri on 12/09/14.
 */
public class Board extends View {

    // Dimensions of the board
    private int mSize;

    // Cell dimensions
    private int mCellWidth;
    private int mCellHeight;

    private Rect mRect = new Rect();
    private Paint mPaintGrid = new Paint();
    private Paint mPaintPath = new Paint();
    private Path mPath = new Path();

    private CellPath mCellPath = new CellPath();

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        //mSize = size;
        mPaintGrid.setStyle(Paint.Style.STROKE);
        mPaintGrid.setColor(Color.GRAY);
        mPaintPath.setStyle(Paint.Style.STROKE);
        mPaintPath.setColor(Color.GREEN);
        mPaintPath.setStrokeWidth(32);
        mPaintPath.setStrokeCap(Paint.Cap.ROUND);
        mPaintPath.setStrokeJoin(Paint.Join.ROUND);
        mPaintPath.setAntiAlias(true);
    }

    public void setSize(int size) {
        mSize = size;
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
        mPath.reset();
        if (!mCellPath.isEmpty()) {
            List<Coordinate> colist = mCellPath.getCoordinates();
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
}
