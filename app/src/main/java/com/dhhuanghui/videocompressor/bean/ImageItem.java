package com.dhhuanghui.videocompressor.bean;

import java.io.Serializable;

/**
 * 一个图片对象
 *
 * @author huanghui
 */
public class ImageItem implements Serializable {
    private static final long serialVersionUID = 1L;
    public String imageId;
    public String thumbnailPath;
    public String imagePath;
    public String size="0";
    public boolean isSelected = false;

}
