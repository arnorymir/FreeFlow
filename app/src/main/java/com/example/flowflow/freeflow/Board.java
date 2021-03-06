package com.example.flowflow.freeflow;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Parcelable;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashSet;
import java.util.List;

/**
 * Created by bjornorri on 12/09/14.
 */
public class Board extends View {

    private int[] colors;

    // Dimensions of the board
    private int mSize;

    // Get the activity
    private Activity mActivity;

    // Cell dimensions
    private int mCellWidth;
    private int mCellHeight;

    // Sound effects
    private SoundEffects soundEffects = SoundEffects.getInstance();

    private Paint mPaintGrid = new Paint();
    private Paint mPaintDots = new Paint();
    private Paint mPaintPath = new Paint();
    private Paint mPaintShade = new Paint();
    private Paint mPaintCircle = new Paint();

    private Dot[] mDots;
    private CellPath[] mCellPaths;
    private CellPath mActiveCellPath = null;
    private Coordinate mFingerCircle = null;

    private Vibrator v;

    public Board(Context context, AttributeSet attrs) {
        super(context, attrs);
        mActivity = (Activity)getContext();
        mPaintGrid.setStyle(Paint.Style.STROKE);
        mPaintGrid.setColor(Color.GRAY);
        mPaintDots.setStyle(Paint.Style.FILL);
        mPaintPath.setStyle(Paint.Style.STROKE);
        mPaintPath.setStrokeCap(Paint.Cap.ROUND);
        mPaintPath.setStrokeJoin(Paint.Join.ROUND);
        mPaintPath.setAntiAlias(true);
        mPaintShade.setStyle(Paint.Style.FILL);
        mPaintCircle.setStyle(Paint.Style.FILL);
        soundEffects.loadSounds(context);
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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
        // If no path is active simply count the length of the paths
        if(mActiveCellPath == null) {
            for(CellPath cellPath : mCellPaths) {
                count += cellPath.length();
            }
        }
        // Otherwise use a more complex counting method.
        else {
            count += mActiveCellPath.length();
            List<Coordinate> activeCoordinates = mActiveCellPath.getCoordinates();
            for(CellPath cellPath : mCellPaths) {
                for(Coordinate c : cellPath.getCoordinates()) {
                    if(activeCoordinates.contains(c)) {
                        break;
                    }
                    else {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    // Methods to map screen coordinates to grid cells
    private int xToCol(int x) {
        return (int)Math.floor((float)(x - getPaddingLeft()) / mCellWidth);
    }

    private int yToRow(int y) {
        return (int)Math.floor((float)(y - getPaddingTop()) / mCellHeight);
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
        mPaintPath.setStrokeWidth((float)mCellWidth/4);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        // Draw the grid
        Rect rect = new Rect();
        for (int r = 0; r < mSize; r++) {
            for (int c = 0; c < mSize; c++) {
                int x = colToX(c);
                int y = rowToY(r);
                rect.set(x, y, x + mCellWidth, y + mCellHeight);
                canvas.drawRect(rect, mPaintGrid);
            }
        }

        // Draw the dots.
        for(Dot dot : mDots) {
            drawDot(canvas, dot);
        }

        // Draw the inactive cell paths.
        for(CellPath cellPath : mCellPaths) {
            if(cellPath != mActiveCellPath) {
                drawCellPath(canvas, cellPath);
            }
        }

        // Draw the active cell path.
        drawCellPath(canvas, mActiveCellPath);
        drawFingerCircle(canvas);
    }

    private void drawDot(Canvas canvas, Dot dot) {
        mPaintDots.setColor(colors[dot.getColorID()]);
        Coordinate c = dot.getCell();
        canvas.drawCircle(colToX(c.getCol()) + mCellWidth / 2, rowToY(c.getRow()) + mCellHeight / 2,
                mCellWidth / 3, mPaintDots);
    }

    private void drawCellPath(Canvas canvas, CellPath cellPath) {
        if(cellPath != null) {
            if (!cellPath.isEmpty()) {
                // Draw color shade in all the cells
                Rect rect = new Rect();
                if(cellPath != mActiveCellPath) {
                    for(Coordinate c : cellPath.getCoordinates()) {
                        int x = colToX(c.getCol());
                        int y = rowToY(c.getRow());
                        rect.set(x, y, x + mCellWidth, y + mCellHeight);
                        mPaintShade.setColor(colors[cellPath.getColorID()]);
                        mPaintShade.setAlpha(50);
                        canvas.drawRect(rect, mPaintShade);
                    }
                }

                // Draw the path itself
                Path path = new Path();
                List<Coordinate> colist = cellPath.getCoordinates();
                Coordinate co = colist.get(0);
                path.moveTo(colToX(co.getCol()) + mCellWidth / 2,
                        rowToY(co.getRow()) + mCellHeight / 2);

                for(int i = 0; i < colist.size(); i++) {
                    if(i > 0) {
                        co = colist.get(i);
                        path.lineTo(colToX(co.getCol()) + mCellWidth / 2,
                                rowToY(co.getRow()) + mCellHeight / 2);
                    }
                    if(mActiveCellPath != null) {
                        if(i < colist.size() - 1) {
                            Coordinate nextCo = colist.get(i + 1);
                            if(cellPath != mActiveCellPath && mActiveCellPath.contains(nextCo)) {
                                break;
                            }
                        }
                    }
                }
                mPaintPath.setColor(colors[cellPath.getColorID()]);
                canvas.drawPath(path, mPaintPath);
            }
        }
    }

    private void drawFingerCircle(Canvas canvas) {
        if(mFingerCircle != null && mActiveCellPath != null) {
            Rect newRect = canvas.getClipBounds();
            newRect.inset(-mCellWidth, -mCellWidth);  //make the rect larger
            canvas.clipRect(newRect, Region.Op.REPLACE);
            mPaintCircle.setColor(colors[mActiveCellPath.getColorID()]);
            mPaintCircle.setAlpha(50);
            canvas.drawCircle(mFingerCircle.getCol(), mFingerCircle.getRow(), mCellWidth, mPaintCircle);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        int c = xToCol(x);
        int r = yToRow(y);

        // The user can avoid the ACTION_UP event, catch this here.
        if(c >= mSize || r >= mSize || c < 0 || r < 0) {
            if(mActiveCellPath != null) {
                commitActiveCellPath();
                if(mActivity.getClass().equals(PlayActivity.class)) {
                    ((PlayActivity) mActivity).update();
                }
            }
            mFingerCircle = null;
            invalidate();
            // Need to invalidate the whole view to allow the finger circle to be drawn outside the board.
            if(mActivity.getClass().equals(PlayActivity.class)) {
                ((PlayActivity) mActivity).getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
            }
            else if(mActivity.getClass().equals(TimeTrialActivity.class)) {
                ((TimeTrialActivity) mActivity).getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
            }
            return true;
        }
        Coordinate coordinate = new Coordinate(c, r);
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            CellPath cellPath = getCellPathAtCoordinate(coordinate, true);
            if(cellPath != null) {
                if(containsDot(coordinate)) {
                    cellPath.reset();
                    cellPath.append(new Coordinate(c, r));
                    if(mActivity.getClass().equals(PlayActivity.class)) {
                        if(((PlayActivity)mActivity).getShouldVibrate()) v.vibrate(20);
                        ((PlayActivity)mActivity).updatePipe();
                    }
                    else if(mActivity.getClass().equals(TimeTrialActivity.class)) {
                        if(((TimeTrialActivity)mActivity).getShouldVibrate()) v.vibrate(20);
                    }
                }
                else {
                    cellPath.popToCoordinate(coordinate);
                    if(mActivity.getClass().equals(PlayActivity.class)) {
                        ((PlayActivity)mActivity).updatePipe();
                    }
                }
                mActiveCellPath = cellPath;
                mFingerCircle = new Coordinate(x, y);
            }
        }
        else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if(mActiveCellPath != null) {
                mFingerCircle = new Coordinate(x, y);
                if(!mActiveCellPath.isEmpty() && !coordinate.equals(mActiveCellPath.getLastCoordinate())) {
                    if(moveIsAllowed(coordinate)) {
                        // Pop the path if the cell is already in it.
                        if(mActiveCellPath.contains(coordinate)) {
                            mActiveCellPath.popToCoordinate(coordinate);
                            if(mActivity.getClass().equals(PlayActivity.class)) {
                                ((PlayActivity) mActivity).updatePipe();
                            }
                        }
                        // Otherwise append the cell.
                        else {
                            mActiveCellPath.append(coordinate);
                            if(mActivity.getClass().equals(PlayActivity.class)) {
                                ((PlayActivity) mActivity).updatePipe();
                            }
                            // If the cell contains the end dot, commit the path.
                            Dot dotAtCoordinate = getDotAtCoordinate(coordinate);
                            if(dotAtCoordinate != null && !coordinate.equals(mActiveCellPath.getFirstCoordinate())) {
                                soundEffects.sp.play(SoundEffects.soundIds[0], 1, 1, 1, 0, (float)1.2);
                                commitActiveCellPath();
                                if(mActivity.getClass().equals(PlayActivity.class)) {
                                    if(((PlayActivity)mActivity).getShouldVibrate()) v.vibrate(20);
                                    ((PlayActivity) mActivity).addMove();
                                    ((PlayActivity) mActivity).update();
                                }
                                // If it's a time trial, check if the puzzle is solved an notify the activity.
                                else if(mActivity.getClass().equals(TimeTrialActivity.class)) {
                                    if(((TimeTrialActivity)mActivity).getShouldVibrate()) v.vibrate(20);
                                    if(numberOfOccupiedCells() == mSize * mSize) {
                                        ((TimeTrialActivity)mActivity).solvedPuzzle();
                                    }
                                }
                                mFingerCircle = null;
                            }
                        }
                    }
                }
            }
        }
        else if(event.getAction() == MotionEvent.ACTION_UP) {
            if(mActiveCellPath != null) {
                commitActiveCellPath();
                if(mActivity.getClass().equals(PlayActivity.class)) {
                    ((PlayActivity) mActivity).update();
                }
            }
        }
        // Must always invalidate to keep finger circle smooth.
        invalidate();
        // Need to invalidate the whole view to allow the finger circle to be drawn outside the board.
        if(mActivity.getClass().equals(PlayActivity.class)) {
            ((PlayActivity) mActivity).getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        }
        else if(mActivity.getClass().equals(TimeTrialActivity.class)) {
            ((TimeTrialActivity) mActivity).getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
        }
        return true;
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

    private CellPath getCellPathAtCoordinate(Coordinate coordinate, boolean ignoreActive) {

        // If the coordinate is in a cell path, return that cell path.
        for(CellPath cellPath : mCellPaths) {
            if(!(ignoreActive && cellPath == mActiveCellPath)) {
                List<Coordinate> coordinates = cellPath.getCoordinates();
                if(coordinates.contains(coordinate)) {
                    return cellPath;
                }
            }
        }

        // If the coordinate contains a dot, start a cell path for that color.
        Dot dot = getDotAtCoordinate(coordinate);
        if(dot != null) {
            int colorID = dot.getColorID();
            CellPath cellPath = getCellPathForColorID(colorID);
            if(!(ignoreActive && cellPath == mActiveCellPath)) {
                return cellPath;
            }
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

    private void commitActiveCellPath() {
        if(mActiveCellPath != null) {
            for(Coordinate c : mActiveCellPath.getCoordinates()) {
                CellPath intersectingPath = getCellPathAtCoordinate(c, true);
                if(intersectingPath != null) {
                    intersectingPath.popPastCoordinate(c);
                    if(mActivity.getClass().equals(PlayActivity.class)) {
                        ((PlayActivity) mActivity).updatePipe();
                    }
                }
            }
            mActiveCellPath = null;
        }
    }

    public void reset() {
        for(CellPath cellPath : mCellPaths) {
            cellPath.reset();
        }
        mActiveCellPath = null;
        mFingerCircle = null;
    }

    public void setColorScheme(String colorScheme) {
        if(colorScheme.equals("Rainbow")) {
            int ORANGE = Color.parseColor("#FFA500");
            colors = new int[]{Color.BLUE, Color.RED, Color.GREEN, ORANGE, Color.MAGENTA, Color.DKGRAY, Color.CYAN};
        }
        else if(colorScheme.equals("50 Shades of Grey")) {
            int GRAY = Color.parseColor("#B0B0B0");
            int GRAY2 = Color.parseColor("#585858");
            int DORIANGRAY = Color.parseColor("#282828");
            colors = new int[]{Color.GRAY, Color.DKGRAY, Color.LTGRAY, GRAY, GRAY2, DORIANGRAY, Color.BLACK};
        }
    }
}
