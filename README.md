# BannerView
实现一个广告栏控件，支持循环滚动，手动滑动，设置点击事件等操作

###可以使用gradle添加依赖：

```
	dependencies {
	        compile 'com.github.HunterArley:BannerView:v1.0.0'
	}
```

###怎样使用？

- 在XML中的使用
```
   <com.tengxin.bannerlibrary.view.BannerView
        android:id="@+id/banner"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        />
```
- 在Java代码中的使用

```
    private BannerView mBannerView;
    private List<Integer> mDatas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatas.add(R.drawable.banner1);
        mDatas.add(R.drawable.banner2);
        mDatas.add(R.drawable.banner3);
        mBannerView = (BannerView) findViewById(R.id.banner);
        mBannerView.setDatas(mDatas, new BannerHolderCreator() {
            @Override
            public BannerHolder createHolder() {
                return new LocalImageHolderView();
            }
        }).startTurning(3000)//设置翻页时间
                .setPageTransformer(new OutPageTransFormer())//设置翻页动画
                .setPageIndicatorAlign(BannerView.PageIndicatorAlign.CENTER_HORIZONTAL)//设置指示器方向
                .setPagerIndicator(new int[]{R.drawable.dot_blur,R.drawable.dot_focus});//设置指示器图片

        //设置点击事件
        mBannerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, "你点击的是第"+position+"个Item", Toast.LENGTH_SHORT).show();
            }
        });

    }
```

###版本说明
- v1.0.0首发版本，欢迎star。
