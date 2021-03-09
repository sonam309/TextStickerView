package com.textstickerlib;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public abstract class StickerView extends FrameLayout {

    public static final String TAG = "com.knef.stickerView";
    public BorderView iv_border;
    public ImageView iv_scale;
    public ImageView iv_delete;
    public ImageView iv_rotate;
    public ImageView iv_edit;
    private OnStickerOperationListener onStickerOperationListener;


    // For scalling
    private float this_orgX = -1, this_orgY = -1;
    private float scale_orgX = -1, scale_orgY = -1;
    private double scale_orgWidth = -1, scale_orgHeight = -1;
    // For rotating
    private float rotate_orgX = -1, rotate_orgY = -1, rotate_newX = -1, rotate_newY = -1;
    // For moving
    private float move_orgX = -1, move_orgY = -1;

    double vAngle = 0;
    double tAngle = 0;
    double dAngle = 0;
    float cX = 0, cY = 0;
    double angle = 0;

    private double centerX, centerY;

    private final static int BUTTON_SIZE_DP = 30;
    private final static int SELF_SIZE_DP = 100;


    public StickerView(Context context) {
        super(context);
        init(context);
    }

    public StickerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public StickerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.iv_border = new BorderView(context);
        this.iv_scale = new ImageView(context);
        this.iv_delete = new ImageView(context);
        this.iv_rotate = new ImageView(context);
        this.iv_edit = new ImageView(context);

        this.iv_scale.setImageResource(R.drawable.ic_scale);
        this.iv_delete.setImageResource(R.drawable.ic_delete);
        this.iv_rotate.setImageResource(R.drawable.ic_rotation);
        this.iv_edit.setImageResource(R.drawable.ic_edit);

        this.setTag("DraggableViewGroup");
        this.iv_border.setTag("iv_border");
        this.iv_scale.setTag("iv_scale");
        this.iv_delete.setTag("iv_delete");
        this.iv_rotate.setTag("iv_rotate");
        this.iv_edit.setTag("iv_edit");

        int margin = convertDpToPixel(BUTTON_SIZE_DP, getContext()) / 2;
        int size = convertDpToPixel(SELF_SIZE_DP, getContext());

        LayoutParams this_params =
                new LayoutParams(
                        size,
                        size
                );
        this_params.gravity = Gravity.CENTER;

        LayoutParams iv_main_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        iv_main_params.setMargins(margin, margin, margin, margin);

        LayoutParams iv_border_params =
                new LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                );
        iv_border_params.setMargins(margin, margin, margin, margin);

        LayoutParams iv_scale_params =
                new LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
        iv_scale_params.gravity = Gravity.BOTTOM | Gravity.RIGHT;

        LayoutParams iv_delete_params =
                new LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
        iv_delete_params.gravity = Gravity.TOP | Gravity.RIGHT;

        LayoutParams iv_rotate_params =
                new LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
        iv_rotate_params.gravity = Gravity.BOTTOM | Gravity.LEFT;
        LayoutParams iv_edit_params =
                new LayoutParams(
                        convertDpToPixel(BUTTON_SIZE_DP, getContext()),
                        convertDpToPixel(BUTTON_SIZE_DP, getContext())
                );
        iv_edit_params.gravity = Gravity.TOP | Gravity.LEFT;

        this.setLayoutParams(this_params);
        this.addView(getMainView(), iv_main_params);
        this.addView(iv_border, iv_border_params);
        this.addView(iv_scale, iv_scale_params);
        this.addView(iv_delete, iv_delete_params);
        this.addView(iv_rotate, iv_rotate_params);
        this.addView(iv_edit, iv_edit_params);


        this.iv_scale.setOnTouchListener(mTouchListener);
        this.setOnTouchListener(mTouchListener);
        this.iv_rotate.setOnTouchListener(mTouchListener);

        this.iv_edit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (StickerView.this.getParent() != null) {

                    onStickerOperationListener.onStickerAdded();
                    onStickerOperationListener.onStickerClicked();
                }

            }
        });



        this.iv_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (StickerView.this.getParent() != null) {
                    ViewGroup myCanvas = ((ViewGroup) StickerView.this.getParent());
                    myCanvas.removeView(StickerView.this);
                }
            }
        });


    }


