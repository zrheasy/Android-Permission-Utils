package com.zrh.permission;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zrh
 * @date 2023/7/3
 */
public class PermissionUtils {
    private static final String TAG = PermissionUtils.class.getSimpleName();

    public static boolean isPermissionGranted(Context context, String permission) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestPermissions(FragmentActivity activity,
                                          String[] permissions,
                                          PermissionCallback callback) {
        requestPermissions(activity, activity.getSupportFragmentManager(), permissions, callback);
    }

    public static void requestPermissions(Fragment fragment, String[] permissions, PermissionCallback callback) {
        requestPermissions(fragment.requireContext(), fragment.getChildFragmentManager(), permissions, callback);
    }

    private static void requestPermissions(Context context,
                                           FragmentManager fragmentManager,
                                           String[] permissions,
                                           PermissionCallback callback) {
        // 过滤出需要向用户申请的权限，即未授权的权限
        List<String> requestPermissions = new ArrayList<>();
        Map<String, Boolean> result = new HashMap<>();
        for (String permission : permissions) {
            if (isPermissionGranted(context, permission)) {
                result.put(permission, true);
            } else {
                requestPermissions.add(permission);
            }
        }

        if (requestPermissions.isEmpty()) {
            callback.onResult(result, true);
            return;
        }

        PermissionCallback wrapper = (requestResult, isAllGranted) -> {
            result.putAll(requestResult);
            callback.onResult(result, isAllGranted);
        };
        PermissionFragment permissionFragment = getPermissionFragment(fragmentManager);
        permissionFragment.setCallback(wrapper);
        permissionFragment.requestPermissions(requestPermissions);
    }

    private static PermissionFragment getPermissionFragment(FragmentManager fragmentManager) {
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new PermissionFragment();
            fragmentManager.beginTransaction().add(fragment, TAG).commitNow();
        }
        return (PermissionFragment) fragment;
    }

    public static class PermissionFragment extends Fragment implements ActivityResultCallback<Map<String, Boolean>> {
        private PermissionCallback mCallback;

        private final ActivityResultLauncher<String[]> launcher =
                registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), this);

        public void setCallback(PermissionCallback callback) {
            this.mCallback = callback;
        }

        public void requestPermissions(List<String> permissions) {
            launcher.launch(permissions.toArray(new String[0]));
        }

        @Override
        public void onActivityResult(Map<String, Boolean> result) {
            if (mCallback != null) {
                boolean granted = true;
                for (String s : result.keySet()) {
                    if (Boolean.FALSE.equals(result.get(s))) {
                        granted = false;
                        break;
                    }
                }
                mCallback.onResult(result, granted);
            }
        }
    }

    public interface PermissionCallback {
        void onResult(Map<String, Boolean> result, boolean isAllGranted);
    }
}
