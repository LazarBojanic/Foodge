package rs.raf.projekat_jun_lazar_bojanic_11621.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import rs.raf.projekat_jun_lazar_bojanic_11621.R;
import rs.raf.projekat_jun_lazar_bojanic_11621.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {
    private MainActivityViewModel mainActivityViewModel;
    private BottomNavigationView bottomNavigationView;
    private NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.navHostFragment);
        if(navHostFragment != null){
            navController = navHostFragment.getNavController();
        }
        NavGraph navGraph = navController.getGraph();
        navGraph.setStartDestination(R.id.fragmentHome);
        navController.setGraph(navGraph);
        navController.navigate(R.id.fragmentHome);
        bottomNavigationView.setSelectedItemId(R.id.itemHome);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if(itemId == R.id.itemMeals){
                navController.navigate(R.id.fragmentMeals);
                return true;
            }
            else if(itemId == R.id.itemHome){
                navController.navigate(R.id.fragmentHome);
                return true;
            }
            else if(itemId == R.id.itemStats){
                navController.navigate(R.id.fragmentStats);
                return true;
            }
            else{
                return false;
            }
        });
    }
    public MainActivityViewModel getMainActivityViewModel(){
        return this.mainActivityViewModel;
    }

    public BottomNavigationView getBottomNavigationView() {
        return bottomNavigationView;
    }

    public NavController getNavController() {
        return navController;
    }
}