package mmrx.com.myuserdefinedview.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.widget.ImageView;

import mmrx.com.myuserdefinedview.R;

/**
 * Created by mmrx on 2015/6/5.
 */
public class CustomRIV2 extends ImageView {

    private Context mContext;
    private Bitmap mBitmap;
    //图片画笔和边框画笔和文字画笔
    private Paint mBitMapPaint;
    private Paint mBorderPaint;
    private Paint mTextPaint;
    //边框颜色
    private int mBorderColor = Color.WHITE;
    //边框宽度 边框宽度为原图半径的1/8
    private float mBorderWidth = 5f;
    //边框比圆形图片半径大本身的1/2，也就是说边框和图片的距离为 边框宽度的1/2
    private int BORDER = 10;
    //第一行和第二行文字之间的距离为2
    private final float TEXT = 2f;
    //边框和文字之间的距离 等于边框的宽度*2
    private float BORDER_TEXT = 10f;
    //边框角度
    private float mBorderAngle = 90f;
    private float mBorderAngle_ = 90f;//最初设定的数值
    //文字大小
    private int mTextSize = 18;
    //第一行文//
    private String mTextRow1 = "";
    //第二行
    private String mTextRow2 = "";
    //第一行文字颜色
    private int mTextColorRow1 = Color.BLACK;
    //第二行文字颜色
    private int mTextColorRow2 = Color.GRAY;
    //渲染器
    private BitmapShader mBitMapShader;
    //图片拉伸方式 默认为按比例缩放
    private int mImageScale = 1;
    //控件的长宽
    private int mWidth;
    private int mHeight;
    //边框和圆形图片的半径
    private float mBorderRadius;
    private float mDrawableRadius;
    //矩形
    private RectF mDrawableRect;
    private RectF mBorderRect;
    private RectF mOuterBorderRect;
    private Rect mRow1Rect;
    private Rect mRow2Rect;
    //变换矩形
    private Matrix mBitMapMatrix;

    //控制圆环变化的标识符
    boolean isAdd = true;
    boolean isDecreased = true;
    //圆环增减的速度
    final int mSpeed = 5;

    public CustomRIV2(Context mContext){
        super(mContext);
        this.mContext = mContext;
    }

    public CustomRIV2(Context mContext,AttributeSet attr){
        this(mContext, attr, R.attr.CustomImageView04Style);
        this.mContext = mContext;
    }

