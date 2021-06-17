package com.developersolution.viewwindow.viewwindow;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
/**
 * Created by Hp on 12/9/2017.
 */
public class BootService extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent) {
       if(intent.getAction().equals(intent.ACTION_BOOT_COMPLETED)){
           Toast.makeText(context,"boot compleat ",Toast.LENGTH_LONG).show();
           Intent intent2 = new Intent(context,MainActivity.class);
           intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
           context.startActivity(intent2);
       }
    }
}
