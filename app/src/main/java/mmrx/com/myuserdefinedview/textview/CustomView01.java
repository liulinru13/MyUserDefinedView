package mmrx.com.myuserdefinedview.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import mmrx.com.myuserdefinedview.R;

/**
 * Created by mmrx on 2015/4/1.
 */
public class CustomView01 extends View{
    //文本内容
    private String mTitleText;
    //文本的颜色
    private int mTitleColor;
    //文本大小
    private int mTitleTextSize;
    //文本时候的文字显示范围
    private Rect mBound;
    private Paint mPaint;
    //在code中会调用这个构造函数
    public CustomView01(Context context){
        this(context,null);
    }
    //在xml中定义会调用这个构造函数
    public CustomView01(Context context,AttributeSet set){
        this(context,set,R.attr.CustomViewSytle);

    }

    //这个构造函数不是系统调用的，是需要显示调用的
    //获得自定义的样式属性
    public CustomView01(Context context,AttributeSet set,int defStyle){
        super(context,set,defStyle);
        //获得自定义样式的属性
        /*set是属性集合，第二个参数attrs是自定义view的属性类型数组，第三个是默认的style
        是当前Theme中指向style的一个引用，第四个也是指向一个style的引用，只有第三个参数为0或者不为0但是
        Theme中没有为difStyleAttr属性赋值时起作用*/
        TypedArray ta = context.obtainStyledAttributes(set, R.styleable.CustomView,
                defStyle,R.style.CustomizeStyle);
        //获取属性值有两种方法，一是显示的取用，二是顺序遍历TypedArray数组
        /*显示的取用
        * String str = ta.getString(R.styleable.CustomView_titleText);
        * 顺序遍历数组
        * */
        int n = ta.getIndexCount();
        for(int i=0;i<n;i++){
            int attr = ta.getIndex(i);
            switch (attr){
                case R.styleable.CustomView_titleText:
                    mTitleText = ta.getString(attr);
                    break;
                case R.styleable.CustomView_titleColor:
                    //获得颜色，默认为黑色
                    mTitleColor = ta.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CustomView_titleSize:
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    mTitleTextSize = ta.getDimensionPixelSize(attr,
                            (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                                    16,getResources().getDisplayMetrics()));
                    break;
            }//end switch
        }//end for
        /*在TypedArray后调用recycle主要是为了缓存。当recycle被调用后
        这就说明这个对象从现在可以被重用了。TypedArray 内部持有部分数组
        它们缓存在Resources类中的静态字段中，这样就不用每次使用前都需要分配内存*/
        ta.recycle();

        mPaint = new Paint();
        mPaint.setTextSize(mTitleTextSize);
        mBound = new Rect();
        //返回包围整个字符串的最小的一个rect区域
        mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);

        this.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mTitleText = randomText();
                /*刷新该界面
                * invalidate()是用来刷新View的，必须是在UI线程中进行工作。
                * invalidate()得在UI线程中被调动，在工作者线程中可以通过Handler来通知UI线程进行界面更新。
                *而postInvalidate()在工作者线程中被调用
                * */
                postInvalidate();
                //当前后Text长度不同时，显示会有些问题，调用该方法来通知parentView,调用该view
                //的onMeasure重绘
                requestLayout();
            }
        });
     }

    private String randomText(){
        Random random = new Random();
        Set<Integer> set = new HashSet<Integer>();
        while(set.size()<4){
            int randomInt = random.nextInt(10);
            set.add(randomInt);
        }
        StringBuffer sb = new StringBuffer();
        for(Integer i:set)
            sb.append(i);
        return sb.toString();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //当明确在xml中设置了宽度和高度时候，显示的就是设置的数值，但是当设置为
        //wrap_content或者是match_parent时，由于没有重写onMeasure方法，系统帮我们测量的
        //就是match_parent的数据
        Log.i("----onMeasure----","onMeasure被调用");
        //获得在xml中设置的模式
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMod = MeasureSpec.getMode(heightMeasureSpec);
        //获得具体的高宽数值
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int width,height;
        //当宽度模式是具体的数值的时候，就用具体数值
        if(widthMode == MeasureSpec.EXACTLY){
            width = widthSize;
        }else{
            //获取包围字符的最小的矩形
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
            //获取矩形的宽度
            float textWidth = mBound.width();
            //将矩形的宽度加上左右padding值，取整
            int desired = (int)(getPaddingLeft()+textWidth+getPaddingRight());
            width = desired;
        }
        //高度设置同理
        if(heightMod == MeasureSpec.EXACTLY){
            height = heightSize;
        }else{
            //获取包围字符的最小的矩形
            mPaint.setTextSize(mTitleTextSize);
            mPaint.getTextBounds(mTitleText,0,mTitleText.length(),mBound);
            //获取矩形的宽度
            float textHeight = mBound.height();
            //将矩形的宽度加上左右padding值，取整
            int desired = (int)(getPaddingBottom()+textHeight+getPaddingTop());
            height = desired;
        }
        //将计算好的高度宽度放入
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mPaint.setColor(Color.YELLOW);
        //参数为左上右下的坐标
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),mPaint);
        //设置字体的颜色
        mPaint.setColor(mTitleColor);
        //绘制文字，第二三个参数是文字绘制的坐标 xy，收到画笔里文字对齐方式的影响
        canvas.drawText(mTitleText,getWidth()/2-mBound.width()/2,
                getHeight()/2+mBound.height()/2,mPaint);
    }
}
