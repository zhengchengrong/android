1.补间动画它只是改变了View的显示效果而已，而不会真正去改变View的属性.
2.属性动画的实现机制是通过对目标对象进行赋值并修改其属性来实现的.
3.ValueAnimator是整个属性动画机制当中最核心的一个类，
属性动画的运行机制是通过不断地对值进行操作来实现的，而初始值和结束值之间的动画过渡就是由ValueAnimator这个类来负责计算的。
它的内部使用一种时间循环的机制来计算值与值之间的动画过渡，我们只需要将初始值和结束值提供给ValueAnimator，
并且告诉它动画所需运行的时长，那么ValueAnimator就会自动帮我们完成从初始值平滑过渡到结束值这样的效果。
除此之外，ValueAnimator还负责管理动画的播放次数、播放模式、以及对动画设置监听器等，确实是一个非常重要的类。

ValueAnimator anim = ValueAnimator.offload(0f,1f);
anim.setDuration(300);
anim.addUpadteListener(new ValueAnimator.AnimatorUpdateListener(ValueAnimator animation){
float currentValue = (float)animation.getAnimatedValue();
 Log.d("TAG", "cuurent value is " + currentValue;
});

anim.start();

12-25 17:27:58.019 3318-3318/com.zcr.anim01 D/TAG: current value is 0.669369
12-25 17:27:58.129 3318-3318/com.zcr.anim01 D/TAG: current value is 1.0


将一个值在5秒内从0过渡到5，再过渡到3，再过渡到10
ValueAnimator anim = ValueAnimator.offload(0f,5f,3f,10f);
anim.setDuration(5000);
anim.start();

#ObjectAnimator

相比于ValueAnimator,ObjectAnimator可能才是我们最常接触到的类，
因为ValueAnimator只不过是对值进行了一个平滑的动画过渡，但我们实际使用到这种功能的场景并不多。
而ObjectAnimator则就不同，它是可以直接对任意对象的任意属性进行动画操作的，比如说View的alpha属性。


透明变换成常规，就可以这样写:

ObjectAnimator animator = ObjectAnimator.ofFloat(textview,"alpha",1f,0f,1f);
animator.setDuration(5000);
animator.start();

第一个参数要求传入一个object对象，我们想要对哪个对象进行动画操作就传入什么，这里我传入了一个textview。
第二个参数是想要对该对象的哪个属性进行动画操作，由于我们想要改变TextView的不透明度，因此这里传入"alpha"。
后面的参数就是不固定长度了，想要完成什么样的动画就传入什么值，这里传入的值就表示将TextView从常规变换成全透明，再从全透明变换成常规。
之后调用setDuration()方法来设置动画的时长，然后调用start()方法启动动画.



ObjectAnimator animator2 = ObjectAnimator.ofFloat(tv_hello,"scaleY",1f,3f,1f);
animator2.setDuration(3000);
animator2.start();




ObjectAnimator在设计的时候就没有针对于View来进行设计，而是针对于任意对象的，
它所负责的工作就是不断地向某个对象中的某个属性进行赋值，然后对象根据属性值的改变再来决定如何展现出来。




ObjectAnimator.ofFloat(textview, "alpha", 1f, 0f);
其实这段代码的意思就是ObjectAnimator会帮我们不断地改变textview对象中alpha属性的值，从1f变化到0f。
然后textview对象需要根据alpha属性值的改变来不断刷新界面的显示，从而让用户可以看出淡入淡出的动画效果。



那么textview对象中是不是有alpha属性这个值呢？没有，不仅textview没有这个属性，连它所有的父类也是没有这个属性的！这就奇怪了，
textview当中并没有alpha这个属性，ObjectAnimator是如何进行操作的呢？其实ObjectAnimator内部的工作机制并不是直接对我们传入的属性名进行操作的，
而是会去寻找这个属性名对应的get和set方法，因此alpha属性所对应的get和set方法应该就是：

public void setAlpha(float value);
public float getAlpha();

那么textview对象中是否有这两个方法呢？确实有，并且这两个方法是由View对象提供的，也就是说不仅TextView可以使用这个属性来进行淡入淡出动画操作，
任何继承自View的对象都可以的。

既然alpha是这个样子，相信大家一定已经明白了，前面我们所用的所有属性都是这个工作原理，
那么View当中一定也存在着setRotation()、getRotation()、setTranslationX()、getTranslationX()、setScaleY()、getScaleY()这些方法，
不信的话你可以到View当中去找一下。



 组合动画
 
 组合动画功能主要需要借助AnimatorSet这个类，这个类提供了一个play()方法，如果我们向这个方法中传入一个Animator对象(ValueAnimator或ObjectAnimator)将会返回一个AnimatorSet.Builder的实例，AnimatorSet.Builder中包括以下四个方法：

*   after(Animator anim)   将现有动画插入到传入的动画之后执行
*   after(long delay)   将现有动画延迟指定毫秒后执行
*   before(Animator anim)   将现有动画插入到传入的动画之前执行
*   with(Animator anim)   将现有动画和传入的动画同时执行
好的，有了这四个方法，我们就可以完成组合动画的逻辑了


让TextView先从屏幕外移动进屏幕，然后开始旋转360度，旋转的同时进行淡入淡出操作，就可以这样写：
ObjectAnimator moveIn = ObjectAnimator.ofFloat(textview, "translationX", -500f, 0f);
ObjectAnimator rotate = ObjectAnimator.ofFloat(textview, "rotation", 0f, 360f);
ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(textview, "alpha", 1f, 0f, 1f);
AnimatorSet animSet = new AnimatorSet();
animSet.play(rotate).with(fadeInOut).after(moveIn); // 表示rotate、fadeInOut在moveIn的后面执行。
animSet.setDuration(5000);
animSet.start();

让旋转和淡入淡出动画同时进行，并把它们插入到了平移动画的后面，最后是设置动画时长以及启动动画。


#Animator监听器

在很多时候，我们希望可以监听到动画的各种事件，比如动画何时开始，何时结束，然后在开始或者结束的时候去执行一些逻辑处理。
这个功能是完全可以实现的，Animator类当中提供了一个addListener()方法，这个方法接收一个AnimatorListener，
我们只需要去实现这个AnimatorListener就可以监听动画的各种事件了。

ObjectAnimator是继承自ValueAnimator的，而ValueAnimator又是继承自Animator的，因此不管是ValueAnimator还是ObjectAnimator都是可以使用addListener()这个方法的。
另外AnimatorSet也是继承自Animator的，因此addListener()这个方法算是个通用的方法。

anim.addListener(new AnimatorListener(){

@Override
public void onAnimationStart(Animator animation){

}

@Override
public void onAnimationRepeat(Animator animation){

}

@Override
public void onAnimationEnd(Animator animation){

}

@Override
publilc void onAnimationCancel(Animator animation){

}
});

可以看到，我们需要实现接口中的四个方法，onAnimationStart()方法会在动画开始的时候调用，onAnimationRepeat()方法会在动画重复执行的时候调用，onAnimationEnd()方法会在动画结束的时候调用，onAnimationCancel()方法会在动画被取消的时候调用。

但是也许很多时候我们并不想要监听那么多个事件，可能我只想要监听动画结束这一个事件，那么每次都要将四个接口全部实现一遍就显得非常繁琐。没关系，为此Android提供了一个适配器类，叫作AnimatorListenerAdapter，使用这个类就可以解决掉实现接口繁琐的问题了，如下所示：


anim.addListener(new AnimatorListenerAdapter(){
@Override
public void onAnimatorEnd(Animator animation){
}
});

扩展：显然，这个AnimatorListenerAdapter类，就是一个接口适配器

http://blog.csdn.net/zxt0601/article/details/52848004



#使用XML编写动画

XML来编写动画可能会比通过代码来编写动画要慢一些，但是在重用方面将会变得非常轻松


某个将通用的动画编写到XML里面，我们就可以在各个界面当中轻松去重用它

如果想要使用XML来编写动画，首先要在res目录下面新建一个animator文件夹，所有属性动画的XML文件都应该存放在这个文件夹当中。然后在XML文件中我们一共可以使用如下三种标签：
*   <animator>  对应代码中的ValueAnimator
*   <objectAnimator>  对应代码中的ObjectAnimator
*   <set>  对应代码中的AnimatorSet

那么比如说我们想要实现一个从0到100平滑过渡的动画，在XML当中就可以这样写：

<animator xmlns:android="http://shemas.android.com/apk/res/android"
android:valueFrom="0"
android:valueTo="100"
android:valueType="intType"
/>
我们也可以使用XML来完成复杂的组合动画操作，比如将一个视图先从屏幕外移动进屏幕，然后开始旋转360度，旋转的同时进行淡入淡出操作，就可以这样写：
<set xmlns:android="http://schemas.android.com/apk/res/android"
    android:ordering="sequentially" >

    <objectAnimator
        android:duration="2000"
        android:propertyName="translationX"
        android:valueFrom="-500"
        android:valueTo="0"
        android:valueType="floatType" >
    </objectAnimator>

    <set android:ordering="together" >
        <objectAnimator
            android:duration="3000"
            android:propertyName="rotation"
            android:valueFrom="0"
            android:valueTo="360"
            android:valueType="floatType" >
        </objectAnimator>

        <set android:ordering="sequentially" >
            <objectAnimator
                android:duration="1500"
                android:propertyName="alpha"
                android:valueFrom="1"
                android:valueTo="0"
                android:valueType="floatType" >
            </objectAnimator>
            <objectAnimator
                android:duration="1500"
                android:propertyName="alpha"
                android:valueFrom="0"
                android:valueTo="1"
                android:valueType="floatType" >
            </objectAnimator>
        </set>
    </set>
这段XML实现的效果和我们刚才通过代码来实现的组合动画的效果是一模一样的，每个参数的含义都非常清楚，相信大家都是一看就懂，我就不再一一解释了。

最后XML文件是编写好了，那么我们如何在代码中把文件加载进来并将动画启动呢？只需调用如下代码即可：


Animator animator = AnimatorInflater.loadAnimator(context,R.animator.anim_file);
animator.setTarget(view);
animator.start();

调用AnimatorInflater的loadAnimator来将XML动画文件加载进来，然后再调用setTarget()方法将这个动画设置到某一个对象上面，最后再调用start()方法启动动画就可以了，就是这么简单。