package mmrx.com.myuserdefinedview.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import mmrx.com.myuserdefinedview.R;

/**
 * Created by mmrx on 2015/4/6.
 */
public class CustomImageView extends View {

    //文本内容
    private String mTitleText;
    //文本的颜色
    private int mTitleColor;
    //文本大小
    private int mTitleTextSize;
    //图片资源
    private Bitmap mImage;
    //图片缩放
    private int mImageScale;
    //文本时候的文字显示范围
    private Rect mBound;
    private Paint mPaint;
    //图片和字外面的矩形
    private Rect rect;
    //高度和宽度
    int mWidth,mHeight;

    //在代码中调用
    public CustomImageView(Context context){
        this(context,null);
    }
    //在布局文件中调用
    public CustomImageView(Context context,AttributeSet set){
        this(context,set,R.attr.CustomImageView02Style);
    }
    //显示被调用
    public CustomImageView(Context context,AttributeSet set,int defStyle){
        super(context,set,defStyle);

        TypedArray ta = context.obtainStyledAttributes(set, R.styleable.CustomImageView,
                defStyle,R.style.CustomizeStyle02);
        int count = ta.getIndexCount();

        for(int i=0;i<count;i++){
            int index = ta.getIndex(i);

            switch (index){
                case R.styleable.CustomImageView_image:
                    /*
                    * decodeResource(Resources res, int id)
                    * res 是一个Resources类对象，用来读取res文件夹下的资源
                    * 这个res是的来源是该view创建时传入的Context对象， context.getResources()
                    * */
                    mImage = BitmapFactory.decodeResource(getResources(),ta.getResourceId(index,0));
//                    assert mImage!=null;
                    break;
                case R.styleable.CustomImageView_imageScaleType:
                    mImageScale = ta.getInt(index,0);
                    break;
                case R.styleable.CustomImageView_titleColor:
                    mTitleColor = ta.getColor(index, Color.BLACK);
                    break;
                case R.styleable.CustomImageView_titleSize:
                    /*
                    * TypedValue.applyDimension是转变为标准尺寸的函数
                    * 第一个参数是 单位，第二个参数是要数值，第三个是DisplayMetircs 对象，用于获取屏幕分辨率
                    * */
                    mTitleTextSize = ta.getDimensionPixelSize(index,
                            (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    16,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomImageView_titleText:
                    mTitleText = ta.getString(index);
                    break;
                default:
                    break;
            }//end switch
        }//end for

        ta.recycle();

        rect = new Rect();
        mPaint = new Paint();
        mBound = new Rect();
        mPaint.setTextSize(mTitleTextSize);
        //计算字体所需范围
        mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
    }//end method

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int hightMod = MeasureSpec.getMode(heightMeasureSpec);
        int hightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthMod = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //高度有具体数值
        if(hightMod == MeasureSpec.EXACTLY){
            mHeight = hightSize;
        }
        //wrap_content & others
        else{
            //获得图片的高度
            int imageHeight = mImage.getHeight();
            //获得文字高度
            int textHeight = mBound.height();
            //总体高度
            int totalHeight = imageHeight + textHeight + getPaddingTop() + getPaddingBottom();
            if(hightMod == MeasureSpec.AT_MOST){
                //warp_content
                //获得相对较小的高度
                mHeight = Math.min(hightSize,totalHeight);
            }
            else{
                //多个自定义CustomImageView放入ScrollView中，只会显示出最后一个处理办法
                mHeight = hightSize;
            }
        }

        //宽度有具体数值
        if(widthMod == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }
        //wrap_content & others
        else{
            // 由图片决定的宽
            int desireByImg = getPaddingLeft() + getPaddingRight() + mImage.getWidth();
            // 由字体决定的宽
            int desireByTitle = getPaddingLeft() + getPaddingRight() + mBound.width();
            Log.w("CustomImageView","desireByTitle："+desireByTitle+" desireByImg："+desireByImg+" widthSize: "+widthSize);
            if(widthMod == MeasureSpec.AT_MOST){
                //warp_content
                Log.w("CustomImageView","MeasureSpec.AT_MOST");
                //获得相对较大的宽度
                int desire = Math.max(desireByImg,desireByTitle);
                mWidth = Math.min(widthSize,desire);
            }
            else{
                Log.w("CustomImageView","NOT MeasureSpec.AT_MOST");
                //多个自定义CustomImageView放入ScrollView中，只会显示出最后一个处理办法
                mWidth = widthSize;
            }
        }

        //将计算好的高度宽度放入,一定要记得这句话不要丢了...
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        //边框
        //设置线宽
        mPaint.setStrokeWidth(4);
        //设置样式为空心
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.RED);
        /*开画,前四个参数是 左 上 右 下 边的位置(注意是边，不是点) 画笔
         * left 矩形左边的x坐标 right 矩形右边的x坐标
         * top 矩形上边的y坐标 bottom 矩形下边的y坐标
         * */
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        //获得绘制图片的矩形框
        rect.left = getPaddingLeft();
        rect.right = mWidth - getPaddingRight();
        rect.top = getPaddingTop();
        rect.bottom = mHeight - getPaddingBottom();

        //设置画笔风格为 实心
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mTitleColor);

        //当当前字体的宽度大于边框的宽度，截取字符串的，改为xxxx...
        if(mBound.width()>mWidth){
            //TextPaint 是 Paint 的一个子类
            TextPaint paint = new TextPaint(mPaint);
            /*TextUtils是处理字符串的工具类，ellipsize是用来截断给定长度的字符串的方法
            * 参数 需要截断的字符串 显示的字符串的宽度 省略号的位置
            * */
            String msg = TextUtils.ellipsize(mTitleText,paint,
                    (float)mWidth-getPaddingLeft()-getPaddingRight(),TextUtils.TruncateAt.END).toString();
            /*
            * 坐标参数 x,y x默认是字符串左边在屏幕的位置，如果设置了paint.setTextAlign(Paint.Align.CENTER);
            * 那就是字符的中心，y是指定这个字符baseline在屏幕上的位置
            * */
            canvas.drawText(msg, getPaddingLeft(), mHeight - getPaddingBottom(), mPaint);
        }
        //正常情况，字体居中
        else{
            canvas.drawText(mTitleText,mWidth/2-mBound.width()*1.0f/2,
                    mHeight-getPaddingBottom(),mPaint);
        }
        //这句话是什么意思？如果图片缩放选择的是FITXY，则需要将rect的bottom边向上提一下，防止把
        //字覆盖掉，如果选择的是CENTER,rect的位置会重新定义就不用这句话了。我感觉这句话放到
        //if(mImageScale == 0)语句块里比较好懂一些。
        rect.bottom -= mBound.height();
        //如果缩放为 FITXY
        if(mImageScale == 0){
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }
        //如果为居中 CENTER
        else{
            //计算居中的矩形范围
            rect.left = mWidth/2 - mImage.getWidth()/2;
            rect.right = mWidth/2 + mImage.getWidth()/2;
            rect.bottom = (mHeight-mBound.height())/2 + mImage.getHeight()/2;
            rect.top = (mHeight-mBound.height())/2 - mImage.getHeight()/2;
            canvas.drawBitmap(mImage,null,rect,mPaint);
        }
    }
}
