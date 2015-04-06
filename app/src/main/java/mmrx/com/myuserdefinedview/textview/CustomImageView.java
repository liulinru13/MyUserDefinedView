package mmrx.com.myuserdefinedview.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
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
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    16,getResources().getDisplayMetrics()));
                    break;
                case R.styleable.CustomView_titleText:
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
                mHeight = totalHeight;
            }
        }

        //宽度有具体数值
        if(widthMod == MeasureSpec.EXACTLY){
            mWidth = widthSize;
        }
        //wrap_content & others
        else{
            //获得图片的宽度
            int imageWidth = mImage.getWidth();
            //获得文字宽度
            int textWidth = mBound.width();
            //总体宽度度
            int totalWidth = imageWidth>textWidth?imageWidth:textWidth
                    + getPaddingRight() + getPaddingLeft();
            if(widthMod == MeasureSpec.AT_MOST){
                //warp_content
                //获得相对较大的宽度
                mHeight = Math.max(widthSize,totalWidth);
            }
            else{
                //多个自定义CustomImageView放入ScrollView中，只会显示出最后一个处理办法
                mHeight = totalWidth;
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
