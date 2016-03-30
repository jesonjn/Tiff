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

package com.nqsky.meap.widget.tiff;

public class TiffDecoder {

	public static native int nativeTiffOpen(String name);

	public static native int[] nativeTiffGetBytes();

	public static native int nativeTiffGetLength();

	public static native int nativeTiffGetWidth();

	public static native int nativeTiffGetHeight();

	public static native void nativeTiffClose();

	/** 增加支持 文件总页数 **/
	public static native int nativeTiffTotal();

	/** 增加支持 当前打开页码 **/
	public static native int nativeTiffNowPage();

	/** 增加支持 调用打开页面 **/
	public static native int nativeTiffSetPage(int page);

	static {
		System.loadLibrary("tiff");
		System.loadLibrary("tiffdecoder");
	}
}
