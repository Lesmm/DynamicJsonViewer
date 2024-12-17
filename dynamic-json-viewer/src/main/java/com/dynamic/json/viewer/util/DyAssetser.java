package com.dynamic.json.viewer.util;

import android.content.res.AssetManager;

import com.dynamic.json.viewer.DyRenderApi;
import com.dynamic.json.viewer.util.json.JSONArrayUtil;
import com.dynamic.json.viewer.util.json.JSONObjectUtil;
import com.dynamic.json.viewer.util.json.JSONUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;


public class DyAssetser {

    public static Object getAssetsAsJson(String assetsItemPath) {
        Object json = getAssetsAsJsonObject(assetsItemPath);
        if (json == null) {
            json = getAssetsAsJsonArray(assetsItemPath);
        }
        return json;
    }

    public static JSONObject getAssetsAsJsonObject(String assetsItemPath) {
        try {
            return JSONObjectUtil.createJSONObject(getAssetsAsString(assetsItemPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray getAssetsAsJsonArray(String assetsItemPath) {
        try {
            return JSONArrayUtil.createJSONArray(getAssetsAsString(assetsItemPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getAssetsAsString(String assetsItemPath) {
        try {
            return JSONUtil.readStreamToText(getAssetsAsStream(assetsItemPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static InputStream getAssetsAsStream(String assetsItemPath) {
        try {
            AssetManager assetManager = DyRenderApi.getApplication().getAssets();
            File fileInAssets = new File(assetsItemPath);
            File parent = fileInAssets.getParentFile();
            if (parent == null) {
                return null;
            }
            String assetsDir = parent.getAbsolutePath().trim();
            if (assetsDir.startsWith("/")) {
                assetsDir = assetsDir.substring(1);
            }
            if (Arrays.asList(assetManager.list(assetsDir)).contains(fileInAssets.getName())) {
                return assetManager.open(assetsItemPath);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
