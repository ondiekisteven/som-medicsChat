package cf.somwaki.medicalchat;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.View;

import com.mindorks.placeholderview.PlaceHolderView;

public class MainActivity extends AppCompatActivity {

    private PlaceHolderView mDrawerView;
    private DrawerLayout mDrawer;
    private androidx.appcompat.widget.Toolbar mToolBar;
    private PlaceHolderView mGalleryView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

}
