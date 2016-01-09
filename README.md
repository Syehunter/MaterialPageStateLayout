# PageStateLayout
PageStateLayout could let you show [Loading][Empty][Error][Succeed][Requesting] state in Activity, Fragment, ViewGroup as you want.

Improt library:

	


![](http://7xn4z4.com1.z0.glb.clouddn.com/PageStateLayout.gif)

U can use the layout whereever u want!

Following Methods are supported:

	//replace activity.setContentView()
	pageStateLayout.load(activity, succeedView);

	//parent instanceof ViewGroup
	pageStateLayout.load(parent, suuccedView);
	
	//use this if u don't want any containers
	//such as in fragment.onCreateView, just return pageStateLayout
	pageStateLayout.load(succeedView);
	
It has 5 states:

	onLoading();
	
	onEmpty();
	
	onError();
	
	onSucceed();
	
	//In some activities u may want to show both the progressbar and succeedView, such as LoginActivity, then switch on this state
	onRequesting();
	
If u wan't to replace these pages with some others designed by yourself(you'd better do it in you application or BaseActivity)

	PageStateLayout.Builder.setLoadingView(resId);
	
	PageStateLayout.Builder.setEmptyView(resId);
	
	PageSateLayout.Builder.setErrorView(resId);
	
U can use other methods in PageStateLayout.Builder to change the progressColor, the errorImage, the emptyPromt and so on;