    public CustomRIV2(Context mContext,AttributeSet attr,int defSytle){
        super(mContext,attr,defSytle);
        TypedArray ta = mContext.obtainStyledAttributes(attr,R.styleable.CustomRIV2_style,
                defSytle,R.style.CustomizeStyle04);
        int count = ta.getIndexCount();
        for(int i=0;i<count;i++){
            int index = ta.getIndex(i);
            switch (index){
                case R.styleable.CustomRIV2_style_borderColor:
                    mBorderColor = ta.getColor(index,Color.WHITE);
                    break;
//                case R.styleable.CustomRIV2_style_borderWidth:
//                    mBorderWidth = ta.getDimension(index,5f);
//                    break;
                case R.styleable.CustomRIV2_style_image:
                    mBitmap = BitmapFactory.decodeResource(getResources(),ta.getResourceId(index,0));
                    break;
                case R.styleable.CustomRIV2_style_imageScaleType:
                    mImageScale = ta.getInt(index,0);
                    break;
                case R.styleable.CustomRIV2_style_titleSize:
                    mTextSize = ta.getDimensionPixelSize(index,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    18, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomRIV2_style_borderAngle:
                    mBorderAngle = ta.getFloat(index,90);
                    mBorderAngle_ = mBorderAngle;
                    break;
                case R.styleable.CustomRIV2_style_titleText:
                    mTextRow1 = ta.getString(index);
                    break;
                case R.styleable.CustomRIV2_style_contentText:
                    mTextRow2 = ta.getString(index);
                    break;
                case R.styleable.CustomRIV2_style_titleColor:
                    mTextColorRow1 = ta.getColor(index,Color.BLACK);
                    break;
                case R.styleable.CustomRIV2_style_contentColor:
                    mTextColorRow2 = ta.getColor(index,Color.GRAY);
                    break;
                default:
                    break;
            }
        }
        ta.recycle();
        mBorderPaint = new Paint();
        mBitMapPaint = new Paint();
        mTextPaint = new Paint();
        mDrawableRect = new RectF();
        mOuterBorderRect = new RectF();
        mBorderRect = new RectF();
        mRow1Rect = new Rect();
        mRow2Rect = new Rect();
        //计算字体所需范围
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.getTextBounds(mTextRow1,0,mTextRow1.length(),mRow1Rect);
        mTextPaint.setTextSize(mTextSize*4/5);
        mTextPaint.getTextBounds(mTextRow2,0,mTextRow2.length(),mRow2Rect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMod = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMod = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int row1width = 0,row2width = 0;
        int row1height = 0,row2height = 0;

        //march_parent & exactly dimen
        if(heightMod == MeasureSpec.EXACTLY){
            mHeight = heightSize;
        }
        //wrap_content & others
        else{
            //获得图片高度
            mHeight = mBitmap.getHeight();
        }

        if(widthMod == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }
        //wrap_content & others
        else{
            //获得图片宽度
            mWidth = mBitmap.getWidth();

        }
        //获得文字的尺寸
        row1width = mRow1Rect.width();
        row2width = mRow2Rect.width();
        row1height = mRow1Rect.height();
        row2height = mRow2Rect.height();
        //图片的半径
        mDrawableRadius = Math.min(mWidth,mHeight);
        mBorderWidth = (int)(mDrawableRadius/10);
        BORDER = (int)(mBorderWidth/3);
        BORDER_TEXT = mBorderWidth*2;
        //外边框的半径
        mBorderRadius = mDrawableRadius + BORDER + mBorderWidth;
        //计算控件的高度
        int height =(int)(mBorderRadius*2 + BORDER_TEXT + row1height + TEXT + row2height + 0.5);
        //计算控件的宽度
        int textWidth = Math.max(row1width,row2width);
        int width = Math.max(textWidth,(int)(mBorderRadius*2+0.5))+(int)mBorderWidth;
//        mWidth = Math.min(mWidth,mHeight);
//        mHeight = Math.min(mWidth,mHeight);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if(mBitmap == null)
            return;
        //设置渲染器
        mBitMapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        //抗锯齿
        mBitMapPaint.setAntiAlias(true);
        //设置渲染器
        mBitMapPaint.setShader(mBitMapShader);
        //设置外框的相关参数
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);

        //边框矩形
        mBorderRect.set(0,0,getWidth(),getHeight());
        //边框半径,考虑线条的宽度,如果没有考虑线条宽度，显示会把线条的一部分遮蔽
//        mBorderRadius = Math.min((mBorderRect.width()-mBorderWidth)/2,(mBorderRect.height()-mBorderWidth)/2);
        //图片外框的矩形,因为图片是在外边框内部，所以位置矩形的坐标要考虑到边框的宽度
        final float borderLeft = getWidth()/2-mBorderRadius;
        final float borderTop = (getHeight()-mRow1Rect.height()-mRow2Rect.height()-BORDER_TEXT-TEXT)/2-mBorderRadius;
        final float borderRight = getWidth()/2+mBorderRadius;
        final float borderBottom =  getHeight()-mRow1Rect.height()-mRow2Rect.height()-BORDER_TEXT-TEXT;
        mOuterBorderRect.set(borderLeft,borderTop,
                borderRight,borderBottom);
        //图片的矩形
        mDrawableRect.set(borderLeft-BORDER,
                borderTop+BORDER,
                borderRight-BORDER,
                borderBottom-BORDER);
        //圆形图片的半径
//        mDrawableRadius = mBorderRadius-mBorderWidth;
        //设置图片的缩放
        setBitMapScale();

        canvas.drawCircle(getWidth()/2,
                (getHeight()-mRow1Rect.height()-mRow2Rect.height()-BORDER_TEXT-TEXT)/2,
                mDrawableRadius,mBitMapPaint);
        if(mBorderWidth != 0) {
            //设置起始角度 0度位置为三点钟方向
            float mBorderAngle_start = 135 - mBorderAngle/2;
            canvas.drawArc(mOuterBorderRect, mBorderAngle_start, mBorderAngle, false, mBorderPaint);
        }

        //绘制文字
        if(mTextRow1 != null){
            //设置字体画笔相关参数
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setColor(mTextColorRow1);
            mTextPaint.setTextSize(mTextSize);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mTextRow1,getWidth()/2,borderBottom+BORDER_TEXT+mRow1Rect.height()/2,mTextPaint);
        }

        if(mTextRow2 != null){
            //设置字体画笔相关参数
            mTextPaint.setStyle(Paint.Style.FILL);
            mTextPaint.setColor(mTextColorRow2);
            mTextPaint.setTextSize(mTextSize*4/5);
            mTextPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText(mTextRow2,getWidth()/2,borderBottom+BORDER_TEXT+mRow1Rect.height()+TEXT+mRow2Rect.height()/2,mTextPaint);
        }
    }
    //根据控件的尺寸和设置的图片缩放模式，来对图片进行缩放
    private void setBitMapScale(){

        float scaleX = 0,scaleY = 0;
        //获得圆形的直径和图片的尺寸
        float diameter = mDrawableRadius*2;
        float mBitMapWidth = mBitmap.getWidth();
        float mBitMapHeight = mBitmap.getHeight();
        mBitMapMatrix = new Matrix();
        mBitMapMatrix.set(null);
        //fillXY 宽高单独缩放
        if(mImageScale == 0){
            scaleX = diameter/mBitMapWidth;
            scaleY = diameter/mBitMapHeight;
        }
        //center 等比例缩放
        else{
            float scale = 0;
            scaleX = diameter/mBitMapWidth;
            scaleY = diameter/mBitMapHeight;
            //如果宽度和高度至少有一个需要放大
            if(scaleX > 1 || scaleY > 1){
                scale = Math.max(scaleX,scaleY);
            }
            else{
                scale = Math.min(scaleX, scaleY);
            }
            scaleX = scale;
            scaleY = scale;
        }
        mBitMapMatrix.setScale(scaleX,scaleY);
        mBitMapShader.setLocalMatrix(mBitMapMatrix);
    }
    //设置图片
    public void setImage(Bitmap bm){
        this.mBitmap = bm;
        invalidate();
    }
    //设置图片
    public void setImage(int rid){
        this.mBitmap = BitmapFactory.decodeResource(getResources(),rid);
        invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                Log.v("CustomRIV2-onTouchEvent","ACTION_DOWN");
                isDecreased = false;
                isAdd = true;
                new Thread(new AddRunnable()).start();
                break;
            case MotionEvent.ACTION_UP:
                Log.v("CustomRIV2-onTouchEvent","ACTION_UP");
                isDecreased = true;
                isAdd = false;
                new Thread(new DecreaseRunnable()).start();
                break;
            default:
                break;
        }
        return true;
    }

    private class AddRunnable implements Runnable{
        @Override
        public void run() {
            while (isAdd && mBorderAngle <= 360) {
                mBorderAngle += 2;
                postInvalidate();
                try {
                    Thread.sleep(mSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    };

    private class DecreaseRunnable implements Runnable{
        @Override
        public void run() {
            while (isDecreased && mBorderAngle >mBorderAngle_) {
                mBorderAngle -= 2;
                postInvalidate();
                try {
                    Thread.sleep(mSpeed);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    break;
                }
            }
        }
    };
}
