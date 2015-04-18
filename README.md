
自定义view学习代码记录
======================

##更新记录
###时间 2015年4月18日21:56:51
1. 在MainActivity上增加了四个Fragment，可以来回切换。
2. 修改了原有的布局，增加了几个布局文件。
3. 发现问题。CustomRoundImageView不适用于放入Fragment的布局文件中。原因是在onMeasure方法中有通过将mContext强转成Activity类型来获得屏幕参数。因此在非Activity布局中就会报错...这可能也是用自定义的图片标签的弊端之一。
