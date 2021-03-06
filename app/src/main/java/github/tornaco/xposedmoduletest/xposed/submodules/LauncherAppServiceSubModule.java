package github.tornaco.xposedmoduletest.xposed.submodules;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.lang.reflect.Method;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import github.tornaco.xposedmoduletest.BuildConfig;
import github.tornaco.xposedmoduletest.xposed.XAppBuildVar;
import github.tornaco.xposedmoduletest.xposed.app.XAppVerifyMode;
import github.tornaco.xposedmoduletest.xposed.service.VerifyListener;
import github.tornaco.xposedmoduletest.xposed.util.XposedLog;

/**
 * Created by guohao4 on 2017/10/31.
 * Email: Tornaco@163.com
 */

// Hook hookStartShortcut settings.
class LauncherAppServiceSubModule extends AndroidSubModule {
    @Override
    public String needBuildVar() {
        return XAppBuildVar.APP_LOCK;
    }

    @Override
    public int needMinSdk() {
        return Build.VERSION_CODES.N;
    }

    @Override
    public void handleLoadingPackage(String pkg, XC_LoadPackage.LoadPackageParam lpparam) {
        hookStartShortcut(lpparam);
        hookVerifyCallingPackage(lpparam);
    }

    private void hookVerifyCallingPackage(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedLog.verbose("LauncherAppServiceSubModule hookVerifyCallingPackage...");
        try {
            Class clz = XposedHelpers.findClass("com.android.server.pm.LauncherAppsService$LauncherAppsImpl",
                    lpparam.classLoader);

            Method verifyMethod = null;

            for (Method m : clz.getDeclaredMethods()) {
                if ("verifyCallingPackage".equals(m.getName())) {
                    verifyMethod = m;
                }
            }

            XposedLog.boot("verifyCallingPackage method: " + verifyMethod);

            if (verifyMethod == null) return;

            Object unHooks = XposedBridge.hookMethod(verifyMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);

                    XposedLog.verbose("verifyCallingPackage: " + param.args[0]);

                    param.setResult(null);
                }
            });
            XposedLog.verbose("LauncherAppServiceSubModule hookVerifyCallingPackage OK:" + unHooks);
            setStatus(unhookToStatus((XC_MethodHook.Unhook) unHooks));
        } catch (Exception e) {
            XposedLog.verbose("LauncherAppServiceSubModule Fail hookVerifyCallingPackage: " + Log.getStackTraceString(e));
            setStatus(SubModuleStatus.ERROR);
            setErrorMessage(Log.getStackTraceString(e));
        }
    }

    private void hookStartShortcut(XC_LoadPackage.LoadPackageParam lpparam) {
        XposedLog.verbose("LauncherAppServiceSubModule hookStartShortcut...");
        try {
            Class clz = XposedHelpers.findClass("com.android.server.pm.LauncherAppsService$LauncherAppsImpl",
                    lpparam.classLoader);

            Method startShortcutMethod = null;

            for (Method m : clz.getDeclaredMethods()) {
                if ("startShortcut".equals(m.getName())) {
                    startShortcutMethod = m;
                }
            }

            XposedLog.boot("startShortcut method: " + startShortcutMethod);

            if (startShortcutMethod == null) return;

            final Method finalStartShortcutMethod = startShortcutMethod;
            Object unHooks = XposedBridge.hookMethod(startShortcutMethod, new XC_MethodHook() {
                @Override
                protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                    super.beforeHookedMethod(param);
                    String pkgName = (String) param.args[1];
                    Bundle op = (Bundle) param.args[4];

                    if (BuildConfig.DEBUG) {
                        XposedLog.verbose("startShortcut: %s %s", pkgName, op);
                    }

                    if (pkgName == null) return;

                    // Package has been passed.
                    if (!getBridge().onEarlyVerifyConfirm(pkgName, "startShortcut")) {
                        return;
                    }

                    getBridge().verify(op, pkgName, 0, 0, new VerifyListener() {
                        @Override
                        public void onVerifyRes(String pkg, int uid, int pid, int res) {
                            if (res == XAppVerifyMode.MODE_ALLOWED) try {
                                XposedBridge.invokeOriginalMethod(finalStartShortcutMethod, param.thisObject, param.args);
                            } catch (Exception e) {
                                XposedLog.wtf("Error@" + Log.getStackTraceString(e));
                            }
                        }
                    });
                    param.setResult(true);
                }
            });
            XposedLog.verbose("LauncherAppServiceSubModule hookStartShortcut OK:" + unHooks);
            setStatus(unhookToStatus((XC_MethodHook.Unhook) unHooks));
        } catch (Exception e) {
            XposedLog.verbose("LauncherAppServiceSubModule Fail hookStartShortcut: " + Log.getStackTraceString(e));
            setStatus(SubModuleStatus.ERROR);
            setErrorMessage(Log.getStackTraceString(e));
        }
    }
}
