package lavalink.client.player.equalizer;

public interface Equalizer {

    void setGain(int band, float gain);

    void setGain(float[] bands);
}