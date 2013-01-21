package de.unierlangen.like.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import de.unierlangen.like.R;
import de.unierlangen.like.preferences.PreferenceWithHeaders;

public class OptionsMenuActivity extends Activity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * override onOptions ItemSelected here
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.find_room:
            startActivity(new Intent(OptionsMenuActivity.this, MainYourLocationActivity.class));
            break;
        case R.id.about:
            startActivity(new Intent(OptionsMenuActivity.this, AboutActivity.class));
            break;
        case R.id.help:
            startActivity(new Intent(OptionsMenuActivity.this, HelpActivity.class));
            break;
        case R.id.prefs:
            startActivity(new Intent(OptionsMenuActivity.this, PreferenceWithHeaders.class));
            break;
        default:
            UserMessages.showMsg((String) item.getTitle(), this);
        }
        // return super.onOptionsItemSelected(item);
        return false;
    }
}