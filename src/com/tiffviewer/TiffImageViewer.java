/* libtiffdecoder A tiff decoder run on android system. Copyright (C) 2009 figofuture
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public 
 * License as published by the Free Software Foundation; either version 2.1 of the License, or (at your option) any later 
 * version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to the Free 
 * Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
 * 
 * */

package com.tiffviewer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nqsky.meap.widget.tiff.TiffDecoder;

public class TiffImageViewer extends Activity {
	private GestureDetector gestureDetector;
	Bitmap mBitmap;
	LinearLayout mLinearLayout;
	private static final String LOG_TAG = "TiffImageViewer";

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mLinearLayout = new LinearLayout(this);
		final ImageView image = new ImageView(this);
		Intent imageIntent = getIntent();
		final Uri uri = imageIntent.getData();

		TiffDecoder.nativeTiffOpen(uri.getPath());
		Log.i(LOG_TAG, "width = " + TiffDecoder.nativeTiffGetWidth()
				+ " height = " + TiffDecoder.nativeTiffGetHeight());
		long begin = System.currentTimeMillis();

		int[] pixels = TiffDecoder.nativeTiffGetBytes();
		long end = System.currentTimeMillis();
		Log.i(LOG_TAG, "the cost time is " + (end - begin));

		mBitmap = Bitmap.createBitmap(pixels, TiffDecoder.nativeTiffGetWidth(),
				TiffDecoder.nativeTiffGetHeight(), Bitmap.Config.ARGB_8888);
		Log.i(LOG_TAG, "width = " + TiffDecoder.nativeTiffGetWidth()
				+ " height = " + TiffDecoder.nativeTiffGetHeight());

		image.setImageBitmap(mBitmap);
		image.setAdjustViewBounds(true); // set the ImageView bounds to match
											// the Drawable's dimensions
		image.setLayoutParams(new Gallery.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		// Add the ImageView to the layout and set the layout as the content
		// view
		mLinearLayout.addView(image);
		setContentView(mLinearLayout);
		gestureDetector = new BuileGestureExt(this,
				new BuileGestureExt.OnGestureResult() {

					@Override
					public void onGestureResult(int direction) {

						Log.i(LOG_TAG, "direction = " + direction);
						if (direction == BuileGestureExt.GESTURE_RIGHT) {
							if (TiffDecoder.nativeTiffNowPage() == TiffDecoder
									.nativeTiffTotal() - 1) {
								Toast.makeText(TiffImageViewer.this, "已到最后一页面",
										1).show();
							} else {

								TiffDecoder.nativeTiffSetPage((TiffDecoder
										.nativeTiffNowPage() + 1));
								int[] pixels = TiffDecoder.nativeTiffGetBytes();
								mBitmap.recycle();
								mBitmap = Bitmap.createBitmap(pixels,
										TiffDecoder.nativeTiffGetWidth(),
										TiffDecoder.nativeTiffGetHeight(),
										Bitmap.Config.ARGB_8888);
								Log.i(LOG_TAG,
										"width = "
												+ TiffDecoder
														.nativeTiffGetWidth()
												+ " height = "
												+ TiffDecoder
														.nativeTiffGetHeight());
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										image.setImageBitmap(mBitmap);

									}
								});
							}
						} else if (direction == BuileGestureExt.GESTURE_LEFT) {
							if (TiffDecoder.nativeTiffNowPage() == 0) {
								Toast.makeText(TiffImageViewer.this, "已到第一页面",
										1).show();
							} else {
								TiffDecoder.nativeTiffSetPage((TiffDecoder
										.nativeTiffNowPage() - 1));
								int[] pixels = TiffDecoder.nativeTiffGetBytes();
								mBitmap.recycle();
								mBitmap = Bitmap.createBitmap(pixels,
										TiffDecoder.nativeTiffGetWidth(),
										TiffDecoder.nativeTiffGetHeight(),
										Bitmap.Config.ARGB_8888);
								Log.i(LOG_TAG,
										"width = "
												+ TiffDecoder
														.nativeTiffGetWidth()
												+ " height = "
												+ TiffDecoder
														.nativeTiffGetHeight());
								runOnUiThread(new Runnable() {

									@Override
									public void run() {
										image.setImageBitmap(mBitmap);

									}
								});

							}
						}

					}
				}

		).Buile();

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		return gestureDetector.onTouchEvent(event);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		TiffDecoder.nativeTiffClose();
	}

}
