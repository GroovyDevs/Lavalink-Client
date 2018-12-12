package lavalink.client.player.equalizer;

import lavalink.client.io.Link;
import org.json.JSONArray;
import org.json.JSONObject;

public class EqualizerImpl implements Equalizer {

    private final Link link;
    private float[] bands;

    public EqualizerImpl(Link link) {
        this.link = link;
        this.bands = new float[15];
    }

    @Override
    public void setGain(int band, float gain) {
        if (band > bands.length)
            throw new IndexOutOfBoundsException("There are only 15 bands. (0-14)");
        if (gain < -0.25 || gain > 1)
            throw new IllegalStateException("Valid gain range is -0.25 to 0.25");
        bands[band] = gain;
        updateGain();
    }

    @Override
    public void setGain(float[] bands) {
        if (bands.length != this.bands.length)
            throw new IndexOutOfBoundsException("You must cover all 15 bands.)");
        for(float gain : bands) {
            if (gain < -0.25 || gain > 1)
                throw new IllegalStateException("Valid gain range is -0.25 to 0.25");
        }
        this.bands = bands;
        updateGain();
    }

    private void updateGain() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("op", "equalizer");
        jsonObject.put("guildId", link.getGuildId());
        JSONArray array = new JSONArray();
        for (int i = 0; i < bands.length; i++)
            array.put(new JSONObject().put("band", String.valueOf(i)).put("gain", bands[i]));
        jsonObject.put("bands", array);
        if (link.getNode() != null) {
            System.out.println(jsonObject.toString());
            link.getNode().send(jsonObject.toString());
        }
        else
            throw new IllegalStateException("Node is null");
    }
}