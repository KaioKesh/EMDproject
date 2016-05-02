package kesh.emd.marist.powertree;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

/**
 * Created by Keshine on 4/24/2016.
 */
public class TreeView extends View {

    private Context myContext;
    public TreeView(Context context) {
        super(context);
        myContext = context;
    }

    protected void onDraw(Canvas canvas) {


        invalidate();
    }
}
