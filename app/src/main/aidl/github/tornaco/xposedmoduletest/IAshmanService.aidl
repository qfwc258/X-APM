package github.tornaco.xposedmoduletest;

import github.tornaco.xposedmoduletest.IProcessClearListener;
import github.tornaco.xposedmoduletest.IPackageUninstallCallback;
import github.tornaco.xposedmoduletest.IAshmanWatcher;
import github.tornaco.xposedmoduletest.ITopPackageChangeListener;
import github.tornaco.xposedmoduletest.xposed.bean.BlockRecord2;
import github.tornaco.xposedmoduletest.xposed.bean.DozeEvent;

import github.tornaco.xposedmoduletest.xposed.bean.OpLog;
import github.tornaco.xposedmoduletest.xposed.bean.AppSettings;
import github.tornaco.xposedmoduletest.xposed.bean.BlurSettings;
import github.tornaco.xposedmoduletest.xposed.bean.PackageSettings;
import github.tornaco.xposedmoduletest.xposed.bean.VerifySettings;

import github.tornaco.xposedmoduletest.bean.CongfigurationSetting;

import android.content.ComponentName;
import java.util.Map;

interface IAshmanService {

    void clearProcess(in IProcessClearListener listener);

    void setLockKillDelay(long delay);
    long getLockKillDelay();

    void setWhiteSysAppEnabled(boolean enabled);
    boolean isWhiteSysAppEnabled();

    void setBootBlockEnabled(boolean enabled);
    boolean isBlockBlockEnabled();

    void setStartBlockEnabled(boolean enabled);
    boolean isStartBlockEnabled();

    void setLockKillEnabled(boolean enabled);
    boolean isLockKillEnabled();

    void setRFKillEnabled(boolean enabled);
    boolean isRFKillEnabled();

    void setGreeningEnabled(boolean enabled);
    boolean isGreeningEnabled();

    // API For Firewall.
    boolean checkService(in ComponentName servicePkgName, int callerUid);

    boolean checkBroadcast(String action, int receiverUid, int callerUid);

    List<BlockRecord2> getBlockRecords();

    void clearBlockRecords();

    void setComponentEnabledSetting(in ComponentName componentName, int newState, int flags);

    int getComponentEnabledSetting(in ComponentName componentName);

    int getApplicationEnabledSetting(String packageName);

    void setApplicationEnabledSetting(String packageName, int newState, int flags);

    void watch(in IAshmanWatcher w);
    void unWatch(in IAshmanWatcher w);

    // Network policy API.
    void setNetworkPolicyUidPolicy(int uid, int policy);

    void restrictAppOnData(int uid, boolean restrict);
    void restrictAppOnWifi(int uid, boolean restrict);

    boolean isRestrictOnData(int uid);
    boolean isRestrictOnWifi(int uid);

    // Power API.
    void restart();

    // Extra API.
    void setCompSettingBlockEnabled(boolean enabled);
    boolean isCompSettingBlockEnabledEnabled();

    // Package loader API.
    String[] getWhiteListApps(int filterOptions);
    String[] getInstalledApps(int filterOptions);

    String[] getBootBlockApps(boolean block);
    void addOrRemoveBootBlockApps(in String[] packages, int op);

    String[] getStartBlockApps(boolean block);
    void addOrRemoveStartBlockApps(in String[] packages, int op);

    String[] getLKApps(boolean kill);
    void addOrRemoveLKApps(in String[] packages, int op);

    String[] getRFKApps(boolean kill);
    void addOrRemoveRFKApps(in String[] packages, int op);

    String[] getGreeningApps(boolean greening);
    void addOrRemoveGreeningApps(in String[] packages, int op);

    boolean isPackageGreening(String packageName);
    boolean isUidGreening(int uid);

    // PM API.
    void unInstallPackage(String pkg, in IPackageUninstallCallback callback);

    boolean isLockKillDoNotKillAudioEnabled();
    void setLockKillDoNotKillAudioEnabled(boolean enabled);

    int getControlMode();
    void setControlMode(int mode);

    String getBuildSerial();

    boolean isAutoAddBlackEnabled();
    void setAutoAddBlackEnable(boolean enable);

    void forceReloadPackages();

    void setPermissionControlEnabled(boolean enabled);
    boolean isPermissionControlEnabled();

    int getPermissionControlBlockModeForPkg(int code, String pkg);
    int getPermissionControlBlockModeForUid(int code, int uid);
    void setPermissionControlBlockModeForPkg(int code, String pkg, int mode);

    void setUserDefinedAndroidId(String id);
    void setUserDefinedDeviceId(String id);
    void setUserDefinedLine1Number(String id);

    String getAndroidId();
    String getDeviceId();
    String getLine1Number();

    String getUserDefinedLine1Number();
    String getUserDefinedDeviceId();
    String getUserDefinedAndroidId();

    boolean isPackageInPrivacyList(String pkg);
    boolean isUidInPrivacyList(int uid);
    int getPrivacyAppsCount();

    String[] getPrivacyList(boolean priv);
    void addOrRemoveFromPrivacyList(String pkg, int op);