//    public boolean isFlip() {
//        return getMainView().getRotationY() == -180f;
//    }

    protected abstract View getMainView();

    private OnTouchListener mTouchListener = new OnTouchListener() {

        PointF DownPT = new PointF();
        PointF StartPT = new PointF();

        @Override
        public boolean onTouch(View view, MotionEvent event) {

            if (view.getTag().equals("iv_scale")) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.v(TAG, "iv_scale action down");

                        this_orgX = StickerView.this.getX();
                        this_orgY = StickerView.this.getY();

                        scale_orgX = event.getRawX();
                        scale_orgY = event.getRawY();
                        scale_orgWidth = StickerView.this.getLayoutParams().width;
                        scale_orgHeight = StickerView.this.getLayoutParams().height;

                        rotate_orgX = event.getRawX();
                        rotate_orgY = event.getRawY();

                        centerX = StickerView.this.getX() +
                                ((View) StickerView.this.getParent()).getX() +
                                (float) StickerView.this.getWidth() / 2;


                        int result = 0;
                        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
                        if (resourceId > 0) {
                            result = getResources().getDimensionPixelSize(resourceId);
                        }
                        double statusBarHeight = result;
                        centerY = StickerView.this.getY() +
                                ((View) StickerView.this.getParent()).getY() +
                                statusBarHeight +
                                (float) StickerView.this.getHeight() / 2;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        Log.v(TAG, "iv_scale action move");

                        rotate_newX = event.getRawX();
                        rotate_newY = event.getRawY();

                        double angle_diff = Math.abs(
                                Math.atan2(event.getRawY() - scale_orgY, event.getRawX() - scale_orgX)
                                        - Math.atan2(scale_orgY - centerY, scale_orgX - centerX)) * 180 / Math.PI;

                        Log.v(TAG, "angle_diff: " + angle_diff);


                        double offsetX = event.getRawX() - scale_orgX;
                        double offsetY = event.getRawY() - scale_orgY;
                        double offset = Math.max(offsetX, offsetY);

                        float width = iv_rotate.getLayoutParams().width;
                        float size = width * 2;



                        float width_sticker = (float) (StickerView.this.getLayoutParams().width + offset);
                        float height_sticker= (float) (StickerView.this.getLayoutParams().height+offset);


                        if (size <= width_sticker && size<=height_sticker) {

                            StickerView.this.getLayoutParams().width += offsetX;
                            StickerView.this.getLayoutParams().height += offsetY;
                            onScaling(true);

                        }

                        rotate_orgX = rotate_newX;
                        rotate_orgY = rotate_newY;

                        scale_orgX = event.getRawX();
                        scale_orgY = event.getRawY();

                        postInvalidate();
                        requestLayout();
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.v(TAG, "iv_scale action up");
                        break;
                }
            } else if (view.getTag().equals("DraggableViewGroup")) {

                StickerView.this.setControlsVisibility(true);


                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE:
                        StickerView.this.setX((int) (StartPT.x + event.getX() - DownPT.x));
                        StickerView.this.setY((int) (StartPT.y + event.getY() - DownPT.y));
                        StartPT.set(StickerView.this.getX(), StickerView.this.getY());
                        break;
                    case MotionEvent.ACTION_DOWN:
                        DownPT.set(event.getX(), event.getY());
                        StartPT.set(StickerView.this.getX(), StickerView.this.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        // Nothing have to do
                        break;
                    default:
                        break;
                }

            } else if (view.getTag().equals("iv_rotate")) {

                StickerView rl = (StickerView) view.getParent();


                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (rl != null) {
                            rl.requestDisallowInterceptTouchEvent(true);
                        }

                        Rect rect = new Rect();

                        ((View) view.getParent()).getGlobalVisibleRect(rect);

                        cX = rect.exactCenterX();
                        cY = rect.exactCenterY();

                        vAngle = ((View) view.getParent()).getRotation();

                        tAngle = Math.atan2(cY - event.getRawY(), cX - event.getRawX()) * 180 / Math.PI;

                        dAngle = vAngle - tAngle;

                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (rl != null) {
                            rl.requestDisallowInterceptTouchEvent(true);
                        }

                        angle = Math.atan2(cY - event.getRawY(), cX - event.getRawX()) * 180 / Math.PI;
                        float rotation = (float) (angle + dAngle);
                        ((View) view.getParent()).setRotation(rotation);

                        ((View) view.getParent()).invalidate();
                        ((View) view.getParent()).requestLayout();


                        float res = Math.abs(90 - Math.abs(rotation));
                        if (res <= 5) {
                            if (rotation > 0)
                                rotation = 90f;
                            else
                                rotation = -90f;
                        }

                        res = Math.abs(0 - Math.abs(rotation));
                        if (res <= 5) {
                            if (rotation > 0)
                                rotation = 0f;
                            else
                                rotation = -0f;
                        }

                        res = Math.abs(180 - Math.abs(rotation));
                        if (res <= 5) {
                            if (rotation > 0)
                                rotation = 180f;
                            else
                                rotation = -180f;
                        }
                        ((View) view.getParent()).setRotation(rotation);
                        break;
                    case MotionEvent.ACTION_UP:


                        break;
                }

            }

            return true;
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private double getLength(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(y2 - y1, 2) + Math.pow(x2 - x1, 2));
    }

    private float[] getRelativePos(float absX, float absY) {
        Log.v("ken", "getRelativePos getX:" + ((View) this.getParent()).getX());
        Log.v("ken", "getRelativePos getY:" + ((View) this.getParent()).getY());
        float[] pos = new float[]{
                absX - ((View) this.getParent()).getX(),
                absY - ((View) this.getParent()).getY()
        };
        Log.v(TAG, "getRelativePos absY:" + absY);
        Log.v(TAG, "getRelativePos relativeY:" + pos[1]);
        return pos;
    }

    public void setControlItemsHidden(boolean isHidden) {
        if (isHidden) {
            iv_border.setVisibility(View.GONE);
            iv_scale.setVisibility(View.GONE);
            iv_delete.setVisibility(View.GONE);
            iv_rotate.setVisibility(View.GONE);
            iv_edit.setVisibility(View.GONE);

        } else {
            iv_border.setVisibility(View.VISIBLE);
            iv_scale.setVisibility(View.VISIBLE);
            iv_delete.setVisibility(View.VISIBLE);
            iv_rotate.setVisibility(View.VISIBLE);
            iv_edit.setVisibility(View.VISIBLE);

        }
    }


    protected View getImageViewEdit() {
        return iv_edit;
    }

    protected void onScaling(boolean scaleUp) {
    }

    protected void onRotating() {
    }

    private class BorderView extends View {

        public BorderView(Context context) {
            super(context);
        }

        public BorderView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public BorderView(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            // Draw sticker border

            LayoutParams params = (LayoutParams) this.getLayoutParams();

            Log.v(TAG, "params.leftMargin: " + params.leftMargin);

            Rect border = new Rect();
            border.left = (int) this.getLeft() - params.leftMargin;
            border.top = (int) this.getTop() - params.topMargin;
            border.right = (int) this.getRight() - params.rightMargin;
            border.bottom = (int) this.getBottom() - params.bottomMargin;
            Paint borderPaint = new Paint();
            borderPaint.setStrokeWidth(10);
            borderPaint.setColor(Color.RED);
            borderPaint.setStyle(Paint.Style.STROKE);
            canvas.drawRect(border, borderPaint);

        }
    }

    private static int convertDpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return (int) px;
    }

    public void setControlsVisibility(boolean isVisible) {
        if (!isVisible) {
            iv_border.setVisibility(View.GONE);
            iv_delete.setVisibility(View.GONE);
            iv_scale.setVisibility(View.GONE);
            iv_rotate.setVisibility(View.GONE);
            iv_edit.setVisibility(View.GONE);

        } else {
            iv_border.setVisibility(View.VISIBLE);
            iv_delete.setVisibility(View.VISIBLE);
            iv_scale.setVisibility(View.VISIBLE);
            iv_edit.setVisibility(View.VISIBLE);
            iv_rotate.setVisibility(View.VISIBLE);

        }

    }

    @NonNull
    public StickerView setOnStickerOperationListener(
            @Nullable OnStickerOperationListener onStickerOperationListener) {
        this.onStickerOperationListener = onStickerOperationListener;
        return this;
    }

    @Nullable public OnStickerOperationListener getOnStickerOperationListener() {
        return onStickerOperationListener;
    }

    public interface OnStickerOperationListener {
        void onStickerAdded();

        void onStickerClicked();
//
//        void onStickerDeleted();
//
//        void onStickerDragFinished();
//
//        void onStickerTouchedDown();
//
//        void onStickerZoomFinished();
//
//        void onStickerFlipped();
//
//        void onStickerDoubleTapped();
    }

}
