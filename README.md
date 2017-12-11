# goRefresh  
![image](https://jitpack.io/v/yanyiqun001/goRefresh.svg)
### Apk下载体验
![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/icon.png)
## 使用方式

项目gradle文件添加
```
allprojects {
          repositories {
              ...
              maven { url "https://jitpack.io" }
          }
      }
 ```
module下添加依赖 

   ```
dependencies{
         compile 'com.github.yanyiqun001.goRefresh:refreshlayout:0.6.1'
         compile 'com.github.yanyiqun001.goRefresh:refreshlayout_lottie:0.6.1' (需要使用lottie动画时添加)
}
 ```
 
 
 
### 根布局文件
```
<com.GoRefresh.GoRefreshLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/goRefreshLayout"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="@color/defaultbg">
    <android.support.v7.widget.RecyclerView
        android:id="@+id/rv"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:background="#fff"/>
</com.GoRefresh.GoRefreshLayout>
```

### 下拉刷新

##### 简单实现  
 
```   
     // 设置下拉监听  
      goRefreshLayout.setOnRefreshListener(new RefreshListener() {
             @Override
             public void onRefresh() {
                //添加你自己的代码
             }
         });
         
    //  结束刷新  
      goRefreshLayout.finishRefresh();
```
##### 自定义实现
   
   自定义header需要实现IHeaderView接口，具体请参看demo
   
##### 自定义头部使用lottie动画 (注意添加依赖)  
#### 说明:lottie所使用的文件全部来源于 https://www.lottiefiles.com/  并且不断添加新的动画效果。demo中只是选取了其中几个样式。大家可自由选择合适的样式作为刷新动画

#### 集成步骤：
1. 下载动画文件(xxx.json)
2. 在自己洗项目中建立assets文件夹，将动画文件拷贝其中
3. 头部布局文件中添加LottieAnimationView： lottie_fileName属性添加动画文件名
```
   <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:app="http://schemas.android.com/apk/res-auto"
       android:orientation="vertical"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       android:gravity="center">
       <com.airbnb.lottie.LottieAnimationView
           android:id="@+id/animation_view"
           android:layout_width="wrap_content"
           android:layout_height="wrap_content"
           app:lottie_fileName="loading_animation.json"
           />
   </LinearLayout>
```
4.代码中设置
  ```
  //参数2 头部布局文件layoutid 参数3 LottieAnimationView的id
  LottieView lottieView = new LottieView(this, R.layout.lottle_loading_animation, R.id.animation_view);
  goRefreshLayout.setHeaderView(lottieView);
 ```
### 上拉加载
#### 说明:为了更自然流畅的体验，footerview并没有添加在gorefresh上面，还是使用传统的adapter中添加footer的方式实现,gorefresh只是提供一个统一的入口方便调用。由于listview 和recyclerview添加上拉的实现方式是不同的（lv有setFooterView方法，rv没有），所以在使用方法上也略有不同。
#### lv使用：
```
    //必须设置
    goRefreshLayout.setHasFooter(true); 
    
    //设置加载状态的footerview (不设置使用默认)
    goRefreshLayout.setLoadingView(R.layout.footer_loading);
    
    //设置加载完毕没有更多数据的footerview(不设置使用默认)
    goRefreshLayout.setFinishWithNodataView(R.layout.footer_finish);
    
    //设置错误状态并且具有重试功能的footerview 第二个参数为重试控件的id (不设置使用默认)
    goRefreshLayout.setErrorViewWithRetry(R.layout.footerview_error, R.id.tips);
    
    //设置监听
    goRefreshLayout.setOnLoadMoreListener(new LoadMoreListener() {
                @Override
                public void onLoadmore() {
                    //添加自己的代码
                    new Handler().postDelayed(new Runnable() {
                           @Override
                          public void run() {
                            //结束刷新
                            rvLoadMoreWrapper.finishLoadMore();    
                            //结束刷新，没有更多数据
      //                    rvLoadMoreWrapper.finishLoadMoreWithNoData();
                            //加载错误
      //                    rvLoadMoreWrapper.finishLoadMoreWithError();                                                                                              
                          }
                    }, 2000);
                }
            });
``` 
    
#### rv使用：   
```
   //方式1 与lv类似
    goRefreshLayout.setHasFooter(true); 
    
    goRefreshLayout.setLoadingView(R.layout.footer_loading);
 
    goRefreshLayout.setFinishWithNodataView(R.layout.footer_finish);
    
    goRefreshLayout.setErrorViewWithRetry(R.layout.footerview_error, R.id.tips);
    //构建上拉加载adapter
    RvLoadMoreWrapper rvLoadMoreWrapper=goRefreshLayout.buildRvLoadMoreAdapter(adapter);
  
    recyclerView.setAdapter(rvLoadMoreWrapper);
    
    goRefreshLayout.setOnLoadMoreListener(new LoadMoreListener() {
                @Override
                public void onLoadmore() {
                    //添加自己的代码
                    new Handler().postDelayed(new Runnable() {
                           @Override
                          public void run() {
                            //结束刷新
                            rvLoadMoreWrapper.finishLoadMore();    
                            //结束刷新，没有更多数据
      //                    rvLoadMoreWrapper.finishLoadMoreWithNoData();
                            //加载错误
      //                    rvLoadMoreWrapper.finishLoadMoreWithError();                                                                                              
                          }
                    }, 2000);
                }
            });
    
   //方式2  直接使用RvLoadMoreWrapper进行操作(推荐)
    //构建上拉加载adapter
    RvLoadMoreWrapper rvLoadMoreWrapper=goRefreshLayout.buildRvLoadMoreAdapter(adapter);
    
    rvLoadMoreWrapper.setHasFooter(true)
                    .setLoadingView(R.layout.lottle_loading_animation_footer)
                    .setFinishView(R.layout.footer_finish)
                    .setErrorViewWithRetry(R.layout.footer_error,R.id.tips)
                    .setLoadMoreListener(new LoadMoreListener() {
                        @Override
                        public void onLoadmore() {
                             //添加自己的代码
                             new Handler().postDelayed(new Runnable() {
                                  @Override
                                 public void run() {
                                      //结束刷新
                                      rvLoadMoreWrapper.finishLoadMore();    
                                      //结束刷新，没有更多数据
                                 //   rvLoadMoreWrapper.finishLoadMoreWithNoData();
                                      //加载错误
                                 //   rvLoadMoreWrapper.finishLoadMoreWithError();                                                                                              
                              }
                            }, 2000);                
                        }
                    });
    recyclerView.setAdapter(rvLoadMoreWrapper);
```
#### 自定义footer：  

自定义footer需要实现IFooterView接口

示例
    
```
    public class CustomFooter implements IFooterView {
        private LayoutInflater inflater;
        public CustomFooter(Context context) {
            inflater=LayoutInflater.from(context);
        }
    
        @Override
        public View getLoadingView() {
            return inflater.inflate(R.layout.lottle_loading_animation_footer,null);
        }
    
        @Override
        public View getFinishView() {
            return inflater.inflate(R.layout.footer_finish,null);
        }
    
        @Override
        public View getFailureView() {
            return inflater.inflate(R.layout.footer_error,null);
        }
    
        @Override
        public int getRetryId() {
            return R.id.tips;
        }
    }
```
使用
```
 CustomFooter customFooter = new CustomFooter(this);
 goRefreshLayout.setFooterView(customFooter);
 //或者
 rvLoadMoreWrapper.setFooterView(customFooter)
 //...其余代码略
```

## demo gif


![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/gif.gif?raw=true)
![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/gif2.gif?raw=true)

![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/gif3.gif?raw=true)
![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/gif4.gif?raw=true)

![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/gif5.gif?raw=true)
![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/gif6.gif?raw=true)

![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/gif7.gif?raw=true)
![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/gif8.gif?raw=true)

![image](https://github.com/yanyiqun001/goRefresh/blob/master/screenshots/gif9.gif?raw=true)