package github.tornaco.xposedmoduletest.ui.activity.app;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.List;

import dev.nick.tiles.tile.Category;
import dev.nick.tiles.tile.DashboardFragment;
import github.tornaco.android.common.util.ColorUtil;
import github.tornaco.xposedmoduletest.R;
import github.tornaco.xposedmoduletest.loader.PaletteColorPicker;
import github.tornaco.xposedmoduletest.provider.XSettings;
import github.tornaco.xposedmoduletest.ui.activity.WithWithCustomTabActivity;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppBlurSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppBootSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppLKSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppLazySetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppLockSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppPrivacySetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppRFKSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppServiceSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppStartSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppTRKSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppTimerSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppUPSetting;
import github.tornaco.xposedmoduletest.ui.tiles.app.per.AppWakeLockSetting;
import github.tornaco.xposedmoduletest.xposed.app.XAshmanManager;
import github.tornaco.xposedmoduletest.xposed.bean.AppSettings;
import github.tornaco.xposedmoduletest.xposed.util.PkgUtil;

/**
 * Created by guohao4 on 2017/11/2.
 * Email: Tornaco@163.com
 */

public class PerAppSettingsDashboardActivity extends WithWithCustomTabActivity {

    public static void start(Context context, String pkg) {
        Intent starter = new Intent(context, PerAppSettingsDashboardActivity.class);
        starter.putExtra("pkg_name", pkg);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.container_with_appbar_fab_template);
        setupToolbar();
        showHomeAsUp();

        String pkgName = getIntent().getStringExtra("pkg_name");
        if (pkgName == null) return;

        setTitle(getTitle() + "\t" + PkgUtil.loadNameByPkgName(getContext(), pkgName));

        replaceV4(R.id.container, Dashboards.newInstance(getContext(), pkgName), null, false);

        // Apply theme color.
        int color = ContextCompat.getColor(this, XSettings.getThemes(this).getThemeColor());

        // Apply palette color.
        PaletteColorPicker.pickPrimaryColor(this, new PaletteColorPicker.PickReceiver() {
            @Override
            public void onColorReady(int color) {
                applyColor(color);
            }
        }, pkgName, color);

        findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFabClick();
            }
        });
    }

    void onFabClick() {
        finish();
    }

    @SuppressWarnings("ConstantConditions")
    private void applyColor(int color) {
        int dark = ColorUtil.colorBurn(color);
        getWindow().setStatusBarColor(dark);
        getWindow().setNavigationBarColor(dark);
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setBackgroundColor(color);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    AppSettings onRetrieveAppSettings(String pkg) {
        return XAshmanManager.get()
                .retrieveAppSettingsForPackage(pkg);
    }

    public static class Dashboards extends DashboardFragment {

        private String mPkg;

        public static Dashboards newInstance(Context context, String pkg) {
            Dashboards d = new Dashboards();
            Bundle bd = new Bundle(1);
            bd.putString("pkg_name", pkg);
            d.setArguments(bd);
            return d;
        }

        @Override
        protected int getLayoutId() {
            return R.layout.dashboard_fab_workaround;
        }

        @Override
        public void onAttach(Context context) {
            super.onAttach(context);
            mPkg = getArguments().getString("pkg_name", null);
        }

        @Override
        protected void onCreateDashCategories(List<Category> categories) {
            super.onCreateDashCategories(categories);

            if (mPkg == null) return;

            AppSettings appSettings = ((PerAppSettingsDashboardActivity) getActivity()).onRetrieveAppSettings(mPkg);

            Category sec = new Category();
            sec.titleRes = R.string.title_secure;

            sec.addTile(new AppLockSetting(getActivity(), appSettings));
            sec.addTile(new AppBlurSetting(getActivity(), appSettings));
            sec.addTile(new AppUPSetting(getActivity(), appSettings));
            sec.addTile(new AppPrivacySetting(getActivity(), appSettings));

            Category rest = new Category();
            rest.titleRes = R.string.title_restrict;
            rest.addTile(new AppBootSetting(getActivity(), appSettings));
            rest.addTile(new AppStartSetting(getActivity(), appSettings));
            rest.addTile(new AppLKSetting(getActivity(), appSettings));
            rest.addTile(new AppRFKSetting(getActivity(), appSettings));
            rest.addTile(new AppTRKSetting(getActivity(), appSettings));
            rest.addTile(new AppLazySetting(getActivity(), appSettings));

            Category green = new Category();
            green.titleRes = R.string.title_greening;
            green.addTile(new AppWakeLockSetting(getActivity(), appSettings));
            green.addTile(new AppTimerSetting(getActivity(), appSettings));
            green.addTile(new AppServiceSetting(getActivity(), appSettings));

            categories.add(sec);
            categories.add(rest);
            categories.add(green);
        }
    }


}