    boolean showFocusedActivityInfoEnabled();
    void setShowFocusedActivityInfoEnabled(boolean enabled);

    void restoreDefaultSettings();

    List<RunningServiceInfo> getRunningServices(int max);
    List<RunningAppProcessInfo> getRunningAppProcesses();

    void writeSystemSettings(String key, String value);
    String getSystemSettings(String key);

    long[] getProcessPss(in int[] pids);

    boolean onApplicationUncaughtException(String packageName, String thread, String exception, String trace);
    boolean isAppCrashDumpEnabled();
    void setAppCrashDumpEnabled(boolean enabled);

    void registerOnTopPackageChangeListener(in ITopPackageChangeListener listener);
    void unRegisterOnTopPackageChangeListener(in ITopPackageChangeListener listener);

    boolean isLazyModeEnabled();
    boolean isLazyModeEnabledForPackage(String pkg);
    void setLazyModeEnabled(boolean enabled);

    String[] getLazyApps(boolean lazy);
    void addOrRemoveLazyApps(in String[] packages, int op);

    // Long press back kill.
    void setLPBKEnabled(boolean enabled);
    boolean isLPBKEnabled();

    // Usually called by systemui.
    void onTaskRemoving(int callingUid, int taskId);

    void addOrRemoveAppFocusAction(String pkg, in String[] actions, boolean add);
    String[] getAppFocusActionPackages();
    String[] getAppFocusActions(String pkg);

    void addOrRemoveAppUnFocusAction(String pkg, in String[] actions, boolean add);
    String[] getAppUnFocusActionPackages();
    String[] getAppUnFocusActions(String pkg);

    void setDozeEnabled(boolean enable);
    boolean isDozeEnabled();

    void setForceDozeEnabled(boolean enable);
    boolean isForceDozeEnabled();

    // Deperacated, please use getLastDozeEvent instead.
    long getLastDozeEnterTimeMills();

    DozeEvent getLastDozeEvent();

    long getDozeDelayMills();
    void setDozeDelayMills(long delayMills);

    void setDoNotKillSBNEnabled(boolean enable, String module);// Flag distint which module
    boolean isDoNotKillSBNEnabled(String module);

    void setTaskRemoveKillEnabled(boolean enable);
    boolean isTaskRemoveKillEnabled();

    // Get or Add task remove kill apps.
    String[] getTRKApps(boolean kill);
    void addOrRemoveTRKApps(in String[] packages, int op);

    List<DozeEvent> getDozeEventHistory();

    void setPrivacyEnabled(boolean enable);
    boolean isPrivacyEnabled();

    String[] getPluginApps();
    boolean isAppInPluginList(String pkg);
    void addOrRemovePluginApp(String appPackageName, boolean add);

    boolean hasNotificationForPackage(String pkg);

    int getAppLevel(String pkg);

    String packageForTaskId(int taskId);

    // APP GUARD SERVICE API.

     boolean isAppLockEnabled();
     void setAppLockEnabled(boolean enabled);

     boolean isBlurEnabled();
     boolean isBlurEnabledForPackage(String packageName);
     void setBlurEnabled(boolean enabled);

     int getBlurRadius();
     void setBlurRadius(int r);

     boolean isUninstallInterruptEnabled();
     void setUninstallInterruptEnabled(boolean enabled);

     void setVerifySettings(in VerifySettings settings);

     VerifySettings getVerifySettings();

     void setResult(int transactionID, int res);

     boolean isTransactionValid(int transactionID);

        // For test only.
     void mockCrash();

     void setVerifierPackage(String pkg);

     void injectHomeEvent();

     void setDebug(boolean debug);

     boolean isDebug();

     void onActivityPackageResume(String pkg);

     boolean isInterruptFPEventVBEnabled(int event);
     void setInterruptFPEventVBEnabled(int event, boolean enabled);

     void addOrRemoveComponentReplacement(in ComponentName from, in ComponentName to, boolean add);
     Map getComponentReplacements();

     // void forceReloadPackages();

     String[] getLockApps(boolean lock);
     void addOrRemoveLockApps(in String[] packages, boolean add);

     String[] getBlurApps(boolean lock);
     void addOrRemoveBlurApps(in String[] packages, boolean blur);

     String[] getUPApps(boolean lock);
     void addOrRemoveUPApps(in String[] packages, boolean add);

     // void restoreDefaultSettings();

     // void onTaskRemoving(String pkg);

        // APPGUARD API END.

     AppSettings retrieveAppSettingsForPackage(String pkg);
     void applyAppSettingsForPackage(String pkg, in AppSettings settings);

     void backupTo(String dir);
     void restoreFrom(String dir);

     String[] getRawPermSettings(int page, int countInPage);

     void setAppInstalledAutoApplyTemplate(in AppSettings settings);
     AppSettings getAppInstalledAutoApplyTemplate();

     String[] getOpLogPackages();
     List<OpLog> getOpLogForPackage(String packageName);

     String getUserName();
     Bitmap getUserIcon();

     void addPendingDisableApps(String pkg);
}
