package com.fih.idx.deriklibrary.view;

import android.app.AlertDialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import com.fih.idx.deriklibrary.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShareDialog {
	
	private AlertDialog dialog;
	private GridView gridView;
	private RelativeLayout cancelButton;
	private SimpleAdapter saImageItems;
	
//	private int[] image={R.drawable.ssdk_oks_classic_qq,R.drawable.ssdk_oks_classic_qzone,R.drawable.ssdk_oks_classic_sinaweibo,R.drawable.ssdk_oks_classic_wechat,R.drawable.ssdk_oks_classic_wechatmoments,R.drawable.ssdk_oks_classic_wechatfavorite,R.drawable.ssdk_oks_classic_line,R.drawable.ssdk_oks_classic_facebook,R.drawable.ssdk_oks_classic_email};
//	private String[] name={"QQ","QQ空间","新浪微博","微信好友","微信朋友圈","微信收藏","Line","FaceBook","Email"};

	private int[] image={R.drawable.ssdk_oks_classic_wechat,R.drawable.ssdk_oks_classic_wechatmoments,R.drawable.ssdk_oks_classic_wechatfavorite,R.drawable.ssdk_oks_classic_line,R.drawable.ssdk_oks_classic_facebook,R.drawable.ssdk_oks_classic_email};
	private String[] name={"微信好友","微信朋友圈","微信收藏","Line","FaceBook","Email"};
	
	public ShareDialog(Context context){
		
		dialog=new android.app.AlertDialog.Builder(context,R.style.myTransparent).create();
		dialog.show();
		Window window = dialog.getWindow();
		window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		window.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
		window.setContentView(R.layout.share_dialog);
		gridView=(GridView) window.findViewById(R.id.share_gridView);
		cancelButton=(RelativeLayout) window.findViewById(R.id.share_cancel);
		List<HashMap<String, Object>> shareList=new ArrayList<HashMap<String,Object>>();
		for(int i=0;i<image.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();
	        map.put("ItemImage", image[i]);//添加图像资源的ID  
	        map.put("ItemText", name[i]);//按序号做ItemText  
	        shareList.add(map);
		}
		  
		saImageItems =new SimpleAdapter(context, shareList, R.layout.share_item, new String[] {"ItemImage","ItemText"}, new int[] {R.id.imageView1,R.id.textView1});
		gridView.setAdapter(saImageItems);
	}
	
	public void setCancelButtonOnClickListener(OnClickListener Listener){
		cancelButton.setOnClickListener(Listener);
	}
	
	public void setOnItemClickListener(OnItemClickListener listener){
		gridView.setOnItemClickListener(listener);
	}
			
	
	/**
	 * 关闭对话框
	 */
	public void dismiss() {
		dialog.dismiss();
	}
}