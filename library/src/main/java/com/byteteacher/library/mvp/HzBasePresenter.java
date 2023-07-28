package com.byteteacher.library.mvp;

/**
 * Created by cj on 2020/7/23.
 */
public class HzBasePresenter<T extends IHzViewController> implements IHzPresenter {

    public T mViewController;

    public HzBasePresenter(T mViewController) {
        this.mViewController = mViewController;
    }

    public T getViewController() {
        return mViewController;
    }

    public void setViewController(T viewController) {
        this.mViewController = viewController;
    }

    public void onDestroy(){
        mViewController =  null;
    }

}
