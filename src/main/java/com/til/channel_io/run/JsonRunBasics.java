package com.til.channel_io.run;

import com.til.json_read_write.annotation.BaseClass;
import com.til.json_read_write.annotation.DefaultNew;
import com.til.json_read_write.annotation.SonClass;
import com.til.util.UseString;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

@BaseClass(sonClass = {JsonRunBasics.EmptyRunBasics.class})
@DefaultNew(newExample = JsonRunBasics.EmptyRunBasics.class)
public abstract class JsonRunBasics implements Runnable {

    public Supplier<NetworkEvent.Context> supplier;

    @SonClass(name = UseString.EMPTY)
    public static class EmptyRunBasics extends JsonRunBasics {
        @Override
        public void run() {

        }
    }
}
