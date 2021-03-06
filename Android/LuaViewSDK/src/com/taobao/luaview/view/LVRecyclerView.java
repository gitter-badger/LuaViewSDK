package com.taobao.luaview.view;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.taobao.android.luaview.R;
import com.taobao.luaview.userdata.list.UDBaseRecyclerView;
import com.taobao.luaview.userdata.list.UDRecyclerView;
import com.taobao.luaview.userdata.ui.UDView;
import com.taobao.luaview.util.LuaViewUtil;
import com.taobao.luaview.view.interfaces.ILVRecyclerView;
import com.taobao.luaview.view.recyclerview.LVRecyclerViewAdapter;
import com.taobao.luaview.view.recyclerview.RecyclerViewHelper;
import com.taobao.luaview.view.recyclerview.decoration.DividerGridItemDecoration;
import com.taobao.luaview.view.recyclerview.layout.LVGridLayoutManager;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;

/**
 * LuaView - RecyclerView
 *
 * @author song
 * @date 15/8/20
 */
public class LVRecyclerView extends RecyclerView implements ILVRecyclerView {
    public Globals mGlobals;
    private UDBaseRecyclerView mLuaUserdata;

    //adapter
    private RecyclerView.Adapter mAdapter;
    private LayoutManager mLayoutManager;
    private ItemDecoration mItemDecoration;
    private int mSpacing;//间隔

    public static LVRecyclerView createVerticalView(Globals globals, LuaValue metaTable, Varargs varargs, UDBaseRecyclerView udBaseRecyclerView) {
        final LVRecyclerView lvRecyclerView = (LVRecyclerView) LayoutInflater.from(globals.context).inflate(R.layout.lv_recyclerview_vertical, null);
        return lvRecyclerView.init(globals, metaTable, varargs, udBaseRecyclerView);
    }

    private LVRecyclerView(Globals globals, LuaValue metaTable, Varargs varargs, UDBaseRecyclerView udBaseRecyclerView) {
        super(globals.context);
        init(globals, metaTable, varargs, udBaseRecyclerView);
    }

    public LVRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LVRecyclerView init(Globals globals, LuaValue metaTable, Varargs varargs, UDBaseRecyclerView udBaseRecyclerView){
        LuaViewUtil.setId(this);
        this.mGlobals = globals;
        this.mLuaUserdata = udBaseRecyclerView != null ? udBaseRecyclerView : new UDRecyclerView(this, globals, metaTable, varargs != null ? varargs.arg1() : null);
        init();
        return this;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //改变大小的时候需要更新spanCount & spanSize
        updateMaxSpanCount();
    }

    private void init() {
        mAdapter = new LVRecyclerViewAdapter(mGlobals, mLuaUserdata);
        this.setAdapter(mAdapter);
        mLayoutManager = new LVGridLayoutManager(this);
        this.setLayoutManager(mLayoutManager);
        mLuaUserdata.initOnScrollCallback(this);
        this.setHasFixedSize(true);
    }

    /**
     * 更新最大间隔
     */
    public void updateMaxSpanCount() {
        if (mLayoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) mLayoutManager).setSpanCount(mLuaUserdata.getMaxSpanCount());
        } else if (mLayoutManager instanceof StaggeredGridLayoutManager) {
            ((StaggeredGridLayoutManager) mLayoutManager).setSpanCount(mLuaUserdata.getMaxSpanCount());
        }
    }

    public int getSpanSize(int position) {
        return mLuaUserdata.getSpanSize(position);
    }

    @Override
    public UDView getUserdata() {
        return mLuaUserdata;
    }

    @Override
    public void addLVView(View view, Varargs varargs) {
        //TODO 这里不做操作，因为ListView不应该加子view
    }

    @Override
    public RecyclerView.Adapter getLVAdapter() {
        return mAdapter;
    }

    //-------------------------------------------list view封装---------------------------------------

    public int getFirstVisiblePosition() {
        return RecyclerViewHelper.getFirstVisiblePosition(this);
    }

    public int getLastVisiblePosition() {
        return RecyclerViewHelper.getLastVisiblePosition(this);
    }

    public int getVisibleItemCount() {
        return RecyclerViewHelper.getVisibleItemCount(this);
    }

    public void setMiniSpacing(int spacing) {
        if (mItemDecoration == null || mSpacing != spacing) {
            this.removeItemDecoration(mItemDecoration);
            mSpacing = spacing;
            mItemDecoration = new DividerGridItemDecoration(spacing);
            this.addItemDecoration(mItemDecoration);
        }
    }

    public int getMiniSpacing() {
        return mSpacing;
    }
}
